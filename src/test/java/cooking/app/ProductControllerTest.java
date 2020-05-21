package cooking.app;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.FileInputStream;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
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
		FileInputStream fis = new FileInputStream("/Users/masato/Desktop/24KB.png");
		MockMultipartFile file = new MockMultipartFile("multipartFile",fis);
		mockMvc.perform(MockMvcRequestBuilders.multipart("/product-registration")
				.file(file)
				.param("productName", "テスト商品")
				.param("maker", "パナソニック")
				.param("sellingPrice", "49800"))
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
