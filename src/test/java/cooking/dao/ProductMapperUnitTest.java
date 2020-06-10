package cooking.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import cooking.entity.Product;
import cooking.repository.ProductMapper;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductMapperの単体テスト")
class ProductMapperUnitTest {

	@Autowired
	private ProductMapper productMapper;
	
	@Test
	@DisplayName("product全件取得で0件の場合のテスト")
	void testFindAllReturnEmptyList() {
		try {
			String productName = "テスト商品";
			String maker = "ソニー";
			BigDecimal sellingPrice = new BigDecimal(9800);
			Timestamp insertDate = new Timestamp(System.currentTimeMillis());
			Timestamp updateDate = new Timestamp(System.currentTimeMillis());
			
			Product product = prepare(0, "1", productName, maker, sellingPrice, insertDate, updateDate);
			insert(product);
			Integer productID = product.getProductID();
			Product registeredProduct = searchOf(productID);
			validate(productName, maker, sellingPrice, registeredProduct);
		} catch (Exception e) {
		}
		
	}
	
	Product prepare(Integer productID, String genre, String productName, String maker, BigDecimal sellingPrice, Timestamp insertDate, Timestamp updateDate) {
		Product product = new Product();
		product.setProductID(productID);
		product.setGenre(genre);
		product.setProductName(productName);
		product.setMaker(maker);
		product.setSellingPrice(sellingPrice);
		product.setInsertDate(insertDate);
		product.setUpdateDate(updateDate);
		return product;
	}
	
	void insert(Product product) {
		productMapper.insertProductInfo(product);
	}
	
	Product searchOf(Integer productID) {
		return productMapper.getProductInfo(productID);
	}
	
	void deleteDataOf(Product product) {
		productMapper.deleteProductInfo(product);
	}
	
	void validate(String expectedProductName, String expectedMaker, BigDecimal expectedSellingPrice, Product actual) {
		assertNotNull(actual);
		Product expected = new Product();
		expected.setProductName(expectedProductName);
		expected.setMaker(expectedMaker);
		expected.setSellingPrice(expectedSellingPrice);
		assertEquals(expected, actual);
		System.out.println(expected);
		System.out.println(actual);
	}

}
