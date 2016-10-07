package com.service.pricing.cache;

import java.util.HashSet;
import java.util.Set;

import net.sf.ehcache.Ehcache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.stereotype.Component;

import com.service.pricing.dto.ProductPriceDTO;
@Component
public class ProductPrice implements ProductPriceCache<Integer, ProductPriceDTO> {

	@Autowired
	private Cache productPriceCache;
	
	@Override
	public ProductPriceDTO getValue(Integer key) {
		ValueWrapper val = productPriceCache.get(key);
		return val != null && val.get() != null ? (ProductPriceDTO)val.get() : null;
	}

	@Override
	public void put(Integer key, ProductPriceDTO value) {
		productPriceCache.put(key, value);
	}

	@Override
	public void remove(Integer key) {
		productPriceCache.evict(key);
	}
	@Override
	public Set<Integer> getExistingKeys() {
		 return new HashSet<Integer>(((Ehcache) productPriceCache.getNativeCache()).getKeys());
	}

	public void putProductInPriceCache(Set<ProductPriceDTO> products) {
		if (products != null) {
			for (ProductPriceDTO product : products) {
				productPriceCache.put(product.getProductId(), product);
			}
		}
	}

}
