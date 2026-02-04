package com.pisciculture.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "poissons_statut")
public class PoissonStatut {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idpoisson")
    private Poisson poisson;

    @ManyToOne
    @JoinColumn(name = "idstatut")
    private Statut statut;

    @Column(name = "date_changement")
    private LocalDateTime dateChangement;

    public PoissonStatut() {
        this.dateChangement = LocalDateTime.now();
    }

    public PoissonStatut(Poisson poisson, Statut statut) {
        this();
        this.poisson = poisson;
        this.statut = statut;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Poisson getPoisson() { return poisson; }
    public void setPoisson(Poisson poisson) { this.poisson = poisson; }

    public Statut getStatut() { return statut; }
    public void setStatut(Statut statut) { this.statut = statut; }

    public LocalDateTime getDateChangement() { return dateChangement; }
    public void setDateChangement(LocalDateTime dateChangement) { this.dateChangement = dateChangement; }
}
