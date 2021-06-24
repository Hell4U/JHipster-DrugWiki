import React from 'react';
import { InputGroup, InputGroupAddon, InputGroupText, Input } from 'reactstrap';
import { Button } from 'reactstrap';

const tmpData = [
  {
    id: 1,
    name: "TeraByte"
  },
  {
    id: 2,
    name: "ZotaByte"
  },
  {
    id: 3,
    name:"PetaByte"
  },
  {
    id: 4,
    name: "Tera kya hoga"
  }
]

const SearchFnc = props => {
  const searchHandler = event => {
    if (event.type === 'click' || event.keyCode === 13) {
    }
  };

  const searchInputHandler = event => {
    if (event.target.value.length >= 3)
      
  };

  return (
    <div>
      <InputGroup className="width-100" style={{ width: '32rem' }}>
        <Input placeholder="Search...." onChange={searchInputHandler} onKeyDown={searchHandler} />
        <Button color="primary" className="ml-1" onClick={searchHandler}>
          Search
        </Button>
      </InputGroup>
    </div>
  );
};

export default SearchFnc;
