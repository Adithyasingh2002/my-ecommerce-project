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

      const {
        token,
        id,
        fullName,
        email: returnedEmail,
        phoneNumber,
        address,
        role,
      } = response.data;

      if (!id || !role) {
        setMessage("❌ Invalid user data returned from server.");
        return;
      }

      // Save token and user info using consistent keys
      localStorage.setItem("token", token);
      localStorage.setItem(
        "user",
        JSON.stringify({
          id,
          fullName,
          email: returnedEmail,
          phoneNumber,
          address,
          role,
        })
      );

      setMessage("✅ Login successful!");

      // Navigate based on role
      if (role === "ADMIN") {
        navigate("/admin-dashboard");
      } else {
        navigate("/customer-dashboard");
      }
    } catch (err) {
      console.error("Login failed:", err.response || err);
      const errorMessage =
        err.response?.data?.message ||
        err.response?.data ||
        "❌ Login failed. Please try again.";
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
          <p
            style={{
              color: message.includes("✅") ? "green" : "red",
              marginTop: "10px",
            }}
          >
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
