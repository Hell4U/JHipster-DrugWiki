import React from 'react';

import { NavItem, NavLink, NavbarBrand } from 'reactstrap';
import { Row } from 'reactstrap';
import { InputGroup, InputGroupAddon, InputGroupText, Input } from 'reactstrap';

import { NavLink as Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import appConfig from 'app/config/constants';

export const BrandIcon = props => (
  <div {...props} className="brand-icon">
    <img src="content/images/logo-jhipster.png" alt="Logo" />
  </div>
);

export const Brand = props => (
  <NavbarBrand tag={Link} to="/" className="brand-logo">
    {/* <BrandIcon /> */}
    <span className="brand-title">DrugWiki</span>{' '}
  </NavbarBrand>
);

export const Search = props => (
  <div>
    <InputGroup className="width-100" style={{ width: '32rem' }}>
      <Input placeholder="Search...." />
    </InputGroup>
  </div>
);

export const Home = props => (
  <NavItem>
    <NavLink tag={Link} to="/" className="d-flex align-items-center text-light">
      <FontAwesomeIcon icon="home" />
      <span>Home</span>
    </NavLink>
  </NavItem>
);
