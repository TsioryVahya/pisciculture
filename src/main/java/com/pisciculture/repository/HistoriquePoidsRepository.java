package com.pisciculture.repository;

import com.pisciculture.model.HistoriquePoids;
import com.pisciculture.model.Poisson;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HistoriquePoidsRepository extends JpaRepository<HistoriquePoids, Long> {
    List<HistoriquePoids> findByPoissonOrderByDateMesureDesc(Poisson poisson);
}
