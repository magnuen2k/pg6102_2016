import React, { useContext } from "react";
import { Container } from "react-bootstrap";
import { UserContext } from "../contexts/UserContext";
import { UserContextType } from "../types/UserContextType";

const Home = () => {
  const { user } = useContext(UserContext) as UserContextType;

  return (
    <Container>
      {user ? (
        <h3 className="mt-5">Welcome, {user.name}</h3>
      ) : (
        <h3 className="mt-5">Sign in to get started</h3>
      )}
      <p className="mt-3">
        On this page you can plan, book and see your trips.
      </p>
    </Container>
  );
};

export default Home;
