import React from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import {
  Auth,
  BookTrip,
  Home,
  NotFound,
  PlanTrip,
  Trips,
  UserBookedTrips,
} from "./pages";
import Navigation from "./components/Navgation";

const App = () => {
  return (
    <div className="App">
      <BrowserRouter>
        <Navigation />

        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/trips" element={<Trips />} />
          <Route path="/trips/mytrips" element={<UserBookedTrips />} />
          <Route path="/trips/plan" element={<PlanTrip />} />
          <Route path="/trips/booking-details/:id" element={<BookTrip />} />
          <Route path="/auth" element={<Auth />} />
          <Route path="*" element={<NotFound />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
};

export default App;
