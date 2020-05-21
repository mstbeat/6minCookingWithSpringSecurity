package cooking.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cooking.entity.Product;
import cooking.repository.ProductMapper;

@Service
public class ProductService {

	@Autowired
	ProductMapper productMapper;

	@Transactional
	public List<Product> findAll() {
		return productMapper.getProductInfoList();
	}

	@Transactional
	public void save(Product product) {
		productMapper.createProduct(product);
	}

	@Transactional
	public Product findOne(int productID) {
		return productMapper.findById(productID);
	}

	@Transactional
	public int update(Product product) {
		Integer count = productMapper.updateProduct(product);
		return count;
	}

	@Transactional
	public int delete(int productID, Timestamp updateDate) {
		Integer count = productMapper.deleteProduct(productID, updateDate);
		return count;
	}
}
