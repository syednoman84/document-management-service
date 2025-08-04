import React, { useState } from "react";
import axios from "axios";
import "./GenerateDocument.css";

const GenerateDocument = () => {
  const [jsonInput, setJsonInput] = useState(`{
  "templateName": "invoice.html",
  "inputType": "HTML",
  "outputType": "PDF",
  "parameters": {
    "invoiceNumber": "HTML-20250732",
    "invoiceDate": "2025-07-31",
    "customerName": "Juliahtml",
    "customerEmail": "juliahtml@example.com",
    "itemDescription": "Car Rental",
    "itemQty": "1",
    "itemPrice": "10000.00",
    "amountDue": "4000.00"
  }
}`);

  const [message, setMessage] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage("");
    try {
      const parsed = JSON.parse(jsonInput);
      const res = await axios.post("http://localhost:8080/documents/generate", parsed);
      setMessage("✅ Document generated successfully.");
    } catch (err) {
      console.error(err);
      setMessage("❌ Failed to generate document.");
    }
  };

  return (
    <div className="generate-doc-container">
      <h2>📄 Generate Document</h2>
      <p>Please provide the json in the text area below for the document you want to generate:</p>
      <form onSubmit={handleSubmit}>
        <textarea
          rows={16}
          value={jsonInput}
          onChange={(e) => setJsonInput(e.target.value)}
          className="json-textarea"
        />
        <button type="submit" className="generate-btn">Generate</button>
      </form>
      {message && <p className="message">{message}</p>}
    </div>
  );
};

export default GenerateDocument;
