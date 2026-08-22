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

  findById(id: number): Observable<ProduitResponse> {
    return this.http.get<ProduitResponse>(`${this.baseUrl}/${id}`);
  }

  create(request: ProduitRequest): Observable<ProduitResponse> {
    return this.http.post<ProduitResponse>(this.baseUrl, request);
  }

  update(id: number, request: ProduitRequest): Observable<ProduitResponse> {
    return this.http.put<ProduitResponse>(`${this.baseUrl}/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
