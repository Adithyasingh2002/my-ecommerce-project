import React, { useEffect, useState } from "react";
import axios from "../api/axios";
import { useNavigate } from "react-router-dom";
import "./DeleteProduct.css";

const DeleteProduct = () => {
  const [products, setProducts] = useState([]);
  const navigate = useNavigate();

  const user = JSON.parse(localStorage.getItem("user"));
  const token = localStorage.getItem("token"); // Get token directly
  const role = user?.role; // Use correct role extraction

  useEffect(() => {
    if (!user || role !== "ADMIN") {
      alert("Access denied");
      navigate("/");
    } else {
      fetchProducts();
    }
    
  }, []);

  const fetchProducts = async () => {
    try {
      const res = await axios.get("/api/products", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setProducts(res.data);
    } catch (err) {
      console.error("❌ Failed to fetch products", err);
      alert("Failed to fetch products");
    }
  };

  const handleDelete = async (id) => {
    const confirm = window.confirm("Are you sure you want to delete this product?");
    if (!confirm) return;

    try {
      await axios.delete(`/api/products/${id}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setProducts(products.filter((p) => p.id !== id));
      alert("🗑️ Product deleted successfully!");
    } catch (err) {
      console.error("❌ Failed to delete product", err);
      alert("Failed to delete product");
    }
  };

  return (
    <div
      style={{
        minHeight: "100vh",
        padding: "40px",
        color: "white",
        textAlign: "center",
      }}
    >
      <h2>🗑️ Delete Products</h2>
      {products.length === 0 ? (
        <p>No products available.</p>
      ) : (
        <div
          style={{
            display: "flex",
            flexWrap: "wrap",
            justifyContent: "center",
            gap: "20px",
            marginTop: "20px",
          }}
        >
          {products.map((product) => (
            <div
              key={product.id}
              style={{
                background: "rgba(255, 255, 255, 0.1)",
                padding: "20px",
                borderRadius: "10px",
                width: "220px",
                boxShadow: "0 4px 10px rgba(0, 0, 0, 0.4)",
                backdropFilter: "blur(4px)",
                color: "white",
              }}
            >
              <img
                src={product.imageUrl}
                alt={product.name}
                style={{
                  width: "100%",
                  height: "150px",
                  objectFit: "cover",
                  borderRadius: "8px",
                }}
              />
              <h3>{product.name}</h3>
              <p>₹{product.price}</p>
              <button
                onClick={() => handleDelete(product.id)}
                style={{
                  marginTop: "10px",
                  padding: "8px 12px",
                  backgroundColor: "#dc3545",
                  border: "none",
                  color: "white",
                  fontWeight: "bold",
                  borderRadius: "6px",
                  cursor: "pointer",
                  width: "100%",
                }}
              >
                ❌ Delete
              </button>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default DeleteProduct;
