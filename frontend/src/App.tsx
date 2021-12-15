import React from 'react';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import {Auth, Home} from "./pages";
import Navigation from "./components/Navgation";
import Trips from "./pages/Trips";


const App = () => {
  return (
    <div className="App">
        <BrowserRouter>
            <Navigation />

            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/trips" element={<Trips />} />
                <Route path="/auth" element={<Auth />} />
            </Routes>
        </BrowserRouter>
    </div>
  );
}

export default App;
