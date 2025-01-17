package subway.ui;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.application.StationService;
import subway.dto.StationDeleteRequest;
import subway.dto.StationInitialSaveRequest;
import subway.dto.StationSaveRequest;

@RestController
public class StationController {

    private final StationService stationService;

    public StationController(final StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping("/stations")
    public ResponseEntity<Void> save(@RequestBody @Valid final StationSaveRequest request) {
        stationService.save(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/stations")
    public ResponseEntity<Void> delete(@RequestBody @Valid final StationDeleteRequest request) {
        stationService.delete(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/stations/init")
    public ResponseEntity<Void> initialSave(@RequestBody @Valid final StationInitialSaveRequest request) {
        stationService.initialSave(request);
        return ResponseEntity.ok().build();
    }
}
