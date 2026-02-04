package com.pisciculture.repository;

import com.pisciculture.model.Poisson;
import com.pisciculture.model.PoissonStatut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PoissonStatutRepository extends JpaRepository<PoissonStatut, Long> {
    @Query("SELECT ps FROM PoissonStatut ps WHERE ps.poisson = ?1 ORDER BY ps.dateChangement DESC LIMIT 1")
    Optional<PoissonStatut> findTopByPoissonOrderByDateChangementDesc(Poisson poisson);

    List<PoissonStatut> findByPoissonOrderByDateChangementDesc(Poisson poisson);
}
