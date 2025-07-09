// src/api/axios.js
import axios from "axios";

const instance = axios.create({
  baseURL: "http://localhost:8090", // Change if backend runs elsewhere
  headers: {
    "Content-Type": "application/json",
  },
  timeout: 10000, // Optional: timeout after 10 seconds
});

// ✅ Automatically attach token if available
instance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    // Optional: handle request errors
    return Promise.reject(error);
  }
);

export default instance;
