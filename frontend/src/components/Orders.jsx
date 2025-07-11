import React, { useEffect, useState } from "react";
import axios from "../api/axios";

const Orders = () => {
  const [orders, setOrders] = useState([]);

  useEffect(() => {
    const fetchOrders = async () => {
      try {
        const res = await axios.get("/api/orders/user");
        setOrders(res.data);
      } catch (err) {
        console.error("❌ Failed to fetch orders", err);
      }
    };

    fetchOrders();
  }, []);

  const cancelOrder = async (orderId) => {
    try {
      await axios.delete(`/api/orders/${orderId}`);
      setOrders(orders.filter((o) => o.id !== orderId));
    } catch (err) {
      console.error("❌ Cancel failed", err);
    }
  };

  const trackOrder = (orderId) => {
    alert(`Tracking order ${orderId}...`);
    // TODO: Replace with navigation or modal logic
  };

  return (
    <div style={{ padding: "20px", color: "#fff" }}>
      <h2>Your Orders</h2>
      {orders.length === 0 ? (
        <p>No orders found.</p>
      ) : (
        orders.map((order) => (
          <div
            key={order.id}
            style={{
              marginBottom: "20px",
              padding: "10px",
              border: "1px solid #ccc",
              borderRadius: "8px",
              backgroundColor: "transparent",
              backdropFilter: "blur(2px)",
            }}
          >
            <p><strong>Order ID:</strong> {order.id}</p>
            <p><strong>Total:</strong> ₹{order.totalAmount}</p>

            {order.items?.length > 0 && (
              <div>
                <p><strong>Items:</strong></p>
                <ul style={{ listStyleType: "none", paddingLeft: 0, margin: 0 }}>
                  {order.items.map((item, idx) => (
                    <li key={idx}>
                      {item.product?.name || "Unknown Product"} × {item.quantity}
                    </li>
                  ))}
                </ul>
              </div>
            )}

            <div style={{ display: "flex", gap: "10px", marginTop: "10px" }}>
              <button
                onClick={() => cancelOrder(order.id)}
                style={{
                  backgroundColor: "#e53935",
                  color: "white",
                  border: "none",
                  padding: "8px 16px",
                  borderRadius: "4px",
                  cursor: "pointer",
                }}
              >
                Cancel Order
              </button>

              <button
                onClick={() => trackOrder(order.id)}
                style={{
                  backgroundColor: "#43a047",
                  color: "white",
                  border: "none",
                  padding: "8px 16px",
                  borderRadius: "4px",
                  cursor: "pointer",
                }}
              >
                Track Order
              </button>
            </div>
          </div>
        ))
      )}
    </div>
  );
};

export default Orders;
