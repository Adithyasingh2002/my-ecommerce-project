// src/components/Navbar.jsx
import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "./Navbar.css";

const Navbar = () => {
  const navigate = useNavigate();
  const [userRole, setUserRole] = useState("");

  useEffect(() => {
    try {
      const userInfo = JSON.parse(localStorage.getItem("userInfo"));
      if (userInfo?.role) {
        setUserRole(userInfo.role);
      }
    } catch (e) {
      console.error("Failed to parse userInfo", e);
    }
  }, []);

  const handleLogout = () => {
    localStorage.clear();
    navigate("/");
  };

  return (
    <nav className="navbar">
      <div className="nav-left">
        <h2 className="logo">🛍️ E-Commerce</h2>
      </div>

      <div className="nav-links">
        {userRole === "CUSTOMER" && (
          <Link to="/orders" className="nav-link">
            📜 Order History
          </Link>
        )}
        <Link to="/cart" className="nav-link">🛒</Link>
        <button onClick={handleLogout}>Logout</button>
      </div>
    </nav>
  );
};

export default Navbar;
