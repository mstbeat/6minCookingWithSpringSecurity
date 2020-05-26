package cooking.app;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import cooking.entity.Product;
import cooking.repository.ProductMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("ProductControllerの単体テスト")
class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Mock
	ProductMapper mapper;
	
	@InjectMocks
	private ProductListController controller;
	
	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	@DisplayName("商品情報登録画面の表示")
	public void getRegistrationTest() throws Exception {
		mockMvc.perform(get("/product-registration"))
		.andExpect(status().isOk())
		.andExpect(view().name("ProductRegistration"))
		.andReturn();
	}
	
	@Test
	@DisplayName("商品情報登録のエラー")
	public void getRegistrationErrors() throws Exception {
		Path path = Paths.get("/Users/masato/Desktop/no_image.png");
		String name ="no_image.png";
		String originalFileName="no_image.png";
		String contentType = "image/png";
		byte[] content = null;
		try {
			content = Files.readAllBytes(path);
		} catch (final IOException e) {
			
		}
		MockMultipartFile result = new MockMultipartFile(name, originalFileName, contentType, content);
//		MockMultipartFile multipartFile = new MockMultipartFile("file", file.getBytes());
		BigDecimal price = new BigDecimal(49800);
		Product product = new Product();
		product.setProductName("テスト商品");
		product.setMaker("パナソニック");
		product.setSellingPrice(price);
		mockMvc.perform((post("/product-registration")).flashAttr("product", product).flashAttr("multipartFile", result))
			.andExpect(model().hasNoErrors());
		
	}
	
	@Test
	@DisplayName("商品情報件数の確認")
	public void getIndexTest() throws Exception {
		List<Product> list = mapper.getProductInfoList();
		Product product1 = new Product();
		Product product2 = new Product();
		list.add(product1);
		list.add(product2);
		assertEquals(2, list.size());
	}
	
	@Test
	@DisplayName("商品情報登録のバリデーション")
	public void getRegistrationFormTest() throws Exception{
		
	}

}
