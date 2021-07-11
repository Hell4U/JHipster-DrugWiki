export interface IIngredients {
  id?: number;
  iname?: string;
  symptoms?: string;
  sideeffects?: string;
  cautions?: string;
}

export const defaultValue: Readonly<IIngredients> = {};
