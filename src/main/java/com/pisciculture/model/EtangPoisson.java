package com.pisciculture.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "etang_poisson")
public class EtangPoisson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_poisson")
    private Poisson poisson;

    @ManyToOne
    @JoinColumn(name = "id_etang")
    private Etang etang;

    private LocalDate date;

    public EtangPoisson() {
        this.date = LocalDate.now();
    }

    public EtangPoisson(Poisson poisson, Etang etang) {
        this();
        this.poisson = poisson;
        this.etang = etang;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Poisson getPoisson() { return poisson; }
    public void setPoisson(Poisson poisson) { this.poisson = poisson; }

    public Etang getEtang() { return etang; }
    public void setEtang(Etang etang) { this.etang = etang; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}
