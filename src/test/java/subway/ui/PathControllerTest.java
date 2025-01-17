package subway.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.IntegrationTest;
import subway.domain.Line;
import subway.domain.Section;
import subway.dto.ShortestPathRequest;
import subway.repository.LineRepository;

@SuppressWarnings("NonAsciiCharacters")
class PathControllerTest extends IntegrationTest {

    @Autowired
    private LineRepository lineRepository;

    @Nested
    class 성공_테스트 {

        @Test
        void 최단_경로를_조회한다() {
            // given
            lineRepository.save(new Line("1호선", "RED",
                    List.of(
                            new Section("A", "B", 2),
                            new Section("B", "C", 3),
                            new Section("C", "D", 4)
                    )
            ));
            lineRepository.save(new Line("2호선", "BLUE",
                    List.of(
                            new Section("D", "B", 2),
                            new Section("B", "E", 2)
                    )
            ));
            final ShortestPathRequest request = new ShortestPathRequest("A", "D");

            // when
            ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .param("startStationName", request.getStartStationName())
                    .param("endStationName", request.getEndStationName())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().get("/path")
                    .then().log().all()
                    .extract();

            // then
            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                    () -> assertThat(response.body().jsonPath().getList("path")).containsExactly("A", "B", "D"),
                    () -> assertThat(response.body().jsonPath().getInt("fare")).isEqualTo(1250),
                    () -> assertThat(response.body().jsonPath().getInt("distance")).isEqualTo(4)
            );
        }
    }

    @Nested
    class 예외_테스트 {

        @Test
        void 최단_경로를_조회시_출발역을_입력하지_않으면_예외가_발생한다() {
            // given
            lineRepository.save(new Line("1호선", "RED",
                    List.of(
                            new Section("A", "B", 2),
                            new Section("B", "C", 3),
                            new Section("C", "D", 4)
                    )
            ));
            lineRepository.save(new Line("2호선", "BLUE",
                    List.of(
                            new Section("D", "B", 2),
                            new Section("B", "E", 2)
                    )
            ));
            final ShortestPathRequest request = new ShortestPathRequest("A", "D");

            // when
            ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .queryParam("endStationName", request.getEndStationName())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().get("/path")
                    .then().log().all()
                    .extract();

            // then
            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                    () -> assertThat(response.body().jsonPath().getString("message"))
                            .isEqualTo("최단 경로를 조회할 출발역과 도착역 모두 입력해주세요.")
            );
        }

        @Test
        void 최단_경로를_조회시_도착역을_입력하지_않으면_예외가_발생한다() {
            // given
            lineRepository.save(new Line("1호선", "RED",
                    List.of(
                            new Section("A", "B", 2),
                            new Section("B", "C", 3),
                            new Section("C", "D", 4)
                    )
            ));
            lineRepository.save(new Line("2호선", "BLUE",
                    List.of(
                            new Section("D", "B", 2),
                            new Section("B", "E", 2)
                    )
            ));
            final ShortestPathRequest request = new ShortestPathRequest("A", "D");

            // when
            ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .param("startStationName", request.getStartStationName())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().get("/path")
                    .then().log().all()
                    .extract();

            // then
            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                    () -> assertThat(response.body().jsonPath().getString("message"))
                            .isEqualTo("최단 경로를 조회할 출발역과 도착역 모두 입력해주세요.")
            );
        }
    }
}
