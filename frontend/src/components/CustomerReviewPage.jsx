import React, { useEffect, useState } from "react";
import axios from "axios";

const CustomerReviewPage = () => {
  const [orders, setOrders] = useState([]);
  const [reviews, setReviews] = useState({});
  const [loading, setLoading] = useState(true);

  const user = JSON.parse(localStorage.getItem("user"));
  const token = localStorage.getItem("token");

  useEffect(() => {
    const fetchOrders = async () => {
      try {
        const res = await axios.get(
          `http://localhost:8090/api/orders/user/${user?.id}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        setOrders(res.data);
      } catch (err) {
        console.error("Failed to fetch orders", err);
      } finally {
        setLoading(false);
      }
    };

    if (user?.id && token) {
      fetchOrders();
    } else {
      setLoading(false); 
    }
  }, [user?.id, token]);

  const handleReviewChange = (productId, value) => {
    setReviews({ ...reviews, [productId]: value });
  };

  const submitReview = async (productId) => {
    const review = {
      rating: 5,
      comment: reviews[productId],
    };
    try {
      await axios.post(
        `http://localhost:8090/api/reviews/${user.id}/${productId}`,
        review,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      alert("✅ Review submitted!");
    } catch (err) {
      console.error("Failed to submit review", err);
      alert("❌ Error submitting review.");
    }
  };

  if (loading) {
    return <p style={{ color: "white", padding: "20px" }}>Loading orders...</p>;
  }

  if (!user || !token) {
    return <p style={{ color: "white", padding: "20px" }}>You must be logged in to view your orders.</p>;
  }

  return (
    <div style={{ padding: "20px", color: "white" }}>
      <h2>📝 Submit Reviews for Ordered Products</h2>
      {orders.length === 0 ? (
        <p>You haven't ordered any products yet.</p>
      ) : (
        orders.map((order) =>
          order.items.map((item) => (
            <div
              key={item.productId}
              style={{
                background: "#1e293b",
                padding: "15px",
                marginTop: "15px",
                borderRadius: "10px",
              }}
            >
              <h3>{item.productName}</h3>
              <textarea
                rows="3"
                style={{
                  width: "100%",
                  padding: "8px",
                  marginTop: "8px",
                  borderRadius: "5px",
                }}
                placeholder="Write your review here..."
                value={reviews[item.productId] || ""}
                onChange={(e) =>
                  handleReviewChange(item.productId, e.target.value)
                }
              />
              <button
                style={{
                  marginTop: "8px",
                  padding: "6px 12px",
                  backgroundColor: "#2563eb",
                  color: "white",
                  border: "none",
                  borderRadius: "5px",
                  cursor: "pointer",
                }}
                onClick={() => submitReview(item.productId)}
              >
                Submit Review
              </button>
            </div>
          ))
        )
      )}
    </div>
  );
};

export default CustomerReviewPage;
