import React, { FC, useContext, useState } from "react";
import { Button, Form, FormControl } from "react-bootstrap";
import { ITrip } from "../../interfaces/ITrip";
import Loading from "../Loading";

const initialState = {
  origin: "",
  destination: "",
  boat: "",
  crew: 0,
  passengers: 0,
  tripYear: 0,
};

const TripForm = () => {
  const [trip, setTrip] = useState<ITrip>(initialState);
  //const [response, setResponse] = useState<IResponse>();
  const [isLoading, setIsLoading] = useState<boolean>(false);

  const addNewPlayer = async () => {
    // Add trip to database
    let res;

    // Try to POST new trip, else handle error
    try {
      //res = await addPlayer(trip);
      setIsLoading(true);
    } catch (e: any) {
      //handleError(e, setIsLoading, setResponse);
    }

    // If POST successful, display message in popup
    /*if (res && res.status === 201) {
      setIsLoading(false);
      setResponse({
        message: "Player added successfully",
        statusCode: res.status,
      });
    }*/

    // Clear input form
    setTrip(initialState);
  };

  return (
    <div className="mb-2 mt-5">
      <Form>
        <Form.Group>
          <FormControl
            placeholder="Origin"
            value={trip.origin}
            onChange={(e) => setTrip({ ...trip, origin: e.target.value })}
          />
        </Form.Group>
        <Form.Group>
          <FormControl
            placeholder="Destination"
            value={trip.destination}
            onChange={(e) => setTrip({ ...trip, destination: e.target.value })}
          />
        </Form.Group>
        <Button onClick={addNewPlayer}>Plan trip</Button>
      </Form>
      {isLoading && <Loading />}
      {/*{response && (
        <ResponseView
          message={response.message}
          statusCode={response.statusCode}
        />
      )}*/}
    </div>
  );
};

export default TripForm;
