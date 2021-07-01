import React from 'react';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import PageNotFound from 'app/shared/error/page-not-found';
import { withRouter } from 'react-router-dom';
import { RouteComponentProps } from 'react-router';
type PathParamsType = {
  id: string;
};

// Your component own properties
type PropsType = RouteComponentProps<PathParamsType> & {
  someString: string;
};
class BrandDetail extends React.Component<PropsType> {
  render() {
    return (
      <>
        <div>BrandDetail ${this.props.match.params.id}</div>
      </>
    );
  }
}
export default withRouter(BrandDetail);
