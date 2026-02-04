package com.pisciculture.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "poissons")
public class Poisson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @Column(name = "poids_initial")
    private BigDecimal poidsInitial;

    @ManyToOne
    @JoinColumn(name = "idrace")
    private Race race;

    @Transient
    private Statut currentStatut;

    @Transient
    private Etang currentEtang;

    @Transient
    private BigDecimal currentPoids;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public BigDecimal getPoidsInitial() { return poidsInitial; }
    public void setPoidsInitial(BigDecimal poidsInitial) { this.poidsInitial = poidsInitial; }

    public Race getRace() { return race; }
    public void setRace(Race race) { this.race = race; }

    public Statut getCurrentStatut() { return currentStatut; }
    public void setCurrentStatut(Statut currentStatut) { this.currentStatut = currentStatut; }

    public Etang getCurrentEtang() { return currentEtang; }
    public void setCurrentEtang(Etang currentEtang) { this.currentEtang = currentEtang; }

    public BigDecimal getCurrentPoids() { return currentPoids; }
    public void setCurrentPoids(BigDecimal currentPoids) { this.currentPoids = currentPoids; }
}
