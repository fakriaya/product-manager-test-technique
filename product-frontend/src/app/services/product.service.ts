import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { map, tap, catchError } from 'rxjs/operators';
import { Product } from '../models/product';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiUrl = 'http://localhost:8081/api/produits';
  private productsSubject = new BehaviorSubject<Product[]>([]);
  public products$ = this.productsSubject.asObservable();

  constructor(private http: HttpClient) {
    this.loadProducts();
  }

  loadProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.apiUrl).pipe(
      tap(products => this.productsSubject.next(products))
    );
  }

  createProduct(formData: FormData): Observable<Product> {
    return this.http.post<Product>(this.apiUrl, formData).pipe(
      tap(newProduct => {
        const current = this.productsSubject.value;
        this.productsSubject.next([...current, newProduct]);
      }),
      catchError(error => {
        console.error('Erreur création produit:', error);
        return throwError(() => error);
      })
    );
  }

  updateProduct(id: number, formData: FormData): Observable<Product> {
    return this.http.put<Product>(`${this.apiUrl}/${id}`, formData).pipe(
      tap(updatedProduct => {
        const current = this.productsSubject.value;
        const index = current.findIndex(p => p.id === id);
        if (index !== -1) {
          current[index] = updatedProduct;
          this.productsSubject.next([...current]);
        }
      }),
      catchError(error => {
        console.error('Erreur mise à jour produit:', error);
        return throwError(() => error);
      })
    );
  }

  deleteProduct(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`).pipe(
      tap(() => {
        const current = this.productsSubject.value;
        this.productsSubject.next(current.filter(p => p.id !== id));
      }),
      catchError(error => {
        console.error('Erreur suppression produit:', error);
        return throwError(() => error);
      })
    );
  }
}
