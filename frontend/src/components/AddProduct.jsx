// src/components/AddProduct.jsx
import React, { useState } from "react";
import axios from "../api/axios";
import { useNavigate } from "react-router-dom";
import "./AddProduct.css"; // Consistent background

function AddProduct() {
  const navigate = useNavigate();

  const [product, setProduct] = useState({
    name: "",
    description: "",
    price: "",
    quantity: "",
    category: "",
    imageUrl: "",
  });

  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const handleChange = (e) => {
    setError("");
    setSuccess("");
    setProduct({ ...product, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!product.name || !product.price || !product.description) {
      setError("❌ Name, Description and Price are required.");
      return;
    }

    try {
      const token = localStorage.getItem("token");

      const res = await axios.post("/api/products", product, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (res.status === 200 || res.status === 201) {
        setSuccess("✅ Product added successfully!");
        setProduct({
          name: "",
          description: "",
          price: "",
          quantity: "",
          category: "",
          imageUrl: "",
        });
        setTimeout(() => navigate("/admin-dashboard"), 1000);
      }
    } catch (err) {
      const msg =
        err.response?.data || "❌ Error adding product. Make sure you're an admin.";
      setError(msg);
    }
  };

  return (
    <div className="add-product-background">
      <div className="add-product-overlay" />
      <div className="add-product-container">
        <h2>Add New Product</h2>
        <form onSubmit={handleSubmit} className="add-product-form">
          <input
            name="name"
            placeholder="Name"
            value={product.name}
            onChange={handleChange}
            required
          />
          <input
            name="description"
            placeholder="Description"
            value={product.description}
            onChange={handleChange}
            required
          />
          <input
            name="price"
            type="number"
            placeholder="Price"
            value={product.price}
            onChange={handleChange}
            required
          />
          <input
            name="quantity"
            type="number"
            placeholder="Quantity"
            value={product.quantity}
            onChange={handleChange}
          />
          <input
            name="category"
            placeholder="Category"
            value={product.category}
            onChange={handleChange}
          />
          <input
            name="imageUrl"
            placeholder="Image URL"
            value={product.imageUrl}
            onChange={handleChange}
          />

          {error && <p className="error-msg">{error}</p>}
          {success && <p className="success-msg">{success}</p>}

          <button type="submit">➕ Add Product</button>
        </form>
      </div>
    </div>
  );
}

export default AddProduct;
