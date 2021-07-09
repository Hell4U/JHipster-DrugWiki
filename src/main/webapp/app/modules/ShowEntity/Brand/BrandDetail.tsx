import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Table } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from 'app/entities/brand/brand.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const BrandDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();
  const brandEntity = useAppSelector(state => state.brand.entity);
  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, [brandEntity]);
  const { bname, price, date, packageunit, type, typeunit, companyofMedicine, genericsuseds } = brandEntity;
  return (
    <Row>
      <Col md="12">
        <div className="display-4 text-center">Brand</div>
        <Table bordered>
          <thead>
            <tr>
              <th className="text-center">Name</th>
              <th className="text-center">Price</th>
              <th className="text-center">Date</th>
              <th className="text-center">PackageUnit</th>
              <th className="text-center">Type</th>
              <th className="text-center">TypeUnit</th>
              <th className="text-center">Company</th>
              <th className="text-center">Generics</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td className="text-center bold">{bname}</td>
              <td className="text-center">₹ {price}</td>
              <td className="text-center">{date}</td>
              <td className="text-center">{packageunit}</td>
              <td className="text-center">{type}</td>
              <td className="text-center">{typeunit}</td>
              <td className="text-center">{companyofMedicine ? companyofMedicine.cname : 'No company has been assigned.'}</td>
              <td className="text-center">
                {genericsuseds ? (
                  genericsuseds.map(data => {
                    return (
                      <>
                        <Link to={`/guest/generic/${data.id}`}>{data.gname}</Link>
                        <br></br>
                      </>
                    );
                  })
                ) : (
                  <div>There is no generic</div>
                )}{' '}
              </td>
            </tr>
          </tbody>
        </Table>
      </Col>
    </Row>
  );
};

export default BrandDetail;
