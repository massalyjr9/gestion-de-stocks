import { DecimalPipe } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../core/auth.service';
import { ProduitResponse } from '../produit.model';
import { ProduitService } from '../produit.service';

@Component({
  selector: 'app-produit-list',
  imports: [RouterLink, DecimalPipe],
  templateUrl: './produit-list.html',
  styleUrl: './produit-list.scss',
})
export class ProduitList implements OnInit {
  private readonly produitService = inject(ProduitService);
  protected readonly authService = inject(AuthService);

  readonly produits = signal<ProduitResponse[]>([]);
  readonly loading = signal(true);
  readonly errorMessage = signal<string | null>(null);

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.produitService.findAll().subscribe({
      next: (produits) => {
        this.produits.set(produits);
        this.loading.set(false);
      },
      error: () => {
        this.errorMessage.set('Impossible de charger les produits');
        this.loading.set(false);
      },
    });
  }

  remove(produit: ProduitResponse): void {
    if (!confirm(`Supprimer le produit "${produit.nom}" ?`)) {
      return;
    }
    this.produitService.delete(produit.id).subscribe({
      next: () => this.produits.update((list) => list.filter((p) => p.id !== produit.id)),
      error: () => this.errorMessage.set('Suppression impossible'),
    });
  }
}
