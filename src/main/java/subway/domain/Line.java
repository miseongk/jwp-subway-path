package subway.domain;

import static subway.domain.Direction.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import subway.exception.InvalidSectionException;
import subway.exception.LineNotEmptyException;

public class Line {

    private final String name;
    private final String color;
    private final List<Section> sections;

    public Line(String name, String color, List<Section> sections) {
        this.name = name;
        this.color = color;
        this.sections = new ArrayList<>(sections);
    }

    // 역 추가
    public void add(Station base, Station additional, Distance distance, Direction direction) {
        validate(base, additional, distance, direction);

        if (direction == RIGHT) {
            Optional<Section> sectionAtStart = findSectionAtStart(base);
            if (sectionAtStart.isPresent()) {
                Section originSection = sectionAtStart.get();
                changeDistance(additional, originSection.getEnd(), originSection, distance);
            }
            sections.add(new Section(base, additional, distance));
        }

        if (direction == LEFT) {
            Optional<Section> sectionAtEnd = findSectionAtEnd(base);
            if (sectionAtEnd.isPresent()) {
                Section originSection = sectionAtEnd.get();
                changeDistance(originSection.getStart(), additional, originSection, distance);
            }
            sections.add(new Section(additional, base, distance));
        }
    }

    private void validate(Station base, Station additional, Distance distance, Direction direction) {
        if (!contains(base)) {
            throw new InvalidSectionException("기준역이 존재하지 않습니다.");
        }
        if (contains(additional)) {
            throw new InvalidSectionException("등록할 역이 이미 존재합니다.");
        }
        if (isNotValidDistanceToAddRight(base, distance, direction)
                || isNotValidDistanceToAddLeft(base, distance, direction)) {
            throw new InvalidSectionException("등록할 구간의 거리가 기존 구간의 거리보다 같거나 클 수 없습니다.");
        }
    }

    private boolean contains(Station station) {
        return sections.stream()
                .anyMatch(section -> section.contains(station));
    }

    private boolean isNotValidDistanceToAddRight(Station base, Distance distance, Direction direction) {
        if (direction == LEFT) {
            return false;
        }
        Optional<Section> findSection = findSectionAtStart(base);
        return findSection.map(section -> section.moreThanOrEqual(distance)).orElse(false);
    }

    private Optional<Section> findSectionAtStart(Station base) {
        return sections.stream()
                .filter(section -> section.isStart(base))
                .findFirst();
    }

    private boolean isNotValidDistanceToAddLeft(Station base, Distance distance, Direction direction) {
        if (direction == RIGHT) {
            return false;
        }
        Optional<Section> findSection = findSectionAtEnd(base);
        return findSection.map(section -> section.moreThanOrEqual(distance)).orElse(false);
    }

    private Optional<Section> findSectionAtEnd(Station base) {
        return sections.stream()
                .filter(section -> section.isEnd(base))
                .findFirst();
    }

    private void changeDistance(Station start, Station end, Section originSection, Distance distance) {
        sections.add(new Section(start, end, originSection.subtract(distance)));
        sections.remove(originSection);
    }

    // 역 제거
     public void remove(Station station) {
        if (!contains(station)) {
            throw new InvalidSectionException("역을 찾을 수 없습니다.");
        }

         final Optional<Section> sectionAtStart = findSectionAtStart(station);
         final Optional<Section> sectionAtEnd = findSectionAtEnd(station);

         // 제거할 역이 중간에 있는 경우
         if (sectionAtStart.isPresent() && sectionAtEnd.isPresent()) {
             final Section left = sectionAtEnd.get();
             final Section right = sectionAtStart.get();
             sections.add(new Section(left.getStart(), right.getEnd(), left.add(right.getDistance())));
             sections.remove(left);
             sections.remove(right);
             return;
         }

         // 제거할 역이 상행종점에 있는 경우
         if (sectionAtStart.isPresent()) {
             sections.remove(sectionAtStart.get());
             return;
         }
         // 제거할 역이 하행종점에 있는 경우
         sections.remove(sectionAtEnd.get());
     }

    public List<Station> findAllStation() {
        final Map<Station, Station> stationToStation = sections.stream()
                .collect(Collectors.toMap(Section::getStart, Section::getEnd));

        final Optional<Station> firstStation = findFirstStation(stationToStation);
        return firstStation.map(station -> orderStations(stationToStation, station))
                .orElse(Collections.emptyList());
    }

    private Optional<Station> findFirstStation(final Map<Station, Station> stationToStation) {
        final Set<Station> stations = new HashSet<>(stationToStation.keySet());
        stations.removeAll(stationToStation.values());
        return stations.stream().findFirst();
    }

    private List<Station> orderStations(final Map<Station, Station> stationToStation, Station station) {
        final List<Station> result = new ArrayList<>(List.of(station));
        while (stationToStation.containsKey(station)) {
            final Station next = stationToStation.get(station);
            result.add(next);
            station = next;
        }
        return result;
    }

    // 초기에 역 추가
    public void initialAdd(final Section section) {
        if (!sections.isEmpty()) {
            throw new LineNotEmptyException();
        }
        sections.add(section);
    }

    public boolean containsAll(final Station start, final Station end) {
        return sections.stream()
                .anyMatch(section -> section.containsAll(start, end));
    }

    public boolean isSameName(final String lineName) {
        return name.equals(lineName);
    }

    @Override
    public String toString() {
        return "Line{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", sections=" + sections +
                '}';
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Section> getSections() {
        return sections;
    }
}
