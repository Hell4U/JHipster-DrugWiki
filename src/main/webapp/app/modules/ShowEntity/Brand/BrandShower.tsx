import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Row, Col } from 'reactstrap';
import { Card, CardBody, CardTitle, CardSubtitle, CardText, CardImg } from 'reactstrap';
import { getEntities } from 'app/entities/brand/brand.reducer';
import { IRootState } from 'app/shared/reducers';
import Capsule from 'app/Images/capsule.png';
import Injection from 'app/Images/injection.png';
import Tablet from 'app/Images/tablet.png';
import Syrup from 'app/Images/syrup.png';

const imageType = {
  Capsule: Capsule,
  Injection: Injection,
  Tablet: Tablet,
  Syrup: Syrup,
};

const BrandShower = props => {
  const { brandList } = props;

  useEffect(() => {
    props.getEntities();
  }, []);

  return (
    <>
      <Row className="mt-5">
        {brandList && brandList.length > 0
          ? brandList.map((elm, idx) => {
              return (
                <Col md="2" className="mb-5" key={elm.id}>
                  <Card>
                    <CardImg top width="300px" height="150px" alt="Type" src={imageType[elm.type]} />
                    <CardBody>
                      <CardTitle tag="h4">{elm.bname}</CardTitle>
                      <CardSubtitle tag="h6" className="mb-4 mt-0 text-muted">
                        {elm.companyofMedicine ? elm.companyofMedicine.cname : ''}
                      </CardSubtitle>
                      <CardText>Price:-{elm.price}</CardText>
                    </CardBody>
                  </Card>
                </Col>
              );
            })
          : null}
      </Row>
    </>
  );
};

const mapStateToProps = ({ brand }: IRootState) => ({
  brandList: brand.entities,
  loading: brand.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(BrandShower);
