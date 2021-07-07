export interface ICompany {
  id?: number;
  cname?: string;
  address?: string;
  website?: string;
  email?: string;
  fax?: string;
  phoneno?: string | null;
}

export const defaultValue: Readonly<ICompany> = {};
