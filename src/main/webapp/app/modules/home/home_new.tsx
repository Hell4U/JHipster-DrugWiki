import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Row, Col } from 'reactstrap';
import { Card, CardBody, CardTitle, CardSubtitle, CardText, CardImg } from 'reactstrap';
import { getEntities } from '../../entities/company/company.reducer';
import { IRootState } from 'app/shared/reducers';

const tmp = [
  {
    id: 1,
    text: 'Levo',
  },
  {
    id: 2,
    text: 'Cryco',
  },
  {
    id: 3,
    text: 'Azeo',
  },
];

const HomeNew = props => {
  const { companyList } = props;

  useEffect(() => {
    props.getEntities();
  }, []);

  return (
    <>
      <Row>
        {tmp.map(elm => {
          return (
            <Col md="3" key={elm.id}>
              <Card>
                <CardImg top width="100%" alt="Type" />
                <CardBody>
                  <CardTitle tag="h4">Medicine-Name</CardTitle>
                  <CardSubtitle tag="h6" className="mb-4 mt-0 text-muted">
                    Company-Name
                  </CardSubtitle>
                  <CardText>{elm.text}</CardText>
                </CardBody>
              </Card>
            </Col>
          );
        })}
      </Row>
      <Row>
        {companyList && companyList.length > 0
          ? companyList.map(elm => {
              return <Col>{elm.name}</Col>;
            })
          : null}
      </Row>
    </>
  );
};

const mapStateToProps = ({ company }: IRootState) => ({
  companyList: company.entities,
  loading: company.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(HomeNew);
