package cooking.app;

import static org.hamcrest.CoreMatchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;

import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import cooking.entity.Product;
import cooking.service.ProductService;

@RunWith(SpringRunner.class)
@SpringJUnitConfig
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("ProductUpdateControllerの単体テスト")
class ProductUpdateControllerTest {

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
	@DisplayName("該当商品がある場合の商品情報更新画面の表示")
	public void testUpdateReturnProduct() throws Exception {
		// act
		when(productService.findOne(3)).thenReturn(getProductId3());

		mockMvc.perform(post("/product-update").param("productID", "3"))
				.andExpect(status().isOk())
				.andExpect(view().name("productupdate"))
				.andExpect(model().hasNoErrors())
				.andReturn();

		// assert
		verify(productService, times(1)).findOne(3);
	}

	@Test
	@DisplayName("該当商品がない場合の商品情報更新画面の表示")
	public void testUpdateReturnNull() throws Exception {
		// act
		when(productService.findOne(999)).thenReturn(null);

		mockMvc.perform(post("/product-update").param("productID", "999"))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/product-list"))
				.andExpect(view().name("redirect:/product-list"))
				.andExpect(model().hasNoErrors())
				.andExpect(flash().attributeExists("message"))
				.andExpect(flash().attribute("message", is("選択された情報は既に他ユーザーにより削除されています。")))
				.andReturn();

		// assert
		verify(productService, times(1)).findOne(999);
	}

	@Test
	@DisplayName("該当商品がある場合の商品情報更新")
	public void testUpdateProduct() throws Exception {
		// arrange
		Product product = getProductId3();
		product.setProductName("テスト商品更新");

		// act
		when(productService.update(any(Product.class))).thenReturn(1);

		mockMvc.perform(put("/product-update").flashAttr("product", product))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/product-list"))
				.andExpect(view().name("redirect:/product-list"))
				.andExpect(model().hasNoErrors())
				.andExpect(flash().attributeExists("message"))
				.andExpect(flash().attribute("message", is("商品情報を更新しました。")))
				.andReturn();

		// assert
		verify(productService, times(1)).findOne(anyInt());
		verify(productService, times(1)).update(any(Product.class));
	}

	@Test
	@DisplayName("更新時エラーがある場合の商品情報更新")
	public void testUpdateProductReturnsError() throws Exception {
		// arrange
		Product product = getProductId3();
		product.setProductImg(product.getMultipartFile().getBytes());
		product.setStringImg(product.getStringImg());
		product.setProductName("");

		// act
		when(productService.update(any(Product.class))).thenReturn(0);
		when(productService.findOne(anyInt())).thenReturn(product);

		mockMvc.perform(put("/product-update").flashAttr("product", product))
				.andExpect(status().isOk())
				.andExpect(view().name("productupdate"))
				.andExpect(model().hasErrors())
				.andReturn();

		// assert
		verify(productService, times(1)).findOne(anyInt());
		verify(productService, times(0)).update(any(Product.class));
	}

	@Test
	@DisplayName("画像更新がない場合の商品情報更新")
	public void testUpdateProductWithoutMultipartFile() throws Exception {
		// arrange
		Product product = getProductId3();
		product.setProductImg(product.getMultipartFile().getBytes());
		byte[] fileImage = new byte[0];
		MockMultipartFile file2 = new MockMultipartFile("file", "", "image/png", fileImage);
		product.setMultipartFile(file2);

		// act
		when(productService.update(any(Product.class))).thenReturn(1);
		when(productService.findOne(anyInt())).thenReturn(product);

		mockMvc.perform(put("/product-update").flashAttr("product", product))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/product-list"))
				.andExpect(view().name("redirect:/product-list"))
				.andExpect(model().hasNoErrors())
				.andExpect(flash().attributeExists("message"))
				.andExpect(flash().attribute("message", is("商品情報を更新しました。")))
				.andReturn();

		// assert
		verify(productService, times(1)).findOne(anyInt());
		verify(productService, times(1)).update(any(Product.class));
	}

	@Test
	@DisplayName("該当商品がない場合の商品情報更新")
	public void testUpdateNullReturnsError() throws Exception {
		// arrange
		Product product = getProductId3();
		product.setProductName("テスト商品更新");

		// act
		when(productService.update(any(Product.class))).thenReturn(0);

		mockMvc.perform(put("/product-update").flashAttr("product", product))
				.andExpect(status().isOk())
				.andExpect(view().name("productupdate"))
				.andExpect(model().hasNoErrors())
				.andExpect(model().attributeExists("message"))
				.andExpect(model().attribute("message", is("選択された情報は既に他ユーザーにより更新されています。")))
				.andReturn();

		// assert
		verify(productService, times(1)).findOne(anyInt());
		verify(productService, times(1)).update(any(Product.class));
	}

	@Test
	@DisplayName("該当商品がある場合の商品情報削除")
	public void testDeleteProduct() throws Exception {
		// arrange
		Product product = getProductId3();

		// act
		when(productService.delete(any(Product.class))).thenReturn(1);

		mockMvc.perform(delete("/product-delete").flashAttr("product", product))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/product-list"))
				.andExpect(view().name("redirect:/product-list"))
				.andExpect(model().hasNoErrors())
				.andExpect(flash().attributeExists("message"))
				.andExpect(flash().attribute("message", is("商品情報を削除しました。")))
				.andReturn();

		// assert
		verify(productService, times(1)).delete(any(Product.class));
	}

	@Test
	@DisplayName("該当商品がない場合の商品情報削除")
	public void testDeleteNullReturnsError() throws Exception {
		// arrange
		Product product = getProductId3();

		// act
		when(productService.delete(any(Product.class))).thenReturn(0);

		mockMvc.perform(delete("/product-delete").flashAttr("product", product))
				.andExpect(status().isOk())
				.andExpect(view().name("productupdate"))
				.andExpect(model().hasNoErrors())
				.andExpect(model().attributeExists("message"))
				.andExpect(model().attribute("message", is("選択された情報は既に他ユーザーにより削除されています。")))
				.andReturn();

		// assert
		verify(productService, times(1)).delete(any(Product.class));
	}

	public Product getProductId3() throws IOException {
		Product product = new Product();
		product.setProductID(0);
		product.setProductName("テスト商品1");
		product.setGenre("1");
		product.setMaker("メーカー1");
		product.setSellingPrice(new BigDecimal(9800));
		product.setMultipartFile(fileSetUp());
		product.setDeleteFlg("0");
		product.setInsertDate(new Timestamp(System.currentTimeMillis()));
		product.setUpdateDate(new Timestamp(System.currentTimeMillis()));

		return product;
	}

	public MockMultipartFile fileSetUp() throws IOException {
		FileInputStream inputFile = new FileInputStream("src/main/resources/static/images/no_image.png");
		MockMultipartFile file = new MockMultipartFile("file", "no_image.png", "image/png", inputFile);
		return file;
	}

}
