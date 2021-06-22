import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Ingredients from './ingredients';
import IngredientsDetail from './ingredients-detail';
import IngredientsUpdate from './ingredients-update';
import IngredientsDeleteDialog from './ingredients-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={IngredientsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={IngredientsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={IngredientsDetail} />
      <ErrorBoundaryRoute path={match.url} component={Ingredients} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={IngredientsDeleteDialog} />
  </>
);

export default Routes;
