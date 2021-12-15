import React, { createContext, FC, useEffect, useState } from "react";
import { UserContextType } from "../types/UserContextType";
import { IUser } from "../interfaces/IUser";
import axios from "axios";

export const UserContext = createContext<UserContextType | null>(null);

export const UserProvider: FC = ({ children }) => {
  const [user, setUser] = useState<IUser>();

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      await axios.get("api/auth/user").then((res) => {
        setUser(res.data);
      });
    } catch (e: any) {
      // @ts-ignore
      setUser(null);
    }
  };

  const updateUser = (u: IUser | null) => {
    if (u) {
      setUser(u);
    } else {
      // @ts-ignore
      setUser(null);
    }
  };

  return (
    <UserContext.Provider value={{ user, updateUser }}>
      {children}
    </UserContext.Provider>
  );
};
