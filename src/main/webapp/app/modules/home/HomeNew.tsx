import React, { useState, useEffect } from 'react';
import { ButtonGroup, Button, Row, Col } from 'reactstrap';
import { Link } from 'react-router-dom';

const HomeNew = props => {
  const tmp = {
    listStyleType: 'none',
  };
  return (
    <div>
      <h1 className="text-center">Welcome</h1>
      <Row className="text-center">
        <Col>
          <p>
            Drugwiki Application an easy access to accurate information on wide range of medicinal formulations for effectively managing
            medicine and drugs during pandemics and shortage of medicines.
          </p>
          <p>It has been a trusted source of information pertaining to:</p>
          <ul style={tmp}>
            <li> Drugs being manufactured and/or marketed.</li>
            <li> List of various generics used, ingredients of generics</li>
          </ul>
        </Col>
      </Row>
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
