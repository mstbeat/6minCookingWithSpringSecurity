package cooking.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cooking.entity.Product;
import cooking.repository.ProductMapper;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductMapperの単体テスト")
class ProductMapperUnitTest {

	@Mock
	private ProductMapper product;
	
	@InjectMocks
	private ProductMapper productMapper;
	
	@Test
	@DisplayName("product全件取得で0件の場合のテスト")
	void testFindAllReturnEmptyList() {
		List<Product> actualList = productMapper.getProductInfoList();
		assertEquals(0, actualList.size());
	}

}
