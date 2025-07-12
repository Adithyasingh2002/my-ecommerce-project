package com.adi.ecomerce.controller;

import com.adi.ecomerce.entities.Payment;
import com.adi.ecomerce.service.PaymentService;
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
@RequestMapping("/api/payments")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentService paymentService;

    //  Create new payment (allowed for USER & ADMIN)
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {
        logger.info("Creating new payment: {}", payment);
        Payment saved = paymentService.savePayment(payment);
        logger.debug("Payment saved: {}", saved);
        return ResponseEntity.ok(saved);
    }

    //  Get all payments (ADMIN only)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Payment>> getAllPayments() {
        logger.info("Fetching all payments");
        List<Payment> payments = paymentService.getAllPayments();
        logger.debug("Total payments found: {}", payments.size());
        return ResponseEntity.ok(payments);
    }

    //  Get payment by ID (allowed for USER & ADMIN)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        logger.info("Fetching payment with ID: {}", id);
        Optional<Payment> payment = paymentService.getPaymentById(id);
        if (payment.isPresent()) {
            logger.debug("Payment found: {}", payment.get());
            return ResponseEntity.ok(payment.get());
        } else {
            logger.warn("Payment not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    //  Delete payment (ADMIN only)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        logger.info("Deleting payment with ID: {}", id);
        try {
            paymentService.deletePayment(id);
            logger.debug("Payment deleted with ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            logger.error("Payment not found for deletion, ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
}
