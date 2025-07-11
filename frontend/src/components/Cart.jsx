// src/components/Cart.jsx
import React, { useEffect, useState } from "react";
import axios from "../api/axios";
import "./Cart.css";

function Cart() {
  const [cart, setCart] = useState([]);
  const [message, setMessage] = useState("");

  useEffect(() => {
    const storedCart = JSON.parse(localStorage.getItem("cart")) || [];
    setCart(storedCart);
  }, []);

  const updateCart = (newCart) => {
    setCart(newCart);
    localStorage.setItem("cart", JSON.stringify(newCart));
  };

  const incrementQuantity = (id) => {
    const updated = cart.map((item) =>
      item.id === id ? { ...item, quantity: item.quantity + 1 } : item
    );
    updateCart(updated);
  };

  const decrementQuantity = (id) => {
    const updated = cart.map((item) =>
      item.id === id && item.quantity > 1
        ? { ...item, quantity: item.quantity - 1 }
        : item
    );
    updateCart(updated);
  };

  const deleteItem = (id) => {
    const updated = cart.filter((item) => item.id !== id);
    updateCart(updated);
  };

  const getTotal = () =>
    cart.reduce((total, item) => total + item.price * item.quantity, 0);

  const handleOrder = async () => {
    const token = localStorage.getItem("token");
    const user = JSON.parse(localStorage.getItem("user")); // ✅ Get user from localStorage

    if (!token || !user) {
      alert("❌ Please login first!");
      return;
    }

    if (cart.length === 0) {
      alert("❌ Cart is empty!");
      return;
    }

    try {
      const payload = {
        user: { id: user.id }, // ✅ Add user ID
        orderItems: cart.map((item) => ({
          product: { id: item.id },
          quantity: item.quantity,
        })),
      };

      const res = await axios.post("/api/orders", payload, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (res.status === 200 || res.status === 201) {
        setMessage("✅ Order placed successfully!");
        updateCart([]); // Clear cart
      }
    } catch (err) {
      console.error("❌ Failed to place order", err);
      setMessage("❌ Error placing order. Try again.");
    }
  };

  return (
    <div className="cart-wrapper">
      <div className="cart-container">
        <h2>🛒 Your Cart</h2>
        {message && (
          <p style={{ color: message.includes("✅") ? "green" : "red" }}>
            {message}
          </p>
        )}
        {cart.length === 0 ? (
          <p>No items in cart.</p>
        ) : (
          <>
            <ul className="cart-items">
              {cart.map((item) => (
                <li key={item.id} className="cart-item">
                  <div className="item-details">
                    <h3>{item.name}</h3>
                    <div className="quantity-controls">
                      <button onClick={() => decrementQuantity(item.id)}>
                        ➖
                      </button>
                      <span>{item.quantity}</span>
                      <button onClick={() => incrementQuantity(item.id)}>
                        ➕
                      </button>
                    </div>
                  </div>
                  <div className="item-price">
                    <p>₹{item.price * item.quantity}</p>
                    <button
                      className="delete-btn"
                      onClick={() => deleteItem(item.id)}
                    >
                      🗑️
                    </button>
                  </div>
                </li>
              ))}
            </ul>
            <h3 className="cart-total">Total: ₹{getTotal()}</h3>
            <button className="order-button" onClick={handleOrder}>
              Place Order
            </button>
          </>
        )}
      </div>
    </div>
  );
}

export default Cart;
