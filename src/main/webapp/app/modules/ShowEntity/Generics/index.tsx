import React from 'react';
import { Switch } from 'react-router-dom';
import PageNotFound from 'app/shared/error/page-not-found';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

const Routes = ({ match }) => (
  <Switch>
    <ErrorBoundaryRoute path={match.url} component={} />
  </Switch>
);
