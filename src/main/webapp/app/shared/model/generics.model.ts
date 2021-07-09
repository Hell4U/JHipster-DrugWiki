import { IIngredients } from 'app/shared/model/ingredients.model';
import { IBrand } from 'app/shared/model/brand.model';
import { DosageUnit } from 'app/shared/model/enumerations/dosage-unit.model';

export interface IGenerics {
  id?: number;
  gname?: string;
  dosage?: number;
  dosageunit?: DosageUnit;
  ingredientsused?: IIngredients;
  ids?: IBrand[] | null;
}

export const defaultValue: Readonly<IGenerics> = {};
