package com.pisciculture.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "etang")
public class Etang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal longueur;
    private BigDecimal largeur;
    private Integer capacite;

    // Helper method to get area
    public BigDecimal getSurface() {
        if (longueur != null && largeur != null) {
            return longueur.multiply(largeur);
        }
        return BigDecimal.ZERO;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getLongueur() { return longueur; }
    public void setLongueur(BigDecimal longueur) { this.longueur = longueur; }

    public BigDecimal getLargeur() { return largeur; }
    public void setLargeur(BigDecimal largeur) { this.largeur = largeur; }

    public Integer getCapacite() { return capacite; }
    public void setCapacite(Integer capacite) { this.capacite = capacite; }
}
