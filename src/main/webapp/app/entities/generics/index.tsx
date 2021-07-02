import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Generics from './generics';
import GenericsDetail from './generics-detail';
import GenericsUpdate from './generics-update';
import GenericsDeleteDialog from './generics-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GenericsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GenericsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GenericsDetail} />
      <ErrorBoundaryRoute path={match.url} component={Generics} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={GenericsDeleteDialog} />
  </>
);

export default Routes;
