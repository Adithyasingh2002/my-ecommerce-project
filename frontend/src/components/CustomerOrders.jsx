import React, { useEffect, useState } from "react";
import axios from "../api/axios";

const CustomerOrders = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchOrders = async () => {
    try {
      const token = localStorage.getItem("token");
      const res = await axios.get("/api/orders", {
        headers: { Authorization: `Bearer ${token}` },
      });
      setOrders(Array.isArray(res.data) ? res.data : []);
    } catch (err) {
      console.error("❌ Failed to fetch orders", err);
    } finally {
      setLoading(false);
    }
  };

  const cancelOrder = async (id) => {
    try {
      const token = localStorage.getItem("token");
      await axios.delete(`/api/orders/${id}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      fetchOrders();
    } catch (err) {
      console.error("❌ Cancel order failed", err);
    }
  };

  useEffect(() => {
    fetchOrders();
  }, []);

  return (
    <div style={{ padding: "40px", color: "white" }}>
      <h2>📜 Your Order History</h2>
      {loading ? (
        <p>Loading...</p>
      ) : orders.length === 0 ? (
        <p>No orders yet.</p>
      ) : (
        orders.map((order) => (
          <div key={order.id} style={cardStyle}>
            <p><strong>Status:</strong> {order.status}</p>
            <p><strong>Date:</strong> {new Date(order.orderDate).toLocaleString()}</p>
            <p><strong>Total:</strong> ₹{order.totalAmount?.toFixed(2)}</p>
            <ul>
              {order.items?.map((item) => (
                <li key={item.id}>
                  {item.product?.name || "Unknown"} × {item.quantity}
                </li>
              ))}
            </ul>
            {order.status !== "CANCELLED" && (
              <button onClick={() => cancelOrder(order.id)} style={buttonStyle}>
                ❌ Cancel Order
              </button>
            )}
          </div>
        ))
      )}
    </div>
  );
};

const cardStyle = {
  backgroundColor: "rgba(255,255,255,0.1)",
  padding: "20px",
  margin: "10px 0",
  borderRadius: "8px",
  color: "#fff",
};

const buttonStyle = {
  backgroundColor: "#dc3545",
  border: "none",
  padding: "8px 12px",
  borderRadius: "6px",
  color: "white",
  fontWeight: "bold",
  cursor: "pointer",
};

export default CustomerOrders;
