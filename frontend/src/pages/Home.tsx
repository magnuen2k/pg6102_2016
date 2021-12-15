import React, { useContext } from "react";
import { Container } from "react-bootstrap";
import { UserContext } from "../contexts/UserContext";
import { UserContextType } from "../types/UserContextType";

const Home = () => {
  const { user } = useContext(UserContext) as UserContextType;

  return (
    <Container>
      {user ? <p>Welcome, {user.name}</p> : <p>You need to log in to book</p>}
      <p>HOME</p>
    </Container>
  );
};

export default Home;
