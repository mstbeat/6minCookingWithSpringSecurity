package cooking.entity;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

@RunWith(SpringRunner.class)
@SpringBootTest
class ProductUnitTest {

	@Autowired
	private Validator validator;
	
	private Product product = new Product();
	private BindingResult bindingResult = new BindException(product, "Product");
	
	@Before
	public void before() {
		product.setProductID(1);
	}
	
	@Test
	public void noError() {
		product.setProductName("テスト商品");
		product.setMaker("パナソニック");
		BigDecimal price = new BigDecimal(49800);
		product.setSellingPrice(price);
		validator.validate(product,  bindingResult);
		assertThat(bindingResult.getFieldError(), is(nullValue()));
	}
	
	@Test
	public void productNameIsNull() {
		product.setMaker("パナソニック");
		BigDecimal price = new BigDecimal(49800);
		product.setSellingPrice(price);
		validator.validate(product,  bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("productName"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("{0}の入力は必須です。"));
	}
	
	@Test
	public void productNameIsOver25() {
		product.setProductName("12345678901234567890123456");
		product.setMaker("パナソニック");
		BigDecimal price = new BigDecimal(49800);
		product.setSellingPrice(price);
		validator.validate(product,  bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("productName"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("{0}は{1}桁以下の範囲で入力してください。"));
	}
	
	@Test
	public void makerIsNull() {
		product.setProductName("テスト商品");
		BigDecimal price = new BigDecimal(49800);
		product.setSellingPrice(price);
		validator.validate(product,  bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("maker"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("{0}の入力は必須です。"));
	}
	
	@Test
	public void makerIsOver20() {
		product.setProductName("テスト商品");
		product.setMaker("123456789012345678901");
		BigDecimal price = new BigDecimal(49800);
		product.setSellingPrice(price);
		validator.validate(product,  bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("maker"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("{0}は{1}桁以下の範囲で入力してください。"));
	}
	
	@Test
	public void sellingPriceIsNull() {
		product.setProductName("テスト商品");
		product.setMaker("パナソニック");
		validator.validate(product,  bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("sellingPrice"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("{0}の入力は必須です。"));
	}
	
	@Test
	public void sellingPriceIsLessThan1() {
		product.setProductName("テスト商品");
		product.setMaker("パナソニック");
		BigDecimal price = new BigDecimal(0);
		product.setSellingPrice(price);
		validator.validate(product,  bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("sellingPrice"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("{0}は{1}以上の範囲で入力してください。"));
	}
	
	@Test
	public void sellingPriceIsMoreThan99999999() {
		product.setProductName("テスト商品");
		product.setMaker("パナソニック");
		BigDecimal price = new BigDecimal(100000000);
		product.setSellingPrice(price);
		validator.validate(product,  bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("sellingPrice"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("{0}は{1}以下の範囲で入力してください。"));
	}
	
	@Test
	public void sellingPriceIsNotDecimal() {
		product.setProductName("テスト商品");
		product.setMaker("パナソニック");
		BigDecimal price = new BigDecimal("aaaa");
		product.setSellingPrice(price);
		validator.validate(product,  bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("sellingPrice"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("{0}は半角数字以外入力できません。"));
	}
	
	@Test
	public void productDetailIsMoreThan200() {
		product.setProductName("テスト商品");
		product.setMaker("パナソニック");
		BigDecimal price = new BigDecimal(49800);
		product.setSellingPrice(price);
		product.setProductDetail("1234567890123456789012345678901234567890"
				+ "1234567890123456789012345678901234567890"
				+ "1234567890123456789012345678901234567890"
				+ "1234567890123456789012345678901234567890"
				+ "12345678901234567890123456789012345678901");
		validator.validate(product,  bindingResult);
		assertThat(bindingResult.getFieldError().getField(), is("productDetail"));
		assertThat(bindingResult.getFieldError().getDefaultMessage(), is("{0}は{1}桁以下の範囲で入力してください。"));

	}
}
