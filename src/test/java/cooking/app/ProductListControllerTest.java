package cooking.app;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import cooking.entity.Product;
import cooking.service.ProductService;

@RunWith(SpringRunner.class)
@SpringJUnitConfig
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("ProductListControllerの単体テスト")
class ProductListControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	ProductService productService;

	@Autowired
	private ProductListController controller;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	@DisplayName("商品情報一覧画面の表示")
	public void getRegistrationTest() throws Exception {

		// act
		mockMvc.perform(get("/product-list"))
				.andExpect(status().isOk())
				.andExpect(view().name("productlist"))
				.andReturn();

		// assert
		verify(productService, times(1)).findAll();
	}

	@Test
	@DisplayName("商品情報0件の場合の確認")
	public void getIndexTestReturnZero() throws Exception {
		// arrange
		List<Product> list = new ArrayList<Product>();

		// act
		when(productService.findAll()).thenReturn(list);

		MvcResult result = mockMvc.perform(get("/product-list"))
				.andExpect(status().isOk())
				.andExpect(view().name("productlist"))
				.andReturn();

		@SuppressWarnings("unchecked")
		List<Product> actualList = (List<Product>) result.getModelAndView().getModel().get("listProducts");

		// assert
		verify(productService, times(1)).findAll();
		assertEquals(0, actualList.size());
	}

	@Test
	@DisplayName("商品情報件数の確認")
	public void getIndexTestCount() throws Exception {
		// arrange
		List<Product> list = new ArrayList<Product>();
		Product product1 = new Product();
		Product product2 = new Product();
		Product product3 = new Product();
		list.add(product1);
		list.add(product2);
		list.add(product3);

		// act
		when(productService.findAll()).thenReturn(list);

		MvcResult result = mockMvc.perform(get("/product-list"))
				.andExpect(status().isOk())
				.andExpect(view().name("productlist"))
				.andReturn();

		@SuppressWarnings("unchecked")
		List<Product> actualList = (List<Product>) result.getModelAndView().getModel().get("listProducts");

		// assert
		verify(productService, times(1)).findAll();
		assertEquals(3, actualList.size());
	}

}
