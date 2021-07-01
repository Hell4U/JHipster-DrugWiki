import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './generics.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IGenericsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const GenericsDetail = (props: IGenericsDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { genericsEntity } = props;
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
            <span id="dosage">Dosage</span>
          </dt>
          <dd>{genericsEntity.dosage}</dd>
          <dt>
            <span id="dosageunit">Dosageunit</span>
          </dt>
          <dd>{genericsEntity.dosageunit}</dd>
          <dt>Ingredient Name</dt>
          <dd>{genericsEntity.ingredientName ? genericsEntity.ingredientName.iname : ''}</dd>
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

const mapStateToProps = ({ generics }: IRootState) => ({
  genericsEntity: generics.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(GenericsDetail);
