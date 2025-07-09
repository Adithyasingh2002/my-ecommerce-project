// src/components/ProductList.jsx
import React, { useEffect, useState } from "react";
import axios from "../api/axios";
import "./ProductList.css";

const ProductList = () => {
  const [products, setProducts] = useState([]);
  const role = localStorage.getItem("role");

  useEffect(() => {
    fetchProducts();
  }, []);

  const fetchProducts = async () => {
    try {
      const res = await axios.get("/api/products");
      setProducts(res.data);
    } catch (err) {
      console.error("❌ Failed to fetch products", err);
    }
  };

  const handleAddToCart = (product) => {
    const cart = JSON.parse(localStorage.getItem("cart")) || [];
    const existingItem = cart.find((item) => item.id === product.id);

    if (existingItem) {
      existingItem.quantity += 1;
    } else {
      cart.push({ ...product, quantity: 1 });
    }

    localStorage.setItem("cart", JSON.stringify(cart));
    alert(`${product.name} added to cart! 🛒`);
  };

  const getAvailabilityClass = (quantity) => {
    if (quantity === 0) return "out-of-stock";
    if (quantity < 5) return "low-stock";
    return "in-stock";
  };

  return (
    <div className="product-background">
      <div className="product-overlay" />
      <div className="product-container">
        <h2>🛍️ Products</h2>
        <div className="product-grid">
          {products.map((product) => (
            <div key={product.id} className="product-card">
              <img src={product.imageUrl} alt={product.name} />
              <h3>{product.name}</h3>
              <p>{product.description}</p>
              <p>₹{product.price}</p>
              <p className={`availability ${getAvailabilityClass(product.quantity)}`}>
                {product.quantity === 0
                  ? "Out of Stock"
                  : `Available: ${product.quantity}`}
              </p>

              {/* ✅ Keep Add to Cart for all users */}
              <button
                onClick={() => handleAddToCart(product)}
                disabled={product.quantity === 0}
              >
                ➕ Add to Cart
              </button>

              {/* ❌ Removed delete button */}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default ProductList;
