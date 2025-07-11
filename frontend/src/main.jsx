import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter, Routes, Route, useLocation } from "react-router-dom";
import "./index.css";

// Components
import Login from "./components/Login";
import Register from "./components/Register";
import AdminDashboard from "./components/AdminDashboard";
import CustomerDashboard from "./components/CustomerDashboard";
import ProductList from "./components/ProductList";
import Cart from "./components/Cart";
import AddProduct from "./components/AddProduct";
import DeleteProduct from "./components/DeleteProduct";
import Navbar from "./components/Navbar";
import ProtectedRoute from "./components/ProtectedRoute";
import AdminOrders from "./components/AdminOrders";
import Orders from "./components/Orders";
import DeleteOrderPage from "./components/DeleteOrderPage";
import CustomerOrders from "./components/CustomerOrders";
import TrackOrders from "./components/TrackOrders";

// ✅ New Review Components
import CustomerReviewPage from "./components/CustomerReviewPage";
import AdminReviewPage from "./components/AdminReviewPage";

function AppWrapper() {
  const location = useLocation();
  const hideNavbar = location.pathname === "/" || location.pathname === "/register";

  return (
    <>
      {!hideNavbar && <Navbar />}
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/register" element={<Register />} />

        {/* Protected Routes */}
        <Route
          path="/admin-dashboard"
          element={
            <ProtectedRoute>
              <AdminDashboard />
            </ProtectedRoute>
          }
        />
        <Route
          path="/customer-dashboard"
          element={
            <ProtectedRoute>
              <CustomerDashboard />
            </ProtectedRoute>
          }
        />
        <Route
          path="/products"
          element={
            <ProtectedRoute>
              <ProductList />
            </ProtectedRoute>
          }
        />
        <Route
          path="/cart"
          element={
            <ProtectedRoute>
              <Cart />
            </ProtectedRoute>
          }
        />
        <Route
          path="/add-product"
          element={
            <ProtectedRoute>
              <AddProduct />
            </ProtectedRoute>
          }
        />
        <Route
          path="/delete-product"
          element={
            <ProtectedRoute>
              <DeleteProduct />
            </ProtectedRoute>
          }
        />
        <Route
          path="/admin/orders"
          element={
            <ProtectedRoute>
              <AdminOrders />
            </ProtectedRoute>
          }
        />
        <Route
          path="/orders"
          element={
            <ProtectedRoute>
              <Orders />
            </ProtectedRoute>
          }
        />
        <Route
          path="/customer-orders"
          element={
            <ProtectedRoute>
              <CustomerOrders />
            </ProtectedRoute>
          }
        />
        <Route
          path="/track-orders"
          element={
            <ProtectedRoute>
              <TrackOrders />
            </ProtectedRoute>
          }
        />
        <Route
          path="/admin/orders/delete"
          element={
            <ProtectedRoute>
              <DeleteOrderPage />
            </ProtectedRoute>
          }
        />

        {/* ✅ Review Routes */}
        <Route
          path="/customer/reviews"
          element={
            <ProtectedRoute>
              <CustomerReviewPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/admin/reviews"
          element={
            <ProtectedRoute>
              <AdminReviewPage />
            </ProtectedRoute>
          }
        />
      </Routes>
    </>
  );
}

ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <BrowserRouter>
      <AppWrapper />
    </BrowserRouter>
  </React.StrictMode>
);
