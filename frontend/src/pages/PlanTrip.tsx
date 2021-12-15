import React, { FC, useContext } from "react";
import { Container } from "react-bootstrap";
import TripForm from "../components/Trip/TripForm";

const PlanTrip = () => {
  return (
    <Container>
      <TripForm />
    </Container>
  );
};

export default PlanTrip;
