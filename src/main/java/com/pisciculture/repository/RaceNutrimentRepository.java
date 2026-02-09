package com.pisciculture.repository;

import com.pisciculture.model.RaceNutriment;
import com.pisciculture.model.Race;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RaceNutrimentRepository extends JpaRepository<RaceNutriment, Long> {
    List<RaceNutriment> findByRace(Race race);
    void deleteByRace(Race race);
}
