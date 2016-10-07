package com.service.pricing.cache;

import java.util.Set;


public interface ProductPriceCache<K, V> {
	public V getValue(K key);

	public void put(K key, V value);

	public void remove(K key);

	Set<Integer> getExistingKeys();

}
