import React, { useEffect, useState } from "react";
import axios from "axios";
import "./ViewScanLogs.css";

const ViewScanLogs = () => {
  const [logs, setLogs] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    fetchLogs();
  }, []);

  const fetchLogs = async () => {
    try {
      const res = await axios.get("http://localhost:8081/api/scan-logs/all");
      setLogs(res.data);
    } catch (err) {
      setError("❌ Failed to fetch scan logs");
    }
  };

  useEffect(() => {
  console.log("🔍 Logs updated:", logs);
}, [logs]);


  return (
    <div className="scan-log-container">
      <h2>🛡️ Virus Scan Logs</h2>
      {error && <p className="error">{error}</p>}
      <table className="scan-log-table">
        <thead>
          <tr>
            <th>S. No.</th>
            <th>Document ID</th>
            <th>Status</th>
            <th>Message</th>
            <th>Scanned At</th>
          </tr>
        </thead>
        <tbody>
          {logs.map((log, index) => (
            <tr key={log.id} className={log.result === "REJECTED" ? "failed" : "success"}>
              <td>{index + 1}</td>
              <td>{log.documentId}</td>
              <td>{log.result}</td>
              <td>{log.reason}</td>
              <td>{new Date(log.scannedAt).toLocaleString()}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ViewScanLogs;
