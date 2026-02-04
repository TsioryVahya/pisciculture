package com.pisciculture.repository;

import com.pisciculture.model.TypePrix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TypePrixRepository extends JpaRepository<TypePrix, Long> {
    Optional<TypePrix> findByNom(String nom);
}
