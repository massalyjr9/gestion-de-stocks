import { Routes } from '@angular/router';
import { adminGuard, authGuard } from './core/auth.guard';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'produits' },
  {
    path: 'connexion',
    loadComponent: () => import('./auth/login/login').then((m) => m.Login),
  },
  {
    path: 'produits',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./produits/produit-list/produit-list').then((m) => m.ProduitList),
  },
  {
    path: 'produits/nouveau',
    canActivate: [authGuard, adminGuard],
    loadComponent: () =>
      import('./produits/produit-form/produit-form').then((m) => m.ProduitForm),
  },
  {
    path: 'produits/:id/modifier',
    canActivate: [authGuard, adminGuard],
    loadComponent: () =>
      import('./produits/produit-form/produit-form').then((m) => m.ProduitForm),
  },
  { path: '**', redirectTo: 'produits' },
];
