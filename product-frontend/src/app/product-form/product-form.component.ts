import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProductService } from '../services/product.service';
import { Component, EventEmitter, OnInit, Output, Input } from '@angular/core';
import {Product} from '../models/product';

@Component({
  selector: 'app-product-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './product-form.component.html',
  styleUrls: ['./product-form.component.scss']
})
export class ProductFormComponent implements OnInit {
  @Output() formClose = new EventEmitter<void>();
  @Input() product: Product | null = null;

  productForm: FormGroup;
  selectedFile: File | null = null;
  filePreview: string | null = null;
  isSubmitting = false;
  categories = ['Informatique', 'Véhicule', 'Alimentaire'];

  constructor(
    private fb: FormBuilder,
    private productService: ProductService
  ) {
    this.productForm = this.createForm();
  }

  ngOnInit(): void {
    if (this.product) {
      this.populateForm();
    }

    // Listenner sur catégorie pour validation dynamique
    this.productForm.get('categorie')?.valueChanges.subscribe(category => {
      this.updateValidators(category);
    });
  }

  private createForm(): FormGroup {
    return this.fb.group({
      nom: ['', [Validators.required, Validators.maxLength(100)]],
      prix: ['', [Validators.required, Validators.min(0.01)]],
      categorie: ['', [Validators.required]],
      reference: [''],
      matricule: [''],
      dateExpiration: ['']
    });
  }

  private updateValidators(category: string): void {
    const referenceControl = this.productForm.get('reference');
    const matriculeControl = this.productForm.get('matricule');
    const dateExpirationControl = this.productForm.get('dateExpiration');

    referenceControl?.clearValidators();
    matriculeControl?.clearValidators();
    dateExpirationControl?.clearValidators();

    switch (category) {
      case 'Informatique':
        referenceControl?.setValidators([Validators.required]);
        break;
      case 'Véhicule':
        matriculeControl?.setValidators([Validators.required]);
        break;
      case 'Alimentaire':
        dateExpirationControl?.setValidators([Validators.required]);
        break;
    }

    referenceControl?.updateValueAndValidity();
    matriculeControl?.updateValueAndValidity();
    dateExpirationControl?.updateValueAndValidity();
  }

  onFileSelected(event: any): void {
    const file = event.target.files?.[0];
    if (file) {
      this.selectedFile = file;

      if (file.type.startsWith('image/')) {
        const reader = new FileReader();
        reader.onload = (e) => {
          this.filePreview = e.target?.result as string;
        };
        reader.readAsDataURL(file);
      } else {
        this.filePreview = null;
      }
    }
  }

  onRemoveFile(): void {
    this.selectedFile = null;
    this.filePreview = null;
    const fileInput = document.getElementById('fichier') as HTMLInputElement;
    if (fileInput) fileInput.value = '';
  }

  onSubmit(): void {
    if (this.productForm.invalid) {
      this.markFormGroupTouched();
      return;
    }

    this.isSubmitting = true;

    const formData = new FormData();
    const formValue = this.productForm.value;

    formData.append('nom', formValue.nom);
    formData.append('prix', formValue.prix);
    formData.append('categorie', formValue.categorie);

    if (formValue.reference) formData.append('reference', formValue.reference);
    if (formValue.matricule) formData.append('matricule', formValue.matricule);
    if (formValue.dateExpiration) formData.append('dateExpiration', formValue.dateExpiration);
    if (this.selectedFile) formData.append('fichier', this.selectedFile);

    const operation = this.product
      ? this.productService.updateProduct(this.product.id!, formData)
      : this.productService.createProduct(formData);

    operation.subscribe({
      next: (result) => {
        console.log(this.product ? 'Produit modifié:' : 'Produit créé:', result);
        this.resetForm();
        this.formClose.emit();
      },
      error: (error) => {
        console.error('Erreur:', error);
        this.isSubmitting = false;
      }
    });
  }

  onCancel(): void {
    this.resetForm();
    this.formClose.emit();
  }

  private resetForm(): void {
    this.productForm.reset();
    this.selectedFile = null;
    this.filePreview = null;
    this.isSubmitting = false;
  }

  private markFormGroupTouched(): void {
    Object.keys(this.productForm.controls).forEach(key => {
      const control = this.productForm.get(key);
      control?.markAsTouched();
    });
  }

  get nom() { return this.productForm.get('nom'); }
  get prix() { return this.productForm.get('prix'); }
  get categorie() { return this.productForm.get('categorie'); }
  get reference() { return this.productForm.get('reference'); }
  get matricule() { return this.productForm.get('matricule'); }
  get dateExpiration() { return this.productForm.get('dateExpiration'); }

  get shouldShowReference(): boolean {
    return this.productForm.get('categorie')?.value === 'Informatique';
  }

  get shouldShowMatricule(): boolean {
    return this.productForm.get('categorie')?.value === 'Véhicule';
  }

  get shouldShowDateExpiration(): boolean {
    return this.productForm.get('categorie')?.value === 'Alimentaire';
  }

  private populateForm(): void {
    if (this.product) {
      this.productForm.patchValue({
        nom: this.product.nom,
        prix: this.product.prix,
        categorie: this.product.categorie,
        reference: this.product.reference || '',
        matricule: this.product.matricule || '',
        dateExpiration: this.product.dateExpiration || ''
      });
      this.updateValidators(this.product.categorie);
    }
  }
}
