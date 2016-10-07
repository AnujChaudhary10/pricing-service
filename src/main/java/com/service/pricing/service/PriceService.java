package com.service.pricing.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.service.pricing.dto.ProductPriceDTO;
import com.service.pricing.exception.PricingDaoException;


public interface PriceService {

	Set<ProductPriceDTO> fetchPrice(Map<String,String> criteria);

	String addProduct(List<ProductPriceDTO> product) throws PricingDaoException;

}
