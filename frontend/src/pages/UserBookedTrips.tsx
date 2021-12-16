import React, { useEffect } from "react";
import { Container } from "react-bootstrap";
import axios from "axios";

const UserBookedTrips = () => {
  useEffect(() => {
    get();
  }, []);

  const get = async () => {
    const res = await axios.get("/api/booking/mybookings");
    console.log(res);
  };

  return (
    <Container>
      <h1>Here is a list of trips you have booked</h1>
    </Container>
  );
};

export default UserBookedTrips;
