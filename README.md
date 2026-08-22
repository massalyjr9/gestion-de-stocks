# Gestion de stocks

Application full stack de gestion de stocks (CRUD produits, rôles utilisateurs, alertes de stock bas).

## Stack technique

- **Backend** : Java 21, Spring Boot 4, Spring Data JPA, Spring Security (rôles ADMIN/USER), Bean Validation, PostgreSQL / H2, JUnit 5, Mockito
- **Frontend** : Angular 21 (standalone components, signals), TypeScript, Reactive Forms
- **DevOps** : Docker, Docker Compose, GitHub Actions (CI)

## Fonctionnalités

- Authentification par rôle (ADMIN / USER) via HTTP Basic
- CRUD complet des produits (référence, nom, catégorie, quantité, seuil d'alerte, prix)
- Détection automatique du stock bas (quantité ≤ seuil d'alerte)
- Accès en lecture pour tous les utilisateurs authentifiés, écriture réservée aux ADMIN
- Tests unitaires backend (service + contrôleur) et frontend

## Lancer en local (sans Docker)

### Backend

```bash
cd backend
./mvnw spring-boot:run
```

L'API démarre sur `http://localhost:8080` avec une base H2 en mémoire, préremplie avec :
- `admin` / `admin123` (rôle ADMIN)
- `user` / `user123` (rôle USER)

### Frontend

```bash
cd frontend
npm install
npm start
```

L'application est disponible sur `http://localhost:4200`.

## Lancer avec Docker Compose

```bash
docker compose up --build
```

- Frontend : `http://localhost:4200`
- Backend : `http://localhost:8080`
- PostgreSQL persisté dans un volume Docker

## Tests

```bash
# Backend
cd backend && ./mvnw test

# Frontend
cd frontend && npm test -- --watch=false
```

## CI/CD

Un pipeline GitHub Actions ([.github/workflows/ci.yml](.github/workflows/ci.yml)) compile et teste automatiquement le backend et le frontend à chaque push/PR sur `main`.
