package com.examly.springapp.controller;

import com.examly.springapp.model.Fertilizer;
import com.examly.springapp.service.FertilizerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fertilizers")
public class FertilizerController {

    private final FertilizerService fertilizerService;

    public FertilizerController(FertilizerService fertilizerService) {
        this.fertilizerService = fertilizerService;
    }

    @PostMapping("/addFertilizer")
    public ResponseEntity<Fertilizer> addFertilizer(@RequestBody Fertilizer fertilizer) {
        return ResponseEntity.ok(fertilizerService.addFertilizer(fertilizer));
    }

    @GetMapping("/allFertilizers")
    public ResponseEntity<List<Fertilizer>> getAllFertilizers() {
        return ResponseEntity.ok(fertilizerService.getAllFertilizers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fertilizer> getFertilizerById(@PathVariable Long id) {
        return ResponseEntity.ok(fertilizerService.getFertilizerById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Fertilizer> updateFertilizer(@PathVariable Long id, @RequestBody Fertilizer fertilizer) {
        return ResponseEntity.ok(fertilizerService.updateFertilizer(id, fertilizer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFertilizer(@PathVariable Long id) {
        fertilizerService.deleteFertilizer(id);
        return ResponseEntity.ok().build();
    }
}
