import React, { useEffect, useState } from "react";
import axios from "axios";
import { ITrip } from "../../interfaces/ITrip";
import { Button, Col, Container, Form, Row } from "react-bootstrap";
import TripItem from "./TripItem";
import Loading from "../Loading";
import { IBoat } from "../../interfaces/IBoat";

interface ITripPage {
  trips: ITrip[];
  next: string;
  prev?: string;
}

const TripList = () => {
  const [tripPage, setTripPage] = useState<ITripPage>();
  const [boats, setBoats] = useState<IBoat[]>();

  useEffect(() => {
    getFromBackend();
    getBoats();
  }, []);

  const getBoats = async () => {
    const res = await axios.get("/api/trips/boats");
    setBoats(res.data.data);
  };

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
            status={t.status}
            booking={true}
          />
        </Col>
      ))
    );
  };

  const sortList = (boatName: string) => {
    setTripPage({
      ...tripPage,
      // @ts-ignore
      trips: tripPage?.trips.filter((t) => t.boat.name == boatName),
    });
  };

  if (!tripPage?.trips || !boats) {
    return <Loading />;
  }

  return (
    <Container>
      <h3 className="mt-3">All trips</h3>
      <p>Filter selected page by boat</p>
      <Form.Select className="mt-3" onChange={(e) => sortList(e.target.value)}>
        {boats.map((b: IBoat, i) => (
          <option value={b.name} key={i}>
            {b.name}
          </option>
        ))}
      </Form.Select>
      <Button className="mt-5" onClick={getFromBackend}>
        Back to start
      </Button>
      <Button className="mt-5 mx-3" onClick={getNextPage}>
        Next page
      </Button>
      <Row className="mb-5">{createTripList()}</Row>
    </Container>
  );
};

export default TripList;
