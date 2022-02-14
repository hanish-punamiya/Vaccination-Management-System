import React, { useState } from 'react';
import {
  BrowserRouter,
  Routes,
  Route,
} from 'react-router-dom';
import DateAdapter from '@mui/lab/AdapterDateFns';
import LocalizationProvider from '@mui/lab/LocalizationProvider';
import "./App.css"
import Landing from './components/landing/Landing';
import Login from './components/authentication/Login';
import Signup from './components/authentication/Signup';
import Dashboard from './components/user_dashboard/Dashboard';
import VaccinesDue from './components/user_dashboard/VaccinesDue';
import Appointment from './components/user_dashboard/Appointment';
import VaccineHistory from './components/user_dashboard/VaccineHistory';
import PatientReports from './components/user_dashboard/PatientReports';
import AdminManagement from './components/admin/AdminManagement';

import SystemReports from './components/admin/SystemReports';

function App() {
  const [currentDate, setCurrentDate] = useState(new Date());
  const [chosenDate, setChosenDate] = useState(new Date());

  return (
    <LocalizationProvider dateAdapter={DateAdapter}>
      <div className="App">
        <BrowserRouter>
            <div>
              <Routes>
                <Route path="/" element={<Landing/>}/>
                <Route path="/login" element={<Login/>}/>
                <Route path="/signup" element={<Signup/>}/>
                <Route path="/dashboard" element={<Dashboard currentPage={<VaccinesDue currentDate={currentDate} chosenDate={chosenDate} chosenDateHandler={setChosenDate}/>  }/>  }/>
                <Route path="/appointment" element={<Dashboard currentPage={<Appointment currentDate={currentDate} chosenDate={chosenDate}/>}/>   }/>
                <Route path="/history" element={<Dashboard currentPage={<VaccineHistory currentDate={currentDate} chosenDate={chosenDate}/>}/>    }/>
                <Route path="/patientreports" element={<Dashboard currentPage={<PatientReports currentDate={currentDate} chosenDate={chosenDate}/>}/>    }/>
                <Route path="/admin" element={<Dashboard currentPage={<AdminManagement currentDate={currentDate} chosenDate={chosenDate}/>}/>   }/>
                <Route path="/systemreports" element={<Dashboard currentPage={<SystemReports currentDate={currentDate} chosenDate={chosenDate}/>}/>   }/>

              </Routes>
            </div>
        </BrowserRouter>
      </div>
    </LocalizationProvider>
    
  );
}

export default App;
