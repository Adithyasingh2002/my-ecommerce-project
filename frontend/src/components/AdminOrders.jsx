import React, { useEffect, useState } from "react";
import axios from "../api/axios";

const AdminOrders = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchOrders = async () => {
    try {
      const token = localStorage.getItem("token");
      console.log("📡 Sending request to /api/orders with token:", token);

      const res = await axios.get("/api/orders", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      console.log("✅ Raw response:", res.data);
      console.log("📦 Is array?", Array.isArray(res.data));

      setOrders(Array.isArray(res.data) ? res.data : []);
      setLoading(false);
    } catch (err) {
      console.error("❌ Failed to fetch orders:", err);
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchOrders(); // Fetch on initial load
  }, []);

  return (
    <div style={{ padding: "40px", color: "white" }}>
      <h2>📦 All Orders</h2>
      <button onClick={fetchOrders} style={{ marginBottom: "20px" }}>
        🔄 Refresh Orders
      </button>

      {loading ? (
        <p>Loading orders...</p>
      ) : orders.length === 0 ? (
        <p>No orders found.</p>
      ) : (
        orders.map((order) => (
          <div
            key={order.id}
            style={{
              backgroundColor: "rgba(255,255,255,0.1)",
              padding: "20px",
              margin: "10px 0",
              borderRadius: "8px",
              color: "#fff",
            }}
          >
            <p><strong>Order ID:</strong> {order.id}</p>
            <p><strong>User:</strong> {order.user?.fullName || "N/A"} ({order.user?.email || "N/A"})</p>
            <p><strong>Status:</strong> {order.status}</p>
            <p><strong>Date:</strong> {new Date(order.orderDate).toLocaleString()}</p>
            <p><strong>Total:</strong> ₹{order.totalAmount?.toFixed(2) || "0.00"}</p>
            <p><strong>Items:</strong></p>
            <ul>
              {order.items?.map((item) => (
                <li key={item.id}>
                  {item.product?.name || "Unknown Product"} × {item.quantity}
                </li>
              ))}
            </ul>
          </div>
        ))
      )}
    </div>
  );
};

export default AdminOrders;
