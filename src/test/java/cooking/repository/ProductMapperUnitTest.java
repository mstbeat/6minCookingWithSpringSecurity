package cooking.repository;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.test.context.junit4.SpringRunner;

import cooking.entity.Product;

@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("ProductMapperの単体テスト")
class ProductMapperUnitTest {

	@Autowired
	private ProductMapper productMapper;

	@Autowired
	private NamedParameterJdbcOperations jdbcOperations;

	@Test
	@DisplayName("商品情報一覧取得のテスト")
	public void testGetProductInfoList() {
		// arrange
		String sql = "SELECT ProductID,Genre,Maker,ProductName,SellingPrice,ProductImg "
				+ "FROM ProductInfo WHERE DeleteFlg = '0' ORDER BY ProductID ASC";
		List<Product> actualList = jdbcOperations.query(sql, new BeanPropertyRowMapper<>(Product.class));
		Product actualProduct = actualList.get(actualList.size() - 1);

		// act
		List<Product> expectedList = productMapper.getProductInfoList();
		Product expectedProduct = expectedList.get(expectedList.size() - 1);

		// assert
		assertEquals(expectedList.size(), actualList.size());
		validate(expectedProduct, actualProduct);
	}

	@Test
	@DisplayName("商品情報登録のテスト")
	public void testInsertProductInfo() {
		// arrange
		Product product1 = prepare("1", "テスト商品", "ソニー", new BigDecimal(9800), "テスト商品の商品詳細",
				new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
		insert(product1);

		List<Product> actualList = getProductList();
		Product expectedProduct = actualList.get(actualList.size() - 1);

		// act
		Product actualProduct = getProduct(expectedProduct);

		// assert
		validate(expectedProduct, actualProduct);
	}

	@Test
	@DisplayName("商品情報取得のテスト")
	public void testGetProductInfo() {
		// arrange
		Product product1 = prepare("1", "テスト商品", "ソニー", new BigDecimal(9800), "テスト商品の商品詳細",
				new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));

		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		String sql = "INSERT INTO ProductInfo (ProductName, Genre, Maker, SellingPrice, ProductDetail, ProductImg, InsertDate, UpdateDate"
				+ ") VALUES (:productName, :genre, :maker, :sellingPrice, :productDetail, :productImg,"
				+ "CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP())";
		jdbcOperations.update(sql,
				new BeanPropertySqlParameterSource(product1), keyHolder);

		// act        
		Product actualProduct = productMapper.getProductInfo(keyHolder.getKey().intValue());

		// assert
		assertEquals(product1.getGenre(), actualProduct.getGenre());
		assertEquals(product1.getProductName(), actualProduct.getProductName());
		assertEquals(product1.getMaker(), actualProduct.getMaker());
		assertEquals(product1.getSellingPrice(), actualProduct.getSellingPrice());
		assertEquals(product1.getProductDetail(), actualProduct.getProductDetail());
	}

	@Test
	@DisplayName("商品情報更新のテスト")
	public void testUpdateProductInfo() {
		// arrange
		Product product1 = prepare("1", "テスト商品", "ソニー", new BigDecimal(9800), "テスト商品の商品詳細",
				new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
		insert(product1);
		List<Product> actualList = getProductList();
		Product expectedProduct = actualList.get(actualList.size() - 1);

		expectedProduct.setProductName("テスト商品更新");
		expectedProduct.setMaker("ソニー更新");
		expectedProduct.setSellingPrice(new BigDecimal(10000));
		expectedProduct.setProductDetail("更新済み");

		// act
		productMapper.updateProductInfo(expectedProduct);
		Product actualProduct = getProduct(expectedProduct);

		// assert
		validate(expectedProduct, actualProduct);

	}

	@Test
	@DisplayName("商品情報削除のテスト")
	public void testDeleteProductInfo() {
		// arrange
		Product product1 = prepare("1", "MBテスト商品", "ソニー", new BigDecimal(9800), "テスト商品の商品詳細",
				new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
		insert(product1);
		List<Product> list = getProductList();
		Product registeredProduct = list.get(list.size() - 1);

		Product product2 = prepare("1", "2MBテスト商品2", "2ソニー2", new BigDecimal(10), "2テスト商品の商品詳細2",
				new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
		insert(product2);
		List<Product> list2 = getProductList();
		Product registeredProduct2 = list2.get(list2.size() - 1);

		// act
		Product deleteProduct = productMapper.getProductInfo(registeredProduct.getProductID());

		int count = productMapper.deleteProductInfo(deleteProduct);
		Product deletedProduct = getProduct(registeredProduct);
		Product notDeletedProduct = getProduct(registeredProduct2);
		List<Product> newList = getProductList();

		// assert
		assertEquals(1, count);
		assertEquals("1", deletedProduct.getDeleteFlg());
		assertEquals("0", notDeletedProduct.getDeleteFlg());
		assertEquals(list2.size() - 1, newList.size());
	}

	Product prepare(String genre, String productName, String maker, BigDecimal sellingPrice, String productDetail,
			Timestamp insertDate, Timestamp updateDate) {
		Product product = new Product();
		product.setGenre(genre);
		product.setProductName(productName);
		product.setMaker(maker);
		product.setSellingPrice(sellingPrice);
		product.setProductDetail(productDetail);
		product.setInsertDate(insertDate);
		product.setUpdateDate(updateDate);
		return product;
	}

	void insert(Product product) {
		productMapper.insertProductInfo(product);
	}

	List<Product> getProductList() {
		String sql = "SELECT * FROM ProductInfo WHERE DeleteFlg = '0' ORDER BY ProductID ASC";
		return jdbcOperations.query(sql, new BeanPropertyRowMapper<>(Product.class));
	}

	Product getProduct(Product product) {
		return jdbcOperations.queryForObject("SELECT * FROM ProductInfo WHERE ProductID = :id",
				new MapSqlParameterSource("id", product.getProductID()),
				new BeanPropertyRowMapper<>(Product.class));
	}

	void validate(Product expectedProduct, Product actualProduct) {
		assertEquals(expectedProduct.getProductID(), actualProduct.getProductID());
		assertEquals(expectedProduct.getGenre(), actualProduct.getGenre());
		assertEquals(expectedProduct.getProductName(), actualProduct.getProductName());
		assertEquals(expectedProduct.getMaker(), actualProduct.getMaker());
		assertEquals(expectedProduct.getSellingPrice(), actualProduct.getSellingPrice());
		assertEquals(expectedProduct.getProductDetail(), actualProduct.getProductDetail());
		assertEquals(expectedProduct.getInsertDate(), actualProduct.getInsertDate());
	}

}
