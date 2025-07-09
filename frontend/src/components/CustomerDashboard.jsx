// src/components/CustomerDashboard.jsx
import React from "react";
import { useNavigate } from "react-router-dom";

const CustomerDashboard = () => {
  const navigate = useNavigate();

  return (
    <div
      style={{
        padding: "40px",
        textAlign: "center",
        color: "#f5f5f5",
        textShadow: "0 0 8px rgba(255, 255, 255, 0.7)",
        minHeight: "100vh",
      }}
    >
      <h2>Welcome, Customer! 🛒</h2>
      <button
        onClick={() => navigate("/products")}
        style={{
          padding: "12px 20px",
          margin: "10px",
          backgroundColor: "#17a2b8",
          color: "white",
          border: "none",
          borderRadius: "6px",
          cursor: "pointer",
          fontWeight: "bold",
        }}
      >
        View Products
      </button>
    </div>
  );
};

export default CustomerDashboard;
