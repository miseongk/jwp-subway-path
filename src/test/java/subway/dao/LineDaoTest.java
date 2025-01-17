package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.entity.LineEntity;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class LineDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private LineDao lineDao;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate);
    }

    @Test
    void 노선을_추가한다() {
        // given
        final LineEntity line = new LineEntity("1호선", "RED");

        // when
        final LineEntity result = lineDao.insert(line);

        // then
        assertThat(lineDao.findById(result.getId()).get()).isEqualTo(result);
    }

    @Test
    void 노선을_수정한다() {
        // given
        final LineEntity line = new LineEntity("1호선", "RED");
        final LineEntity insertLine = lineDao.insert(line);
        final LineEntity newLine = new LineEntity(insertLine.getId(), "2호선", "BLUE");

        // when
        lineDao.update(newLine);

        // then
        assertThat(lineDao.findById(newLine.getId()).get()).isEqualTo(newLine);
    }

    @Test
    void 노선을_삭제한다() {
        // given
        final LineEntity line = new LineEntity("1호선", "RED");
        final LineEntity insertLine = lineDao.insert(line);

        // when
        lineDao.deleteById(insertLine.getId());

        // then
        assertThat(lineDao.findAll()).hasSize(0);
    }

    @Test
    void 노선을_전체_조회한다() {
        // given
        final LineEntity line1 = new LineEntity("1호선", "RED");
        final LineEntity line2 = new LineEntity("2호선", "BLUE");
        final LineEntity insertLine1 = lineDao.insert(line1);
        final LineEntity insertLine2 = lineDao.insert(line2);

        // when
        final List<LineEntity> result = lineDao.findAll();

        // then
        assertThat(result).containsExactly(insertLine1, insertLine2);
    }

    @Test
    void 노선을_id로_조회한다() {
        // given
        final LineEntity line = new LineEntity("1호선", "RED");
        final LineEntity insertLine = lineDao.insert(line);

        // when
        final Optional<LineEntity> result = lineDao.findById(insertLine.getId());

        // then
        assertThat(result).isPresent();
    }

    @Test
    void 노선을_이름으로_조회한다() {
        // given
        final LineEntity line = new LineEntity("1호선", "RED");
        lineDao.insert(line);

        // when
        final Optional<LineEntity> result = lineDao.findByName("1호선");

        // then
        assertThat(result).isPresent();
    }
}
