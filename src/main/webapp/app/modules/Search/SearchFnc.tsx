import React from 'react';
import { InputGroup, InputGroupAddon, InputGroupText, Input } from 'reactstrap';

const SearchFnc = props => {
  const searchInputHandler = event => {
    if (event.target.value.length >= 3) console.log(event.target.value);
  };

  return (
    <div>
      <InputGroup className="width-100" style={{ width: '32rem' }}>
        <Input placeholder="Search...." onChange={searchInputHandler} />
      </InputGroup>
    </div>
  );
};

export default SearchFnc;
