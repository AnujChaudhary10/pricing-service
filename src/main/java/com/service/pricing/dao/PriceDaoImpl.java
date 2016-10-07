package com.service.pricing.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.service.pricing.common.ServiceConstant;
import com.service.pricing.dto.ProductPriceDTO;
import com.service.pricing.exception.PricingDaoException;

@Component
public class PriceDaoImpl implements PriceDao {

	private final static String SELECT_QUERY = "SELECT * FROM products_price";
	private final static String INSERT_QUERY = " INSERT INTO products_price (product_id, product_price)  VALUES(?,?)";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public Set<ProductPriceDTO> findProduct() {
		Set<ProductPriceDTO> result = jdbcTemplate.query(SELECT_QUERY,
				new ProductPriceExtractor());
		return result;
	}

	private static final class ProductPriceExtractor implements
			ResultSetExtractor<Set<ProductPriceDTO>> {

		@Override
		public Set<ProductPriceDTO> extractData(ResultSet rs)
				throws SQLException, DataAccessException {
			Set<ProductPriceDTO> products = new LinkedHashSet<ProductPriceDTO>();
			while (rs.next()) {
				ProductPriceDTO product = new ProductPriceDTO();
				product.setProductId(rs.getInt("product_id"));
				product.setPrice(rs.getDouble("product_price"));
				products.add(product);
			}
			return products;
		}
	}

	@Override
	public String addProducts(List<ProductPriceDTO> products)
			throws PricingDaoException {
		String statusOfBulkUpload = ServiceConstant.FAIL;
		List<Object[]> batchParamList = new ArrayList<Object[]>();
		try {
			for (ProductPriceDTO product : products) {
				Object[] batchParam = new Object[] { product.getProductId(),
						product.getPrice() };
				batchParamList.add(batchParam);
			}

			int[] rowAffected = jdbcTemplate.batchUpdate(INSERT_QUERY,
					batchParamList);
			if (rowAffected.length > 0) {
				statusOfBulkUpload = ServiceConstant.CREATED;
			}
		} catch (Exception ex) {
			if (ex instanceof DuplicateKeyException) {
				throw new PricingDaoException(
						"Any one product id already exist", ex);
			}
		}
		return statusOfBulkUpload;

	}

}
