import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import BrandShower from './BrandShower';
import BrandDetail from './BrandDetail';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BrandDetail} />
      <ErrorBoundaryRoute path={match.url} component={BrandShower} />
    </Switch>
  </>
);

export default Routes;
