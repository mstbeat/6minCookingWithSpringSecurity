package cooking.entity;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Rule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

@RunWith(SpringRunner.class)
@SpringBootTest
@DisplayName("Productエンティティの単体テスト")
class ProductUnitTest {

	@Autowired
	private Validator validator;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private Product product = new Product();
	private BindingResult bindingResult = new BindException(product, "Product");

	@Test
	@DisplayName("エラーなしの場合")
	public void noError() throws IOException {
		// arrange
		product.setProductName("テスト商品");
		product.setMaker("パナソニック");
		product.setSellingPrice(new BigDecimal(49800));
		product.setMultipartFile(fileSetUp());

		// act
		validator.validate(product, bindingResult);

		// assert
		assertThat(bindingResult.getFieldError(), is(nullValue()));
	}

	@Test
	@DisplayName("商品名が空の場合")
	public void productNameIsNull() throws IOException {
		// arrange
		product.setMaker("パナソニック");
		product.setSellingPrice(new BigDecimal(49800));
		product.setMultipartFile(fileSetUp());

		// act
		validator.validate(product, bindingResult);

		// assert
		assertThat(bindingResult.getFieldError().getField(), is("productName"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("{0}の入力は必須です。"));
	}

	@Test
	@DisplayName("商品名が26桁以上の場合")
	public void productNameIsOver25() throws IOException {
		// arrange
		product.setProductName("12345678901234567890123456");
		product.setMaker("パナソニック");
		product.setSellingPrice(new BigDecimal(49800));
		product.setMultipartFile(fileSetUp());

		// act
		validator.validate(product, bindingResult);

		// assert
		assertThat(bindingResult.getFieldError().getField(), is("productName"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("{0}は{1}桁以下の範囲で入力してください。"));
	}

	@Test
	@DisplayName("メーカーが空の場合")
	public void makerIsNull() throws IOException {
		// arrange
		product.setProductName("テスト商品");
		product.setSellingPrice(new BigDecimal(49800));
		product.setMultipartFile(fileSetUp());

		// act
		validator.validate(product, bindingResult);

		// assert
		assertThat(bindingResult.getFieldError().getField(), is("maker"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("{0}の入力は必須です。"));
	}

	@Test
	@DisplayName("メーカーが21桁以上の場合")
	public void makerIsOver20() throws IOException {
		// arrange
		product.setProductName("テスト商品");
		product.setMaker("123456789012345678901");
		product.setSellingPrice(new BigDecimal(49800));
		product.setMultipartFile(fileSetUp());

		// act
		validator.validate(product, bindingResult);

		// assert
		assertThat(bindingResult.getFieldError().getField(), is("maker"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("{0}は{1}桁以下の範囲で入力してください。"));
	}

	@Test
	@DisplayName("販売価格が空の場合")
	public void sellingPriceIsNull() throws IOException {
		// arrange
		product.setProductName("テスト商品");
		product.setMaker("パナソニック");
		product.setMultipartFile(fileSetUp());

		// act
		validator.validate(product, bindingResult);

		// assert
		assertThat(bindingResult.getFieldError().getField(), is("sellingPrice"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("{0}の入力は必須です。"));
	}

	@Test
	@DisplayName("販売価格が0の場合")
	public void sellingPriceIsLessThan1() throws IOException {
		// arrange
		product.setProductName("テスト商品");
		product.setMaker("パナソニック");
		product.setSellingPrice(new BigDecimal(0));
		product.setMultipartFile(fileSetUp());

		// act
		validator.validate(product, bindingResult);

		// assert
		assertThat(bindingResult.getFieldError().getField(), is("sellingPrice"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("{0}は{1}以上の範囲で入力してください。"));
	}

	@Test
	@DisplayName("販売価格が100000000の場合")
	public void sellingPriceIsMoreThan99999999() throws IOException {
		// arrange
		product.setProductName("テスト商品");
		product.setMaker("パナソニック");
		product.setSellingPrice(new BigDecimal(100000000));
		product.setMultipartFile(fileSetUp());

		// act
		validator.validate(product, bindingResult);

		// assert
		assertThat(bindingResult.getFieldError().getField(), is("sellingPrice"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("{0}は{1}以下の範囲で入力してください。"));
	}

	@Test
	@DisplayName("商品説明が201桁以上の場合")
	public void productDetailIsMoreThan200() throws IOException {
		// arrange
		product.setProductName("テスト商品");
		product.setMaker("パナソニック");
		product.setSellingPrice(new BigDecimal(49800));
		product.setProductDetail("1234567890123456789012345678901234567890"
				+ "1234567890123456789012345678901234567890"
				+ "1234567890123456789012345678901234567890"
				+ "1234567890123456789012345678901234567890"
				+ "12345678901234567890123456789012345678901");
		product.setMultipartFile(fileSetUp());

		// act
		validator.validate(product, bindingResult);

		// assert
		assertThat(bindingResult.getFieldError().getField(), is("productDetail"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("{0}は{1}桁以下の範囲で入力してください。"));

	}

	public MockMultipartFile fileSetUp() throws IOException {
		FileInputStream inputFile = new FileInputStream("src/main/resources/static/images/no_image.png");
		MockMultipartFile file = new MockMultipartFile("file", "no_image.png", "image/png", inputFile);
		return file;
	}
}
