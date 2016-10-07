package com.service.pricing.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.service.pricing.common.ServiceConstant;
import com.service.pricing.dto.ProductPriceDTO;
import com.service.pricing.exception.PricingDaoException;
import com.service.pricing.service.PriceService;

@RestController
@RequestMapping(value = "/price")
public class PriceServiceController {
	@Autowired
	private PriceService pricing;
	
	@RequestMapping(value = "/search", method = RequestMethod.GET, produces = "application/json")
	public Set<ProductPriceDTO> getPriceOfAllProduct(@RequestParam Map<String,String> requestCriteria){
		return pricing.fetchPrice(requestCriteria);
	}
	
	@RequestMapping(value = "/products", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<String> addProduct(@RequestBody List<ProductPriceDTO> product){
		String message = ServiceConstant.FAIL;
		try{
			message = pricing.addProduct(product);
		}catch(PricingDaoException ex){
			message = ex.getMessage();
		}
		if(message.equalsIgnoreCase(ServiceConstant.CREATED)){
			return new ResponseEntity<String>(message,HttpStatus.CREATED);
		}
		return new ResponseEntity<String>(message,HttpStatus.BAD_REQUEST);
	}
	

}
