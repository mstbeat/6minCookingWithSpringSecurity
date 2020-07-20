package cooking.app;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.ModelAndView;

import cooking.entity.Product;
import cooking.service.ProductService;

@RunWith(SpringRunner.class)
@SpringJUnitConfig
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("ProductRegistrationControllerの単体テスト")
class ProductRegistrationControllerTest {

	private MockMvc mockMvc;

	@MockBean
	ProductService productService;

	@Autowired
	private ProductRegistrationController controller;

	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	@DisplayName("商品情報登録画面の表示")
	public void getRegistrationTest() throws Exception {
		// act
		mockMvc.perform(get("/product-registration"))
				.andExpect(status().isOk())
				.andExpect(view().name("productregistration"))
				.andReturn();
	}

	@Test
	@DisplayName("エラーなしの場合")
	public void testRegistrationReturnsNoErrors() throws Exception {
		// arrange
		Product product = new Product();
		product.setGenre("1");
		product.setProductName("テスト商品");
		product.setMaker("パナソニック");
		product.setSellingPrice(new BigDecimal(49800));
		product.setMultipartFile(fileSetUp());

		// act
		mockMvc.perform((post("/product-registration"))
				.flashAttr("product", product))
				.andExpect(model().hasNoErrors())
				.andExpect(view().name("redirect:/product-list"));

		// assert
		verify(productService, times(1)).save(product);

	}

	@Test
	@DisplayName("商品名が空の場合")
	public void testProductNameEmptyReturnsError() throws Exception {
		// arrange
		Product product = new Product();
		product.setGenre("1");
		product.setProductName("");
		product.setMaker("パナソニック");
		product.setSellingPrice(new BigDecimal(49800));
		product.setMultipartFile(fileSetUp());

		// act
		mockMvc.perform((post("/product-registration"))
				.flashAttr("product", product))
				.andExpect(model().hasErrors())
				.andExpect(model().errorCount(1))
				.andExpect(model().attributeHasFieldErrors("product", "productName"))
				.andExpect(view().name("productregistration"));

		// assert
		verify(productService, times(0)).save(product);
	}

	@Test
	@DisplayName("商品名が26桁以上の場合")
	public void testProductNameOver25ReturnsError() throws Exception {
		// arrange
		Product product = new Product();
		product.setGenre("1");
		product.setProductName("12345678901234567890123456");
		product.setMaker("パナソニック");
		product.setSellingPrice(new BigDecimal(49800));
		product.setMultipartFile(fileSetUp());

		// act
		mockMvc.perform((post("/product-registration"))
				.flashAttr("product", product))
				.andExpect(model().hasErrors())
				.andExpect(model().errorCount(1))
				.andExpect(model().attributeHasFieldErrors("product", "productName"))
				.andExpect(model().attributeHasFieldErrorCode("product", "productName", "Size"))
				.andExpect(view().name("productregistration"));

		// assert
		verify(productService, times(0)).save(product);
	}

	@Test
	@DisplayName("メーカーが空の場合")
	public void testMakerEmptyReturnsError() throws Exception {
		// arrange
		Product product = new Product();
		product.setGenre("1");
		product.setProductName("テスト商品");
		product.setMaker("");
		product.setSellingPrice(new BigDecimal(49800));
		product.setMultipartFile(fileSetUp());

		// act
		mockMvc.perform((post("/product-registration"))
				.flashAttr("product", product))
				.andExpect(model().hasErrors())
				.andExpect(model().errorCount(1))
				.andExpect(model().attributeHasFieldErrors("product", "maker"))
				.andExpect(view().name("productregistration"));

		// assert
		verify(productService, times(0)).save(product);
	}

	@Test
	@DisplayName("メーカーが21桁以上の場合")
	public void testMakerOver20ReturnsError() throws Exception {
		// arrange
		Product product = new Product();
		product.setGenre("1");
		product.setProductName("テスト商品");
		product.setMaker("123456789012345678901");
		product.setSellingPrice(new BigDecimal(49800));
		product.setMultipartFile(fileSetUp());

		// act
		mockMvc.perform((post("/product-registration"))
				.flashAttr("product", product))
				.andExpect(model().hasErrors())
				.andExpect(model().errorCount(1))
				.andExpect(model().attributeHasFieldErrors("product", "maker"))
				.andExpect(view().name("productregistration"));

		// assert
		verify(productService, times(0)).save(product);
	}

