package com.idrissamassaly.gestionstocks.controller;

import com.idrissamassaly.gestionstocks.dto.ProduitRequest;
import com.idrissamassaly.gestionstocks.dto.ProduitResponse;
import com.idrissamassaly.gestionstocks.service.ProduitService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/produits")
@RequiredArgsConstructor
public class ProduitController {

    private final ProduitService produitService;

    @GetMapping
    public List<ProduitResponse> findAll() {
        return produitService.findAll();
    }

    @GetMapping("/{id}")
    public ProduitResponse findById(@PathVariable String id) {
        return produitService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProduitResponse> create(@Valid @RequestBody ProduitRequest request) {
        ProduitResponse created = produitService.create(request);
        return ResponseEntity.created(URI.create("/api/produits/" + created.id())).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ProduitResponse update(@PathVariable String id, @Valid @RequestBody ProduitRequest request) {
        return produitService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        produitService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
