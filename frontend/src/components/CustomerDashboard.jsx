import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import Orders from "./Orders";

const CustomerDashboard = () => {
  const navigate = useNavigate();
  const [selectedView, setSelectedView] = useState(null);

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
        style={buttonStyle("#17a2b8")}
      >
        View Products
      </button>

      <button
        onClick={() => setSelectedView("orders")}
        style={buttonStyle("#6f42c1")}
      >
        📦 Track Orders
      </button>

      <button
        onClick={() => navigate("/customer/reviews")}
        style={buttonStyle("#20c997")}
      >
        📝 Review Products
      </button>

      {selectedView === "orders" && <Orders />}
    </div>
  );
};

const buttonStyle = (bg) => ({
  padding: "12px 20px",
  margin: "10px",
  backgroundColor: bg,
  color: "white",
  border: "none",
  borderRadius: "6px",
  cursor: "pointer",
  fontWeight: "bold",
});

export default CustomerDashboard;
