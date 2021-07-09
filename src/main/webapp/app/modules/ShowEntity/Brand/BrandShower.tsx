import React, { useState, useEffect } from 'react';
import { Card, CardBody, CardTitle, CardSubtitle, CardText, CardImg } from 'reactstrap';
import InfiniteScroll from 'react-infinite-scroller';
import { getEntities, searchBrandEntities, reset } from 'app/entities/brand/brand.reducer';
import Capsule from 'app/Images/capsule.png';
import Injection from 'app/Images/injection.png';
import Tablet from 'app/Images/tablet.png';
import Syrup from 'app/Images/syrup.png';
import { Link } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { IBrand } from 'app/shared/model/brand.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { Translate, TextFormat, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Button, Form, FormGroup, Input, InputGroup, Col, Row, Table, Container } from 'reactstrap';

const imageType = {
  Capsule,
  Injection,
  Tablet,
  Syrup,
};

const BrandShower = props => {
  const dispatch = useAppDispatch();

  const [search, setSearch] = useState('');
  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(props.location, ITEMS_PER_PAGE, 'id'), props.location.search)
  );

  const brandList = useAppSelector(state => state.brand.entities);
  const loading = useAppSelector(state => state.brand.loading);
  const totalItems = useAppSelector(state => state.brand.totalItems);

  const getAllEntities = () => {
    if (search) {
      dispatch(
        searchBrandEntities({
          query: search,
          page: paginationState.activePage - 1,
          size: paginationState.itemsPerPage,
          sort: `${paginationState.sort},${paginationState.order}`,
        })
      );
    } else {
      dispatch(
        getEntities({
          page: paginationState.activePage - 1,
          size: paginationState.itemsPerPage,
          sort: `${paginationState.sort},${paginationState.order}`,
        })
      );
    }
  };

  const startSearching = e => {
    e.preventDefault();
    if (search) {
      setPaginationState({
        ...paginationState,
        activePage: 1,
      });
      dispatch(
        searchBrandEntities({
          query: search,
          page: paginationState.activePage - 1,
          size: paginationState.itemsPerPage,
          sort: `${paginationState.sort},${paginationState.order}`,
        })
      );
    }
  };

  const clear = () => {
    setSearch('');
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    dispatch(getEntities({}));
  };

  const handleSearch = event => setSearch(event.target.value);

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (props.location.search !== endURL) {
      props.history.push(`${props.location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort, search]);

  useEffect(() => {
    const params = new URLSearchParams(props.location.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [props.location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const { match } = props;

  return (
    <div className="Container px-md-5 mt-4">
      <h2 id="brand-heading" data-cy="BrandHeading">
        <div className="display-4 text-center">Brand</div>
      </h2>
      <Row>
        <Col md="9">
          <Form onSubmit={startSearching}>
            <FormGroup>
              <InputGroup>
                <Input type="text" name="search" defaultValue={search} onChange={handleSearch} placeholder="Search..." />
              </InputGroup>
            </FormGroup>
          </Form>
        </Col>
        <Col md="2">
          <Button className="" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
        </Col>
        <Col md="1">
          <Button color="danger" type="reset" className="input-group-addon text-white" onClick={clear}>
            <FontAwesomeIcon icon="trash" />
          </Button>
        </Col>
      </Row>
      <Row>
        {brandList && brandList.length > 0 ? (
          brandList.map((elm, idx) => {
            return (
              <Col md="4" lg="3" sm="12" className="mb-5" key={elm.id}>
                <Link to={`/guest/brand/${elm.id}`}>
                  <Card>
                    <CardImg top height="200px" alt="Type" src={imageType[elm.type]} />
                    <CardBody>
                      <CardTitle tag="h5">{elm.bname}</CardTitle>
                      <CardSubtitle tag="h6" className="mb-4 mt-0 text-muted">
                        {elm.companyofMedicine ? elm.companyofMedicine.cname : ''}
                      </CardSubtitle>
                      <CardText>Price:-{elm.price}</CardText>
                    </CardBody>
                  </Card>
                </Link>
              </Col>
            );
          })
        ) : (
          <p>No Brand Found</p>
        )}
      </Row>
      {totalItems ? (
        <div className={brandList && brandList.length > 0 ? '' : 'd-none'}>
          <Row className="justify-content-center">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} />
          </Row>
          <Row className="justify-content-center">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </Row>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default BrandShower;
