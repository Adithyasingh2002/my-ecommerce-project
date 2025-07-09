import React, { useEffect, useState } from "react";
import axios from "../api/axios";

const DeleteOrderPage = () => {
  const [orders, setOrders] = useState([]);

  useEffect(() => {
    const fetchAllOrders = async () => {
      try {
        const token = localStorage.getItem("token");
        const res = await axios.get("/api/orders", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        setOrders(res.data);
      } catch (err) {
        console.error("Error fetching orders", err);
      }
    };

    fetchAllOrders();
  }, []);

  const deleteOrder = async (orderId) => {
    try {
      const token = localStorage.getItem("token");
      await axios.delete(`/api/orders/${orderId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setOrders(orders.filter((o) => o.id !== orderId));
    } catch (err) {
      console.error("Delete failed", err);
    }
  };

  return (
    <div style={{ padding: "20px", color: "#fff" }}>
      <h2>Delete Any Order (Admin)</h2>
      {orders.length === 0 ? (
        <p>No orders to display.</p>
      ) : (
        orders.map((order) => (
          <div key={order.id} style={{ marginBottom: "10px" }}>
            <p>Order ID: {order.id}</p>
            <p>Customer: {order.customer?.fullName || "N/A"}</p>
            <p>Total: ₹{order.totalAmount}</p>
            <button onClick={() => deleteOrder(order.id)}>Delete Order</button>
          </div>
        ))
      )}
    </div>
  );
};

export default DeleteOrderPage;
