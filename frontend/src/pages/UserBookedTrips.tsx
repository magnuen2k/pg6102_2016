import React, { useEffect, useState } from "react";
import { Card, Col, Container, Row } from "react-bootstrap";
import axios from "axios";
import { ITrip } from "../interfaces/ITrip";
import Loading from "../components/Loading";
import TripItem from "../components/Trip/TripItem";

const UserBookedTrips = () => {
  const [trips, setTrips] = useState<ITrip[]>();

  useEffect(() => {
    get();
  }, []);

  const get = async () => {
    const res = await axios.get("/api/booking/mybookings");
    setTrips(res.data.data);
  };

  if (!trips) {
    return (
      <Container>
        <Loading />
        Have you booked any trips?
      </Container>
    );
  }

  return (
    <Container>
      <h1>Here is a list of trips you have booked</h1>
      <Row>
        {trips.map((t: ITrip, key: number) => (
          <Col md={12} lg={6} xl={6} className="mt-5" key={key}>
            <TripItem
              tripId={t.tripId}
              origin={t.origin}
              destination={t.destination}
              boat={t.boat}
              crew={t.crew}
              passengers={t.passengers}
              tripYear={t.tripYear}
            />
          </Col>
        ))}
      </Row>
    </Container>
  );
};

export default UserBookedTrips;
