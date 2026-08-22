package com.idrissamassaly.gestionstocks.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ProduitRequest(
        @NotBlank String reference,
        @NotBlank String nom,
        String categorie,
        @NotNull @Min(0) Integer quantite,
        @NotNull @Min(0) Integer seuilAlerte,
        @NotNull @DecimalMin(value = "0.0", inclusive = true) BigDecimal prixUnitaire
) {
}
