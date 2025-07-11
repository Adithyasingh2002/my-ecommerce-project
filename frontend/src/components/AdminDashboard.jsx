import React from "react";
import { useNavigate } from "react-router-dom";

const AdminDashboard = () => {
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
      <h2>Welcome, Admin! 🚀</h2>

      <button
        onClick={() => navigate("/products")}
        style={buttonStyle("#0066cc")}
      >
        View All Products
      </button>

      <button
        onClick={() => navigate("/add-product")}
        style={buttonStyle("#28a745")}
      >
        Add New Product
      </button>

      <button
        onClick={() => navigate("/delete-product")}
        style={buttonStyle("#dc3545")}
      >
        Delete Product
      </button>

      <button
        onClick={() => navigate("/admin/orders")}
        style={buttonStyle("#ffc107", "black")}
      >
        📋 View Orders
      </button>

      <button
        onClick={() => navigate("/admin/reviews")}
        style={buttonStyle("#6c757d")}
      >
        📝 View Reviews
      </button>
    </div>
  );
};

const buttonStyle = (bg, color = "white") => ({
  padding: "12px 20px",
  margin: "10px",
  backgroundColor: bg,
  color,
  border: "none",
  borderRadius: "6px",
  cursor: "pointer",
  fontWeight: "bold",
});

export default AdminDashboard;
