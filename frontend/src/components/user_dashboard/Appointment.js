import React, { Component } from 'react'
import DatePicker from "react-datepicker";
import Cookies from 'universal-cookie';
import axios from 'axios';
import './Appointment.css';
import "react-datepicker/dist/react-datepicker.css";
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';
import Button from '@mui/material/Button';

const { API_URL } = require('../utils/Constants').default;
class Appointment extends Component {
    constructor(props) {
        super(props);
        this.state = {
            appointmentDate: this.props.chosenDate,
            clinicSelected:"",
            availableClinics: [
                {name: "Sunnyvale CVS"},
                {name: "Evergreen CVS"},
                {name: "Palo Alto CVS"},
            ],
            vaccinesSelected: [],
            availableVaccines: [
                {id:"123", name: "Pfizer"},
                {id:"124", name: "Moderna"},
                {id:"125", name: "Tetnis"},
                {id:"126", name: "Swine flu"},
                {id:"127", name: "Johnson&Johnson"},
                {id:"128", name: "Small pox"},
                {id:"129", name: "Meningitis"}
            ],

            scheduledAppointments: [
            ],
            appointmentSelected: {},
            rescheduleDate: this.props.chosenDate,
            minRescheduleDate: this.props.chosenDate,
            rescheduleClinicSelected:"",
            rescheduleAvailableClinics: [
                {name: "Sunnyvale CVS"},
                {name: "Evergreen CVS"},
                {name: "Palo Alto CVS"},
            ],
        }
    }

    async componentDidMount() {
        const cookies = new Cookies();
        let userId = cookies.get('userId');
        const payload = {
            userId: userId,
        }
        
        const futurePayload = {
            userId: parseInt(userId),
            currentDate: this.props.chosenDate.toString() 
        }

        try {
            const vaccineResponse = await axios.get(`${API_URL}/vaccination/all`);
            console.log(vaccineResponse);

            const futureresponse = await axios.post(`${API_URL}/appointment/future`, futurePayload);
            console.log(futureresponse.data)


            this.setState({
                availableVaccines: vaccineResponse.data,
                scheduledAppointments: futureresponse.data,
            });
        } catch (error) {
            console.log(error);
        }
    }

    handleDateChange = async (date) => {
        this.setState({
            appointmentDate: date
        });

        const payload = {
            appointmentTime: this.state.appointmentDate.toString(),
        }
        console.log("appointment time  - " , payload);
        try {
            const response = await axios.post(`${API_URL}/clinic/getAvailableClinics`, payload);
            console.log("response = " ,  response.data);
            // if(response.data.length == 0) alert("no clinic available on that date time");
            this.setState({
                availableClinics: response.data,
            });
        } catch (error) {
            console.log(error);
        }
    }

    handleVaccineChange = async (e) => {
        if(e.target.value.length >= this.state.vaccinesSelected.length) {
            const cookies = new Cookies();
            let userId = cookies.get('userId');
            const payload = {
                vaccinationId: e.target.value.at(-1).id,
                userId: parseInt(userId),
                date: this.state.appointmentDate.toString(),
            }
            console.log(payload);

            try {
                const response = await axios.post(`${API_URL}/appointment/shot_number`, payload);
                console.log(response.data);
                if(response.data !== -1) {
                    let addedVac = e.target.value.filter(v => !this.state.vaccinesSelected.includes(v));
                    addedVac[0]["shotNumber"] = response.data;
                    console.log(addedVac[0]);
                    this.setState({
                        vaccinesSelected: [...this.state.vaccinesSelected, addedVac[0]]
                    });
                } else {
                    alert("please complete previous shots before taking this shot");
                }
            } catch (error) {
                console.log(error);
            }
        } else {
            this.setState({
                vaccinesSelected: e.target.value,
            });
        }
    }

    handleClinicChange = (e) => {
        this.setState({
            clinicSelected: e.target.value
        });
    }

