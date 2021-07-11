import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Table } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from 'app/entities/generics/generics.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const GenericsDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();
  const genericsEntity = useAppSelector(state => state.generics.entity);
  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, [genericsEntity]);
  const { gname, dosage, dosageunit, ingredientsused } = genericsEntity;
  return (
    <Row>
      <Col md="12">
        <div className="display-4 text-center">Generics</div>
        <Table bordered>
          <thead>
            <tr>
              <th className="text-center">Name</th>
              <th className="text-center">Ingredient Name</th>
              <th className="text-center">Dosage</th>
              <th className="text-center">Dosage Unit</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td className="text-center bold">{gname}</td>
              <td className="text-center">
                <Link to={`/guest/ingredients/${ingredientsused ? ingredientsused.id : ''}`}>
                  {ingredientsused ? ingredientsused.iname : 'No Ingredients Found'}
                </Link>
              </td>
              <td className="text-center">{dosage}</td>
              <td className="text-center">{dosageunit}</td>
            </tr>
          </tbody>
        </Table>
      </Col>
    </Row>
  );
};

export default GenericsDetail;
