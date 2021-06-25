import React, { useState, useEffect } from 'react';
import { InputGroup, InputGroupAddon, InputGroupText, Input } from 'reactstrap';
import { Button } from 'reactstrap';
import { ListGroup, ListGroupItem } from 'reactstrap';
import styles from './SearchFnc.module.scss';

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
  const [searchList, setSearchList] = useState([]);
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
    if (event.target.value.length >= 3) {
      setSearchList(state => [...tmpData]);
      setSearchTerm(event.target.value);
    } else {
      setSearchList([]);
      setSearchTerm('');
    }
  };

  useEffect(() => {
    searchListRender = (
      <ListGroup className={styles['search-list-group']}>
        {searchList
          .filter(data => {
            if (data.name.toLowerCase().includes(searchTerm.toLowerCase())) return data;
          })
          .map(data => (
            <ListGroupItem className={styles['search-list']} key={data.id} onClick={searchListHandler.bind(this, data.id)}>
              {data.name}
            </ListGroupItem>
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
      {searchListRender}
    </div>
  );
};

export default SearchFnc;
