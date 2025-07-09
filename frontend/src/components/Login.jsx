// src/components/Login.jsx
import React, { useState } from "react";
import axios from "../api/axios";
import "./Login.css";
import { useNavigate } from "react-router-dom";

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setMessage("");

    try {
      const response = await axios.post("/api/auth/login", { email, password });

      const { token, role, fullName, phoneNumber } = response.data;

      // Store token, role, and user info
      localStorage.setItem("token", token);
      localStorage.setItem("role", role);
      localStorage.setItem(
        "userInfo",
        JSON.stringify({ fullName, email, phoneNumber, role })
      );

      setMessage("✅ Login successful!");

      if (role === "ADMIN") {
        navigate("/admin-dashboard");
      } else {
        navigate("/customer-dashboard");
      }
    } catch (err) {
      const errorMessage =
        err.response?.data || "❌ Login failed. Please try again.";
      setMessage(errorMessage);
    }
  };

  return (
    <div className="login-container">
      <div className="login-box">
        <h2>Login</h2>

        <form onSubmit={handleLogin}>
          <input
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />

          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />

          <button type="submit">Login</button>
        </form>

        {message && (
          <p style={{ color: message.includes("✅") ? "green" : "red", marginTop: "10px" }}>
            {message}
          </p>
        )}

        <div className="register-link">
          <span>New Customer? </span>
          <a href="/register">Register</a>
        </div>
      </div>
    </div>
  );
};

export default Login;
