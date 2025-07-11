// src/api/axios.js
import axios from "axios";

const instance = axios.create({
  baseURL: "http://localhost:8090", // ✅ backend port
  headers: {
    "Content-Type": "application/json",
    Accept: "application/json",
  },
  timeout: 10000,
  withCredentials: false,
});

// ✅ Attach token from localStorage
instance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// ✅ Global error logging
instance.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response) {
      console.error("Axios error:", {
        status: error.response.status,
        message: error.response.data || error.message,
        url: error.config?.url,
      });
    } else {
      console.error("Axios network error:", error.message);
    }
    return Promise.reject(error);
  }
);

export default instance;
