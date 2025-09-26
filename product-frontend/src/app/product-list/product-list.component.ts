import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Observable } from 'rxjs';
import { Product } from '../models/product';
import { ProductService } from '../services/product.service';
import { ProductFormComponent } from '../product-form/product-form.component';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, ProductFormComponent],
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.scss']
})
export class ProductListComponent implements OnInit {
  products$: Observable<Product[]>;
  showForm = false;
  selectedProduct: Product | null = null;

  constructor(private productService: ProductService) {
    this.products$ = this.productService.products$;
  }

  ngOnInit(): void {
    this.productService.loadProducts().subscribe();
  }

  onAddProduct(): void {
    this.showForm = true;
  }

  onFormClose(): void {
    this.showForm = false;
    this.selectedProduct = null;
  }
  onEditProduct(product: Product): void {
    this.selectedProduct = { ...product };
    this.showForm = true;
  }
  onDeleteProduct(product: Product): void {
    if (confirm(`Supprimer "${product.nom}" ?`)) {
      this.productService.deleteProduct(product.id!).subscribe({
        next: () => console.log('Produit supprimé'),
        error: (err) => console.error('Erreur suppression:', err)
      });
    }
  }

  getCategoryBadgeClass(category: string): string {
    switch (category) {
      case 'Informatique': return 'bg-info';
      case 'Véhicule': return 'bg-warning';
      case 'Alimentaire': return 'bg-success';
      default: return 'bg-secondary';
    }
  }

  getSpecificField(product: Product): string {
    switch (product.categorie) {
      case 'Informatique': return product.reference || '-';
      case 'Véhicule': return product.matricule || '-';
      case 'Alimentaire': return product.dateExpiration || '-';
      default: return '-';
    }
  }
}
