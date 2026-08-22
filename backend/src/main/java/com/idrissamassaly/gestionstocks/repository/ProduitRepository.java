package com.idrissamassaly.gestionstocks.repository;

import com.idrissamassaly.gestionstocks.entity.Produit;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProduitRepository extends MongoRepository<Produit, String> {
    boolean existsByReference(String reference);

    boolean existsByReferenceAndIdNot(String reference, String id);
}
