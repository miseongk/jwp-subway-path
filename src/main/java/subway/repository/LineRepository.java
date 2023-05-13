package subway.repository;

import static java.util.stream.Collectors.groupingBy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;

@Repository
public class LineRepository {

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;


    public LineRepository(LineDao lineDao, StationDao stationDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }


    public Line save(Line line) {
        Optional<LineEntity> lineEntity = lineDao.findByName(line.getName());
        lineEntity.ifPresent(entity -> deleteAllByLineId(entity.getId()));
        LineEntity newLineEntity = lineDao.insert(new LineEntity(line.getName(), line.getColor()));
        List<StationEntity> stationEntities = StationEntity.of(line, newLineEntity.getId());
        stationDao.insertAll(stationEntities);
        List<SectionEntity> sectionEntities = SectionEntity.of(line, newLineEntity.getId());
        sectionDao.insertAll(sectionEntities);
        return line;
    }

    private void deleteAllByLineId(final Long id) {
        sectionDao.deleteAll(id);
        stationDao.deleteByLineId(id);
        lineDao.deleteById(id);
    }

    public List<Line> findAll() {
        List<LineEntity> lineEntities = lineDao.findAll();
        List<SectionEntity> sectionEntities = sectionDao.findAll();
        Map<Long, List<SectionEntity>> lineIdBySections = sectionEntities.stream()
                .collect(groupingBy(SectionEntity::getLineId));
        return lineEntities.stream()
                .map(lineEntity -> toLine(lineEntity, lineIdBySections.getOrDefault(lineEntity.getId(), new ArrayList<>())))
                .collect(Collectors.toList());
    }

    private Line toLine(LineEntity lineEntity, List<SectionEntity> sectionEntities) {
        List<Section> sections = sectionEntities.stream()
                .map(sectionEntity -> new Section(
                        sectionEntity.getStartStationName(),
                        sectionEntity.getEndStationName(),
                        sectionEntity.getDistance()
                )).collect(Collectors.toList());
        return new Line(lineEntity.getName(), lineEntity.getColor(), sections);
    }
}
