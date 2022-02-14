import React, { Component } from "react";
import Cookies from 'universal-cookie';
import axios from 'axios';
import './VaccineHistory.css';

const { API_URL } = require('../utils/Constants').default;
class VaccineHistory extends Component {
    constructor(props) {
        super(props);

        this.state = {
            vaccinations: []
        }
    }

    async componentDidMount() {
        const cookies = new Cookies();
        let userId = cookies.get('userId');
        
        try {
            const payload = {
                userId: parseInt(userId)
            }
            console.log(payload);
            const response = await axios.post(`${API_URL}/uservaccination/vaccinationHistory`, payload);
            console.log(response.data);
            this.setState({
                vaccinations: response.data
            });
        } catch (error) {
            console.log(error);
        }
    }

    render() {
        return (
            <div className="vaccine-history">
                <div className="d-flex flex-column">
                    <h1>Vaccine History</h1>

                    {
                        this.state.vaccinations.map((v, ind) => {
                            return (
                                <div className="d-flex align-items-center justify-content-evenly history-block" key={ind}>
                                    <h4>{v.vaccination.name}</h4>
                                    <div>Shot Number: {v.shotNumber}</div>
                                    <div>Total Shots Needed: {v.vaccination.numberOfShots}</div>
                                    <div>Clinic: {v.clinic.clinicName}</div>
                                    <div>Appointment Date: {new Date(v.appointment.appointmentDateTime).toLocaleString()}</div>
                                </div>
                            )
                        })
                    }    
                </div>
            </div>
        )
    }
}

export default VaccineHistory;