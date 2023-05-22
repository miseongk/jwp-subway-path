package subway.acceptance.step;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

@SuppressWarnings("NonAsciiCharacters")
public class CommonStep {

    public static HttpStatus 정상_요청 = HttpStatus.OK;

    public static void 요청_결과의_상태를_확인한다(final ExtractableResponse<Response> 요청_결과, final HttpStatus 상태) {
        assertThat(요청_결과.statusCode()).isEqualTo(상태.value());
    }
}
