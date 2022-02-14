import React, { Component } from 'react';
import axios from 'axios';
import TextField from '@mui/material/TextField';
import { Button } from '@mui/material';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';
import './AdminManagement.css';

const { API_URL } = require('../utils/Constants').default;
class AdminManagement extends Component {
    constructor(props) {
        super(props);
        this.state = {
            clinicName: '',
            clinicAddress: '',
            clinicCity: '',
            clinicState: '',
            clinicZipCode: '',
            opening: 8,
            closing: 17,
            physicians: 0,

            disease: '',
            description: '',
            currentDiseases: [
                {name:"COVID-19"},
                {name:"Measles"},
                {name:"Swine Flu"},
                {name:"Small Pox"},
            ],

            diseasesSelected: [

            ],
            vaccine: '',
            manufacturer: '',
            numberShots: 0,
            shotInterval: 0,
            duration: 0,
        };
    }

    async componentDidMount() {
        try {
            const response = await axios.get(`${API_URL}/disease/`);
            if(response.data !== null) {
                this.setState({
                    currentDiseases: response.data
                });
            } else {
                alert("no diseases exist");
            }
        } catch (error) {
            console.log(error);
        }
    }

    // CLINIC FORM
    handleClinicChange = (e) => {
        this.setState({
            clinicName: e.target.value
        });
    }

    handleAddressChange = (e) => {
        this.setState({
            clinicAddress: e.target.value
        });
    }
    handleAddressCityChange = (e) => {
        this.setState({
            clinicCity: e.target.value
        });
    }
    handleAddressStateChange = (e) => {
        this.setState({
            clinicState: e.target.value
        });
    }
    handleAddressZipCodeChange = (e) => {
        this.setState({
            clinicZipCode: e.target.value
        });
    }

    handleOpeningChange = (e) => {
        this.setState({
            opening: e.target.value
        });
    }

    handleClosingChange = (e) => {
        this.setState({
            closing: e.target.value
        });
    }

    handlePhysiciansChange = (e) => {
        this.setState({
            physicians: e.target.value
        });
    }

    createClinic = async () => {
        if(parseInt(this.state.closing) - parseInt(this.state.opening) < 8) {
            alert("business hours must be at least 8 hours")
        } else {
            const payload = {
                name: this.state.clinicName,
                address: this.state.clinicAddress,
                city: this.state.clinicCity,
                state: this.state.clinicState,
                zipCode : this.state.clinicZipCode,
                opening: parseInt(this.state.opening),
                closing: parseInt(this.state.closing),
                businessHours: parseInt(this.state.closing) - parseInt(this.state.opening),
                physicians: parseInt(this.state.physicians),
            }
    
            console.log("clinic payload  = ", payload);
            try {
                const response = await axios.post(`${API_URL}/clinic/createClinic`, payload);
            } catch (error) {
                console.log(error);
                if(error.response.status === 400){
                    alert("Clinic name already exists")   
                } else {
                    alert("internal error when creating clinic")
                }
            }
        }
    }

    // DISEASE FORM
    handleDiseaseChange = (e) => {
        this.setState({
            disease: e.target.value
        });
    }

    handleDescriptionChange = (e) => {
        this.setState({
            description: e.target.value
        });
    }

    createDisease = async () => {   
        console.log(typeof this.state.disease); 
        const payload = {
            diseaseName: this.state.disease,
            diseaseDescription: this.state.description
        }
        console.log(payload)

        try {
            const response = await axios.post(`${API_URL}/disease/createDisease`, payload);
            if(response.data === null){
                alert("disease not created")
            } else {
                console.log("create disease responce = " , response.data)
                this.setState({
                    currentDiseases: [...this.state.currentDiseases, response.data],
                });
            }
        } catch (error) {
            console.log(error);
            if(error.response.status === 400){
                alert("Disease name already exists")   
            } else {
                alert("internal error when creating disease")
            }
        }
    }

    // VACCINE FORM
    handleVaccineChange = (e) => {
        this.setState({
            vaccine: e.target.value
        });
    }

    handleManufacturerChange = (e) => {
        this.setState({
            manufacturer: e.target.value
        });
    }

    handleNumberShotsChange = (e) => {
        this.setState({
            numberShots: e.target.value
        });
    }

    handleShotIntervalChange = (e) => {
        this.setState({
            shotInterval: e.target.value
        });
    }

    handleDurationChange = (e) => {
        this.setState({
            duration: e.target.value
        });
    }

