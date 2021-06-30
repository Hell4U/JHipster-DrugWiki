import React from 'react';
import { BrowserRouter as Router, Switch, Route, RouteComponentProps } from 'react-router-dom';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import BrandDetail from './BrandDetail';
import PageNotFound from 'app/shared/error/page-not-found';
type TParams = { id: string };
const Routes = ({ match }: RouteComponentProps<TParams>) => {
  <>
    <Switch>
      <ErrorBoundaryRoute path={`${match.url}/:id`} exact component={BrandDetail} />
      <ErrorBoundaryRoute component={PageNotFound} />
    </Switch>
  </>;
};

export default Routes;
