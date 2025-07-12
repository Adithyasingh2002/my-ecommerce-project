package com.adi.ecomerce.service.impl;

import com.adi.ecomerce.dao.PaymentRepository;
import com.adi.ecomerce.entities.Payment;
import com.adi.ecomerce.service.PaymentService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment savePayment(Payment payment) {
        logger.info("Saving new payment for order ID: {}", payment.getOrder().getId());
        Payment saved = paymentRepository.save(payment);
        logger.debug("Payment saved: {}", saved);
        return saved;
    }

    @Override
    public List<Payment> getAllPayments() {
        logger.info("Fetching all payments");
        List<Payment> payments = paymentRepository.findAll();
        logger.debug("Total payments found: {}", payments.size());
        return payments;
    }

    @Override
    public Optional<Payment> getPaymentById(Long id) {
        logger.info("Fetching payment by ID: {}", id);
        Optional<Payment> payment = paymentRepository.findById(id);
        if (payment.isPresent()) {
            logger.debug("Payment found: {}", payment.get());
        } else {
            logger.warn("Payment not found with ID: {}", id);
        }
        return payment;
    }

    @Override
    public void deletePayment(Long id) {
        logger.info("Deleting payment with ID: {}", id);
        if (!paymentRepository.existsById(id)) {
            logger.error("Payment not found for deletion, ID: {}", id);
            throw new EntityNotFoundException("Payment not found with ID: " + id);
        }
        paymentRepository.deleteById(id);
        logger.debug("Payment deleted with ID: {}", id);
    }
}