    bookAppointment = async (e) => {
        const cookies = new Cookies();
        let userId = cookies.get('userId');

        let shotNumbers = [];
        let vaccineIds = [];
        this.state.vaccinesSelected.forEach((v) => {
            shotNumbers.push(v.shotNumber);
            vaccineIds.push(v.id);
        })

        const payload = {
            userId: parseInt(userId),
            appointmentDate: this.state.appointmentDate.toString(),
            appointmentBookedDate: this.props.chosenDate.toString(),
            vaccinationIds: vaccineIds,
            clinicId: this.state.clinicSelected.id,
            shotNumber: shotNumbers, 
        }

        console.log(payload);

        try {
            const response = await axios.post(`${API_URL}/appointment/book`, payload);
            console.log(response.data);
            this.setState({
                scheduledAppointments: [...this.state.scheduledAppointments, response.data]
            });
            alert("appointment booked successfully")
        } catch (error) {
            console.log(error);
            alert("error in booking appointment")
        }

    }

    handleAppointmentChange = (e) => {
        this.setState({
            appointmentSelected: e.target.value,
            minRescheduleDate: e.target.value.date,
        });
    }

    handleRescheduleDateChange = async(date) => {
        const payload = {
            appointmentTime: date.toString(),
        }

        try {
            const response = await axios.post(`${API_URL}/clinic/getAvailableClinics`, payload);
            this.setState({
                availableClinics: response.clinics,
                rescheduleDate: date
            });
        } catch (error) {
            console.log(error);
        }
    }

    handleRescheduleClinicChange = (e) => {
        this.setState({
            rescheduleClinicSelected: e.target.value
        });
    }

    rescheduleAppointment = async (e) => {
        const cookies = new Cookies();
        let userId = cookies.get('userId');

        console.log(e);
        const payload = {
            appointmentId: parseInt(this.state.appointmentSelected.id),
            newAppointmentDate: this.state.rescheduleDate.toString(),
            currentDate: this.props.chosenDate.toString(),
        }
        console.log(payload);

        try {
            const response = await axios.post(`${API_URL}/appointment/change`, payload);
            console.log(response);

            this.setState({
                scheduledAppointments: this.state.scheduledAppointments.filter((appt) => {
                    return appt.id !== response.data.id
                }, ()=> {
                    this.setState({
                        scheduledAppointments: [...this.state.scheduledAppointments, response.data]
                    })
                })
            });
            alert("appointment rescheduled successfully")
        } catch (error) {
            console.log(error);
            alert("error when rescheduling");
        }

    }

    cancelAppointment = async (e) => {
        const payload = {
            appointmentId: parseInt(e.target.value),
        }
        console.log(payload);

        try {
            const response = await axios.post(`${API_URL}/appointment/cancel`, payload);
            console.log(response.data)
            this.setState({
                scheduledAppointments: this.state.scheduledAppointments.filter((appt) => {
                    return appt.id !== response.data.id
                })
            });
            alert("appointment cancelled");
        } catch (error) {
            console.log(error);
            alert("error when cancelling");
        }
    }


