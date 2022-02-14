import { Button } from '@mui/material';
import React, { Component } from 'react';
import DatePicker from "react-datepicker";
import addDays from 'date-fns/addDays';
import Cookies from 'universal-cookie';
import axios from 'axios';
import './VaccinesDue.css';


const { API_URL } = require('../utils/Constants').default;
class VaccinesDue extends Component {
    constructor(props) {
        super(props)
        this.state = {
            vaccinesDue: [
            ],
            appointments: [
            ],
            pastAppointments:[],
            futureAppointments:[],
        }
    }

    async componentDidMount() {
        const cookies = new Cookies();
        let userId = cookies.get('userId');
        
        const payload = {
            userId: parseInt(userId),
            currentDate: this.props.chosenDate.toString() 
        }

        try {
            const response = await axios.post(`${API_URL}/uservaccination/due`, payload);
            const pastresponse = await axios.post(`${API_URL}/appointment/past`, payload);
            const futureresponse = await axios.post(`${API_URL}/appointment/future`, payload);
            console.log(futureresponse.data)

            let tempAppointments = [];
            for(let vaccinationsDue of response.data){
                if(vaccinationsDue.appointment){
                    vaccinationsDue.appointment["clinicName"] = vaccinationsDue.clinicName;
                    vaccinationsDue.appointment["vaccinationName"] = vaccinationsDue.vaccinatioName;
                    tempAppointments.push(vaccinationsDue.appointment);
                }
            }

            this.setState({
                vaccinesDue: response.data,
                appointments: tempAppointments,
                pastAppointments: pastresponse.data,
                futureAppointments: futureresponse.data,
            });
        } catch (error) {
            console.log(error);
        }
    }
    
    reloadVaccines = async() => {
        const cookies = new Cookies();
        let userId = cookies.get('userId');
        
        const payload = {
            userId: parseInt(userId),
            currentDate: this.props.chosenDate.toString() 
        }

        try {
            const response = await axios.post(`${API_URL}/uservaccination/due`, payload);
            const pastresponse = await axios.post(`${API_URL}/appointment/past`, payload);
            const futureresponse = await axios.post(`${API_URL}/appointment/future`, payload);
            console.log(pastresponse.data);

            let tempAppointments = [];
            for(let vaccinationsDue of response.data){
                if(vaccinationsDue.appointment){
                    vaccinationsDue.appointment["clinicName"] = vaccinationsDue.clinicName;
                    vaccinationsDue.appointment["vaccinationName"] = vaccinationsDue.vaccinatioName;
                    tempAppointments.push(vaccinationsDue.appointment);
                }
            }

            this.setState({
                vaccinesDue: response.data,
                appointments: tempAppointments,
                pastAppointments: pastresponse.data,
                futureAppointments: futureresponse.data,
            });
        } catch (error) {
            console.log(error);
        }
    }

    handleCheckin = async(e) => {
        try {
            const payload = {
                appointmentId: parseInt(e.target.value)
            }
            const response = await axios.post(`${API_URL}/appointment/checkIn`, payload);
            alert("check in successful")
        } catch (error) {
            console.log(error);
            alert("error in checking in")
        } 
    }

