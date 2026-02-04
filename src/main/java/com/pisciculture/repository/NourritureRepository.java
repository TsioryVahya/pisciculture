package com.pisciculture.repository;

import com.pisciculture.model.Nourriture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NourritureRepository extends JpaRepository<Nourriture, Long> {
}
