import React, { useEffect, useState } from "react";
import axios from "axios";
import { ITrip } from "../../interfaces/ITrip";
import { Button, Col, Container, Row } from "react-bootstrap";
import TripItem from "./TripItem";
import Loading from "../Loading";

interface ITripPage {
  trips: ITrip[];
  next: string;
  prev?: string;
}

const TripList = () => {
  const [tripPage, setTripPage] = useState<ITripPage>();

  useEffect(() => {
    getFromBackend();
  }, []);

  const getFromBackend = async () => {
    const res = await axios.get("/api/trips");
    console.log(res.data.data.list);
    setTripPage({ trips: res.data.data.list, next: res.data.data.next });
  };

  const getNextPage = async () => {
    console.log(tripPage);
    if (tripPage?.next) {
      const prevNext = tripPage.next;
      const res = await axios.get(tripPage.next);
      setTripPage({
        trips: res.data.data.list,
        next: res.data.data.next,
        prev: prevNext,
      });
    } else {
      console.log("no more pages");
    }
  };

  const createTripList = () => {
    return (
      tripPage &&
      tripPage?.trips.map((t: ITrip, key: number) => (
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
      ))
    );
  };

  if (!tripPage?.trips) {
    return <Loading />;
  }

  return (
    <Container>
      <Row className="mb-5">{createTripList()}</Row>
      <Button onClick={getFromBackend}>Back to start</Button>
      <Button onClick={getNextPage}>Next page</Button>
    </Container>
  );
};

export default TripList;
