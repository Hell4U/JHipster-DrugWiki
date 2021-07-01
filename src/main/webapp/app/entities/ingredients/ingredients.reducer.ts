import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IIngredients, defaultValue } from 'app/shared/model/ingredients.model';

export const ACTION_TYPES = {
  FETCH_INGREDIENTS_LIST: 'ingredients/FETCH_INGREDIENTS_LIST',
  FETCH_INGREDIENTS: 'ingredients/FETCH_INGREDIENTS',
  CREATE_INGREDIENTS: 'ingredients/CREATE_INGREDIENTS',
  UPDATE_INGREDIENTS: 'ingredients/UPDATE_INGREDIENTS',
  PARTIAL_UPDATE_INGREDIENTS: 'ingredients/PARTIAL_UPDATE_INGREDIENTS',
  DELETE_INGREDIENTS: 'ingredients/DELETE_INGREDIENTS',
  RESET: 'ingredients/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IIngredients>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type IngredientsState = Readonly<typeof initialState>;

// Reducer

export default (state: IngredientsState = initialState, action): IngredientsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_INGREDIENTS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_INGREDIENTS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_INGREDIENTS):
    case REQUEST(ACTION_TYPES.UPDATE_INGREDIENTS):
    case REQUEST(ACTION_TYPES.DELETE_INGREDIENTS):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_INGREDIENTS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_INGREDIENTS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_INGREDIENTS):
    case FAILURE(ACTION_TYPES.CREATE_INGREDIENTS):
    case FAILURE(ACTION_TYPES.UPDATE_INGREDIENTS):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_INGREDIENTS):
    case FAILURE(ACTION_TYPES.DELETE_INGREDIENTS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_INGREDIENTS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_INGREDIENTS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_INGREDIENTS):
    case SUCCESS(ACTION_TYPES.UPDATE_INGREDIENTS):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_INGREDIENTS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_INGREDIENTS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/ingredients';

// Actions

export const getEntities: ICrudGetAllAction<IIngredients> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_INGREDIENTS_LIST,
    payload: axios.get<IIngredients>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IIngredients> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_INGREDIENTS,
    payload: axios.get<IIngredients>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IIngredients> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_INGREDIENTS,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IIngredients> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_INGREDIENTS,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IIngredients> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_INGREDIENTS,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IIngredients> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_INGREDIENTS,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
