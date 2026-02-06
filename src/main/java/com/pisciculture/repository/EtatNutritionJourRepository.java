package com.pisciculture.repository;

import com.pisciculture.model.EtatNutritionJour;
import com.pisciculture.model.Poisson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EtatNutritionJourRepository extends JpaRepository<EtatNutritionJour, Long> {

    Optional<EtatNutritionJour> findByPoissonAndDateJour(Poisson poisson, LocalDate dateJour);

    Optional<EtatNutritionJour> findTopByPoissonOrderByDateJourDesc(Poisson poisson);

    List<EtatNutritionJour> findByDateJour(LocalDate dateJour);

    // Premier jour où le poisson atteint (ou dépasse) un certain poids
    Optional<EtatNutritionJour> findFirstByPoissonAndPoidsGreaterThanEqualOrderByDateJourAsc(Poisson poisson, java.math.BigDecimal poidsCible);

    List<EtatNutritionJour> findByPoissonOrderByDateJourDesc(Poisson poisson);

    Optional<EtatNutritionJour> findTopByPoissonAndDateJourLessThanEqualOrderByDateJourDesc(Poisson poisson, LocalDate dateJour);

    Optional<EtatNutritionJour> findTopByPoissonAndDateJourBeforeOrderByDateJourDesc(Poisson poisson, LocalDate dateJour);
}
