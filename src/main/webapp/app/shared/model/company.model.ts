import { IBrand } from 'app/shared/model/brand.model';

export interface ICompany {
  id?: number;
  name?: string;
  address?: string | null;
  website?: string | null;
  brands?: IBrand[] | null;
}

export const defaultValue: Readonly<ICompany> = {};
