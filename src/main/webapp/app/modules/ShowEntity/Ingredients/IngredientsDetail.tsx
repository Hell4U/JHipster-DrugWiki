import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Table } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { getEntity } from 'app/entities/ingredients/ingredients.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const IngredientsDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();
  const ingredientsEntity = useAppSelector(state => state.ingredients.entity);
  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, [ingredientsEntity]);
  const { iname, symptoms, sideeffects, cautions } = ingredientsEntity;
  return (
    <Row>
      <Col md="12">
        <div className="display-4 text-center">Ingredients</div>
        <Table bordered>
          <thead>
            <tr>
              <th className="text-center">Name</th>
              <th className="text-center">Symptoms</th>
              <th className="text-center">Side Effects</th>
              <th className="text-center">Cautions</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td className="text-center bold">{iname}</td>
              <td className="text-center">{symptoms}</td>
              <td className="text-center">{sideeffects}</td>
              <td className="text-center">{cautions}</td>
            </tr>
          </tbody>
        </Table>
      </Col>
    </Row>
  );
};

export default IngredientsDetail;
