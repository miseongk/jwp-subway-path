package subway.ui;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.StationService;
import subway.dto.StationSaveRequest;

@RestController
public class StationController {

    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<Void> save(@PathVariable StationSaveRequest request) {
        stationService.save(request);
        return ResponseEntity.created(URI.create("/lines")).build();
    }
}
