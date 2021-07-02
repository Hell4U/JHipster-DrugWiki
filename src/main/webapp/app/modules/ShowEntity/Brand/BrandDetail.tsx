import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from 'app/entities/brand/brand.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IBrandDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const BrandDetail = (props: IBrandDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { brandEntity } = props;
  console.log(brandEntity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="brandDetailsHeading">Brand</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{brandEntity.id}</dd>
          <dt>
            <span id="bname">Bname</span>
          </dt>
          <dd>{brandEntity.bname}</dd>
          <dt>
            <span id="price">Price</span>
          </dt>
          <dd>{brandEntity.price}</dd>
          <dt>
            <span id="date">Date</span>
          </dt>
          <dd>{brandEntity.date ? <TextFormat value={brandEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="packageunit">Packageunit</span>
          </dt>
          <dd>{brandEntity.packageunit}</dd>
          <dt>
            <span id="type">Type</span>
          </dt>
          <dd>{brandEntity.type}</dd>
          <dt>
            <span id="typeunit">Typeunit</span>
          </dt>
          <dd>{brandEntity.typeunit}</dd>
          <dt>Companyof Medicine</dt>
          <dd>{brandEntity.companyofMedicine ? brandEntity.companyofMedicine.cname : ''}</dd>
          <dt>Generics</dt>
          <dd>
            {brandEntity.generics
              ? brandEntity.generics.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {brandEntity.generics && i === brandEntity.generics.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ brand }: IRootState) => ({
  brandEntity: brand.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(BrandDetail);
