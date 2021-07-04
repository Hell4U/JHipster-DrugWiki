import React, { useState, useEffect } from 'react';
import { InputGroup, InputGroupAddon, InputGroupText, Input } from 'reactstrap';
import { Button } from 'reactstrap';
import { ListGroup, ListGroupItem } from 'reactstrap';
import styles from './SearchFnc.module.scss';
import { searchBrandEntities } from '../../entities/brand/brand.reducer';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { Link } from 'react-router-dom';

const tmpData = [
  {
    id: 1,
    name: 'TeraByte',
  },
  {
    id: 2,
    name: 'ZotaByte',
  },
  {
    id: 3,
    name: 'PetaByte',
  },
  {
    id: 4,
    name: 'Giga Hepta Byte',
  },
];
let searchListRender = null;

const SearchFnc = props => {
  const { match } = props;
  const dispatch = useAppDispatch();
  let brandList = useAppSelector(state => state.brand.searchedEntity);

  const [searchTerm, setSearchTerm] = useState('');

  const searchButtonHandler = event => {
    console.log(event.target);
  };

  const searchEnterHandler = event => {
    if (event.keyCode === 13) console.log(event.target);
  };

  const searchListHandler = (id, e) => {
    console.log(id);
  };

  const searchInputHandler = event => {
    const name: string = event.target.value;

    setSearchTerm(event.target.value);

    dispatch(searchBrandEntities(name));
  };

  useEffect(() => {
    searchListRender = (
      <ListGroup className={styles['search-list-group']}>
        {brandList.map(data => (
          <Link to={`/guest/brand/${data.id}`}>
            <ListGroupItem className={styles['search-list']} key={data.id} onClick={searchListHandler.bind(this, data.id)}>
              {data.bname}
            </ListGroupItem>
          </Link>
        ))}
      </ListGroup>
    );
  }, [searchTerm]);

  return (
    <div>
      <InputGroup style={{ width: '32rem' }}>
        <Input className={styles['search-input']} placeholder="Search...." onChange={searchInputHandler} onKeyDown={searchEnterHandler} />
        <Button color="primary" className="ml-1" onClick={searchButtonHandler}>
          Search
        </Button>
      </InputGroup>
      {searchTerm.length > 0 && searchListRender}
    </div>
  );
};

export default SearchFnc;
