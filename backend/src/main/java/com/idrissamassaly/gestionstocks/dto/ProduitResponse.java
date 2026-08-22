package com.idrissamassaly.gestionstocks.dto;

import com.idrissamassaly.gestionstocks.entity.Produit;
import java.math.BigDecimal;
import java.time.Instant;

public record ProduitResponse(
        Long id,
        String reference,
        String nom,
        String categorie,
        int quantite,
        int seuilAlerte,
        boolean stockBas,
        BigDecimal prixUnitaire,
        Instant derniereMiseAJour
) {
    public static ProduitResponse from(Produit produit) {
        return new ProduitResponse(
                produit.getId(),
                produit.getReference(),
                produit.getNom(),
                produit.getCategorie(),
                produit.getQuantite(),
                produit.getSeuilAlerte(),
                produit.getQuantite() <= produit.getSeuilAlerte(),
                produit.getPrixUnitaire(),
                produit.getDerniereMiseAJour()
        );
    }
}
