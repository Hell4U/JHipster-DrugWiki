import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Table } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from 'app/entities/company/company.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const CompanyDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();
  const companyEntity = useAppSelector(state => state.company.entity);
  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, [companyEntity]);
  const { cname, address, website, email, fax, phoneno} = companyEntity;
  return (
    <Row>
      <Col md="12">
        <div className="display-4 text-center">Company</div>
        <Table bordered>
          <thead>
            <tr>
              <th className="text-center">Name</th>
              <th className="text-center">Address</th>
              <th className="text-center">Website</th>
              <th className="text-center">Email</th>
              <th className="text-center">Fax</th>
              <th className="text-center">Phone Number</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td className="text-center bold">{cname}</td>
              <td className="text-center"> {address}</td>
              <td className="text-center">{website}</td>
              <td className="text-center">{email}</td>
              <td className="text-center">{fax}</td>
              <td className="text-center">{phoneno}</td>
            </tr>
          </tbody>
        </Table>
      </Col>
    </Row>
  );
};

export default CompanyDetail;
