import React, { FC, useContext, useEffect, useState } from "react";
import { useParams } from "react-router";
import { ITrip } from "../interfaces/ITrip";
import axios from "axios";
import { Button, Container, Form } from "react-bootstrap";
import { UserContext } from "../contexts/UserContext";
import { UserContextType } from "../types/UserContextType";

interface IPatchTrip {
  passengers: number;
  crew: number;
}

const initialPatchTrip = {
  passengers: 1,
  crew: 1,
};

const BookTrip: FC = () => {
  const { user } = useContext(UserContext) as UserContextType;
  const { id } = useParams();

  const [trip, setTrip] = useState<ITrip>();
  const [patchTrip, setPatchTrip] = useState<IPatchTrip>(initialPatchTrip);

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
      console.log("ERROR booking");
    }

    let patchRes;

    try {
      patchRes = await axios.patch("/api/trips/" + trip?.tripId, patchTrip);
    } catch (e) {
      console.log("ERROR updating trip");
    }

    console.log(res?.data.code);
    setTrip(undefined);
    setPatchTrip(initialPatchTrip);
  };

  const displayPossibilities = (size: number) => {
    return [...Array(size)].map((x: number, i: number) => (
      <option value={i + 1} key={i}>
        {i + 1}
      </option>
    ));
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
              <div>Year created: {trip.tripYear}</div>
              <div>
                Boat: {trip.boat.name} (Max {trip.boat.maxPassengers}{" "}
                passengers, Max {trip.boat.crewSize} crew)
              </div>
              <div>Choose number of crew:</div>
              <Form.Select
                onChange={(e) =>
                  setPatchTrip({ ...patchTrip, crew: parseInt(e.target.value) })
                }
              >
                {displayPossibilities(trip.boat.crewSize)}
              </Form.Select>
              <div>Choose number of passengers:</div>
              <Form.Select
                onChange={(e) =>
                  setPatchTrip({
                    ...patchTrip,
                    passengers: parseInt(e.target.value),
                  })
                }
              >
                {displayPossibilities(trip.boat.maxPassengers)}
              </Form.Select>
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
