import React from 'react';
import PageNotFound from 'app/shared/error/page-not-found';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import { Switch } from 'react-router';
import CompanyShower from './CompanyShower';
const Routes = ({ match }) => (
  <Switch>
    <ErrorBoundaryRoute path={match.url} component={CompanyShower} />
  </Switch>
);
