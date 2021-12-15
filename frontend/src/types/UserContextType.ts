import { IUser } from "../interfaces/IUser";

export type UserContextType = {
  user: IUser | undefined;
  updateUser: (u: IUser | null) => void;
};