    handleVaccineDiseaseChange = (e) => {
        this.setState({
            diseasesSelected: e.target.value
        });
    }

    createVaccine = async () => {   
        const requestBody = {
            name: this.state.vaccine,
            diseaseIds: this.state.diseasesSelected,
            manufacturer: this.state.manufacturer,
            numberOfShots: parseInt(this.state.numberShots),
            shotInterval: parseInt(this.state.shotInterval),
            duration: parseInt(this.state.duration)
        }
        console.log(requestBody);
        try {
            const response = await axios.post(`${API_URL}/vaccination/create`, requestBody);
            console.log(response)
        } catch (error) {
            console.log(error);
            if(error.response.status === 400){
                alert("Vaccine name already exists")   
            } else {
                alert("internal error when creating vaccine")
            }
        }
    }


    render() {
        return (
            <div className='d-flex flex-column justify-content-around admin'>
                <h1>Admin Management</h1>

                {/* Create clinic */}
                <div className="d-flex flex-column">
                    <h3>Create Clinic</h3>
                    <div className='d-flex'>
                        <TextField id="clinicname" label="Clinic Name" variant="outlined" required onChange={this.handleClinicChange}/>
                        <TextField id="physicians" label="Number of Physicians" variant="outlined" required onChange={this.handlePhysiciansChange}/>
                    </div>
                    <div className='d-flex'>
                        <TextField id="clinicaddress" label="Address" variant="outlined" onChange={this.handleAddressChange}/>
                        <TextField id="clinicAddressZipCode" label="Zip Code" variant="outlined" required onChange={this.handleAddressZipCodeChange}/>
                        <TextField id="clinicAddressCity" label="City" variant="outlined" required onChange={this.handleAddressCityChange}/>
                        <TextField id="clinicAddressState" label="State" variant="outlined" required onChange={this.handleAddressStateChange}/>
                    </div>
                    <div className='d-flex'>
                        <TextField id="opening" label="Opening Time" variant="outlined" required onChange={this.handleOpeningChange}/>
                        <TextField id="closing" label="Closing Time" variant="outlined" required onChange={this.handleClosingChange}/>
                    </div>
                    <div>
                        <Button variant="contained" onClick={this.createClinic}>
                            Create Clinic
                        </Button>
                    </div>
                </div>

                {/* Create disease */}
                <div className="d-flex flex-column">
                    <h3>Create Disease</h3>
                    <div className='d-flex'>
                        <TextField id="diseasename" label="Disease Name" variant="outlined" required onChange={this.handleDiseaseChange}/>
                        <TextField id="diseasedescription" label="Description" multiline rows={3} variant="outlined" onChange={this.handleDescriptionChange}/>
                    </div>
                    <div>
                        <Button variant="contained" onClick={this.createDisease}>
                            Create Disease
                        </Button>
                    </div>
                </div>

                {/* Create vaccine */}
                <div className="d-flex flex-column">
                    <h3>Create Vaccine</h3>
                    <div className='d-flex flex-column'>
                        <div className='d-flex'>
                            <TextField id="vaccinename" label="Vaccine Name" variant="outlined" required onChange={this.handleVaccineChange}/>
                            <TextField id="manufacturer" label="Manufacturer" variant="outlined" required inputProps={{ minLength:3 }} onChange={this.handleManufacturerChange}/>
                            <FormControl fullWidth>
                                <InputLabel id="disease-label">Diseases</InputLabel>
                                <Select
                                    labelId="disease-label"
                                    id="disease-select"
                                    multiple
                                    value={this.state.diseasesSelected}
                                    label="Diseases"
                                    onChange={this.handleVaccineDiseaseChange}
                                >
                                {
                                    this.state.currentDiseases.map((disease, index) => {
                                        return (
                                            <MenuItem key={index} value={disease.id}>{disease.name}</MenuItem>
                                        )
                                    })
                                }
                                </Select>
                            </FormControl>  
                        </div>
                        <div className='d-flex'>
                            <TextField id="numbershots" label="Number of Shots" variant="outlined" required onChange={this.handleNumberShotsChange}/>
                            <TextField id="shotinterval" label="Shot Interval" variant="outlined" onChange={this.handleShotIntervalChange}/>
                            <TextField id="duration" label="Duration" variant="outlined" required onChange={this.handleDurationChange}/>
                        </div>
                    </div>
                    <div>
                        <Button variant="contained" onClick={this.createVaccine}>
                            Create Vaccine
                        </Button>
                    </div>
                </div>
            </div>
        )
    }
}

export default AdminManagement
