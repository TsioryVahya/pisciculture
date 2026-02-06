package com.pisciculture.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "historique_poids")
public class HistoriquePoids {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_poisson")
    private Poisson poisson;

    @Column(name = "date_mesure")
    private LocalDate dateMesure;

    @Column(name = "poids")
    private BigDecimal poids;

    public HistoriquePoids() {}

    public HistoriquePoids(Poisson poisson, LocalDate dateMesure, BigDecimal poids) {
        this.poisson = poisson;
        this.dateMesure = dateMesure;
        this.poids = poids;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Poisson getPoisson() { return poisson; }
    public void setPoisson(Poisson poisson) { this.poisson = poisson; }

    public LocalDate getDateMesure() { return dateMesure; }
    public void setDateMesure(LocalDate dateMesure) { this.dateMesure = dateMesure; }

    public BigDecimal getPoids() { return poids; }
    public void setPoids(BigDecimal poids) { this.poids = poids; }
}
