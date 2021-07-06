import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './company.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const CompanyDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const companyEntity = useAppSelector(state => state.company.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="companyDetailsHeading">Company</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{companyEntity.id}</dd>
          <dt>
            <span id="cname">Cname</span>
          </dt>
          <dd>{companyEntity.cname}</dd>
          <dt>
            <span id="address">Address</span>
          </dt>
          <dd>{companyEntity.address}</dd>
          <dt>
            <span id="website">Website</span>
          </dt>
          <dd>{companyEntity.website}</dd>
          <dt>
            <span id="email">Email</span>
          </dt>
          <dd>{companyEntity.email}</dd>
          <dt>
            <span id="fax">Fax</span>
          </dt>
          <dd>{companyEntity.fax}</dd>
          <dt>
            <span id="phoneno">Phoneno</span>
          </dt>
          <dd>{companyEntity.phoneno}</dd>
        </dl>
        <Button tag={Link} to="/company" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/company/${companyEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default CompanyDetail;
