package com.pisciculture.repository;

import com.pisciculture.model.Etang;
import com.pisciculture.model.EtangPoisson;
import com.pisciculture.model.Poisson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EtangPoissonRepository extends JpaRepository<EtangPoisson, Long> {
    @Query("SELECT ep FROM EtangPoisson ep WHERE ep.poisson = ?1 ORDER BY ep.date DESC, ep.id DESC LIMIT 1")
    Optional<EtangPoisson> findTopByPoissonOrderByDateDesc(Poisson poisson);

    List<EtangPoisson> findByEtang(Etang etang);

    List<EtangPoisson> findByPoissonOrderByDateDesc(Poisson poisson);
}
