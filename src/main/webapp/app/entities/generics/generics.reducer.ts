import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IGenerics, defaultValue } from 'app/shared/model/generics.model';

export const ACTION_TYPES = {
  FETCH_GENERICS_LIST: 'generics/FETCH_GENERICS_LIST',
  FETCH_GENERICS: 'generics/FETCH_GENERICS',
  CREATE_GENERICS: 'generics/CREATE_GENERICS',
  UPDATE_GENERICS: 'generics/UPDATE_GENERICS',
  PARTIAL_UPDATE_GENERICS: 'generics/PARTIAL_UPDATE_GENERICS',
  DELETE_GENERICS: 'generics/DELETE_GENERICS',
  RESET: 'generics/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IGenerics>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type GenericsState = Readonly<typeof initialState>;

// Reducer

export default (state: GenericsState = initialState, action): GenericsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_GENERICS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_GENERICS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_GENERICS):
    case REQUEST(ACTION_TYPES.UPDATE_GENERICS):
    case REQUEST(ACTION_TYPES.DELETE_GENERICS):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_GENERICS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_GENERICS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_GENERICS):
    case FAILURE(ACTION_TYPES.CREATE_GENERICS):
    case FAILURE(ACTION_TYPES.UPDATE_GENERICS):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_GENERICS):
    case FAILURE(ACTION_TYPES.DELETE_GENERICS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_GENERICS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_GENERICS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_GENERICS):
    case SUCCESS(ACTION_TYPES.UPDATE_GENERICS):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_GENERICS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_GENERICS):
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

const apiUrl = 'api/generics';

// Actions

export const getEntities: ICrudGetAllAction<IGenerics> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_GENERICS_LIST,
    payload: axios.get<IGenerics>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IGenerics> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_GENERICS,
    payload: axios.get<IGenerics>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IGenerics> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_GENERICS,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IGenerics> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_GENERICS,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IGenerics> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_GENERICS,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IGenerics> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_GENERICS,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
