package com.idrissamassaly.gestionstocks.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.idrissamassaly.gestionstocks.dto.ProduitRequest;
import com.idrissamassaly.gestionstocks.dto.ProduitResponse;
import com.idrissamassaly.gestionstocks.security.AppUserDetailsService;
import com.idrissamassaly.gestionstocks.security.SecurityConfig;
import com.idrissamassaly.gestionstocks.service.ProduitService;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(ProduitController.class)
@Import(SecurityConfig.class)
class ProduitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProduitService produitService;

    @MockitoBean
    private AppUserDetailsService appUserDetailsService;

    private ProduitResponse sample() {
        return new ProduitResponse(1L, "REF-001", "Clavier", "Périphériques", 15, 5, false,
                new BigDecimal("59.90"), Instant.now());
    }

    @Test
    @WithMockUser(roles = "USER")
    void listerProduits_utilisateurAuthentifie_ok() throws Exception {
        when(produitService.findAll()).thenReturn(List.of(sample()));

        mockMvc.perform(get("/api/produits"))
                .andExpect(status().isOk());
    }

    @Test
    void listerProduits_sansAuthentification_401() throws Exception {
        mockMvc.perform(get("/api/produits"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void creerProduit_roleUser_403() throws Exception {
        ProduitRequest request = new ProduitRequest("REF-010", "Souris", "Périphériques", 10, 5,
                new BigDecimal("19.90"));

        mockMvc.perform(post("/api/produits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void creerProduit_roleAdmin_201() throws Exception {
        ProduitRequest request = new ProduitRequest("REF-010", "Souris", "Périphériques", 10, 5,
                new BigDecimal("19.90"));
        when(produitService.create(any(ProduitRequest.class))).thenReturn(sample());

        mockMvc.perform(post("/api/produits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void supprimerProduit_roleAdmin_204() throws Exception {
        mockMvc.perform(delete("/api/produits/{id}", 1L).with(csrf()))
                .andExpect(status().isNoContent());
    }

    private static org.springframework.test.web.servlet.request.RequestPostProcessor csrf() {
        return org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf();
    }
}
