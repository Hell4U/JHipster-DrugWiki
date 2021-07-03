import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ICompany } from 'app/shared/model/company.model';
import { getEntities as getCompanies } from 'app/entities/company/company.reducer';
import { IGenerics } from 'app/shared/model/generics.model';
import { getEntities as getGenerics } from 'app/entities/generics/generics.reducer';
import { getEntity, updateEntity, createEntity, reset } from './brand.reducer';
import { IBrand } from 'app/shared/model/brand.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const BrandUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const companies = useAppSelector(state => state.company.entities);
  const generics = useAppSelector(state => state.generics.entities);
  const brandEntity = useAppSelector(state => state.brand.entity);
  const loading = useAppSelector(state => state.brand.loading);
  const updating = useAppSelector(state => state.brand.updating);
  const updateSuccess = useAppSelector(state => state.brand.updateSuccess);

  const handleClose = () => {
    props.history.push('/brand' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getCompanies({}));
    dispatch(getGenerics({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...brandEntity,
      ...values,
      genericsuseds: mapIdList(values.genericsuseds),
      companyofMedicine: companies.find(it => it.id.toString() === values.companyofMedicineId.toString()),
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
          ...brandEntity,
          type: 'Tablet',
          typeunit: 'PCS',
          companyofMedicineId: brandEntity?.companyofMedicine?.id,
          genericsuseds: brandEntity?.genericsuseds?.map(e => e.id.toString()),
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
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="brand-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Bname"
                id="brand-bname"
                name="bname"
                data-cy="bname"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Price"
                id="brand-price"
                name="price"
                data-cy="price"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField
                label="Date"
                id="brand-date"
                name="date"
                data-cy="date"
                type="date"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Packageunit"
                id="brand-packageunit"
                name="packageunit"
                data-cy="packageunit"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField label="Type" id="brand-type" name="type" data-cy="type" type="select">
                <option value="Tablet">Tablet</option>
                <option value="Injection">Injection</option>
                <option value="Capsule">Capsule</option>
                <option value="Syrup">Syrup</option>
              </ValidatedField>
              <ValidatedField label="Typeunit" id="brand-typeunit" name="typeunit" data-cy="typeunit" type="select">
                <option value="PCS">PCS</option>
                <option value="Miligram">Miligram</option>
                <option value="Gram">Gram</option>
                <option value="Mililiter">Mililiter</option>
              </ValidatedField>
              <ValidatedField
                id="brand-companyofMedicine"
                name="companyofMedicineId"
                data-cy="companyofMedicine"
                label="Companyof Medicine"
                type="select"
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
              </ValidatedField>
              <FormText>This field is required.</FormText>
              <ValidatedField
                label="Genericsused"
                id="brand-genericsused"
                data-cy="genericsused"
                type="select"
                multiple
                name="genericsuseds"
              >
                <option value="" key="0" />
                {generics
                  ? generics.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.gname}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/brand" replace color="info">
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

export default BrandUpdate;
