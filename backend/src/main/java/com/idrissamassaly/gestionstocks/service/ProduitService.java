package com.idrissamassaly.gestionstocks.service;

import com.idrissamassaly.gestionstocks.dto.ProduitRequest;
import com.idrissamassaly.gestionstocks.dto.ProduitResponse;
import com.idrissamassaly.gestionstocks.entity.Produit;
import com.idrissamassaly.gestionstocks.exception.DuplicateReferenceException;
import com.idrissamassaly.gestionstocks.exception.ResourceNotFoundException;
import com.idrissamassaly.gestionstocks.repository.ProduitRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProduitService {

    private final ProduitRepository produitRepository;

    public List<ProduitResponse> findAll() {
        return produitRepository.findAll().stream()
                .map(ProduitResponse::from)
                .toList();
    }

    public ProduitResponse findById(Long id) {
        return ProduitResponse.from(getOrThrow(id));
    }

    public ProduitResponse create(ProduitRequest request) {
        if (produitRepository.existsByReference(request.reference())) {
            throw new DuplicateReferenceException(
                    "Un produit avec la référence '" + request.reference() + "' existe déjà");
        }
        Produit produit = Produit.builder()
                .reference(request.reference())
                .nom(request.nom())
                .categorie(request.categorie())
                .quantite(request.quantite())
                .seuilAlerte(request.seuilAlerte())
                .prixUnitaire(request.prixUnitaire())
                .build();
        return ProduitResponse.from(produitRepository.save(produit));
    }

    public ProduitResponse update(Long id, ProduitRequest request) {
        Produit produit = getOrThrow(id);
        produitRepository.findAll().stream()
                .filter(p -> !p.getId().equals(id) && p.getReference().equals(request.reference()))
                .findAny()
                .ifPresent(p -> {
                    throw new DuplicateReferenceException(
                            "Un produit avec la référence '" + request.reference() + "' existe déjà");
                });

        produit.setReference(request.reference());
        produit.setNom(request.nom());
        produit.setCategorie(request.categorie());
        produit.setQuantite(request.quantite());
        produit.setSeuilAlerte(request.seuilAlerte());
        produit.setPrixUnitaire(request.prixUnitaire());
        return ProduitResponse.from(produitRepository.save(produit));
    }

    public void delete(Long id) {
        if (!produitRepository.existsById(id)) {
            throw new ResourceNotFoundException("Produit introuvable avec l'id " + id);
        }
        produitRepository.deleteById(id);
    }

    private Produit getOrThrow(Long id) {
        return produitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit introuvable avec l'id " + id));
    }
}
