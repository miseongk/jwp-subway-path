package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.Line;
import subway.domain.Subway;
import subway.dto.StationSaveRequest;
import subway.repository.LineRepository;

@Service
public class StationService {

    private final LineRepository lineRepository;

    public StationService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public void save(StationSaveRequest request) {
        // 역 불러오기
        Subway subway = new Subway(lineRepository.findAll());
        // 입력받은 역 추가하기
        subway.add(
                request.getLineName(),
                request.getBaseStationName(),
                request.getAdditionalStationName(),
                request.getDistance(),
                request.getDirection()
        );
        // 추가 할 역에 의해 변경된 노선 찾기
        Line updatedLine = subway.findUpdatedLine(request.getLineName());
        // 변경된 노선 저장(업데이트)
        lineRepository.save(updatedLine);
    }
}
