import React from "react";
import { Link, NavLink } from "react-router-dom";
import "./Navbar.css";

export default function Navbar() {
    return (
        <nav className="navbar">
            <Link to="/" style={{ textDecoration: "none", color: "white" }}>
                <div className="logo">📄 Document Manager</div>
            </Link>
            <div className="nav-links">
                <NavLink to="/upload" activeclassname="active">Upload</NavLink>
                <NavLink to="/generate" activeclassname="active">Generate</NavLink>
                <NavLink to="/documents" activeclassname="active">All Documents</NavLink>
                <NavLink to="/scan-logs" activeclassname="active">Scan Logs</NavLink>
            </div>
        </nav>
    );
}
