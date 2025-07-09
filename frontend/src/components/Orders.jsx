import React, { useEffect, useState } from "react";
import axios from "../api/axios";

const Orders = () => {
  const [orders, setOrders] = useState([]);
  const role = JSON.parse(localStorage.getItem("userInfo"))?.role;

  useEffect(() => {
    const fetchOrders = async () => {
      try {
        const token = localStorage.getItem("token");
        const res = await axios.get("/api/orders", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        setOrders(res.data);
      } catch (err) {
        console.error("Failed to fetch orders", err);
      }
    };

    fetchOrders();
  }, []);

  const cancelOrder = async (orderId) => {
    try {
      const token = localStorage.getItem("token");
      await axios.delete(`/api/orders/${orderId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setOrders(orders.filter((o) => o.id !== orderId));
    } catch (err) {
      console.error("Cancel failed", err);
    }
  };

  return (
    <div style={{ padding: "20px", color: "#fff" }}>
      <h2>Your Orders</h2>
      {orders.length === 0 ? (
        <p>No orders found.</p>
      ) : (
        orders.map((order) => (
          <div key={order.id} style={{ marginBottom: "10px" }}>
            <p>Order ID: {order.id}</p>
            <p>Total: ₹{order.totalAmount}</p>
            <button onClick={() => cancelOrder(order.id)}>Cancel Order</button>
          </div>
        ))
      )}
    </div>
  );
};

export default Orders;
