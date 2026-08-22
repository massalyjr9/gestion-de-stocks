package com.idrissamassaly.gestionstocks.repository;

import com.idrissamassaly.gestionstocks.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
    boolean existsByReference(String reference);
}
