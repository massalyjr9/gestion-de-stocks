package com.idrissamassaly.gestionstocks.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "produit", uniqueConstraints = @UniqueConstraint(columnNames = "reference"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reference;

    private String nom;

    private String categorie;

    private int quantite;

    private int seuilAlerte;

    private BigDecimal prixUnitaire;

    private Instant derniereMiseAJour;

    @PrePersist
    @PreUpdate
    private void onSave() {
        this.derniereMiseAJour = Instant.now();
    }
}
