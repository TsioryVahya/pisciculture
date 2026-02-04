package com.pisciculture.repository;

import com.pisciculture.model.Etang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EtangRepository extends JpaRepository<Etang, Long> {
}
