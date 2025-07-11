import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "./Navbar.css";

const Navbar = () => {
  const navigate = useNavigate();
  const [userRole, setUserRole] = useState("");
  const [userDetails, setUserDetails] = useState({});
  const [showDetails, setShowDetails] = useState(false);

  useEffect(() => {
    try {
      const userInfo = JSON.parse(localStorage.getItem("user"));
      if (userInfo) {
        setUserRole(userInfo.role || "");
        setUserDetails(userInfo);
      }
    } catch (e) {
      console.error("Failed to parse user from localStorage", e);
    }
  }, []);

  const handleLogout = () => {
    localStorage.clear();
    navigate("/");
  };

  const toggleDetails = () => {
    setShowDetails((prev) => !prev);
  };

  return (
    <nav className="navbar">
      <div className="nav-left" style={{ display: "flex", alignItems: "center", gap: "14px" }}>
        {/* Back Button */}
        <button
          onClick={() => navigate(-1)}
          style={{
            backgroundColor: "rgba(255, 255, 255, 0.15)",
            border: "none",
            color: "white",
            fontSize: "18px",
            fontWeight: "bold",
            cursor: "pointer",
            padding: "6px 12px",
            borderRadius: "8px",
            backdropFilter: "blur(2px)",
            transition: "background 0.2s",
          }}
          onMouseOver={(e) => (e.currentTarget.style.backgroundColor = "rgba(255,255,255,0.25)")}
          onMouseOut={(e) => (e.currentTarget.style.backgroundColor = "rgba(255, 255, 255, 0.15)")}
          title="Go Back"
        >
          ←
        </button>

        {/* Logo */}
        <h2 className="logo" style={{ margin: 0 }}>🛍️ SHOPPER</h2>
      </div>

      <div className="nav-links" style={{ display: "flex", alignItems: "center", gap: "12px" }}>
        {/* Cart Icon */}
        <Link to="/cart" className="nav-link" style={{ fontSize: "22px" }}>
          🛒
        </Link>

        {/* Detail Button */}
        {userRole && (
          <div className="dropdown-container" style={{ position: "relative" }}>
            <button
              className="detail-button"
              onClick={toggleDetails}
              style={{
                padding: "6px 14px",
                backgroundColor: "#28a745", // Green
                border: "none",
                color: "white",
                fontWeight: "bold",
                borderRadius: "6px",
                cursor: "pointer",
              }}
            >
              Details
            </button>

            {showDetails && (
              <div
                className="dropdown-menu"
                style={{
                  position: "absolute",
                  top: "40px",
                  right: 0,
                  backgroundColor: "rgba(255, 255, 255, 0.15)",
                  backdropFilter: "blur(8px)",
                  color: "white",
                  padding: "12px",
                  borderRadius: "6px",
                  boxShadow: "0 4px 10px rgba(0,0,0,0.4)",
                  minWidth: "220px",
                  zIndex: 100,
                }}
              >
                <div><strong>Full Name:</strong> {userDetails.fullName}</div>
                <div><strong>Email:</strong> {userDetails.email}</div>
                <div><strong>Role:</strong> {userDetails.role}</div>
              </div>
            )}
          </div>
        )}

        {/* Logout Button */}
        <button
          className="logout-button"
          onClick={handleLogout}
          style={{
            padding: "6px 14px",
            backgroundColor: "#dc3545", // Red
            border: "none",
            color: "white",
            fontWeight: "bold",
            borderRadius: "6px",
            cursor: "pointer",
          }}
        >
          Logout
        </button>
      </div>
    </nav>
  );
};

export default Navbar;
