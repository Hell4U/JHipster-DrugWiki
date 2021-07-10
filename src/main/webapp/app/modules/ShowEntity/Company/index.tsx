import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CompanyShower from './CompanyShower';
import CompanyDetail from './CompanyDetail';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CompanyDetail} />
      <ErrorBoundaryRoute path={match.url} component={CompanyShower} />
    </Switch>
  </>
);

export default Routes;
