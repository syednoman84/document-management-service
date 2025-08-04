import React from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import Navbar from "./components/Navbar";
import UploadDocument from "./components/UploadDocument";
import GenerateDocument from "./components/GenerateDocument";
import ViewAllDocuments from "./components/ViewAllDocuments";
import ViewScanLogs from "./components/ViewScanLogs";

function App() {
  return (
    <Router>
      <Navbar />
      <div className="main-container">
        <Routes>
          <Route path="/" element={<Navigate to="/upload" />} />
          <Route path="/upload" element={<UploadDocument />} />
          <Route path="/generate" element={<GenerateDocument />} />
          <Route path="/documents" element={<ViewAllDocuments />} />
          <Route path="/scan-logs" element={<ViewScanLogs />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
