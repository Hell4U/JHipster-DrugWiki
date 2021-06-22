import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IIngredients } from 'app/shared/model/ingredients.model';
import { getEntities as getIngredients } from 'app/entities/ingredients/ingredients.reducer';
import { IBrand } from 'app/shared/model/brand.model';
import { getEntities as getBrands } from 'app/entities/brand/brand.reducer';
import { getEntity, updateEntity, createEntity, reset } from './generics.reducer';
import { IGenerics } from 'app/shared/model/generics.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IGenericsUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const GenericsUpdate = (props: IGenericsUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { genericsEntity, ingredients, brands, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/generics' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getIngredients();
    props.getBrands();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...genericsEntity,
        ...values,
        ingredientName: ingredients.find(it => it.id.toString() === values.ingredientNameId.toString()),
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
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
            <AvForm model={isNew ? {} : genericsEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="generics-id">ID</Label>
                  <AvInput id="generics-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="dosageLabel" for="generics-dosage">
                  Dosage
                </Label>
                <AvField
                  id="generics-dosage"
                  data-cy="dosage"
                  type="string"
                  className="form-control"
                  name="dosage"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    number: { value: true, errorMessage: 'This field should be a number.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="dosageunitLabel" for="generics-dosageunit">
                  Dosageunit
                </Label>
                <AvInput
                  id="generics-dosageunit"
                  data-cy="dosageunit"
                  type="select"
                  className="form-control"
                  name="dosageunit"
                  value={(!isNew && genericsEntity.dosageunit) || 'Microgram'}
                >
                  <option value="Microgram">Microgram</option>
                  <option value="Miligram">Miligram</option>
                  <option value="Gram">Gram</option>
                  <option value="Mililiter">Mililiter</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="generics-ingredientName">Ingredient Name</Label>
                <AvInput
                  id="generics-ingredientName"
                  data-cy="ingredientName"
                  type="select"
                  className="form-control"
                  name="ingredientNameId"
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
                </AvInput>
                <AvFeedback>This field is required.</AvFeedback>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/generics" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  ingredients: storeState.ingredients.entities,
  brands: storeState.brand.entities,
  genericsEntity: storeState.generics.entity,
  loading: storeState.generics.loading,
  updating: storeState.generics.updating,
  updateSuccess: storeState.generics.updateSuccess,
});

const mapDispatchToProps = {
  getIngredients,
  getBrands,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(GenericsUpdate);
