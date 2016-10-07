package com.service.pricing.loader;

import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.service.pricing.cache.ProductPrice;
import com.service.pricing.dao.PriceDao;
import com.service.pricing.dto.ProductPriceDTO;
@Component
public class PriceCacheLoader {
	@Autowired
	private ProductPrice productPrice;
	@Autowired
	private PriceDao dao;

	@PostConstruct
	public void init() {

		Set<ProductPriceDTO> products = dao.findProduct();
		if (products != null) {
			productPrice.putProductInPriceCache(products);
		}
	}
}
