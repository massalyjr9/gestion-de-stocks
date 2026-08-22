export interface ProduitResponse {
  id: string;
  reference: string;
  nom: string;
  categorie: string | null;
  quantite: number;
  seuilAlerte: number;
  stockBas: boolean;
  prixUnitaire: number;
  derniereMiseAJour: string;
}

export interface ProduitRequest {
  reference: string;
  nom: string;
  categorie: string | null;
  quantite: number;
  seuilAlerte: number;
  prixUnitaire: number;
}
