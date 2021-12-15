import React, { FC, useContext } from "react";
import { ITrip } from "../../interfaces/ITrip";
import { Button, Card } from "react-bootstrap";
import { UserContext } from "../../contexts/UserContext";
import { UserContextType } from "../../types/UserContextType";
import axios from "axios";
import { Link } from "react-router-dom";

const TripItem: FC<ITrip> = ({
  tripId,
  origin,
  destination,
  boat,
  crew,
  passengers,
  tripYear,
}) => {
  const { user } = useContext(UserContext) as UserContextType;

  return (
    <Card>
      <Card.Header>{tripId}</Card.Header>
      <Card.Text>{origin}</Card.Text>
      <Card.Text>{destination}</Card.Text>
      <Card.Text>{boat}</Card.Text>
      <Card.Text>{crew}</Card.Text>
      <Card.Text>{passengers}</Card.Text>
      <Card.Text>{tripYear}</Card.Text>

      {user ? (
        <Link to={`/trips/booking-details/${tripId}`}>
          <Button>BOOK</Button>
        </Link>
      ) : (
        ""
      )}
    </Card>
  );
};

export default TripItem;
