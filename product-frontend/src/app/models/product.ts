export interface Product {
  id?: number;
  nom: string;
  prix: number;
  categorie: 'Informatique' | 'Véhicule' | 'Alimentaire';
  reference?: string;
  matricule?: string;
  dateExpiration?: string;
  fichier?: string;
}

export interface ApiResponse<T> {
  success?: boolean;
  data?: T;
  message?: string;
}
