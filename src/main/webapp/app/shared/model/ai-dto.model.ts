export interface DescriptionDTO {
  description: string;
  serviceType: string;
}

export interface EnhancedDescriptionDTO {
  enhancedDescription: string;
}

export interface SuggestedTitleDTO {
  title: string;
}

export interface ServiceDetailsDTO {
  serviceType: string;
  location: string;
  description: string;
}

export interface PriceEstimationDTO {
  minPrice: number;
  maxPrice: number;
  currency: string;
}
