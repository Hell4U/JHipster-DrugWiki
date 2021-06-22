import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './ingredients.reducer';
import { IIngredients } from 'app/shared/model/ingredients.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IIngredientsUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const IngredientsUpdate = (props: IIngredientsUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { ingredientsEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/ingredients' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...ingredientsEntity,
        ...values,
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
          <h2 id="drugwikiApp.ingredients.home.createOrEditLabel" data-cy="IngredientsCreateUpdateHeading">
            Create or edit a Ingredients
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : ingredientsEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="ingredients-id">ID</Label>
                  <AvInput id="ingredients-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="inameLabel" for="ingredients-iname">
                  Iname
                </Label>
                <AvField
                  id="ingredients-iname"
                  data-cy="iname"
                  type="text"
                  name="iname"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="symptomsLabel" for="ingredients-symptoms">
                  Symptoms
                </Label>
                <AvField
                  id="ingredients-symptoms"
                  data-cy="symptoms"
                  type="text"
                  name="symptoms"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="sideeffectsLabel" for="ingredients-sideeffects">
                  Sideeffects
                </Label>
                <AvField
                  id="ingredients-sideeffects"
                  data-cy="sideeffects"
                  type="text"
                  name="sideeffects"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="cautionsLabel" for="ingredients-cautions">
                  Cautions
                </Label>
                <AvField
                  id="ingredients-cautions"
                  data-cy="cautions"
                  type="text"
                  name="cautions"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/ingredients" replace color="info">
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
  ingredientsEntity: storeState.ingredients.entity,
  loading: storeState.ingredients.loading,
  updating: storeState.ingredients.updating,
  updateSuccess: storeState.ingredients.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(IngredientsUpdate);
