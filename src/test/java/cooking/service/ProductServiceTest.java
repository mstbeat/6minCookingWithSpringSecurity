package cooking.service;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import cooking.entity.Product;
import cooking.repository.ProductMapper;

@SpringJUnitConfig
@SpringBootTest
@DisplayName("ProductServiceの結合テスト")
class ProductServiceTest {

	@Autowired
	ProductMapper productMapper;

	@Test
	@DisplayName("全件検索のテスト")
	void testFindAllCount() {
		assertNotNull(productMapper.getProductInfoList());
	}

	@Test
	@DisplayName("商品が取得できた場合のテスト")
	void testFindOneReturnOne() {
		Product actualProduct = productMapper.getProductInfo(3);
		assertEquals("パナソニック", actualProduct.getMaker());
		assertEquals(new BigDecimal(49800), actualProduct.getSellingPrice());
	}

}
