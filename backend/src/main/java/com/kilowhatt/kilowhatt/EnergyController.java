package com.kilowhatt.kilowhatt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/energies")
public class EnergyController {
    @Autowired
    private EnergyService energyService;

    @GetMapping
    public ResponseEntity<List<EnergyMeter>> getEnergies() {
        return new ResponseEntity<>(energyService.allEnergies(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EnergyMeter> postEnergy(@RequestBody Map<String, String> payload) {
        return new ResponseEntity<>(energyService.createMeasurement(payload.get("value"), payload.get("timeStamp")), HttpStatus.CREATED);
    }

    @GetMapping("/current")
    public ResponseEntity<EnergyMeter> getCurrentEnergy() {
        return new ResponseEntity<>(energyService.getCurrentEnergyMeasurement(), HttpStatus.OK);
    }

    // Включення реле
    @PostMapping("/turn-on/{relayNumber}")
    public ResponseEntity<Void> turnOnRelay(@PathVariable int relayNumber) {
        energyService.turnOnRelay(relayNumber);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Вимкнення реле
    @PostMapping("/turn-off/{relayNumber}")
    public ResponseEntity<Void> turnOffRelay(@PathVariable int relayNumber) {
        energyService.turnOffRelay(relayNumber);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/reset")
    public ResponseEntity<EnergyMeter> resetEnergy() {
        EnergyMeter resetEnergy = energyService.resetCurrentEnergy();
        return new ResponseEntity<>(resetEnergy, HttpStatus.OK);
    }
}
