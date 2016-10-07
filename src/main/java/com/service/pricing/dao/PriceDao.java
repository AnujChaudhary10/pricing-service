package com.service.pricing.dao;

import java.util.List;
import java.util.Set;

import com.service.pricing.dto.ProductPriceDTO;
import com.service.pricing.exception.PricingDaoException;

public interface PriceDao {

	Set<ProductPriceDTO> findProduct();

	String addProducts(List<ProductPriceDTO> product) throws PricingDaoException;

}
