package com.pisciculture.repository;

import com.pisciculture.model.NourritureNutriment;
import com.pisciculture.model.Nourriture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NourritureNutrimentRepository extends JpaRepository<NourritureNutriment, Long> {
    List<NourritureNutriment> findByNourriture(Nourriture nourriture);
    void deleteByNourriture(Nourriture nourriture);
}
