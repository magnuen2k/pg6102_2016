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
import { useNavigate } from "react-router-dom";

const initialState = {
  origin: { name: "", maxBoats: 0 },
  destination: { name: "", maxBoats: 0 },
  boat: { name: "", length: 0, builder: "", crewSize: 0, maxPassengers: 0 },
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

  const navigate = useNavigate();

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
    navigate("/");
  };

  if (!boats || !ports) {
    return <Loading />;
  }

  const handleBoat = (e: any) => {
    // @ts-ignore
    setTrip({ ...trip, boat: boats.find((b) => b.name === e.target.value) });
  };

  const handlePorts = (e: any, port: string) => {
    if (port === "destination") {
      setTrip({
        ...trip,
        // @ts-ignore
        destination: ports.find((p) => p.name === e.target.value),
      });
    } else {
      setTrip({
        ...trip,
        // @ts-ignore
        origin: ports.find((p) => p.name === e.target.value),
      });
    }
  };

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
