import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './brand.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const BrandDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const brandEntity = useAppSelector(state => state.brand.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="brandDetailsHeading">Brand</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{brandEntity.id}</dd>
          <dt>
            <span id="bname">Bname</span>
          </dt>
          <dd>{brandEntity.bname}</dd>
          <dt>
            <span id="price">Price</span>
          </dt>
          <dd>{brandEntity.price}</dd>
          <dt>
            <span id="date">Date</span>
          </dt>
          <dd>{brandEntity.date ? <TextFormat value={brandEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="packageunit">Packageunit</span>
          </dt>
          <dd>{brandEntity.packageunit}</dd>
          <dt>
            <span id="type">Type</span>
          </dt>
          <dd>{brandEntity.type}</dd>
          <dt>
            <span id="typeunit">Typeunit</span>
          </dt>
          <dd>{brandEntity.typeunit}</dd>
          <dt>Companyof Medicine</dt>
          <dd>{brandEntity.companyofMedicine ? brandEntity.companyofMedicine.cname : ''}</dd>
          <dt>Genericsused</dt>
          <dd>
            {brandEntity.genericsuseds
              ? brandEntity.genericsuseds.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.gname}</a>
                    {brandEntity.genericsuseds && i === brandEntity.genericsuseds.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/brand" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/brand/${brandEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default BrandDetail;
