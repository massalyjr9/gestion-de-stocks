import { HttpClient } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';

export interface CurrentUser {
  username: string;
  role: 'ADMIN' | 'USER';
}

const STORAGE_KEY = 'gestion-stocks.credentials';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);

  private readonly currentUserSignal = signal<CurrentUser | null>(null);
  readonly currentUser = this.currentUserSignal.asReadonly();
  readonly isLoggedIn = computed(() => this.currentUserSignal() !== null);
  readonly isAdmin = computed(() => this.currentUserSignal()?.role === 'ADMIN');

  getAuthHeader(): string | null {
    const credentials = sessionStorage.getItem(STORAGE_KEY);
    return credentials ? `Basic ${credentials}` : null;
  }

  login(username: string, password: string): Observable<CurrentUser> {
    const encoded = btoa(`${username}:${password}`);
    sessionStorage.setItem(STORAGE_KEY, encoded);

    return this.http.get<CurrentUser>(`${environment.apiUrl}/auth/me`).pipe(
      tap({
        next: (user) => this.currentUserSignal.set(user),
        error: () => sessionStorage.removeItem(STORAGE_KEY),
      })
    );
  }

  restoreSession(): Observable<CurrentUser> | null {
    if (!sessionStorage.getItem(STORAGE_KEY)) {
      return null;
    }
    return this.http.get<CurrentUser>(`${environment.apiUrl}/auth/me`).pipe(
      tap({
        next: (user) => this.currentUserSignal.set(user),
        error: () => sessionStorage.removeItem(STORAGE_KEY),
      })
    );
  }

  logout(): void {
    sessionStorage.removeItem(STORAGE_KEY);
    this.currentUserSignal.set(null);
  }
}
