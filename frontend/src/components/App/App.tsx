import { Header } from "./App.styled";

import React from "react";
import {
    BrowserRouter as Router,
    Routes,
    Route,
} from "react-router-dom";
import Home from "./pages/home";
import Profile from "./pages/profile";
import Scoreboard from "./pages/scoreboard";
import Exercises from "./pages/exercises";

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/profile" element={<Profile />} />
                <Route path="/scoreboard" element={<Scoreboard />} />
                <Route path="/exercises" element={<Exercises />} />
            </Routes>
        </Router>
    );
}

export default App;
