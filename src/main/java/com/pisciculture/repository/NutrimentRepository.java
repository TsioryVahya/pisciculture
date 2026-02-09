package com.pisciculture.repository;

import com.pisciculture.model.Nutriment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NutrimentRepository extends JpaRepository<Nutriment, Long> {
}
