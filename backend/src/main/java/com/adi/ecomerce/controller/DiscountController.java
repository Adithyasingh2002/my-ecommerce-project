package com.adi.ecomerce.controller;

import com.adi.ecomerce.entities.Discount;
import com.adi.ecomerce.service.DiscountService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/discounts")
public class DiscountController {

    private static final Logger logger = LoggerFactory.getLogger(DiscountController.class);

    @Autowired
    private DiscountService discountService;

    // ✅ Create discount - ADMIN only
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Discount> create(@RequestBody Discount discount) {
        logger.info("Creating new discount: {}", discount);
        Discount createdDiscount = discountService.createDiscount(discount);
        logger.debug("Created discount: {}", createdDiscount);
        return ResponseEntity.ok(createdDiscount);
    }

    // ✅ Get all discounts - ADMIN and USER
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    public ResponseEntity<List<Discount>> getAll() {
        logger.info("Fetching all discounts");
        List<Discount> discounts = discountService.getAllDiscounts();
        logger.debug("Found {} discounts", discounts.size());
        return ResponseEntity.ok(discounts);
    }

    // ✅ Get discount by ID - ADMIN and USER
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<Discount> getById(@PathVariable Long id) {
        logger.info("Fetching discount with ID: {}", id);
        Optional<Discount> discount = discountService.getDiscountById(id);
        if (discount.isPresent()) {
            logger.debug("Found discount: {}", discount.get());
            return ResponseEntity.ok(discount.get());
        } else {
            logger.warn("Discount not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Get discount by code - ADMIN and USER
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/code/{code}")
    public ResponseEntity<Discount> getByCode(@PathVariable String code) {
        logger.info("Fetching discount with code: {}", code);
        Optional<Discount> discount = discountService.getDiscountByCode(code);
        if (discount.isPresent()) {
            logger.debug("Found discount: {}", discount.get());
            return ResponseEntity.ok(discount.get());
        } else {
            logger.warn("Discount not found with code: {}", code);
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Update discount - ADMIN only
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Discount> update(@PathVariable Long id, @RequestBody Discount discount) {
        logger.info("Updating discount with ID: {}", id);
        try {
            Discount updated = discountService.updateDiscount(id, discount);
            logger.debug("Updated discount: {}", updated);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException ex) {
            logger.error("Discount not found for update, ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Delete discount - ADMIN only
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("Deleting discount with ID: {}", id);
        discountService.deleteDiscount(id);
        logger.debug("Deleted discount with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
