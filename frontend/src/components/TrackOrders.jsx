import React, { useEffect, useState } from "react";
import axios from "axios";
import "./TrackOrders.css";

const TrackOrders = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [selectedOrder, setSelectedOrder] = useState(null);
  const [orderToCancel, setOrderToCancel] = useState(null);

  const fetchOrders = async () => {
    try {
      const token = JSON.parse(localStorage.getItem("user"))?.token;
      const response = await axios.get("http://localhost:8090/api/orders/user", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      const activeOrders = response.data.filter(order => order.status !== "CANCELLED");
      setOrders(activeOrders);
    } catch (err) {
      console.error("Error fetching orders:", err);
      setError("Failed to fetch orders.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchOrders();
  }, []);

  const confirmCancelOrder = (orderId) => {
    setOrderToCancel(orderId);
  };

  const handleCancelConfirmed = async () => {
    if (!orderToCancel) return;
    try {
      const token = JSON.parse(localStorage.getItem("user"))?.token;

      await axios.delete(`http://localhost:8090/api/orders/${orderToCancel}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      setOrders(prevOrders => prevOrders.filter(order => order.id !== orderToCancel));
    } catch (err) {
      console.error("Cancel failed:", err.response?.data || err.message);
      alert("❌ Failed to cancel order: " + (err.response?.data || err.message));
    } finally {
      setOrderToCancel(null);
    }
  };

  const handleTrack = (order) => {
    setSelectedOrder(order);
  };

  const closeModal = () => {
    setSelectedOrder(null);
  };

  const closeCancelModal = () => {
    setOrderToCancel(null);
  };

  return (
    <div className="track-orders">
      <h2>📦 Your Orders</h2>

      {loading ? (
        <p>Loading orders...</p>
      ) : error ? (
        <p className="error">{error}</p>
      ) : orders.length === 0 ? (
        <p>No active orders found.</p>
      ) : (
        <ul className="order-list">
          {orders.map((order) => (
            <li key={order.id} className="order-item">
              <p><strong>Order ID:</strong> {order.id}</p>
              <p><strong>Status:</strong> {order.status}</p>
              <p><strong>Placed On:</strong> {new Date(order.orderDate).toLocaleString()}</p>
              <p><strong>Items:</strong></p>
              <ul>
                {order.items?.map((item) => (
                  <li key={item.id}>
                    {item.productName?.trim() || "Unnamed Product"} × {item.quantity}
                  </li>
                ))}
              </ul>

              <div className="order-buttons">
                {order.status === "PENDING" && (
                  <button
                    className="cancel-btn"
                    onClick={() => confirmCancelOrder(order.id)}
                    title="Cancel this order"
                  >
                    ❌ Cancel
                  </button>
                )}
                <button
                  className="track-btn"
                  onClick={() => handleTrack(order)}
                  title="Track order details"
                >
                  🔍 Track
                </button>
              </div>
            </li>
          ))}
        </ul>
      )}

      {selectedOrder && (
        <div className="modal-backdrop">
          <div className="modal">
            <h3>📦 Order Details (ID: {selectedOrder.id})</h3>
            <p><strong>Status:</strong> {selectedOrder.status}</p>
            <p><strong>Total:</strong> ₹{selectedOrder.totalAmount.toFixed(2)}</p>
            <p><strong>Order Date:</strong> {new Date(selectedOrder.orderDate).toLocaleString()}</p>
            <h4>Items:</h4>
            <ul>
              {selectedOrder.items?.map((item) => (
                <li key={item.id}>
                  {item.productName?.trim() || "Unnamed Product"} × {item.quantity} = ₹{item.price.toFixed(2)}
                </li>
              ))}
            </ul>
            <button onClick={closeModal} className="close-btn">Close</button>
          </div>
        </div>
      )}

      {orderToCancel && (
        <div className="modal-backdrop">
          <div className="modal">
            <h3>⚠️ Confirm Cancellation</h3>
            <p>Are you sure you want to cancel order #{orderToCancel}?</p>
            <div style={{ marginTop: "1rem" }}>
              <button className="cancel-btn" onClick={handleCancelConfirmed}>Yes, Cancel</button>
              <button className="track-btn" onClick={closeCancelModal} style={{ marginLeft: "1rem" }}>No, Go Back</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default TrackOrders;
