import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './ingredients.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IIngredientsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const IngredientsDetail = (props: IIngredientsDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { ingredientsEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="ingredientsDetailsHeading">Ingredients</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{ingredientsEntity.id}</dd>
          <dt>
            <span id="iname">Iname</span>
          </dt>
          <dd>{ingredientsEntity.iname}</dd>
          <dt>
            <span id="symptoms">Symptoms</span>
          </dt>
          <dd>{ingredientsEntity.symptoms}</dd>
          <dt>
            <span id="sideeffects">Sideeffects</span>
          </dt>
          <dd>{ingredientsEntity.sideeffects}</dd>
          <dt>
            <span id="cautions">Cautions</span>
          </dt>
          <dd>{ingredientsEntity.cautions}</dd>
        </dl>
        <Button tag={Link} to="/ingredients" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ingredients/${ingredientsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ ingredients }: IRootState) => ({
  ingredientsEntity: ingredients.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(IngredientsDetail);
