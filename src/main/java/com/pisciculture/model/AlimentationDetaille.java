package com.pisciculture.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "alimentation_detaille")
public class AlimentationDetaille {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idalimentation")
    private Alimentation alimentation;

    @ManyToOne
    @JoinColumn(name = "idnourriture")
    private Nourriture nourriture;

    @Column(name = "quantite_kg")
    private BigDecimal quantiteKg;

    @Column(name = "prixkilonourriture")
    private BigDecimal prixKiloNourriture;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Alimentation getAlimentation() { return alimentation; }
    public void setAlimentation(Alimentation alimentation) { this.alimentation = alimentation; }

    public Nourriture getNourriture() { return nourriture; }
    public void setNourriture(Nourriture nourriture) { this.nourriture = nourriture; }

    public BigDecimal getQuantiteKg() { return quantiteKg; }
    public void setQuantiteKg(BigDecimal quantiteKg) { this.quantiteKg = quantiteKg; }

    public BigDecimal getPrixKiloNourriture() { return prixKiloNourriture; }
    public void setPrixKiloNourriture(BigDecimal prixKiloNourriture) { this.prixKiloNourriture = prixKiloNourriture; }
}
