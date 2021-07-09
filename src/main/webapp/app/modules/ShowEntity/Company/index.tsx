import React from 'react';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import PageNotFound from 'app/shared/error/page-not-found';
import { withRouter } from 'react-router-dom';
import { RouteComponentProps } from 'react-router';
import companyDetail from 'app/entities/company/company-detail';
type PathParamsType = {
  param1: string;
};

// Your component own properties
type PropsType = RouteComponentProps<PathParamsType> & {
  someString: string;
};
class Company extends React.Component<PropsType> {
  render() {
    return (
      <>
        <div>company</div>
      </>
    );
  }
}
export default withRouter(Company);
