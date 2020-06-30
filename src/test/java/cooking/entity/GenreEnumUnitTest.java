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
	@DisplayName("GetValueByKey(0)の成功")
	void GenreGetValueByKeyTest_0() {
		String genre = GenreEnum.getValueByKey("0");
		assertEquals("指定なし", genre);
		
	}
	
	@Test
	@DisplayName("GetValueByKey(1)の成功")
	void GenreGetValueByKeyTest_1() {
		String genre = GenreEnum.getValueByKey("1");
		assertEquals("家電", genre);
		
	}

	@Test
	@DisplayName("GetValueByKey(7)の成功")
	void GenreGetValueByKeyFailTest() {
//		IllegalArgumentException thrown = assertThrows(
//				IllegalArgumentException.class,
//				() -> GenreEnum.getValueByKey("7"));
		String genre = GenreEnum.getValueByKey("7");
		assertEquals(null, genre);
	}
	
}
