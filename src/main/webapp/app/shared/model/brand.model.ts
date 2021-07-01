import dayjs from 'dayjs';
import { ICompany } from 'app/shared/model/company.model';
import { IGenerics } from 'app/shared/model/generics.model';
import { BrandType } from 'app/shared/model/enumerations/brand-type.model';
import { TypeUnit } from 'app/shared/model/enumerations/type-unit.model';

export interface IBrand {
  id?: number;
  bname?: string;
  price?: number;
  date?: string;
  packageunit?: number;
  type?: BrandType;
  typeunit?: TypeUnit;
  companyofMedicine?: ICompany;
  generics?: IGenerics[];
}

export const defaultValue: Readonly<IBrand> = {};
