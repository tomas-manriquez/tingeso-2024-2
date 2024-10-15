import './App.css'
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom'
import Navbar from "./components/Navbar"
import Home from './components/Home';
import NotFound from './components/NotFound';
import ClientList from "./components/ClientList.jsx";
import AddEditClient from "./components/AddEditClient.jsx";
import RequestList from "./components/RequestList.jsx";
import SimulateCredit from "./components/SimulateCredit.jsx";
import RequestStatus from "./components/RequestStatus.jsx";
import AddEditRequest from "./components/AddEditRequest.jsx";

function App() {
    return (
        <Router>
            <div className="container">
                <Navbar></Navbar>
                <Routes>
                    <Route path="/home" element={<Home/>} />
                    <Route path="/client/list" element={<ClientList/>} />
                    <Route path="/client/add" element={<AddEditClient/>} />
                    <Route path="/client/edit/:id" element={<AddEditClient/>} />
                    <Route path="/request/list" element={<RequestList/>} />
                    <Route path="/request/calculate" element={<SimulateCredit/>} />
                    <Route path="/request/status/:id" element={<RequestStatus/>} />
                    <Route path="/request/add" element={<AddEditRequest/>} />
                    <Route path="/request/edit/:id" element={<AddEditRequest/>} />
                    <Route path="*" element={<NotFound/>} />
                </Routes>
            </div>
        </Router>
    );
}

export default App
