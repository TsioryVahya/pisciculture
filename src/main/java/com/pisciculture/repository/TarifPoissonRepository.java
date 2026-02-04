package com.pisciculture.repository;

import com.pisciculture.model.TarifPoisson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TarifPoissonRepository extends JpaRepository<TarifPoisson, Long> {
    List<TarifPoisson> findByRaceIdOrderByDateDesc(Long raceId);
}
