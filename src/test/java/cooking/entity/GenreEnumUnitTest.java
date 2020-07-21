package cooking.entity;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;

import cooking.enums.GenreEnum;

@DisplayName("GenreEnumの単体テスト")
class GenreEnumUnitTest {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	@DisplayName("GetValueByKey(0)の場合")
	void GenreGetValueByKeyTest_0() {
		String genre = GenreEnum.getValueByKey("0");
		assertEquals("指定なし", genre);
	}

	@Test
	@DisplayName("GetValueByKey(1)の場合")
	void GenreGetValueByKeyTest_1() {
		String genre = GenreEnum.getValueByKey("1");
		assertEquals("家電", genre);
	}

	@Test
	@DisplayName("GetValueByKey(2)の場合")
	void GenreGetValueByKeyTest_2() {
		String genre = GenreEnum.getValueByKey("2");
		assertEquals("家具", genre);
	}

	@Test
	@DisplayName("GetValueByKey(3)の場合")
	void GenreGetValueByKeyTest_3() {
		String genre = GenreEnum.getValueByKey("3");
		assertEquals("食品", genre);
	}

	@Test
	@DisplayName("GetValueByKey(4)の場合")
	void GenreGetValueByKeyTest_4() {
		String genre = GenreEnum.getValueByKey("4");
		assertEquals("ファッション", genre);
	}

	@Test
	@DisplayName("GetValueByKey(5)の場合")
	void GenreGetValueByKeyTest_5() {
		String genre = GenreEnum.getValueByKey("5");
		assertEquals("書籍", genre);
	}

	@Test
	@DisplayName("GetValueByKey(6)の場合")
	void GenreGetValueByKeyFailTest_6() {
		String genre = GenreEnum.getValueByKey("6");
		assertEquals(null, genre);
	}

	@Test
	@DisplayName("GetValueByKey(7)の場合")
	void GenreGetValueByKeyFailTest_7() {
		String genre = GenreEnum.getValueByKey("7");
		assertEquals(null, genre);
	}

}
