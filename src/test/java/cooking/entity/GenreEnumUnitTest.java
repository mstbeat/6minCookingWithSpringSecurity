package cooking.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

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
	@DisplayName("GetKeyByValueの成功")
	void GenreGetKeyByValueTest() {
		String genre = GenreEnum.getValueByKey("0");
		assertEquals(genre, "指定なし");
		
	}

	@Test
	@DisplayName("GetKeyByValueの失敗")
	void GenreGetKeyByValueFailTest() {
		IllegalArgumentException thrown = assertThrows(
				IllegalArgumentException.class,
				() -> GenreEnum.getValueByKey("6"));
		assertEquals("6", thrown.getMessage());
	}
	
}
