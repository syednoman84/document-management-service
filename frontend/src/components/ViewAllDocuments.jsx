import React, { useEffect, useState } from "react";
import axios from "axios";
import "./ViewAllDocuments.css";
import { FaDownload } from "react-icons/fa";

const ViewAllDocuments = () => {
  const [documents, setDocuments] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    fetchDocuments();
  }, []);

  const fetchDocuments = async () => {
    try {
      const res = await axios.get("http://localhost:8080/documents/all");
      setDocuments(res.data);
    } catch (err) {
      setError("Failed to fetch documents");
    }
  };

  const handleDownload = async (docId, fileName) => {
    try {
      const res = await axios.get(`http://localhost:8080/documents/${docId}`, {
        responseType: "blob", // important for downloading files
      });

      const blob = new Blob([res.data]);
      const url = window.URL.createObjectURL(blob);

      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", fileName || "document");
      document.body.appendChild(link);
      link.click();
      link.remove();
    } catch (err) {
      alert("❌ Failed to download document");
    }
  };

  return (
    <div className="document-history-container">
      <h2>📄 Document History</h2>
      {error && <p className="error">{error}</p>}
      <table className="document-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>File Name</th>
            <th>Type</th>
            <th>Status</th>
            <th>Created At</th>
            <th> <FaDownload /></th>
          </tr>
        </thead>
        <tbody>
          {documents.map((doc) => (
            <tr key={doc.id}>
              <td>{doc.id}</td>
              <td>{doc.fileName}</td>
              <td>{doc.source}</td>
              <td>{doc.status}</td>
              <td>{new Date(doc.createdAt).toLocaleString()}</td>
              <td>
                <FaDownload
                  className="download-icon"
                  onClick={() => handleDownload(doc.id, doc.fileName)}
                  title="Download file"
                />
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ViewAllDocuments;
