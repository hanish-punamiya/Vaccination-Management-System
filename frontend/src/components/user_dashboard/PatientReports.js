import React, { Component } from 'react';
import DatePicker from "react-datepicker";
import Button from '@mui/material/Button';
import addDays from 'date-fns/addDays';
import Cookies from 'universal-cookie';
import './PatientReports.css';
import Axios from "axios";

const { API_URL } = require('../utils/Constants').default;
class PatientReports extends Component {
    constructor(props) {
        super(props);
        this.state = {
            startDate: this.props.chosenDate,
            endDate: this.props.chosenDate,
            noShowRate: 0,
            noShowAppointments: [

            ],
            totalAppointments: [

            ]
        }
    }

    handleStartChange = (date) => {
        this.setState({
            startDate: date
        });
    }

    handleEndChange = (date) => {
        this.setState({
            endDate: date
        });
    }

    handleGetStats = async () => {
        const cookies = new Cookies();
        let userId = cookies.get('userId');
        const config = {
            headers: {
                "Access-Control-Allow-Origin": "*",
                "Access-Control-Allow-Methods": "GET,PUT,POST,DELETE,PATCH,OPTIONS"
            }
        };
        try {

            const response = await Axios.post(`${API_URL}/report/patientreports`,  {
                    // params: {
                        userId: userId,
                        startDate: this.state.startDate.toString(),
                        endDate: this.state.endDate.toString(),
                        currDate: this.props.chosenDate.toString()

                    // },
            }, config);
            console.log("response of patient reports = " , response);
            this.setState({
                totalAppointments: response.data.totalAppointments,
                noShowAppointments: response.data.noShowAppointments,
                noShowRate : response.data.noShowRate,
            });
        } catch (error) {
            console.log(error);
        }
    }


    render() {
        return (
            <div className='d-flex flex-column justify-content-around patient-reports'>
                <h1>Patient Reports</h1>
                <div className="d-flex flex-column">
                    <h6>Chosen Date: {this.props.chosenDate.toLocaleString()}</h6>
                    <label>
                        Start Date:
                        <DatePicker
                            selected={this.state.startDate}
                            onChange={this.handleStartChange}
                            minDate={addDays(this.props.chosenDate, -365)}
                            maxDate={addDays(this.props.chosenDate, 365)}
                        />
                    </label>
                    <label>
                        End Date:
                        <DatePicker
                            selected={this.state.endDate}
                            onChange={this.handleEndChange}
                            minDate={addDays(this.props.chosenDate, -365)}
                            maxDate={addDays(this.props.chosenDate, 365)}
                        />
                    </label>
                    <div>
                        <Button variant="contained" onClick={this.handleGetStats}>
                            Get Statistics
                        </Button>
                    </div>
                </div>
                <div className='d-flex flex-column'>
                    <h4>No Show Rate</h4>
                    <div>{this.state.noShowRate}</div>
                </div>
                <div className='d-flex flex-column'>
                    <h4>No Show Appointments</h4>
                    {
                        this.state.noShowAppointments.map((apt, index) => {
                            console.log(apt);
                            return (
                                <div className="d-flex align-items-center justify-content-evenly no-show-block" key={index}>
                                    <div>Clinic: {apt.clinic.clinicName}</div>
                                    <div>Date: {new Date(apt.appointmentDateTime).toLocaleString()}</div>
                                </div>
                            )
                        })
                    }
                </div>
                <div className='d-flex flex-column'>
                    <h4>Total Appointments</h4>
                    {
                        this.state.totalAppointments.map((apt, index) => {
                            return (
                                <div className="d-flex align-items-center justify-content-evenly total-block" key={index}>
                                    <div>Clinic: {apt.clinic.clinicName}</div>
                                    <div>Date: {new Date(apt.appointmentDateTime).toLocaleString()}</div>
                                </div>
                            )
                        })
                    }
                </div>
            </div>
        )
    }
}

export default PatientReports;