import React, { useState } from "react";

export default function DropdownMenu({ role, onSelect }) {
  const [open, setOpen] = useState(false);

  const handleSelect = (option) => {
    onSelect(option);
    setOpen(false);
  };

  return (
    <div style={{ position: "relative", display: "inline-block" }}>
      <button onClick={() => setOpen(!open)} style={{ fontSize: "20px", cursor: "pointer" }}>
        ⋮
      </button>

      {open && (
        <div
          style={{
            position: "absolute",
            top: "25px",
            right: 0,
            backgroundColor: "#fff",
            border: "1px solid #ccc",
            borderRadius: "4px",
            boxShadow: "0px 2px 6px rgba(0,0,0,0.2)",
            zIndex: 10,
            minWidth: "120px"
          }}
        >
          {role === "customer" && (
            <>
              <div onClick={() => handleSelect("orders")} style={menuItemStyle}>Orders</div>
              <div onClick={() => handleSelect("review")} style={menuItemStyle}>Review</div>
            </>
          )}
          {role === "admin" && (
            <div onClick={() => handleSelect("review")} style={menuItemStyle}>All Reviews</div>
          )}
        </div>
      )}
    </div>
  );
}

const menuItemStyle = {
  padding: "10px 15px",
  cursor: "pointer",
  borderBottom: "1px solid #eee",
  backgroundColor: "#fff",
  color: "#333",
};
