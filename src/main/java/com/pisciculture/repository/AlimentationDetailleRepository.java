package com.pisciculture.repository;

import com.pisciculture.model.Alimentation;
import com.pisciculture.model.AlimentationDetaille;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlimentationDetailleRepository extends JpaRepository<AlimentationDetaille, Long> {
    List<AlimentationDetaille> findByAlimentation(Alimentation alimentation);
}
