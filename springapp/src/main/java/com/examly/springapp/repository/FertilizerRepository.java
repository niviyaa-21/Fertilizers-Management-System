package com.examly.springapp.repository;

import com.examly.springapp.model.Fertilizer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FertilizerRepository extends JpaRepository<Fertilizer, Long> {
}
