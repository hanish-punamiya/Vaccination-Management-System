import React, { Component } from 'react';
import axios from 'axios';
import Cookies from 'universal-cookie';
import { Navigate } from "react-router-dom";
import DatePicker from "react-datepicker";
import TextField from '@mui/material/TextField';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';
import './Signup.css';
import { Button } from '@mui/material';

const { API_URL } = require('../utils/Constants').default;
class Signup extends Component {
  constructor(props) {
    super(props);
    this.state = {
      email: '',
      password: '',
      firstName: '',
      lastName: '',
      middleName: '',
      address: '',
      address2: '',
      city: '',
      state: '',
      zipcode: 0,
      dateOfBirth: new Date(),
      gender: '',
      redirect: false,
    };
    this.handleEmailChange = this.handleEmailChange.bind(this);
    this.handlePasswordChange = this.handlePasswordChange.bind(this);
    this.handleSignupSubmit = this.handleSignupSubmit.bind(this);
  }

  handleEmailChange = (e) => {
    this.setState({
      email: e.target.value,
    });
  }

  handlePasswordChange = (e) => {
    this.setState({
      password: e.target.value,
    });
  }

  handleFirstNameChange = (e) => {
    this.setState({
      firstName: e.target.value,
    });
  }

  handleLastNameChange = (e) => {
    this.setState({
      lastName: e.target.value,
    });
  }

  handleMiddleNameChange = (e) => {
    this.setState({
      middleName: e.target.value,
    });
  }

    handleDobChange = (date) => {
        this.setState({
            dateOfBirth: date
        });
    }

    handleAddressChange = (e) => {
        this.setState({
            address: e.target.value,
        })
    }

    handleAddress2Change = (e) => {
      this.setState({
          address2: e.target.value,
      })
  }

    handleCityChange = (e) => {
      this.setState({
          city: e.target.value,
      })
    }

    handleStateChange = (e) => {
      this.setState({
          state: e.target.value,
      })
    }

    handleZipcodeChange = (e) => {
      this.setState({
          zipcode: e.target.value,
      })
    }

    handleGenderChange = (e) => {
        this.setState({
            gender: e.target.value,
        })
    }

    handleSignupSubmit = async (e) => {
      const { email, password, firstName, lastName, middleName, address, address2, city, state, zipcode, gender, dateOfBirth } = this.state;
      const payload = {
        email,
        password,
        firstName,
        lastName,
        middleName,
        addressLine1: address,
        addressLine2: address2,
        city,
        state,
        zipcode: parseInt(zipcode),
        gender,
        dateOfBirth: dateOfBirth.toString(),
      };

      console.log(payload);
  
      try {
        let response = null;

        response = await axios.post(`${API_URL}/user/signup`, payload);

        console.log(response.data);
  
        if(response.data != null) {
          this.setState({
            redirect: true,
          })
        }
      } catch (error) {
        console.log(error);
      }
    }

  render() {
    const {
      email, password, firstName, lastName, middleName, address, dateOfBirth, gender,
    } = this.state;

    const signupDiv = (
        <div className='d-flex justify-content-center signup'>
            <div className="d-flex flex-column justify-content-evenly align-items-center signup-section">
                <h1>Sign up</h1>
                <form>
                  <TextField id="signupemail" label="Email" variant="outlined" required onChange={this.handleEmailChange}/>
                  <TextField id="signuppassword" label="Password" variant="outlined" type="password" required onChange={this.handlePasswordChange}/>
                  <TextField id="firstname" label="First Name" variant="outlined" required onChange={this.handleFirstNameChange}/>
                  <TextField id="lastname" label="Last Name" variant="outlined" required onChange={this.handleLastNameChange}/>
                  <TextField id="middlename" label="Middle Name" variant="outlined" onChange={this.handleMiddleNameChange}/>
                  <TextField id="address" label="Street Address Line 1" variant="outlined" onChange={this.handleAddressChange}/>
                  <TextField id="address2" label="Street Address Line 2" variant="outlined" onChange={this.handleAddress2Change}/>
                  <TextField id="city" label="City" variant="outlined" required onChange={this.handleCityChange}/>
                  <TextField id="state" label="State" variant="outlined" required onChange={this.handleStateChange}/>
                  <TextField id="zipcode" label="Zipcode" variant="outlined" required onChange={this.handleZipcodeChange}/>
                  <FormControl fullWidth>
                      <InputLabel id="gender-label">Gender</InputLabel>
                      <Select
                          required
                          labelId="gender-label"
                          id="gender"
                          value={gender}
                          label="Gender"
                          onChange={this.handleGenderChange}
                      >
                          <MenuItem value={"male"}>male</MenuItem>
                          <MenuItem value={"female"}>female</MenuItem>
                          <MenuItem value={"other"}>other</MenuItem>
                      </Select>
                  </FormControl>
                  <label>
                    Date of Birth
                    <DatePicker 
                      selected={dateOfBirth} 
                      onChange={this.handleDobChange} 
                    />  
                  </label>
                  <Button variant="contained" onClick={this.handleSignupSubmit}>
                    Sign up
                  </Button>
                </form>
                
            </div>
        </div>
        
    );

    return (
      <div>
        {this.state.redirect===false ? signupDiv : <Navigate to="/"/>}
      </div>
    );
  }
}

export default Signup;
