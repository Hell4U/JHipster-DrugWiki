import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IIngredients } from 'app/shared/model/ingredients.model';
import { getEntities as getIngredients } from 'app/entities/ingredients/ingredients.reducer';
import { IBrand } from 'app/shared/model/brand.model';
import { getEntities as getBrands } from 'app/entities/brand/brand.reducer';
import { getEntity, updateEntity, createEntity, reset } from './generics.reducer';
import { IGenerics } from 'app/shared/model/generics.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const GenericsUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const ingredients = useAppSelector(state => state.ingredients.entities);
  const brands = useAppSelector(state => state.brand.entities);
  const genericsEntity = useAppSelector(state => state.generics.entity);
  const loading = useAppSelector(state => state.generics.loading);
  const updating = useAppSelector(state => state.generics.updating);
  const updateSuccess = useAppSelector(state => state.generics.updateSuccess);

  const handleClose = () => {
    props.history.push('/generics');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getIngredients({}));
    dispatch(getBrands({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...genericsEntity,
      ...values,
      ingredientsused: ingredients.find(it => it.id.toString() === values.ingredientsusedId.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...genericsEntity,
          dosageunit: 'Microgram',
          ingredientsusedId: genericsEntity?.ingredientsused?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="drugwikiApp.generics.home.createOrEditLabel" data-cy="GenericsCreateUpdateHeading">
            Create or edit a Generics
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="generics-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Gname"
                id="generics-gname"
                name="gname"
                data-cy="gname"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Dosage"
                id="generics-dosage"
                name="dosage"
                data-cy="dosage"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField label="Dosageunit" id="generics-dosageunit" name="dosageunit" data-cy="dosageunit" type="select">
                <option value="Microgram">Microgram</option>
                <option value="Miligram">Miligram</option>
                <option value="Gram">Gram</option>
                <option value="Mililiter">Mililiter</option>
              </ValidatedField>
              <ValidatedField
                id="generics-ingredientsused"
                name="ingredientsusedId"
                data-cy="ingredientsused"
                label="Ingredientsused"
                type="select"
                required
              >
                <option value="" key="0" />
                {ingredients
                  ? ingredients.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.iname}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>This field is required.</FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/generics" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default GenericsUpdate;
