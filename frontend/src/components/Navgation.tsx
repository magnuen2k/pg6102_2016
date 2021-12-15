import React, { useContext } from "react";
import { Button, Container, Nav, Navbar } from "react-bootstrap";
import { Link } from "react-router-dom";
import { UserContext } from "../contexts/UserContext";
import { UserContextType } from "../types/UserContextType";
import axios from "axios";

const Navigation = () => {
  const { user, updateUser } = useContext(UserContext) as UserContextType;

  const doLogout = async () => {
    await axios.post("/api/auth/logout");
    updateUser(null);
  };

  return (
    <>
      <Navbar bg="dark" variant="dark">
        <Container>
          <Navbar.Brand href="/">Home</Navbar.Brand>
          <Nav className="me-auto">
            <Nav.Link as={Link} to="/trips">
              Trips
            </Nav.Link>
            {user ? (
              <>
                <Nav.Link as={Link} to="/trips/plan">
                  Plan a trip
                </Nav.Link>
                <Nav.Link as={Link} to="/trips/mytrips">
                  My Booked Trips
                </Nav.Link>
              </>
            ) : (
              ""
            )}
          </Nav>
          <Nav>
            {user ? (
              <Nav.Link onClick={doLogout}>
                <Button variant="outline-danger">Logout</Button>
              </Nav.Link>
            ) : (
              <Nav.Link as={Link} to="/auth">
                <Button variant="outline-danger">Sign in/up</Button>
              </Nav.Link>
            )}
          </Nav>
        </Container>
      </Navbar>
    </>
  );
};

export default Navigation;
