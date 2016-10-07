package com.service.pricing.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.service.pricing.cache.ProductPriceCache;
import com.service.pricing.common.ServiceConstant;
import com.service.pricing.dao.PriceDao;
import com.service.pricing.dto.ProductPriceDTO;
import com.service.pricing.exception.PricingDaoException;

@Component
public class PriceServiceImpl implements PriceService {

	@Autowired
	private Environment env;

	@Autowired
	private PriceDao dao;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ProductPriceCache<Integer, ProductPriceDTO> cache;

	@Override
	public Set<ProductPriceDTO> fetchPrice(Map<String, String> criteria) {
		String uri = env.getProperty("product.catalogue.service.url");
		Set<ProductPriceDTO> productPrice = new HashSet<ProductPriceDTO>();
		ResponseEntity<Set<ProductPriceDTO>> response = restTemplate.exchange(
				getURI(uri, criteria), HttpMethod.GET, null,
				new ParameterizedTypeReference<Set<ProductPriceDTO>>() {
				}, criteria.get("productType"), criteria.get("productName"));
		Set<ProductPriceDTO> products = response.getBody();
		for (ProductPriceDTO product : products) {
			ProductPriceDTO productPriceDTO = cache.getValue(product.getProductId());
			productPriceDTO.setProductName(product.getProductName());
			productPrice.add(cache.getValue(product.getProductId()));
		}
		return productPrice;
	}

	private String getURI(String uri, Map<String, String> criteria) {
		if (CollectionUtils.isEmpty(criteria)) {
			return uri;
		} else {
			return uri + "?productType={productType}&productName={productName}";
		}
	}

	@Override
	public String addProduct(List<ProductPriceDTO> products)
			throws PricingDaoException {
		String statusMessage = ServiceConstant.FAIL;
		if (isValidateRequest(products)) {
			statusMessage = dao.addProducts(products);
		}
		if (ServiceConstant.CREATED.equalsIgnoreCase(statusMessage)) {
			for (ProductPriceDTO product : products) {
				cache.put(product.getProductId(), product);
			}
		}
		return statusMessage;

	}

	private boolean isValidateRequest(List<ProductPriceDTO> products) {
		boolean isValidProduct = true;
		ResponseEntity<Set<ProductPriceDTO>> response = restTemplate.exchange(
				env.getProperty("product.catalogue.service.url"),
				HttpMethod.GET, null,
				new ParameterizedTypeReference<Set<ProductPriceDTO>>() {
				});
		for (ProductPriceDTO product : products) {
			if (response.getBody().contains(product)) {
				isValidProduct = false;
				break;
			}
		}
		return isValidProduct;
	}

}
