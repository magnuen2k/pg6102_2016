import React, { FC, useContext } from "react";
import { ITrip } from "../../interfaces/ITrip";
import { Button, Card } from "react-bootstrap";
import { UserContext } from "../../contexts/UserContext";
import { UserContextType } from "../../types/UserContextType";
import axios from "axios";

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

  const bookTrip = async () => {
    console.log("USER: " + user?.name + ", want to book trip: " + tripId);

    let res;

    try {
      res = await axios.post("/api/booking", { userId: user?.name, tripId });
    } catch (e: any) {
      console.log("ERROR");
    }

    console.log(res?.data.code);
  };

  return (
    <Card>
      <Card.Header>{tripId}</Card.Header>
      <Card.Text>{origin}</Card.Text>
      <Card.Text>{destination}</Card.Text>
      <Card.Text>{boat}</Card.Text>
      <Card.Text>{crew}</Card.Text>
      <Card.Text>{passengers}</Card.Text>
      <Card.Text>{tripYear}</Card.Text>

      <Button onClick={bookTrip}>BOOK</Button>
    </Card>
  );
};

export default TripItem;