    render() {
        console.log(this.state.pastAppointments)
        return (
            <div className='d-flex flex-column'>
                <div className='d-flex justify-content-evenly time-section'>
                    <h5>Current Date: {this.props.currentDate.toLocaleString()}</h5>
                    <h5>Chosen Date: {this.props.chosenDate.toLocaleString()}</h5>
                    <DatePicker 
                            selected={this.props.chosenDate} 
                            onChange={(date) => {
                                this.props.chosenDateHandler(date);
                                this.reloadVaccines();
                            }}
                            showTimeSelect
                            timeFormat="HH:mm"
                            timeIntervals={15}
                            timeCaption="time"
                            dateFormat="MMMM d, yyyy h:mm aa"
                            minDate={this.props.currentDate}
                            maxDate={addDays(this.props.currentDate, 365)}
                    />
                    <Button variant="outlined" onClick={() => this.props.chosenDateHandler(this.props.currentDate)}>
                        Revert to Current Date
                    </Button>
                </div>
                <div className="d-flex flex-fill flex-column due-section">
                    <h1>Vaccines Due</h1>
                    <div className="d-flex flex-column due-list">
                        {
                                this.state.vaccinesDue.map((vaccine, index) => {
                                    return (
                                        <div className='d-flex due-vaccine align-items-center' key={index}>
                                            <h5>{vaccine.vaccinatioName}</h5>
                                            <p>Shot Number Due: {vaccine.numberOfShotDue}</p>
                                            <p>Due Date: {new Date(vaccine.dueDate).toLocaleString()}</p>
                                            <p>Status: {/*this.props.chosenDate > new Date(vaccine.dueDate) ? 'OVERDUE' : */vaccine.status}</p>
                                        </div>
                                    )
                                })
                            }
                    </div>
                </div>
                <div className="d-flex flex-fill flex-column">
                    <h1>Upcoming Appointments</h1>
                    <div className="d-flex flex-column due-list">
                        {
                            this.state.appointments.map((appointment, index) => {
                                return (
                                    <div key={index}>
                                        {   this.props.chosenDate > new Date(appointment.appointmentDateTime) ? null :
                                            <div className='d-flex due-vaccine align-items-center'>
                                            <h5>{appointment.vaccinationName}</h5>
                                            <p>Clinic: {appointment.clinicName}</p>
                                            <p>Date: {new Date(appointment.appointmentDateTime).toLocaleString()}</p>
                                            {/* IF date is within 24hrs of chosen date && user hasn't checked in yet, show check in button ELSE hide button */}
                                            {
                                                !appointment.checkIn && this.props.chosenDate < new Date(appointment.appointmentDateTime) && (new Date(appointment.appointmentDateTime) - this.props.chosenDate) / 36e5 <= 24  ? 
                                                <Button variant="outlined" onClick={this.handleCheckin} value={appointment.id}>
                                                    Check In
                                                </Button>
                                                : null 
                                                
                                            }
                                        </div>
                                        }
                                        
                                    </div>
                                    
                                )
                            })
                        }
                    </div>
                </div>

                <div className="d-flex flex-fill flex-column">
                    <h1>Past Appointments</h1>
                    <div className="d-flex flex-column due-list">
                        {
                            this.state.pastAppointments.map((appointment, index) => {
                                return (
                                    <div key={index}>
                                        <div className='d-flex due-vaccine align-items-center'>
                                                {
                                                    appointment.vaccinations.map((vac,index) => {
                                                        return (
                                                            <h5 key={index}>
                                                                {vac.name}
                                                            </h5>
                                                        )
                                                    })
                                                }
                                            <p>Clinic: {appointment.clinic.clinicName}</p>
                                            <p>Date: {new Date(appointment.appointmentDateTime).toLocaleString()}</p>
                                        </div>  
                                    </div>
                                )
                            })
                        }
                    </div>
                </div>

                <div className="d-flex flex-fill flex-column">
                    <h1>Future Appointments</h1>
                    <div className="d-flex flex-column due-list">
                        {
                            this.state.futureAppointments.map((appointment, index) => {
                                return (
                                    <div key={index}>
                                        <div className='d-flex due-vaccine align-items-center'>
                                            {
                                                appointment.vaccinations.map((vac,index) => {
                                                    return (
                                                        <h5 key={index}>
                                                            {vac.name}
                                                        </h5>
                                                    )
                                                })
                                            }
                                            <p>Clinic: {appointment.clinic.clinicName}</p>
                                            <p>Date: {new Date(appointment.appointmentDateTime).toLocaleString()}</p>
                                        </div> 
                                    </div>
                                )
                            })
                        }
                    </div>
                </div>
            </div>
        )
    }
}

export default VaccinesDue;