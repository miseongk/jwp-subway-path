package subway.entity;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;
import subway.domain.Line;

public class StationEntity {
    private Long id;
    private String name;
    private Long lineId;

    public StationEntity(String name, Long lineId) {
        this(null, name, lineId);
    }

    public StationEntity(final Long id, final String name, final Long lineId) {
        this.id = id;
        this.name = name;
        this.lineId = lineId;
    }

    public static List<StationEntity> of(final Line line, final Long lineId) {
        return line.findAllStation().stream()
                .map(station -> new StationEntity(station.getName(), lineId))
                .collect(toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StationEntity station = (StationEntity) o;
        return id.equals(station.id) && name.equals(station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getLineId() {
        return lineId;
    }
}
