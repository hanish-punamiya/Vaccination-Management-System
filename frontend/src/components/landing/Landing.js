import React, { Component } from "react";
import { Link } from 'react-router-dom';
import "./Landing.css";

class Landing extends Component {
  componentDidMount() {
  }

  render() {
    return (
        <div className="container landing">
            <div className="d-flex landing-text flex-column justify-content-center">
                <h1>
                    Vaccine Management System
                </h1>
                <div className="d-flex login-buttons align-items-center">
                    <Link to="/login">
                        <button type="button" className="auth-button">
                            <span>
                                Log in
                            </span>
                        </button>
                    </Link>
                    <Link to="/signup">
                        <button type="button" className="auth-button">
                            <span>
                                Signup
                            </span>
                        </button>
                    </Link>
                </div>
            </div>
            <div className="d-flex login-buttons align-items-center">
                
            </div>
        </div>
    );
  }
}

export default Landing;
