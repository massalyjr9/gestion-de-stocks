import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ProduitRequest, ProduitResponse } from './produit.model';

@Injectable({ providedIn: 'root' })
export class ProduitService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/produits`;

  findAll(): Observable<ProduitResponse[]> {
    return this.http.get<ProduitResponse[]>(this.baseUrl);
  }

  findById(id: string): Observable<ProduitResponse> {
    return this.http.get<ProduitResponse>(`${this.baseUrl}/${id}`);
  }

  create(request: ProduitRequest): Observable<ProduitResponse> {
    return this.http.post<ProduitResponse>(this.baseUrl, request);
  }

  update(id: string, request: ProduitRequest): Observable<ProduitResponse> {
    return this.http.put<ProduitResponse>(`${this.baseUrl}/${id}`, request);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
