import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './generics.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const GenericsDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const genericsEntity = useAppSelector(state => state.generics.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="genericsDetailsHeading">Generics</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{genericsEntity.id}</dd>
          <dt>
            <span id="gname">Gname</span>
          </dt>
          <dd>{genericsEntity.gname}</dd>
          <dt>
            <span id="dosage">Dosage</span>
          </dt>
          <dd>{genericsEntity.dosage}</dd>
          <dt>
            <span id="dosageunit">Dosageunit</span>
          </dt>
          <dd>{genericsEntity.dosageunit}</dd>
          <dt>Ingredientsused</dt>
          <dd>{genericsEntity.ingredientsused ? genericsEntity.ingredientsused.iname : ''}</dd>
        </dl>
        <Button tag={Link} to="/generics" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/generics/${genericsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default GenericsDetail;
