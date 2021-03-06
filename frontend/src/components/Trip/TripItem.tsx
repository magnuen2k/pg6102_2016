import React, { FC, useContext } from "react";
import { ITrip } from "../../interfaces/ITrip";
import { Button, Card } from "react-bootstrap";
import { UserContext } from "../../contexts/UserContext";
import { UserContextType } from "../../types/UserContextType";
import axios from "axios";
import { Link } from "react-router-dom";
import { IPort } from "../../interfaces/IPort";
import { IBoat } from "../../interfaces/IBoat";

interface ITripItemProps {
  tripId?: number;
  origin: IPort;
  destination: IPort;
  boat: IBoat;
  crew: number;
  passengers: number;
  tripYear: number;
  status: string;
  booking?: boolean;
}

const TripItem: FC<ITripItemProps> = ({
  tripId,
  origin,
  destination,
  boat,
  crew,
  passengers,
  tripYear,
  booking,
  status,
}) => {
  const { user } = useContext(UserContext) as UserContextType;

  return (
    <Card>
      <Card.Header>TRIP - {tripId}</Card.Header>
      <Card.Text>Origin: {origin.name}</Card.Text>
      <Card.Text>Destination: {destination.name}</Card.Text>
      <Card.Text>Boat: {boat.name}</Card.Text>
      <Card.Text>Crew: {crew}</Card.Text>
      <Card.Text>Passengers: {passengers}</Card.Text>
      <Card.Text>Year: {tripYear}</Card.Text>
      <Card.Text>Weather status: {status}</Card.Text>

      {user && booking ? (
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
