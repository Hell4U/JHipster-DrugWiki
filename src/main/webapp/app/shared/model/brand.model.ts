import { ICompany } from 'app/shared/model/company.model';

export interface IBrand {
  id?: number;
  name?: string;
  price?: number | null;
  company?: ICompany | null;
}

export const defaultValue: Readonly<IBrand> = {};
