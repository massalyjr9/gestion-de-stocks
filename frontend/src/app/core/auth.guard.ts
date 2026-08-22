import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { catchError, map, of } from 'rxjs';
import { AuthService } from './auth.service';

export const authGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isLoggedIn()) {
    return true;
  }

  const restore = authService.restoreSession();
  if (!restore) {
    return router.createUrlTree(['/connexion']);
  }

  return restore.pipe(
    map(() => true),
    catchError(() => of(router.createUrlTree(['/connexion'])))
  );
};

export const adminGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return authService.isAdmin() ? true : router.createUrlTree(['/produits']);
};
