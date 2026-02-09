package com.pisciculture.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "nutriments_races")
public class RaceNutriment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idrace")
    private Race race;

    @ManyToOne
    @JoinColumn(name = "idnutriment")
    private Nutriment nutriment;

    @Column(name = "besoin_nutriment", nullable = false)
    private BigDecimal besoinNutriment;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Race getRace() { return race; }
    public void setRace(Race race) { this.race = race; }

    public Nutriment getNutriment() { return nutriment; }
    public void setNutriment(Nutriment nutriment) { this.nutriment = nutriment; }

    public BigDecimal getBesoinNutriment() { return besoinNutriment; }
    public void setBesoinNutriment(BigDecimal besoinNutriment) { this.besoinNutriment = besoinNutriment; }
}
