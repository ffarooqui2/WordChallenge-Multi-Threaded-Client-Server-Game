import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class MyTest {

	@Test
	void constructorTest(){
		WordMasterLogic logic = new WordMasterLogic();
		assertEquals(6, logic.attemptCount, "Attempt count is wrong");
		System.out.println(logic.categoryData);
		System.out.println(logic.categoryWords);
	}

	@Test
	void fillCategoryTest() {
		WordMasterLogic logic = new WordMasterLogic();
		assertEquals(3, logic.categoryData.size());
		assertEquals(3, logic.categoryData.get("animals"));
		assertEquals(3, logic.categoryData.get("countries"));
		assertEquals(3, logic.categoryData.get("food"));

	}

	@Test
	void generateValidWordTest() {
		WordMasterLogic logic = new WordMasterLogic();

		String word = logic.generateWord("animals");
		String word1 = logic.generateWord("countries");
		String word2 = logic.generateWord("food");

		logic.fillCategory();
		assertTrue(logic.categoryWords.get("animals").contains(word), "word not in animals");
		assertTrue(logic.categoryWords.get("countries").contains(word1), "word not in countries");
		assertTrue(logic.categoryWords.get("food").contains(word2), "word not in food");
	}

	@Test
	void generateWordRemoveTest() {
		WordMasterLogic logic = new WordMasterLogic();
		String word = logic.generateWord("animals");

		assertFalse(logic.categoryWords.get("animals").contains(word), "word is in animals");

	}

	@Test
	void checkGuessCorrectTest() {
		WordMasterLogic logic = new WordMasterLogic();
		logic.word = "monkey";
		logic.currentLetters = new ArrayList<>(Arrays.asList('_', '_', '_', '_','_','_'));
		assertTrue(logic.checkGuess('k'));
		assertEquals(6, logic.attemptCount, "Wrong attempt count");
		assertArrayEquals(new List[]{Arrays.asList('_', '_', '_', 'k', '_', '_')}, new ArrayList[]{logic.currentLetters});
	}

	@Test
	void checkGuessIncorrectTest() {
		WordMasterLogic logic = new WordMasterLogic();
		logic.word = "monkey";
		logic.currentLetters = new ArrayList<>(Arrays.asList('_', '_', '_', '_', '_', '_'));
		assertFalse(logic.checkGuess('l'));
		assertEquals(5, logic.attemptCount, "Wrong attempt count");
		assertFalse(logic.checkGuess('l'));
		assertEquals(4, logic.attemptCount, "Wrong attempt count");
		assertArrayEquals(new List[]{Arrays.asList('_', '_', '_', '_', '_', '_')}, new ArrayList[]{logic.currentLetters});
	}

	@Test
	void checkGuessSixthAttemptTest() {
		WordMasterLogic logic = new WordMasterLogic();
		logic.chosenCategory = "animals";
		logic.word = "monkey";
		logic.currentLetters = new ArrayList<>(Arrays.asList('_', '_', '_', '_', '_', '_'));
		logic.attemptCount = 1;
		assertFalse(logic.checkGuess('l'));
		assertEquals(2, logic.categoryData.get(logic.chosenCategory), "Not updating category attempt");
	}

	@Test
	void checkGuessCategoryFailTest() {
		WordMasterLogic logic = new WordMasterLogic();
		logic.chosenCategory = "animals";
		logic.word = "monkey";
		logic.currentLetters = new ArrayList<>(Arrays.asList('_', '_', '_', '_'));
		logic.attemptCount = 1;
		logic.categoryData.put(logic.chosenCategory, 1);
		assertFalse(logic.checkGuess('l'));
		assertEquals(0, logic.categoryData.get(logic.chosenCategory), "Not updating category attempt");
		assertTrue(logic.gameEnded);
	}

	@Test
	void evaluateCorrectWordTest(){
		WordMasterLogic logic = new WordMasterLogic();
		logic.chosenCategory = "animals";
		logic.word = "monkey";
		logic.currentLetters = new ArrayList<>(Arrays.asList('m', 'o', 'n', 'k','e','y'));
		assertTrue(logic.evaluateWord(logic.chosenCategory));
		assertTrue(logic.cat1);
	}

	@Test
	void evaluateIncorrectWordTest(){
		WordMasterLogic logic = new WordMasterLogic();
		logic.chosenCategory = "animals";
		logic.word = "monkey";
		logic.currentLetters = new ArrayList<>(Arrays.asList('m', '_', 'n', 'k','e','y'));
		assertFalse(logic.evaluateWord(logic.chosenCategory));
		assertFalse(logic.cat1);
	}
}