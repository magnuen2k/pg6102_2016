import { IPort } from "./IPort";
import { IBoat } from "./IBoat";

export interface ITrip {
  tripId?: number;
  origin: IPort;
  destination: IPort;
  boat: IBoat;
  crew: number;
  passengers: number;
  tripYear: number;
}
