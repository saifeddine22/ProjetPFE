import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

// Interface pour les détails du service
export interface ServiceDetailsDTO {
  description: string;
  categorieId?: number;
  activiteId?: number;
  serviceType?: string;
}

// Interface pour les suggestions de mots-clés
export interface KeywordSuggestionRequest {
  description: string;
  serviceType?: string;
}

@Injectable({ providedIn: 'root' })
export class AIService {
  // Utilisez l'URL relative au lieu de SERVER_API_URL
  private resourceUrl = 'api/ai-assistance';

  constructor(private http: HttpClient) {}

  /**
   * Génère un titre basé sur la description de l'annonce
   */
  generateTitle(description: string): Observable<{ title: string }> {
    return this.http.post<{ title: string }>(`${this.resourceUrl}/generate-title`, { description });
  }

  /**
   * Améliore la description fournie avec l'IA
   */
  enhanceDescription(description: string): Observable<{ enhancedDescription: string }> {
    return this.http.post<{ enhancedDescription: string }>(`${this.resourceUrl}/enhance-description`, { description });
  }

  /**
   * Génère des mots-clés pertinents basés sur la description
   */
  generateKeywords(request: KeywordSuggestionRequest): Observable<string[]> {
    return this.http.post<string[]>(`${this.resourceUrl}/generate-keywords`, request);
  }

  /**
   * Suggère des mots-clés (alias pour generateKeywords pour correspondre à l'utilisation dans le composant)
   */
  suggestKeywords(request: KeywordSuggestionRequest): Observable<string[]> {
    return this.generateKeywords(request);
  }

  /**
   * Estime un prix pour le service en fonction de la description et des catégories
   */
  estimatePrice(serviceDetails: ServiceDetailsDTO): Observable<{ minPrice: number; maxPrice: number; currency: string }> {
    return this.http.post<{ minPrice: number; maxPrice: number; currency: string }>(`${this.resourceUrl}/estimate-price`, serviceDetails);
  }
}