	@Test
	@DisplayName("販売価格が空の場合")
	public void testSellingPriceEmptyReturnsError() throws Exception {
		// arrange
		Product product = new Product();
		product.setGenre("1");
		product.setProductName("テスト商品");
		product.setMaker("パナソニック");
		product.setSellingPrice(null);
		product.setMultipartFile(fileSetUp());

		// act
		mockMvc.perform((post("/product-registration"))
				.flashAttr("product", product))
				.andExpect(model().hasErrors())
				.andExpect(model().errorCount(1))
				.andExpect(model().attributeHasFieldErrors("product", "sellingPrice"))
				.andExpect(view().name("productregistration"));

		// assert
		verify(productService, times(0)).save(product);
	}

	@Test
	@DisplayName("販売価格が100000000以上の場合")
	public void testSellingPriceOver99999999ReturnsError() throws Exception {
		// arrange
		Product product = new Product();
		product.setGenre("1");
		product.setProductName("テスト商品");
		product.setMaker("パナソニック");
		product.setSellingPrice(new BigDecimal(100000000));
		product.setMultipartFile(fileSetUp());

		// act
		mockMvc.perform((post("/product-registration"))
				.flashAttr("product", product))
				.andExpect(model().hasErrors())
				.andExpect(model().errorCount(1))
				.andExpect(model().attributeHasFieldErrors("product", "sellingPrice"))
				.andExpect(view().name("productregistration"));

		// assert
		verify(productService, times(0)).save(product);
	}

	@Test
	@DisplayName("販売価格が数字でない場合")
	public void testSellingPriceNotDigitReturnsError() throws Exception {
		// arrange
		Product product = new Product();
		product.setGenre("1");
		product.setProductName("テスト商品");
		product.setMaker("パナソニック");
		product.setMultipartFile(fileSetUp());

		// act
		MvcResult mvcResult = mockMvc.perform((post("/product-registration").param("sellingPrice", "aaa"))
				.flashAttr("product", product))
				.andExpect(model().hasErrors())
				.andExpect(model().errorCount(1))
				.andExpect(model().attributeHasFieldErrors("product", "sellingPrice"))
				.andExpect(model().attributeHasFieldErrorCode("product", "sellingPrice", "typeMismatch"))
				.andExpect(view().name("productregistration"))
				.andReturn();

		// assert
		verify(productService, times(0)).save(product);

		ModelAndView mav = mvcResult.getModelAndView();
		Map<String, Object> model = mav.getModel();
		Object productObject = model.get("product");
		assertThat(productObject, is(notNullValue()));
		assertThat(productObject, is(instanceOf(Product.class)));
		Product productModel = (Product) productObject;
		assertThat(productModel.getSellingPrice(), is(nullValue()));

		Object messageObject = mav.getModel().get("org.springframework.validation.BindingResult.product");
		assertThat(messageObject, is(not(nullValue())));
		assertThat(messageObject, is(instanceOf(BindingResult.class)));
		BindingResult bindingResult = (BindingResult) messageObject;

		List<FieldError> list = bindingResult.getFieldErrors("sellingPrice");
		assertThat(list, is(not(nullValue())));
		assertThat(list.size(), is(1));

		FieldError fieldError = list.get(0);
		assertThat(fieldError.getCode(), is("typeMismatch"));

		Object[] args = fieldError.getArguments();
		assertThat(args.length, is(1));
		assertThat(args[0], is(instanceOf(DefaultMessageSourceResolvable.class)));
		DefaultMessageSourceResolvable dmr = (DefaultMessageSourceResolvable) args[0];
		assertThat(dmr.getCode(), is("sellingPrice"));
	}

