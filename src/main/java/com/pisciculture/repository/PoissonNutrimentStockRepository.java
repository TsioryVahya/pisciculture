package com.pisciculture.repository;

import com.pisciculture.model.Nutriment;
import com.pisciculture.model.Poisson;
import com.pisciculture.model.PoissonNutrimentStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PoissonNutrimentStockRepository extends JpaRepository<PoissonNutrimentStock, Long> {
    Optional<PoissonNutrimentStock> findByPoissonAndNutrimentAndDateJour(Poisson poisson, Nutriment nutriment, LocalDate dateJour);
    List<PoissonNutrimentStock> findByPoissonAndDateJour(Poisson poisson, LocalDate dateJour);
    Optional<PoissonNutrimentStock> findTopByPoissonAndNutrimentOrderByDateJourDesc(Poisson poisson, Nutriment nutriment);
}
