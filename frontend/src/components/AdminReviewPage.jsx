import React, { useEffect, useState } from "react";
import axios from "axios";

const AdminReviewPage = () => {
  const [reviews, setReviews] = useState([]);
  const token = localStorage.getItem("token");

  const fetchAllReviews = async () => {
    try {
      const res = await axios.get("http://localhost:8090/api/admin/reviews", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setReviews(res.data);
    } catch (err) {
      console.error("❌ Failed to fetch reviews:", err.response?.data || err.message);
      alert("❌ Unauthorized or failed to load reviews.");
    }
  };

  const deleteReview = async (id) => {
    const confirmDelete = window.confirm("Are you sure you want to delete this review?");
    if (!confirmDelete) return;

    try {
      await axios.delete(`http://localhost:8090/api/admin/reviews/${id}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      alert("✅ Review deleted!");
      setReviews((prev) => prev.filter((r) => r.id !== id));
    } catch (err) {
      console.error("❌ Failed to delete review:", err.response?.data || err.message);
      alert("❌ Failed to delete review. Unauthorized?");
    }
  };

  useEffect(() => {
    fetchAllReviews();
  }, []);

  return (
    <div style={{ padding: "20px", color: "white" }}>
      <h2>📋 Admin - All Product Reviews</h2>
      {reviews.length === 0 ? (
        <p>No reviews found.</p>
      ) : (
        reviews.map((review) => (
          <div
            key={review.id}
            style={{
              background: "#1e293b",
              padding: "15px",
              marginTop: "15px",
              borderRadius: "10px",
            }}
          >
            <p><strong>Product:</strong> {review.product?.name || "N/A"}</p>
            <p><strong>User:</strong> {review.user?.fullName || "N/A"}</p>
            <p><strong>Comment:</strong> {review.comment}</p>
            <p><strong>Rating:</strong> {review.rating ?? "N/A"} ⭐</p>
            <p><strong>Date:</strong> {new Date(review.createdAt).toLocaleString()}</p>
            <button
              style={{
                marginTop: "8px",
                padding: "6px 12px",
                backgroundColor: "#dc2626",
                color: "white",
                border: "none",
                borderRadius: "5px",
                cursor: "pointer",
              }}
              onClick={() => deleteReview(review.id)}
            >
              Delete
            </button>
          </div>
        ))
      )}
    </div>
  );
};

export default AdminReviewPage;
