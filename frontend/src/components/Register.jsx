import React, { useState } from "react";
import "./Register.css";
import axios from "../api/axios";
import { useNavigate } from "react-router-dom";

function Register() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    fullName: "",
    email: "",
    password: "",
    address: "",
    phoneNumber: "",
    role: "CUSTOMER",
    adminSecret: "", //  Will be sent to backend
  });

  const [error, setError] = useState("");
  const ADMIN_SECRET = "admin123"; // Frontend-only check (optional)

  const handleChange = (e) => {
    setError("");
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    setError("");

    //  Optional frontend check
    if (formData.role === "ADMIN" && formData.adminSecret !== ADMIN_SECRET) {
      setError("❌ Invalid Admin Password.");
      return;
    }

    try {
      const response = await axios.post("/api/auth/register", formData); // ✅ send full payload
      console.log("✅ Registration success:", response);

      if (response.status === 200 || response.status === 201) {
        alert("✅ Registration successful!");
        navigate("/");
      } else {
        throw new Error("Unexpected response status: " + response.status);
      }
    } catch (err) {
      console.error("❌ Registration error:", err.response?.data || err.message);
      setError(err.response?.data || "❌ Registration failed. Please try again.");
    }
  };

  return (
    <div className="register-container">
      <div className="overlay" />
      <form className="register-form" onSubmit={handleRegister}>
        <h2>Register</h2>

        <input
          type="text"
          name="fullName"
          placeholder="Full Name"
          value={formData.fullName}
          onChange={handleChange}
          required
        />

        <input
          type="email"
          name="email"
          placeholder="Email"
          value={formData.email}
          onChange={handleChange}
          required
        />

        <input
          type="password"
          name="password"
          placeholder="Password"
          value={formData.password}
          onChange={handleChange}
          required
        />

        <input
          type="text"
          name="address"
          placeholder="Address"
          value={formData.address}
          onChange={handleChange}
          required
        />

        <input
          type="text"
          name="phoneNumber"
          placeholder="Phone Number"
          value={formData.phoneNumber}
          onChange={handleChange}
          required
        />

        <select
          name="role"
          value={formData.role}
          onChange={handleChange}
          required
        >
          <option value="CUSTOMER">Customer</option>
          <option value="ADMIN">Admin</option>
        </select>

        {formData.role === "ADMIN" && (
          <input
            type="password"
            name="adminSecret"
            placeholder="Enter Admin Password"
            value={formData.adminSecret}
            onChange={handleChange}
            required
          />
        )}

        {error && <p style={{ color: "red", marginTop: "8px" }}>{error}</p>}

        <button type="submit">Register</button>

        <div className="login-link">
          <span>Already have an account?</span>
          <a href="/">Login</a>
        </div>
      </form>
    </div>
  );
}

export default Register;
