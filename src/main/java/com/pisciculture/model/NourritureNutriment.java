package com.pisciculture.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "nutriments_nourritures")
public class NourritureNutriment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idnourriture")
    private Nourriture nourriture;

    @ManyToOne
    @JoinColumn(name = "idnutriment")
    private Nutriment nutriment;

    @Column(name = "pourcentage_apport_nutriment", nullable = false)
    private BigDecimal pourcentageApportNutriment;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Nourriture getNourriture() { return nourriture; }
    public void setNourriture(Nourriture nourriture) { this.nourriture = nourriture; }

    public Nutriment getNutriment() { return nutriment; }
    public void setNutriment(Nutriment nutriment) { this.nutriment = nutriment; }

    public BigDecimal getPourcentageApportNutriment() { return pourcentageApportNutriment; }
    public void setPourcentageApportNutriment(BigDecimal pourcentageApportNutriment) { this.pourcentageApportNutriment = pourcentageApportNutriment; }
}
