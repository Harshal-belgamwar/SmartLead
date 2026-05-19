# SmartLead Dashboard

A full-stack Lead Management Dashboard built with the MERN stack (MongoDB, Express, React, Node.js) and TypeScript.

## Features
- **Authentication**: JWT-based login and registration with Role-Based Access Control (Admin, Sales User).
- **Leads Management**: Full CRUD operations for leads.
- **Advanced Filtering**: Filter by Status, Source and Search by Name or Email.
- **Pagination**: Backend-driven pagination for optimal performance.
- **CSV Export**: Export lead data to CSV.
- **Dark Mode**: Support for both light and dark themes.
- **Responsive Design**: Mobile-friendly UI built with Tailwind CSS.
- **Dockerized**: Ready to run with Docker Compose.

## Tech Stack
- **Frontend**: React 19, TypeScript, Tailwind CSS 4, Axios, React Router.
- **Backend**: Node.js, Express, TypeScript, MongoDB, Mongoose.

## Environment Variables

To run this project, you will need to add the following environment variables to your `.env` files.

#### Backend (`Backend/.env`)
Create a `.env` file in the `Backend` directory:
```env
PORT=5000
MONGODB_URI=your_mongodb_connection_string
JWT_SECRET=your_jwt_secret_key
NODE_ENV=development
```

#### Frontend (`frontend/.env`)
Create a `.env` file in the `frontend` directory:
```env
VITE_API_URL=http://localhost:5000/api
```

## Getting Started

### Prerequisites
- Node.js (v18+)
- MongoDB (running locally or via Docker)
- Docker (optional)

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/SmartLead.git
cd SmartLead
```

### 2. Run with Docker (Recommended)

**For Windows (PowerShell/CMD):**
```bash
docker compose up --build
```

**For Linux / WSL:**
```bash
sudo -E docker compose up --build
```

1. Open `http://localhost:5173` for the frontend.
2. The API will be available at `http://localhost:5000/api`.

### 3. Manual Local Setup

#### Backend
1. Go to the `Backend` directory: `cd Backend`.
2. Install dependencies: `npm install`.
3. Create a `.env` file based on `.env.example`.
4. Run in development mode: `npm run dev`.

#### Frontend
1. Go to the `frontend` directory: `cd frontend`.
2. Install dependencies: `npm install`.
3. Create a `.env` file based on `.env.example`.
4. Run the development server: `npm run dev`.
5. Open `http://localhost:5173` (default Vite port).

## API Documentation
- `POST /api/auth/register`: Register a new user.
- `POST /api/auth/login`: Login and receive a JWT.
- `GET /api/leads`: Get all leads (supports `search`, `status`, `source`, `sort`, `page`).
- `POST /api/leads`: Create a new lead.
- `GET /api/leads/:id`: Get a lead by ID.
- `PATCH /api/leads/:id`: Update a lead.
- `DELETE /api/leads/:id`: Delete a lead (Admin only).


