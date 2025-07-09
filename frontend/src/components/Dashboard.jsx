// src/components/Dashboard.jsx
import React, { useEffect, useState } from "react";
import axios from "../api/axios";

const Dashboard = () => {
  const [user, setUser] = useState(null);

  useEffect(() => {
    axios
      .get("/api/user/profile") // or /api/users/me — based on your backend
      .then((res) => setUser(res.data))
      .catch((err) => console.error("❌ Unauthorized", err));
  }, []);

  return (
    <div>
      <h2>Dashboard</h2>
      {user ? (
        <div>
          <p><strong>Full Name:</strong> {user.fullName}</p>
          <p><strong>Email:</strong> {user.email}</p>
        </div>
      ) : (
        <p>Loading or not authorized...</p>
      )}
    </div>
  );
};

export default Dashboard;
