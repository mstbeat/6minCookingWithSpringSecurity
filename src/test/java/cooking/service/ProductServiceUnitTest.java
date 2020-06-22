package cooking.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cooking.entity.Product;
import cooking.repository.ProductMapper;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class ProductServiceUnitTest {

	@Mock
	private ProductMapper productMapper;
	
	@InjectMocks
	private ProductService productService;

//	@Before
//	public void setup() {
//		MockitoAnnotations.initMocks(this);
//	}
	
	@SuppressWarnings("null")
	@Test
	public void testFindAll() {
		// arrange
		List<Product> productList = new ArrayList<Product>();
		Product product1 = null;
		product1.setProductID(0);
		product1.setProductName("テスト商品1");
		product1.setGenre("1");
		product1.setMaker("メーカー1");
		product1.setSellingPrice(new BigDecimal(9800));
		product1.setDeleteFlg("0");
		product1.setInsertDate(new Timestamp(System.currentTimeMillis()));
		product1.setUpdateDate(new Timestamp(System.currentTimeMillis()));
		
		Product product2 = null;
		product2.setProductID(1);
		product2.setProductName("テスト商品2");
		product2.setGenre("2");
		product2.setMaker("メーカー2");
		product2.setSellingPrice(new BigDecimal(4800));
		product2.setDeleteFlg("0");
		product2.setInsertDate(new Timestamp(System.currentTimeMillis()));
		product2.setUpdateDate(new Timestamp(System.currentTimeMillis()));
		
		Product product3 = null;
		product3.setProductID(2);
		product3.setProductName("テスト商品3");
		product3.setGenre("3");
		product3.setMaker("メーカー3");
		product3.setSellingPrice(new BigDecimal(3000));
		product3.setDeleteFlg("0");
		product3.setInsertDate(new Timestamp(System.currentTimeMillis()));
		product3.setUpdateDate(new Timestamp(System.currentTimeMillis()));
		
		productList.add(product1);
		productList.add(product2);
		productList.add(product3);
		
		when(productMapper.getProductInfoList()).thenReturn(productList);
		
		// act
		List<Product> actualList = productService.findAll();
		
		// assert
		assertNotNull(actualList);
		assertEquals(3, actualList.size());
		
		Product actualProduct1 = actualList.get(0);
		assertNotNull(actualProduct1);
		assertEquals(product1.getProductName(), actualProduct1.getProductName());
		assertEquals(product1.getMaker(), actualProduct1.getMaker());
		assertEquals(product1.getGenre(), actualProduct1.getGenre());
		assertEquals(product1.getSellingPrice(), actualProduct1.getSellingPrice());
		
		Product actualProduct2 = actualList.get(1);
		assertNotNull(actualProduct2);
		assertEquals(product2.getProductName(), actualProduct2.getProductName());
		assertEquals(product2.getMaker(), actualProduct2.getMaker());
		assertEquals(product2.getGenre(), actualProduct2.getGenre());
		assertEquals(product2.getSellingPrice(), actualProduct2.getSellingPrice());
		
		Product actualProduct3 = actualList.get(2);
		assertNotNull(actualProduct3);
		assertEquals(product3.getProductName(), actualProduct3.getProductName());
		assertEquals(product3.getMaker(), actualProduct3.getMaker());
		assertEquals(product3.getGenre(), actualProduct3.getGenre());
		assertEquals(product3.getSellingPrice(), actualProduct3.getSellingPrice());
	}
	
	@Test
	public void testSave() {
		// arrange
		
		
		// act

		
		// assert
		
	}
	
	@SuppressWarnings("null")
	@Test
	public void testFindOne() {
		// arrange
		List<Product> productList = new ArrayList<Product>();
		Product product1 = null;
		product1.setProductID(0);
		product1.setProductName("テスト商品1");
		product1.setGenre("1");
		product1.setMaker("メーカー1");
		product1.setSellingPrice(new BigDecimal(9800));
		product1.setDeleteFlg("0");
		product1.setInsertDate(new Timestamp(System.currentTimeMillis()));
		product1.setUpdateDate(new Timestamp(System.currentTimeMillis()));
		
		Product product2 = null;
		product2.setProductID(1);
		product2.setProductName("テスト商品2");
		product2.setGenre("2");
		product2.setMaker("メーカー2");
		product2.setSellingPrice(new BigDecimal(4800));
		product2.setDeleteFlg("0");
		product2.setInsertDate(new Timestamp(System.currentTimeMillis()));
		product2.setUpdateDate(new Timestamp(System.currentTimeMillis()));
		
		Product product3 = null;
		product3.setProductID(2);
		product3.setProductName("テスト商品3");
		product3.setGenre("3");
		product3.setMaker("メーカー3");
		product3.setSellingPrice(new BigDecimal(3000));
		product3.setDeleteFlg("0");
		product3.setInsertDate(new Timestamp(System.currentTimeMillis()));
		product3.setUpdateDate(new Timestamp(System.currentTimeMillis()));
		
		productList.add(product1);
		productList.add(product2);
		productList.add(product3);
		
		when(productMapper.getProductInfo(0)).thenReturn(product1);

		// act
		Product actualProduct1 = productService.findOne(0);

		// assert
		assertNotNull(actualProduct1);
		assertEquals(product1.getProductName(), actualProduct1.getProductName());
		assertEquals(product1.getMaker(), actualProduct1.getMaker());
		assertEquals(product1.getGenre(), actualProduct1.getGenre());
		assertEquals(product1.getSellingPrice(), actualProduct1.getSellingPrice());
	}

	@SuppressWarnings("null")
	@Test
	public void testDelete() {
		// arrange
		List<Product> productList = new ArrayList<Product>();
		Product product1 = null;
		product1.setProductID(0);
		product1.setProductName("テスト商品1");
		product1.setGenre("1");
		product1.setMaker("メーカー1");
		product1.setSellingPrice(new BigDecimal(9800));
		product1.setDeleteFlg("0");
		product1.setInsertDate(new Timestamp(System.currentTimeMillis()));
		product1.setUpdateDate(new Timestamp(System.currentTimeMillis()));
		
		Product product2 = null;
		product2.setProductID(1);
		product2.setProductName("テスト商品2");
		product2.setGenre("2");
		product2.setMaker("メーカー2");
		product2.setSellingPrice(new BigDecimal(4800));
		product2.setDeleteFlg("0");
		product2.setInsertDate(new Timestamp(System.currentTimeMillis()));
		product2.setUpdateDate(new Timestamp(System.currentTimeMillis()));
		
		Product product3 = null;
		product3.setProductID(2);
		product3.setProductName("テスト商品3");
		product3.setGenre("3");
		product3.setMaker("メーカー3");
		product3.setSellingPrice(new BigDecimal(3000));
		product3.setDeleteFlg("0");
		product3.setInsertDate(new Timestamp(System.currentTimeMillis()));
		product3.setUpdateDate(new Timestamp(System.currentTimeMillis()));
		
		productList.add(product1);
		productList.add(product2);
		productList.add(product3);
		
		when(productMapper.deleteProductInfo(product1)).thenReturn(1);
		
		// act
		int actualCount = productService.delete(product1);
		
		// assert
		assertEquals(1, actualCount);
	}
}
