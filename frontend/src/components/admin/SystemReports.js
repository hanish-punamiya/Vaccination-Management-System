import React, { Component } from 'react';
import DatePicker from "react-datepicker";
import Button from '@mui/material/Button';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';
import addDays from 'date-fns/addDays';
import Cookies from 'universal-cookie';
import axios from 'axios';
import './SystemReports.css';

const { API_URL } = require('../utils/Constants').default;
class SystemReports extends Component {
    constructor(props) {
        super(props);
        this.state = {
            startDate: this.props.chosenDate,
            endDate: this.props.chosenDate,
            clinicSelected:{},
            allClinics: [
            ],
            noShowRate: 0,
            noShowAppointments: [

            ],
            totalAppointments: [

            ]
        }
    }

    async componentDidMount() {
        try {
            const response = await axios.get(`${API_URL}/clinic/getAllClinics`);
            console.log(response.data);
            this.setState({
                allClinics: response.data,
            });
        } catch (error) {
            console.log(error);
        }
    }

    handleClinicChange = (e) => {
        this.setState({
            clinicSelected: e.target.value
        });
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
        try {
            const payload = {
                clinicId: this.state.clinicSelected.id,
                startDate: this.state.startDate.toString(),
                endDate: this.state.endDate.toString(),
                currDate: this.props.chosenDate.toString()
            }
            const response = await axios.post(`${API_URL}/report/systemreports`, payload);
            this.setState({
                totalAppointments: response.data.totalAppointments,
                noShowAppointments: response.data.noShowAppointments,
                noShowRate : response.data.noShowRate,
                clinic: response.data.clinic,
            });
        } catch (error) {
            console.log(error);
        }
    }

    render() {
        return (
            <div className='d-flex flex-column justify-content-around system-reports'>
                <h1>System Reports</h1>
                <div className="d-flex flex-column">
                    <h6>Chosen Date: {this.props.chosenDate.toLocaleString()}</h6>
                    <FormControl fullWidth>
                        <InputLabel id="sys-clinic-label">Clinic</InputLabel>
                        <Select
                            labelId="sys-clinic-label"
                            id="sys-clinic-select"
                            value={this.state.clinicSelected}
                            label="Clinic"
                            onChange={this.handleClinicChange}
                        >
                        {
                            this.state.allClinics.map((clinic, index) => {
                                return (
                                    <MenuItem key={index} value={clinic}>{clinic.clinicName}</MenuItem>
                                )
                            })
                        }
                        </Select>
                    </FormControl>
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
                            return (
                                <div className="d-flex align-items-center justify-content-evenly sys-no-show-block" key={index}>
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
                                <div className="d-flex align-items-center justify-content-evenly sys-total-block" key={index}>
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

export default SystemReports; 
