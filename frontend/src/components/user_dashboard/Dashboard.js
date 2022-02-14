import React, { Component } from "react";
import { Link, Route } from 'react-router-dom';
import "./Dashboard.css";
import VaccinesDue from "./VaccinesDue";
import Cookies from "universal-cookie";
import { Navigate } from "react-router-dom";

class Dashboard extends Component {
    constructor(props) {
        super(props);
        this.state = {
            redirect: false,
            userRole: "USER",
        }
    }

    componentDidMount() {
        const cookies = new Cookies();
        let role = cookies.get("userRole");
        if(role === "USER") {
            this.setState({
                userRole: "USER",
            })
        } else {
            this.setState({
                userRole: "ADMIN"
            })
        }
    }

    handleLogout = (e) => {
        e.preventDefault();
        const cookies = new Cookies();
        cookies.remove("userId");
        cookies.remove("userRole");
        this.setState({
            redirect: true,
        });
    }

    render() {
        return (
            <div>
                <div className="d-flex justify-content-center dashboard">
                    <div className="d-flex flex-column navbar-section">
                        <Link to="/dashboard">
                            <button type="button" className="d-flex align-items-center nav-button">
                                <span>
                                    Home
                                </span>
                            </button>
                        </Link>
                        <Link to="/history">
                            <button type="button" className="d-flex align-items-center nav-button">
                                <span>
                                   Vaccine History
                                </span>
                            </button>
                        </Link>
                        <Link to="/appointment">
                            <button type="button" className="d-flex align-items-center nav-button">
                                <span>
                                    Make/Cancel an Appointment
                                </span>
                            </button>
                        </Link>

                        <Link to="/patientreports">
                            <button type="button" className="d-flex align-items-center nav-button">
                                <span>
                                    Patient Reports
                                </span>
                            </button>
                        </Link>

                        {/* if user is an admin, add two more links for admin dash and system reports */}
                        {
                            this.state.userRole === "USER" ? 
                            null :
                                <div>
                                    <Link to="/admin">
                                        <button type="button" className="d-flex align-items-center nav-button">
                                            <span>
                                                Admin Home
                                            </span>
                                        </button>
                                    </Link>
                                    <Link to="/systemreports">
                                        <button type="button" className="d-flex align-items-center nav-button">
                                            <span>
                                                System Reports
                                            </span>
                                        </button>
                                    </Link>
                                </div>
                        }
                            

                        <button type="button" className="d-flex align-items-center nav-button" onClick={this.handleLogout}>
                            <span>
                                Logout
                            </span>
                        </button>
                    </div>
                    <div className="d-flex flex-column current-page">
                        {this.state.redirect ? <Navigate to="/"/> : this.props.currentPage}
                    </div>
                </div>
            </div>
        );
    }
}

export default Dashboard;