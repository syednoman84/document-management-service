# 📁 Document Management Service

A full-stack Document Management System that allows users to upload documents, generate system templates (PDF, DOCX, HTML), view scan logs, and download files—secured by a real-time virus scanning microservice. Built with Spring Boot (Java), React.js, MySQL, RabbitMQ, and Docker.

---

## 📦 Services

### 1. `document-management-service` (Port: `8080`)
- Upload user documents
- Generate system-generated documents from GitHub templates
- Store metadata in MySQL
- Publish file upload events to RabbitMQ
- Expose APIs to:
  - Upload file
  - Generate document
  - Download document
  - View all documents

### 2. `virus-scan-service` (Port: `8081`)
- Listens to `document.upload.queue` from RabbitMQ
- Simulates virus scan
- Emits scan result events to update status
- Persists scan logs in MySQL
- Supports retry & Dead Letter Queue (DLQ) mechanism
- Expose API to:
  - View all scan logs (success/failure)

---

## 🌐 React Frontend (`/frontend`)
A sleek user interface for:

- 📤 Uploading documents
- 🧾 Generating system documents via JSON-based templates
- 📄 Viewing uploaded/generated document history
- 🐛 Viewing scan logs (color-coded success/failure)
- ⬇️ Downloading documents with one click

Built using:
- React.js
- Axios
- CSS for styling
- React Router DOM

---

## 🗂️ Template Configuration

Templates (HTML, DOCX, PDF) are stored in a separate GitHub repo:  
🔗 [`documents-templates-config`](https://github.com/syednoman84/documents-templates-config)

During system document generation, templates are dynamically fetched based on `templateName` and `inputType`.

---

## 🛠️ Tech Stack

| Layer         | Technology         |
|---------------|--------------------|
| Backend       | Spring Boot (Java) |
| Frontend      | React.js           |
| DB            | MySQL              |
| Messaging     | RabbitMQ           |
| File Storage  | Local FS (pluggable) |
| Virus Scan    | Event-driven simulation |
| Template Host | GitHub Repo        |

---

## 📸 Screenshots

> ✨ UI Screenshots will go here if desired (upload later)

---

## 📑 API Summary

### `document-management-service`

| Method | Endpoint                      | Description                  |
|--------|-------------------------------|------------------------------|
| POST   | `/documents/upload`           | Upload user document         |
| POST   | `/documents/generate`         | Generate system document     |
| GET    | `/documents/{id}`             | Download document by ID      |
| GET    | `/documents/all`              | List all documents           |

### `virus-scan-service`

| Method | Endpoint                  | Description                 |
|--------|---------------------------|-----------------------------|
| GET    | `/api/scan-logs/all`      | List all scan logs          |

---

## 🧪 Sample JSON for Generation

```json
{
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
}
```

---

## 🚀 Running Locally

### 1. Start RabbitMQ (Docker)

```bash
docker run -d --hostname my-rabbit --name some-rabbit -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```

> Dashboard available at: `http://localhost:15672`  
> Default credentials: guest / guest

---

### 2. Start Backend Services

```bash
cd document-management-service
./mvnw spring-boot:run

cd virus-scan-service
./mvnw spring-boot:run
```

---

### 3. Start Frontend

```bash
cd frontend
npm install
npm start
```

> Open in browser: `http://localhost:3000`

---

## ✅ Features Summary

- [x] Upload documents
- [x] Virus scan simulation
- [x] Dead Letter Queue support for failures
- [x] Generate documents from GitHub templates
- [x] Download documents
- [x] View scan logs
- [x] Styled React UI with color-coded statuses
- [x] Retry logic and event-based communication

---

## 🔐 Security Notes

- CORS enabled for frontend-backend communication
- Document uploads are scanned before marked as "ACCEPTED"
- Failed scans are stored and viewable

---

## 📂 Folder Structure

```bash
document-management-service/
virus-scan-service/
frontend/
```

---

## 🤝 Contributions

Pull requests welcome. For major changes, open an issue first to discuss what you would like to change.

---

## 🧾 License

MIT License (Optional)

---

## 👤 Author

**Syed Noman Ahmed**  
📧 [syednoman84@gmail.com](mailto:syednoman84@gmail.com)  
🔗 GitHub: [syednoman84](https://github.com/syednoman84)