	@Test
	@DisplayName("商品説明が201桁以上の場合")
	public void testProductDetailOver200ReturnsError() throws Exception {
		// arrange
		Product product = new Product();
		product.setGenre("1");
		product.setProductName("テスト商品");
		product.setMaker("パナソニック");
		product.setSellingPrice(new BigDecimal(49800));
		product.setMultipartFile(fileSetUp());
		product.setProductDetail("1234567890123456789012345678901234567890"
				+ "1234567890123456789012345678901234567890"
				+ "1234567890123456789012345678901234567890"
				+ "1234567890123456789012345678901234567890"
				+ "12345678901234567890123456789012345678901");

		// act
		mockMvc.perform((post("/product-registration"))
				.flashAttr("product", product))
				.andExpect(model().hasErrors())
				.andExpect(model().errorCount(1))
				.andExpect(model().attributeHasFieldErrors("product", "productDetail"))
				.andExpect(view().name("productregistration"));

		// assert
		verify(productService, times(0)).save(product);
	}

	@Test
	@DisplayName("商品画像が512KB以上の場合")
	public void testMultipartFileOver512KBReturnsError() throws Exception {
		// arrange
		FileInputStream inputFile = new FileInputStream("/Users/masato/Desktop/images/512KB.jpg");
		MockMultipartFile file = new MockMultipartFile("file", "512KB.jpg", "image/png", inputFile);
		Product product = new Product();
		product.setGenre("1");
		product.setProductName("テスト商品");
		product.setMaker("パナソニック");
		product.setSellingPrice(new BigDecimal(49800));
		product.setMultipartFile(file);

		// act
		mockMvc.perform((post("/product-registration"))
				.flashAttr("product", product))
				.andExpect(model().hasErrors())
				.andExpect(model().errorCount(1))
				.andExpect(model().attributeHasFieldErrors("product", "multipartFile"))
				.andExpect(view().name("productregistration"));

		// assert
		verify(productService, times(0)).save(product);
	}

	@Test
	@DisplayName("商品画像がJPG/PNG以外の場合")
	public void testMultipartFileNotJpgOrPngReturnsError() throws Exception {
		// arrange
		FileInputStream inputFile = new FileInputStream("/Users/masato/Desktop/images/sample.gif");
		MockMultipartFile file = new MockMultipartFile("file", "sample.gif", "image/gif", inputFile);
		Product product = new Product();
		product.setGenre("1");
		product.setProductName("テスト商品");
		product.setMaker("パナソニック");
		product.setSellingPrice(new BigDecimal(49800));
		product.setMultipartFile(file);

		// act
		mockMvc.perform((post("/product-registration"))
				.flashAttr("product", product))
				.andExpect(model().hasErrors())
				.andExpect(model().errorCount(1))
				.andExpect(model().attributeHasFieldErrors("product", "multipartFile"))
				.andExpect(view().name("productregistration"));

		// assert
		verify(productService, times(0)).save(product);
	}

	@Test
	@DisplayName("商品画像ファイル名が15桁以上の場合")
	public void testMultipartFileOver15ReturnsError() throws Exception {
		// arrange
		FileInputStream inputFile = new FileInputStream("/Users/masato/Desktop/images/morethan15__.jpg");
		MockMultipartFile file = new MockMultipartFile("file", "morethan15__.jpg", "image/png", inputFile);
		Product product = new Product();
		product.setGenre("1");
		product.setProductName("テスト商品");
		product.setMaker("パナソニック");
		product.setSellingPrice(new BigDecimal(49800));
		product.setMultipartFile(file);

		// act
		mockMvc.perform((post("/product-registration"))
				.flashAttr("product", product))
				.andExpect(model().hasErrors())
				.andExpect(model().errorCount(1))
				.andExpect(model().attributeHasFieldErrors("product", "multipartFile"))
				.andExpect(view().name("productregistration"));

		// assert
		verify(productService, times(0)).save(product);
	}

	public MockMultipartFile fileSetUp() throws IOException {
		FileInputStream inputFile = new FileInputStream("src/main/resources/static/images/no_image.png");
		MockMultipartFile file = new MockMultipartFile("file", "no_image.png", "image/png", inputFile);
		return file;
	}
}
