import React from "react";
import "./Home.css";
import { FaFileUpload, FaMagic, FaShieldAlt } from "react-icons/fa";

const Home = () => {
  return (
    <div className="home-wrapper">
      <div className="hero-section">
        <h1>🚀 Document Manager Pro</h1>
        <p>Your one-stop solution for secure, smart, and seamless document handling.</p>
      </div>

      <div className="features-section">
        <div className="feature-card">
          <FaFileUpload className="feature-icon" />
          <h3>Upload Documents</h3>
          <p>Securely upload and track documents with real-time virus scan updates.</p>
        </div>
        <div className="feature-card">
          <FaMagic className="feature-icon" />
          <h3>Generate Documents</h3>
          <p>Create beautiful PDFs or DOCX files from dynamic templates effortlessly.</p>
        </div>
        <div className="feature-card">
          <FaShieldAlt className="feature-icon" />
          <h3>Scan & Protect</h3>
          <p>Built-in virus scanning to ensure all files are clean before access.</p>
        </div>
      </div>

      <div className="cta-section">
        <p>Ready to manage your documents like a pro?</p>
        <p></p>
        <a href="/upload" className="cta-button">Upload Now</a>
      </div>
    </div>
  );
};

export default Home;
