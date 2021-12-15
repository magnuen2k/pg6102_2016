import React, { FC, useContext, useEffect, useState } from "react";
import { Button, Form, FormControl } from "react-bootstrap";
import Loading from "../Loading";
import { ITrip } from "../../interfaces/ITrip";
import { IBoat } from "../../interfaces/IBoat";
import { IPort } from "../../interfaces/IPort";
import axios from "axios";
import DropDownOptions from "../DropDownOptions";
import { IResponse } from "../../interfaces/IResponse";
import ResponseView from "../ResponseView";
import { handleError } from "../../util/HandleError";

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
  const [response, setResponse] = useState<IResponse>();
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [boats, setBoats] = useState<IBoat[]>();
  const [ports, setPorts] = useState<IPort[]>();

  useEffect(() => {
    getBoats();
    getPorts();
  }, []);

  const getBoats = async () => {
    const res = await axios.get("/api/trips/boats");
    //const boatNames = res.data.data.map
    setBoats(res.data.data);
  };

  const getPorts = async () => {
    const res = await axios.get("/api/trips/ports");
    setPorts(res.data.data);
  };

  const planTrip = async () => {
    // Add trip to database
    let res;

    // Try to POST new trip, else handle error
    try {
      console.log(trip);
      res = await axios.put("/api/trips", trip);
      setIsLoading(true);
    } catch (e: any) {
      handleError(e, setIsLoading, setResponse);
    }

    // If POST successful, display message in popup
    if (res && res.status === 201) {
      setIsLoading(false);
      setResponse({
        message: "Trip planned successfully",
        statusCode: res.status,
      });
    }

    // Clear input form
    setTrip(initialState);
  };

  const handleBoat = (e: any) => {
    if (e.target.value === "default") {
      setTrip({ ...trip, boat: "No boat" });
    } else {
      setTrip({ ...trip, boat: e.target.value });
    }
  };

  const handlePorts = (e: any, port: string) => {
    if (port === "destination") {
      if (e.target.value === "default") {
        setTrip({ ...trip, destination: "No port" });
      } else {
        setTrip({ ...trip, destination: e.target.value });
      }
    } else {
      if (e.target.value === "default") {
        setTrip({ ...trip, origin: "No port" });
      } else {
        setTrip({ ...trip, origin: e.target.value });
      }
    }
  };

  if (!boats || !ports) {
    return <Loading />;
  }

  return (
    <div className="mb-2 mt-5">
      <Form>
        <Form.Group>
          <FormControl
            type="number"
            placeholder="Trip Year"
            value={trip.tripYear}
            onChange={(e) =>
              setTrip({ ...trip, tripYear: parseInt(e.target.value) })
            }
          />
        </Form.Group>
        <Form.Group>
          <DropDownOptions
            handleChange={handleBoat}
            options={boats.map((b: IBoat) => {
              return b.name;
            })}
          />
        </Form.Group>
        <Form.Group>
          <DropDownOptions
            handleChange={(e) => handlePorts(e, "destination")}
            options={ports.map((p: IPort) => {
              return p.name;
            })}
          />
        </Form.Group>
        <Form.Group>
          <DropDownOptions
            handleChange={(e) => handlePorts(e, "origin")}
            options={ports.map((p: IPort) => {
              return p.name;
            })}
          />
        </Form.Group>
        <Button onClick={planTrip}>Plan trip</Button>
      </Form>
      {isLoading && <Loading />}
      {response && (
        <ResponseView
          message={response.message}
          statusCode={response.statusCode}
        />
      )}
    </div>
  );
};

export default TripForm;
