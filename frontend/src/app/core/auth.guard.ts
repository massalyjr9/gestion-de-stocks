import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { Observable, catchError, map, of } from 'rxjs';
import { AuthService } from './auth.service';

function ensureSessionRestored(authService: AuthService): Observable<boolean> {
  if (authService.isLoggedIn()) {
    return of(true);
  }

  const restore = authService.restoreSession();
  if (!restore) {
    return of(false);
  }

  return restore.pipe(
    map(() => true),
    catchError(() => of(false))
  );
}

export const authGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return ensureSessionRestored(authService).pipe(
    map((loggedIn) => (loggedIn ? true : router.createUrlTree(['/connexion'])))
  );
};

export const adminGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return ensureSessionRestored(authService).pipe(
    map((loggedIn) => {
      if (!loggedIn) {
        return router.createUrlTree(['/connexion']);
      }
      return authService.isAdmin() ? true : router.createUrlTree(['/produits']);
    })
  );
};
