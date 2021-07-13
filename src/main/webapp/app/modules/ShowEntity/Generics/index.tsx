import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import GenericsShower from './GenericsShower';
import GenericsDetail from './GenericsDetail';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GenericsDetail} />
      <ErrorBoundaryRoute path={match.url} component={GenericsShower} />
    </Switch>
  </>
);

export default Routes;
