import React, { useState, useEffect } from "react";
import axios from "axios";
import "./UploadDocument.css";
import { FaDownload } from "react-icons/fa";


export default function UploadDocument() {
  const [selectedFile, setSelectedFile] = useState(null);
  const [uploadStatus, setUploadStatus] = useState("");
  const [uploadedDocs, setUploadedDocs] = useState([]);

  const fetchDocuments = async () => {
    try {
      const res = await axios.get("http://localhost:8080/documents/all");
      setUploadedDocs(res.data);
    } catch (err) {
      console.error("❌ Error fetching documents", err);
    }
  };

  useEffect(() => {
    fetchDocuments();
  }, []);

  const handleUpload = async () => {
    if (!selectedFile) {
      alert("Please select a file.");
      return;
    }

    const formData = new FormData();
    formData.append("file", selectedFile);

    try {
      setUploadStatus("Uploading...");
      await axios.post("http://localhost:8080/documents/upload", formData);
      setUploadStatus("✅ Upload submitted");
      setSelectedFile(null);
      fetchDocuments();
    } catch (error) {
      console.error("Upload error:", error);
      setUploadStatus("❌ Upload failed");
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
    <div className="upload-container">
      <h2>📤 Upload Document</h2>
      <div className="upload-box">
        <input
          type="file"
          onChange={(e) => setSelectedFile(e.target.files[0])}
        />
        <button className="upload-btn" onClick={handleUpload}>
          Upload
        </button>
      </div>
      <p className="status-text">{uploadStatus}</p>

      <h3>📁 List of Documents</h3>
      <table className="upload-table">
        <thead>
          <tr>
            <th>Document ID</th>
            <th>File Name</th>
            <th>Type</th>
            <th>Status</th>
            <th>Upload Date</th>
            <th> <FaDownload /></th>
          </tr>
        </thead>
        <tbody>
          {uploadedDocs.map((doc) => (
            <tr key={doc.id}>
              <td>{doc.id}</td>
              <td>{doc.fileName}</td>
              <td>{doc.source}</td>
              <td className={`status-${doc.status.toLowerCase()}`}>
                {doc.status}
              </td>
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
}
