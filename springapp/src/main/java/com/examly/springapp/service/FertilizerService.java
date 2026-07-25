package com.examly.springapp.service;

import com.examly.springapp.exception.FertilizerNotFoundException;
import com.examly.springapp.model.Fertilizer;
import com.examly.springapp.repository.FertilizerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FertilizerService {

    private final FertilizerRepository fertilizerRepository;

    public FertilizerService(FertilizerRepository fertilizerRepository) {
        this.fertilizerRepository = fertilizerRepository;
    }

    public Fertilizer addFertilizer(Fertilizer fertilizer) {
        return fertilizerRepository.save(fertilizer);
    }

    public List<Fertilizer> getAllFertilizers() {
        return fertilizerRepository.findAll();
    }

    public Fertilizer getFertilizerById(Long id) {
        return fertilizerRepository.findById(id)
                .orElseThrow(() -> new FertilizerNotFoundException("Fertilizer not found with id: " + id));
    }

    public Fertilizer updateFertilizer(Long id, Fertilizer updated) {
        Fertilizer existing = getFertilizerById(id);
        existing.setFertilizerName(updated.getFertilizerName());
        existing.setManufacturer(updated.getManufacturer());
        existing.setType(updated.getType());
        existing.setQuantity(updated.getQuantity());
        existing.setPrice(updated.getPrice());
        return fertilizerRepository.save(existing);
    }

    public void deleteFertilizer(Long id) {
        getFertilizerById(id);
        fertilizerRepository.deleteById(id);
    }
}
