package com.idrissamassaly.gestionstocks.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.idrissamassaly.gestionstocks.dto.ProduitRequest;
import com.idrissamassaly.gestionstocks.dto.ProduitResponse;
import com.idrissamassaly.gestionstocks.entity.Produit;
import com.idrissamassaly.gestionstocks.exception.DuplicateReferenceException;
import com.idrissamassaly.gestionstocks.exception.ResourceNotFoundException;
import com.idrissamassaly.gestionstocks.repository.ProduitRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProduitServiceTest {

    @Mock
    private ProduitRepository produitRepository;

    @InjectMocks
    private ProduitService produitService;

    private Produit produit() {
        return Produit.builder()
                .id(1L)
                .reference("REF-001")
                .nom("Clavier")
                .categorie("Périphériques")
                .quantite(2)
                .seuilAlerte(5)
                .prixUnitaire(new BigDecimal("59.90"))
                .build();
    }

    @Test
    void findById_retourneLeProduitEtMarqueStockBas() {
        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit()));

        ProduitResponse response = produitService.findById(1L);

        assertThat(response.reference()).isEqualTo("REF-001");
        assertThat(response.stockBas()).isTrue();
    }

    @Test
    void findById_produitInconnu_leveResourceNotFound() {
        when(produitRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> produitService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void create_referenceDejaExistante_leveDuplicateReference() {
        ProduitRequest request = new ProduitRequest("REF-001", "Clavier", "Périphériques", 10, 5,
                new BigDecimal("59.90"));
        when(produitRepository.existsByReference("REF-001")).thenReturn(true);

        assertThatThrownBy(() -> produitService.create(request))
                .isInstanceOf(DuplicateReferenceException.class);
    }

    @Test
    void create_referenceUnique_sauvegardeEtRetourneLeProduit() {
        ProduitRequest request = new ProduitRequest("REF-002", "Souris", "Périphériques", 20, 5,
                new BigDecimal("19.90"));
        when(produitRepository.existsByReference("REF-002")).thenReturn(false);
        when(produitRepository.save(any(Produit.class))).thenAnswer(invocation -> {
            Produit p = invocation.getArgument(0);
            p.setId(2L);
            return p;
        });

        ProduitResponse response = produitService.create(request);

        assertThat(response.id()).isEqualTo(2L);
        assertThat(response.nom()).isEqualTo("Souris");
    }

    @Test
    void delete_produitExistant_supprime() {
        when(produitRepository.existsById(1L)).thenReturn(true);

        produitService.delete(1L);

        verify(produitRepository).deleteById(1L);
    }

    @Test
    void delete_produitInconnu_leveResourceNotFound() {
        when(produitRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> produitService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void findAll_retourneLaListeMappee() {
        when(produitRepository.findAll()).thenReturn(List.of(produit()));

        List<ProduitResponse> responses = produitService.findAll();

        assertThat(responses).hasSize(1);
    }
}
