import React, { useState, useEffect } from 'react';
import BrandShower from '../ShowEntity/Brand/BrandShower';
import { ButtonGroup, Button, Row, Col } from 'reactstrap';
import { Link } from 'react-router-dom';

const HomeNew = props => {
  return (
    <div>
      <h1 className="text-center">Welcome</h1>
      <Row className="text-center">
        <Col>
          <Link to="/guest/brand">
            <Button color="primary" className="m-5" style={{ fontSize: '1.2rem' }}>
              Brand
            </Button>
          </Link>
          <Link to="/guest/company">
            <Button color="primary" className="m-5" style={{ fontSize: '1.2rem' }}>
              Company
            </Button>
          </Link>
          <Link to="/guest/generics">
            <Button color="primary" className="m-5" style={{ fontSize: '1.2rem' }}>
              Generics
            </Button>
          </Link>
        </Col>
      </Row>
    </div>
  );
};

export default HomeNew;
