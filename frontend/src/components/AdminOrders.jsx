import React, { useEffect, useState } from "react";
import axios from "../api/axios";

const AdminOrders = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchOrders = async () => {
    try {
      const token = localStorage.getItem("token");
      const res = await axios.get("/api/orders", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      setOrders(Array.isArray(res.data) ? res.data : []);
    } catch (err) {
      console.error("❌ Failed to fetch orders:", err);
    } finally {
      setLoading(false);
    }
  };

  const deleteOrder = async (id) => {
    if (!window.confirm("🗑️ Are you sure you want to permanently delete this order?")) return;
    try {
      const token = localStorage.getItem("token");
      await axios.delete(`/api/orders/${id}/delete`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setOrders((prev) => prev.filter((o) => o.id !== id));
    } catch (err) {
      console.error("❌ Delete failed", err);
      alert("Failed to delete order.");
    }
  };

  const cancelOrder = async (id) => {
    if (!window.confirm("🚫 Are you sure you want to cancel this order?")) return;
    try {
      const token = localStorage.getItem("token");
      await axios.delete(`/api/orders/${id}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setOrders((prev) =>
        prev.map((o) => (o.id === id ? { ...o, status: "CANCELLED" } : o))
      );
    } catch (err) {
      console.error("❌ Cancel failed", err);
      alert("Failed to cancel order.");
    }
  };

  useEffect(() => {
    fetchOrders();
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
              position: "relative",
            }}
          >
            {/* 🔴 Action Buttons */}
            <div style={{ position: "absolute", top: "10px", right: "10px" }}>
              {order.status === "CANCELLED" ? (
                <button
                  disabled
                  style={{
                    backgroundColor: "#333",
                    color: "#ccc",
                    marginRight: "10px",
                    padding: "5px 10px",
                    borderRadius: "5px",
                    border: "none",
                    cursor: "not-allowed",
                  }}
                >
                  ❌ Cancelled
                </button>
              ) : (
                <button
                  onClick={() => cancelOrder(order.id)}
                  style={{
                    backgroundColor: "darkred",
                    color: "white",
                    marginRight: "10px",
                    padding: "5px 10px",
                    borderRadius: "5px",
                    border: "none",
                    cursor: "pointer",
                  }}
                >
                  🚫 Cancel Order
                </button>
              )}
              <button
                onClick={() => deleteOrder(order.id)}
                style={{
                  backgroundColor: "#d11a2a",
                  color: "white",
                  padding: "5px 10px",
                  borderRadius: "5px",
                  border: "none",
                  cursor: "pointer",
                }}
              >
                🗑️ Delete Order
              </button>
            </div>

            {/* Order Details */}
            <p><strong>Order ID:</strong> {order.id}</p>
            <p><strong>User:</strong> {order.user?.fullName || "N/A"} ({order.user?.email || "N/A"})</p>
            <p><strong>Status:</strong> {order.status}</p>
            <p><strong>Date:</strong> {order.createdAt ? new Date(order.createdAt).toLocaleString() : "N/A"}</p>
            <p><strong>Total:</strong> ₹{order.totalAmount?.toFixed(2) || "0.00"}</p>
            <p><strong>Items:</strong></p>
            <ul>
              {order.items && order.items.length > 0 ? (
                order.items.map((item) => (
                  <li key={item.id}>
                    {item.productName || "Unnamed Product"} × {item.quantity}
                  </li>
                ))
              ) : (
                <li>No items found</li>
              )}
            </ul>
          </div>
        ))
      )}
    </div>
  );
};

export default AdminOrders;
