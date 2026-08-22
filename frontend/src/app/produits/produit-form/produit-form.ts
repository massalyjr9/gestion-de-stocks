import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ProduitService } from '../produit.service';

@Component({
  selector: 'app-produit-form',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './produit-form.html',
  styleUrl: './produit-form.scss',
})
export class ProduitForm implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly produitService = inject(ProduitService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  private produitId: string | null = null;
  readonly isEditMode = signal(false);
  readonly errorMessage = signal<string | null>(null);
  readonly saving = signal(false);

  readonly form = this.fb.nonNullable.group({
    reference: ['', Validators.required],
    nom: ['', Validators.required],
    categorie: [''],
    quantite: [0, [Validators.required, Validators.min(0)]],
    seuilAlerte: [0, [Validators.required, Validators.min(0)]],
    prixUnitaire: [0, [Validators.required, Validators.min(0)]],
  });

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (!idParam) {
      return;
    }

    this.produitId = idParam;
    this.isEditMode.set(true);
    this.produitService.findById(this.produitId).subscribe({
      next: (produit) =>
        this.form.patchValue({
          reference: produit.reference,
          nom: produit.nom,
          categorie: produit.categorie ?? '',
          quantite: produit.quantite,
          seuilAlerte: produit.seuilAlerte,
          prixUnitaire: produit.prixUnitaire,
        }),
      error: () => this.errorMessage.set('Produit introuvable'),
    });
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.saving.set(true);
    this.errorMessage.set(null);
    const request = this.form.getRawValue();

    const result$ = this.isEditMode()
      ? this.produitService.update(this.produitId!, request)
      : this.produitService.create(request);

    result$.subscribe({
      next: () => this.router.navigateByUrl('/produits'),
      error: (err) => {
        this.saving.set(false);
        this.errorMessage.set(err?.error?.message ?? 'Une erreur est survenue');
      },
    });
  }
}
