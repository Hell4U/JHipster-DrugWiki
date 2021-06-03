import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label, UncontrolledTooltip } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ICompany } from 'app/shared/model/company.model';
import { getEntities as getCompanies } from 'app/entities/company/company.reducer';
import { getEntity, updateEntity, createEntity, reset } from './brand.reducer';
import { IBrand } from 'app/shared/model/brand.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IBrandUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const BrandUpdate = (props: IBrandUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { brandEntity, companies, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/brand');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getCompanies();
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
        company: companies.find(it => it.id.toString() === values.companyId.toString()),
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
          <h2 id="drugWikiApp.brand.home.createOrEditLabel" data-cy="BrandCreateUpdateHeading">
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
                <Label id="nameLabel" for="brand-name">
                  Name
                </Label>
                <AvField
                  id="brand-name"
                  data-cy="name"
                  type="text"
                  name="name"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
                <UncontrolledTooltip target="nameLabel">name</UncontrolledTooltip>
              </AvGroup>
              <AvGroup>
                <Label id="priceLabel" for="brand-price">
                  Price
                </Label>
                <AvField id="brand-price" data-cy="price" type="string" className="form-control" name="price" />
                <UncontrolledTooltip target="priceLabel">price</UncontrolledTooltip>
              </AvGroup>
              <AvGroup>
                <Label for="brand-company">Company</Label>
                <AvInput id="brand-company" data-cy="company" type="select" className="form-control" name="companyId">
                  <option value="" key="0" />
                  {companies
                    ? companies.map(otherEntity => (
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
  brandEntity: storeState.brand.entity,
  loading: storeState.brand.loading,
  updating: storeState.brand.updating,
  updateSuccess: storeState.brand.updateSuccess,
});

const mapDispatchToProps = {
  getCompanies,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(BrandUpdate);
