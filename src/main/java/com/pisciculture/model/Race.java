package com.pisciculture.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "race")
public class Race {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(name = "prix_achat_par_kg")
    private BigDecimal prixAchatParKg;

    @Column(name = "prix_vente_par_kg")
    private BigDecimal prixVenteParKg;

    @Column(name = "poids_max")
    private BigDecimal poidsMax;

    @Column(name = "capacite_augmentation_poids")
    private BigDecimal capaciteAugmentationPoids; // en grammes par jour

    @OneToMany(mappedBy = "race", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RaceNutriment> nutriments;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public BigDecimal getPrixAchatParKg() { return prixAchatParKg; }
    public void setPrixAchatParKg(BigDecimal prixAchatParKg) { this.prixAchatParKg = prixAchatParKg; }

    public BigDecimal getPrixVenteParKg() { return prixVenteParKg; }
    public void setPrixVenteParKg(BigDecimal prixVenteParKg) { this.prixVenteParKg = prixVenteParKg; }

    public BigDecimal getPoidsMax() { return poidsMax; }
    public void setPoidsMax(BigDecimal poidsMax) { this.poidsMax = poidsMax; }

    public BigDecimal getCapaciteAugmentationPoids() { return capaciteAugmentationPoids; }
    public void setCapaciteAugmentationPoids(BigDecimal capaciteAugmentationPoids) { this.capaciteAugmentationPoids = capaciteAugmentationPoids; }

    public List<RaceNutriment> getNutriments() { return nutriments; }
    public void setNutriments(List<RaceNutriment> nutriments) { this.nutriments = nutriments; }
}
