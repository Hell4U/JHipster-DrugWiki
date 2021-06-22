import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ICompany } from 'app/shared/model/company.model';
import { getEntities as getCompanies } from 'app/entities/company/company.reducer';
import { IGenerics } from 'app/shared/model/generics.model';
import { getEntities as getGenerics } from 'app/entities/generics/generics.reducer';
import { getEntity, updateEntity, createEntity, reset } from './brand.reducer';
import { IBrand } from 'app/shared/model/brand.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IBrandUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const BrandUpdate = (props: IBrandUpdateProps) => {
  const [idsgenerics, setIdsgenerics] = useState([]);
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { brandEntity, companies, generics, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/brand' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getCompanies();
    props.getGenerics();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...brandEntity,
        ...values,
        generics: mapIdList(values.generics),
        companyofMedicine: companies.find(it => it.id.toString() === values.companyofMedicineId.toString()),
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
          <h2 id="drugwikiApp.brand.home.createOrEditLabel" data-cy="BrandCreateUpdateHeading">
            Create or edit a Brand
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : brandEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="brand-id">ID</Label>
                  <AvInput id="brand-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="bnameLabel" for="brand-bname">
                  Bname
                </Label>
                <AvField
                  id="brand-bname"
                  data-cy="bname"
                  type="text"
                  name="bname"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="priceLabel" for="brand-price">
                  Price
                </Label>
                <AvField
                  id="brand-price"
                  data-cy="price"
                  type="string"
                  className="form-control"
                  name="price"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    number: { value: true, errorMessage: 'This field should be a number.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="dateLabel" for="brand-date">
                  Date
                </Label>
                <AvField
                  id="brand-date"
                  data-cy="date"
                  type="date"
                  className="form-control"
                  name="date"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="packageunitLabel" for="brand-packageunit">
                  Packageunit
                </Label>
                <AvField
                  id="brand-packageunit"
                  data-cy="packageunit"
                  type="string"
                  className="form-control"
                  name="packageunit"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    number: { value: true, errorMessage: 'This field should be a number.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="typeLabel" for="brand-type">
                  Type
                </Label>
                <AvInput
                  id="brand-type"
                  data-cy="type"
                  type="select"
                  className="form-control"
                  name="type"
                  value={(!isNew && brandEntity.type) || 'Tablet'}
                >
                  <option value="Tablet">Tablet</option>
                  <option value="Injection">Injection</option>
                  <option value="Capsule">Capsule</option>
                  <option value="Syrup">Syrup</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="typeunitLabel" for="brand-typeunit">
                  Typeunit
                </Label>
                <AvInput
                  id="brand-typeunit"
                  data-cy="typeunit"
                  type="select"
                  className="form-control"
                  name="typeunit"
                  value={(!isNew && brandEntity.typeunit) || 'PCS'}
                >
                  <option value="PCS">PCS</option>
                  <option value="Miligram">Miligram</option>
                  <option value="Gram">Gram</option>
                  <option value="Mililiter">Mililiter</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="brand-companyofMedicine">Companyof Medicine</Label>
                <AvInput
                  id="brand-companyofMedicine"
                  data-cy="companyofMedicine"
                  type="select"
                  className="form-control"
                  name="companyofMedicineId"
                  required
                >
                  <option value="" key="0" />
                  {companies
                    ? companies.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.cname}
                        </option>
                      ))
                    : null}
                </AvInput>
                <AvFeedback>This field is required.</AvFeedback>
              </AvGroup>
              <AvGroup>
                <Label for="brand-generics">Generics</Label>
                <AvInput
                  id="brand-generics"
                  data-cy="generics"
                  type="select"
                  multiple
                  className="form-control"
                  name="generics"
                  value={!isNew && brandEntity.generics && brandEntity.generics.map(e => e.id)}
                >
                  <option value="" key="0" />
                  {generics
                    ? generics.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/brand" replace color="info">
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
  companies: storeState.company.entities,
  generics: storeState.generics.entities,
  brandEntity: storeState.brand.entity,
  loading: storeState.brand.loading,
  updating: storeState.brand.updating,
  updateSuccess: storeState.brand.updateSuccess,
});

const mapDispatchToProps = {
  getCompanies,
  getGenerics,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(BrandUpdate);
