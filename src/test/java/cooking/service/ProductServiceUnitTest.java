package cooking.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cooking.entity.Product;
import cooking.repository.ProductMapper;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
@DisplayName("ProductServiceの単体テスト")
class ProductServiceUnitTest {

	@Mock
	private ProductMapper productMapper;

	@InjectMocks
	private ProductService productService;

	@Test
	@DisplayName("全件取得の場合")
	public void testFindAll() {
		// arrange
		List<Product> productList = makeProductList();
		Product product1 = productList.get(0);
		Product product2 = productList.get(1);
		Product product3 = productList.get(2);

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
	@DisplayName("2件登録の場合")
	public void testSave() {
		// arrange
		List<Product> productList = makeProductList();
		Product product1 = productList.get(0);
		Product product2 = productList.get(1);

		// act
		productService.save(product1);
		System.out.println("***saveの実行結果1***");

		ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
		System.out.println("productMapper.insertProductInfo(product1)が呼ばれました");

		productService.save(product2);
		verify(productMapper, times(2)).insertProductInfo(productCaptor.capture());
		System.out.println("productMapper.insertProductInfo(product2)が呼ばれました");

		// assert
		productList = productCaptor.getAllValues();
		assertEquals(2, productList.size());

		Product expected1 = new Product();
		expected1.setProductID(0);
		expected1.setProductName("テスト商品1");
		expected1.setGenre("1");
		expected1.setMaker("メーカー1");
		expected1.setSellingPrice(new BigDecimal(9800));
		expected1.setDeleteFlg("0");
		expected1.setInsertDate(new Timestamp(System.currentTimeMillis()));
		expected1.setUpdateDate(new Timestamp(System.currentTimeMillis()));
		assertEquals(expected1.getProductID(), productList.get(0).getProductID());
		assertEquals(expected1.getProductName(), productList.get(0).getProductName());
		assertEquals(expected1.getGenre(), productList.get(0).getGenre());
		assertEquals(expected1.getMaker(), productList.get(0).getMaker());
		assertEquals(expected1.getSellingPrice(), productList.get(0).getSellingPrice());

		Product expected2 = new Product();
		expected2.setProductID(1);
		expected2.setProductName("テスト商品2");
		expected2.setGenre("2");
		expected2.setMaker("メーカー2");
		expected2.setSellingPrice(new BigDecimal(4800));
		expected2.setDeleteFlg("0");
		expected2.setInsertDate(new Timestamp(System.currentTimeMillis()));
		expected2.setUpdateDate(new Timestamp(System.currentTimeMillis()));
		assertEquals(expected2.getProductID(), productList.get(1).getProductID());
		assertEquals(expected2.getProductName(), productList.get(1).getProductName());
		assertEquals(expected2.getGenre(), productList.get(1).getGenre());
		assertEquals(expected2.getMaker(), productList.get(1).getMaker());
		assertEquals(expected2.getSellingPrice(), productList.get(1).getSellingPrice());

	}

	@Test
	@DisplayName("1件情報取得の場合")
	public void testFindOne() {
		// arrange
		List<Product> productList = makeProductList();
		Product product1 = productList.get(0);

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

	@Test
	@DisplayName("1件更新の場合")
	public void testUpdate() {
		// arrange
		List<Product> productList = makeProductList();
		Product product1 = productList.get(0);

		when(productMapper.updateProductInfo(product1)).thenReturn(1);

		// act
		product1.setProductName("テスト商品1更新");
		product1.setGenre("4");
		product1.setMaker("メーカー更新");
		product1.setSellingPrice(new BigDecimal(999999));
		int actualCount = productService.update(product1);
		System.out.println("***updateの実行結果***");

		ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
		System.out.println("productMapper.updateProductInfo(product1)が呼ばれました");
		verify(productMapper, times(1)).updateProductInfo(productCaptor.capture());

		// assert
		productList = productCaptor.getAllValues();
		assertEquals(1, productList.size());

		Product expected = new Product();
		expected.setProductID(0);
		expected.setProductName("テスト商品1更新");
		expected.setGenre("4");
		expected.setMaker("メーカー更新");
		expected.setSellingPrice(new BigDecimal(999999));
		assertEquals(expected.getProductID(), productList.get(0).getProductID());
		assertEquals(expected.getProductName(), productList.get(0).getProductName());
		assertEquals(expected.getGenre(), productList.get(0).getGenre());
		assertEquals(expected.getMaker(), productList.get(0).getMaker());
		assertEquals(expected.getSellingPrice(), productList.get(0).getSellingPrice());

		// assert
		assertEquals(1, actualCount);
	}

	@Test
	@DisplayName("1件削除の場合")
	public void testDelete() {
		// arrange
		List<Product> productList = makeProductList();
		Product product1 = productList.get(0);

		when(productMapper.deleteProductInfo(product1)).thenReturn(1);

		// act
		int actualCount = productService.delete(product1);

		// assert
		assertEquals(1, actualCount);
	}

	public List<Product> makeProductList() {
		List<Product> productList = new ArrayList<Product>();
		Product product1 = new Product();
		product1.setProductID(0);
		product1.setProductName("テスト商品1");
		product1.setGenre("1");
		product1.setMaker("メーカー1");
		product1.setSellingPrice(new BigDecimal(9800));
		product1.setDeleteFlg("0");
		product1.setInsertDate(new Timestamp(System.currentTimeMillis()));
		product1.setUpdateDate(new Timestamp(System.currentTimeMillis()));

		Product product2 = new Product();
		product2.setProductID(1);
		product2.setProductName("テスト商品2");
		product2.setGenre("2");
		product2.setMaker("メーカー2");
		product2.setSellingPrice(new BigDecimal(4800));
		product2.setDeleteFlg("0");
		product2.setInsertDate(new Timestamp(System.currentTimeMillis()));
		product2.setUpdateDate(new Timestamp(System.currentTimeMillis()));

		Product product3 = new Product();
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

		return productList;
	}
}
