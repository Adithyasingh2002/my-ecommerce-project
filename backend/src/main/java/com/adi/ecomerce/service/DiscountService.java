package com.adi.ecomerce.service;

import com.adi.ecomerce.entities.Discount;
import java.util.List;
import java.util.Optional;

public interface DiscountService {
    Discount createDiscount(Discount discount);
    List<Discount> getAllDiscounts();
    Optional<Discount> getDiscountById(Long id);
    Optional<Discount> getDiscountByCode(String code);
    Discount updateDiscount(Long id, Discount discount);
    void deleteDiscount(Long id);
}
