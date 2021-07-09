import React, { useState, useEffect } from 'react';
import { InputGroup, InputGroupAddon, InputGroupText, Input } from 'reactstrap';
import { Button } from 'reactstrap';
import { ListGroup, ListGroupItem } from 'reactstrap';
import styles from './SearchFnc.module.scss';
import { searchBrandEntities } from '../../entities/brand/brand.reducer';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { Translate, TextFormat, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { Link } from 'react-router-dom';

// const searchListRender = null;

const SearchFnc = props => {
  const { match } = props;
  const dispatch = useAppDispatch();
  const brandList = useAppSelector(state => state.brand.searchedEntity);
  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(props.location, ITEMS_PER_PAGE, 'id'), props.location.search)
  );

  const [searchTerm, setSearchTerm] = useState('');

  const searchButtonHandler = event => {
    // console.log(event.target);
  };

  const searchEnterHandler = event => {};

  const searchInputHandler = event => {
    const name: string = event.target.value;

    setSearchTerm(event.target.value);

    dispatch(searchBrandEntities(name));
    // console.log(brandList);
  };

  return (
    <div>
      <InputGroup style={{ width: '32rem' }}>
        <Input className={styles['search-input']} placeholder="Search...." onChange={searchInputHandler} onKeyDown={searchEnterHandler} />
        <Button color="primary" className="ml-1" onClick={searchButtonHandler}>
          <FontAwesomeIcon icon="search" />
        </Button>
      </InputGroup>
    </div>
  );
};

export default SearchFnc;

// const clearSearchTerm = () => {
//   setSearchTerm('');
// };

// const searchListRender = (
//   <ListGroup className={styles['search-list-group']}>
//     {brandList.map(data => (
//       <Link key={data.id} to={`/guest/brand/${data.id}`} onClick={clearSearchTerm}>
//         <ListGroupItem className={styles['search-list']} key={data.id} onClick={searchListHandler.bind(this, data.id)}>
//           {data.bname}
//         </ListGroupItem>
//       </Link>
//     ))}
//   </ListGroup>
// );
