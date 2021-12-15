import React, { FC } from "react";
import { ITrip } from "../../interfaces/ITrip";
import { Button, Card } from "react-bootstrap";

const TripItem: FC<ITrip> = ({
  tripId,
  origin,
  destination,
  boat,
  crew,
  passengers,
  tripYear,
}) => {
  return (
    <Card>
      <Card.Header>{tripId}</Card.Header>
      <Card.Text>{origin}</Card.Text>
      <Card.Text>{destination}</Card.Text>
      <Card.Text>{boat}</Card.Text>
      <Card.Text>{crew}</Card.Text>
      <Card.Text>{passengers}</Card.Text>
      <Card.Text>{tripYear}</Card.Text>

      <Button>BOOK</Button>
    </Card>
  );
};

export default TripItem;
