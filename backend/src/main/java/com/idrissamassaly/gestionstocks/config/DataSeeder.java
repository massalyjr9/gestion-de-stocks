package com.idrissamassaly.gestionstocks.config;

import com.idrissamassaly.gestionstocks.entity.Produit;
import com.idrissamassaly.gestionstocks.entity.Role;
import com.idrissamassaly.gestionstocks.entity.User;
import com.idrissamassaly.gestionstocks.repository.ProduitRepository;
import com.idrissamassaly.gestionstocks.repository.UserRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProduitRepository produitRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            userRepository.save(User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .build());
            userRepository.save(User.builder()
                    .username("user")
                    .password(passwordEncoder.encode("user123"))
                    .role(Role.USER)
                    .build());
        }

        if (produitRepository.count() == 0) {
            produitRepository.save(Produit.builder()
                    .reference("REF-001")
                    .nom("Clavier mécanique")
                    .categorie("Périphériques")
                    .quantite(15)
                    .seuilAlerte(5)
                    .prixUnitaire(new BigDecimal("59.90"))
                    .build());
            produitRepository.save(Produit.builder()
                    .reference("REF-002")
                    .nom("Écran 27 pouces")
                    .categorie("Périphériques")
                    .quantite(3)
                    .seuilAlerte(5)
                    .prixUnitaire(new BigDecimal("219.00"))
                    .build());
            produitRepository.save(Produit.builder()
                    .reference("REF-003")
                    .nom("Câble HDMI 2m")
                    .categorie("Accessoires")
                    .quantite(42)
                    .seuilAlerte(10)
                    .prixUnitaire(new BigDecimal("7.50"))
                    .build());
        }
    }
}
