package com.idrissamassaly.gestionstocks.entity;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "produits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Produit {

    @Id
    private String id;

    @Indexed(unique = true)
    private String reference;

    private String nom;

    private String categorie;

    private int quantite;

    private int seuilAlerte;

    private BigDecimal prixUnitaire;

    private Instant derniereMiseAJour;
}
