package com.adi.ecomerce.service.impl;

import com.adi.ecomerce.dao.DiscountRepository;
import com.adi.ecomerce.entities.Discount;
import com.adi.ecomerce.service.DiscountService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DiscountServiceImpl implements DiscountService {

    private static final Logger logger = LoggerFactory.getLogger(DiscountServiceImpl.class);

    @Autowired
    private DiscountRepository discountRepository;

    @Override
    public Discount createDiscount(Discount discount) {
        logger.info("Creating new discount: {}", discount);
        Discount saved = discountRepository.save(discount);
        logger.debug("Discount created: {}", saved);
        return saved;
    }

    @Override
    public List<Discount> getAllDiscounts() {
        logger.info("Fetching all discounts");
        List<Discount> discounts = discountRepository.findAll();
        logger.debug("Total discounts found: {}", discounts.size());
        return discounts;
    }

    @Override
    public Optional<Discount> getDiscountById(Long id) {
        logger.info("Fetching discount by ID: {}", id);
        Optional<Discount> discount = discountRepository.findById(id);
        if (discount.isPresent()) {
            logger.debug("Discount found: {}", discount.get());
        } else {
            logger.warn("Discount not found with ID: {}", id);
        }
        return discount;
    }

    @Override
    public Optional<Discount> getDiscountByCode(String code) {
        logger.info("Fetching discount by code: {}", code);
        Optional<Discount> discount = discountRepository.findByCode(code);
        if (discount.isPresent()) {
            logger.debug("Discount found: {}", discount.get());
        } else {
            logger.warn("Discount not found with code: {}", code);
        }
        return discount;
    }

    @Override
    public Discount updateDiscount(Long id, Discount updated) {
        logger.info("Updating discount with ID: {}", id);
        Discount existing = discountRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Discount not found with ID: {}", id);
                    return new EntityNotFoundException("Discount not found with ID: " + id);
                });

        existing.setCode(updated.getCode());
        existing.setAmount(updated.getAmount());
        existing.setType(updated.getType());
        existing.setStartDate(updated.getStartDate());
        existing.setEndDate(updated.getEndDate());
        existing.setActive(updated.isActive());

        Discount saved = discountRepository.save(existing);
        logger.debug("Discount updated: {}", saved);
        return saved;
    }

    @Override
    public void deleteDiscount(Long id) {
        logger.info("Deleting discount with ID: {}", id);
        discountRepository.deleteById(id);
        logger.debug("Discount deleted with ID: {}", id);
    }
}