    render() {
        console.log(this.state.scheduledAppointments);
        return (
            <div className='d-flex flex-column'>
                <div className="d-flex flex-fill flex-column appt-section">
                    <h1>Make an Appointment</h1>
                    <div className="d-flex flex-column appt-form justify-content-evenly">
                        <DatePicker 
                            selected={this.state.appointmentDate} 
                            onChange={this.handleDateChange} 
                            minDate={this.props.chosenDate}
                            showTimeSelect
                            timeFormat="HH:mm"
                            timeIntervals={15}
                            timeCaption="time"
                            dateFormat="MMMM d, yyyy h:mm aa"
                        />
                        <FormControl fullWidth>
                            <InputLabel id="vaccine-label">Vaccine</InputLabel>
                            <Select
                                labelId="vaccine-label"
                                id="vaccine-select"
                                multiple
                                value={this.state.vaccinesSelected}
                                label="Vaccine"
                                onChange={this.handleVaccineChange}
                            >
                            {
                                this.state.availableVaccines.map((vaccine, index) => {
                                    return (
                                        <MenuItem 
                                            key={index}
                                            value={vaccine}>{vaccine.name}
                                        </MenuItem>
                                    )
                                })
                            }
                            </Select>
                        </FormControl>
                        {
                            this.state.vaccinesSelected.map((vaccine, index) => {
                                return (
                                    <h6 key={index}>
                                        {vaccine.name} shot number: {vaccine.shotNumber}
                                    </h6>
                                )
                            })
                        }

                        <FormControl fullWidth>
                            <InputLabel id="clinic-label">Clinic</InputLabel>
                            <Select
                                labelId="clinic-label"
                                id="clinic-select"
                                value={this.state.clinicSelected}
                                label="Clinic"
                                onChange={this.handleClinicChange}
                            >
                            {
                                this.state.availableClinics?.map((clinic, index) => {
                                    return (
                                        <MenuItem key={index} value={clinic}>{clinic.clinicName}</MenuItem>
                                    )
                                })
                            }
                            </Select>
                        </FormControl>
                        <Button variant="contained" onClick={this.bookAppointment}>
                            Schedule Appointment
                        </Button>
                    </div>
                </div>

                {/*reschedule to time later than set appt */}
                <div className="d-flex flex-fill flex-column appt-section">
                    <h1>Reschedule an Appointment</h1>
                    <div className="d-flex flex-column appt-form justify-content-evenly">
                        <FormControl fullWidth>
                            <InputLabel id="scheduled-label">Appointment</InputLabel>
                            <Select
                                labelId="scheduled-label"
                                id="scheduled-select"
                                value={this.state.appointmentSelected}
                                label="Scheduled"
                                onChange={this.handleAppointmentChange}
                            >
                            {
                                this.state.scheduledAppointments.map((appointment, index) => {
                                    return (
                                        <MenuItem 
                                            key={index}
                                            value={appointment}
                                        >
                                            {
                                                appointment?.vaccinations.map((v, ind) => {
                                                    return (
                                                        <div key={ind}>{v.name}</div>
                                                    )
                                                })
                                            }
                                            {` at ${appointment?.clinic.clinicName} on ${new Date(appointment?.appointmentDateTime).toLocaleString()}`}
                                        </MenuItem>
                                    )
                                })
                            }
                            </Select>
                        </FormControl>
                        <DatePicker 
                            selected={this.state.rescheduleDate} 
                            onChange={this.handleRescheduleDateChange} 
                            minDate={this.state.minRescheduleDate}
                            showTimeSelect
                            timeFormat="HH:mm"
                            timeIntervals={15}
                            timeCaption="time"
                            dateFormat="MMMM d, yyyy h:mm aa"
                        />
                        <Button variant="contained" onClick={this.rescheduleAppointment}>
                            Reschedule Appointment
                        </Button>
                    </div>
                </div>
                <div className="d-flex flex-fill flex-column">
                    <h1>Upcoming Appointments</h1>
                    <div className="d-flex flex-column due-list">
                        {
                            this.state.scheduledAppointments.map((appointment) => {
                                return (
                                    <div className='d-flex due-vaccine align-items-center'>
                                        {
                                            appointment?.vaccinations.map((v, ind) => {
                                                return(
                                                    <div key={ind}>{v.name}</div>
                                                )
                                            })
                                        }
                                        <p>Clinic: {appointment?.clinic.clinicName}</p>
                                        <p>Date: {new Date(appointment?.appointmentDateTime).toLocaleString()}</p>
                                        <Button variant="contained" color='error' value={appointment?.id} onClick={this.cancelAppointment}>
                                            Cancel
                                        </Button>
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

export default Appointment;