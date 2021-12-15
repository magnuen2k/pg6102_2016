import React, { FC, useContext, useEffect, useState } from "react";
import { useParams } from "react-router";
import { ITrip } from "../interfaces/ITrip";
import axios from "axios";
import { Button, Container } from "react-bootstrap";
import { UserContext } from "../contexts/UserContext";
import { UserContextType } from "../types/UserContextType";

const BookTrip: FC = () => {
  const { user } = useContext(UserContext) as UserContextType;
  const { id } = useParams();

  const [trip, setTrip] = useState<ITrip>();

  useEffect(() => {
    getTrip();
  }, []);

  const getTrip = async () => {
    if (id) {
      const res = await axios.get("/api/trips/" + id);
      setTrip(res.data.data);
    }
  };

  const bookTrip = async () => {
    console.log("USER: " + user?.name + ", want to book trip: " + trip?.tripId);

    let res;

    try {
      res = await axios.post("/api/booking", {
        userId: user?.name,
        tripId: trip?.tripId,
      });
    } catch (e: any) {
      console.log("ERROR");
    }

    console.log(res?.data.code);
  };

  return (
    <Container>
      {trip ? (
        <div>
          {user ? (
            <>
              <h1>Trip booking</h1>
              <div>
                From port: {trip.origin.name} (Max {trip.origin.maxBoats} boats)
              </div>
              <div>
                To port: {trip.destination.name} (Max{" "}
                {trip.destination.maxBoats} boats)
              </div>
              <div>
                Boat: {trip.boat.name} (Max {trip.boat.maxPassengers}{" "}
                passengers, Max {trip.boat.crewSize} crew)
              </div>
              <div>Year created: {trip.tripYear}</div>
              <div>Choose number of crew: {trip.crew}</div>
              <div>Choose number of passengers: {trip.passengers}</div>

              <Button onClick={bookTrip}>Start/Book Trip</Button>
            </>
          ) : (
            ""
          )}
        </div>
      ) : (
        <div>Test there is NO trip clicked</div>
      )}
    </Container>
  );
};

export default BookTrip;
