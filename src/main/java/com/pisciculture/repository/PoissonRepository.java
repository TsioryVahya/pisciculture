package com.pisciculture.repository;

import com.pisciculture.model.Poisson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoissonRepository extends JpaRepository<Poisson, Long> {
}
