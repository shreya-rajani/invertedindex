import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    WordCounterTester.NormalizeWordTester.class,
    WordCounterTester.CountWordsStringTester.class,
    WordCounterTester.CountWordsFileTester.class
})
public class WordCounterTester {

	/**
	 * These tests focus on your {@link WordCounter.normalizeWord(String)}
	 * method. These should be the first tests you focus on passing. These
	 * tests also assume you do NOT throw any exceptions in your code.
	 */
	public static class NormalizeWordTester {

		@Test
		public void testNormalizeNull() {
			String input = null;
			String expected = null;
			String actual = WordCounter.normalizeWord(input);
			Assert.assertEquals(expected, actual);
		}

		@Test
		public void testNormalizeEmpty() {
			String input = "";
			String expected = "";
			String actual = WordCounter.normalizeWord(input);
			Assert.assertEquals(expected, actual);
		}

		@Test
		public void testNormalizeSpecial() {
			String input = "+";
			String expected = "";
			String actual = WordCounter.normalizeWord(input);
			Assert.assertEquals(expected, actual);
		}

		@Test
		public void testNormalizeUnderscore() {
			String input = "_";
			String expected = "";
			String actual = WordCounter.normalizeWord(input);
			Assert.assertEquals(expected, actual);
		}

		@Test
		public void testNormalizeDigits() {
			String input = "42";
			String expected = "42";
			String actual = WordCounter.normalizeWord(input);
			Assert.assertEquals(expected, actual);
		}

		@Test
		public void testNormalizeUppercase() {
			String input = "ELEPHANT";
			String expected = "elephant";
			String actual = WordCounter.normalizeWord(input);
			Assert.assertEquals(expected, actual);
		}

		@Test
		public void testNormalizeLowercase() {
			String input = "canary";
			String expected = "canary";
			String actual = WordCounter.normalizeWord(input);
			Assert.assertEquals(expected, actual);
		}

		@Test
		public void testNormalizeWhitespace() {
			String input = "\t";
			String expected = "";
			String actual = WordCounter.normalizeWord(input);
			Assert.assertEquals(expected, actual);
		}

		@Test
		public void testNormalizeComplex() {
			String input = "  aL-pA_Ca\t";
			String expected = "alpaca";
			String actual = WordCounter.normalizeWord(input);
			Assert.assertEquals(expected, actual);
		}
	}

	/**
	 * These tests focus on WordCounter.countWords(String), to make sure you
	 * are splitting text and skipping empty or null words properly. These are
	 * the second set of tests you should try to pass with your code. Again,
	 * no exceptions should be thrown by your code.
	 */
	public static class CountWordsStringTester {
		@Test
		public void testCountNull() {
			String input = null;
			int expected = 0;
			int actual = WordCounter.countWords(input);
			Assert.assertEquals(expected, actual);
		}

		@Test
		public void testCountEmpty() {
			String input = "";
			int expected = 0;
			int actual = WordCounter.countWords(input);
			Assert.assertEquals(expected, actual);
		}

		@Test
		public void testCountWhitespace() {
			String input = "\t\r\n";
			int expected = 0;
			int actual = WordCounter.countWords(input);
			Assert.assertEquals(expected, actual);
		}

		@Test
		public void testCountSpecial() {
			String input = "+ @ #";
			int expected = 0;
			int actual = WordCounter.countWords(input);
			Assert.assertEquals(expected, actual);
		}

		@Test
		public void testCountDigits() {
			String input = "1 2 3";
			int expected = 3;
			int actual = WordCounter.countWords(input);
			Assert.assertEquals(expected, actual);
		}

		@Test
		public void testCountSimple() {
			String input = "a b c d e f g";
			int expected = 7;
			int actual = WordCounter.countWords(input);
			Assert.assertEquals(expected, actual);
		}

		@Test
		public void testCountComplex1() {
			String input = "llama, LLAMA 114M4, l-l-a-m-a!";
			int expected = 4;
			int actual = WordCounter.countWords(input);
			Assert.assertEquals(expected, actual);
		}

		@Test
		public void testCountComplex2() {
			String input = " Time FLIES... like an ArR0w.\n Fruit FLIES... like a BANANA!!11!!1\n\n";
			int expected = 10;
			int actual = WordCounter.countWords(input);
			Assert.assertEquals(expected, actual);
		}
	}

	/**
	 * These are the last set of tests you should try to pass. They test your
	 * count on actual files. Since some of these files are large, it will be
	 * difficult to debug if you have not yet passed the previous tests. Also
	 * expects no exceptions to be thrown by your code.
	 *
	 * Make sure you have downloaded the proper test files into your root
	 * project directory before running these tests.
	 */
	public static class CountWordsFileTester {
		@Test
		public void testFileNull() {
			Path input = null;
			int expected = 0;
			int actual = WordCounter.countWords(input);
			Assert.assertEquals(expected, actual);
		}

		@Test
		public void testFileDirectory() {
			Path input = Paths.get("");
			int expected = 0;
			int actual = WordCounter.countWords(input);
			Assert.assertEquals(expected, actual);
		}

		@Test
		public void testFileNowhere() {
			Path input = Paths.get("nowhere.txt");

			try {
				Files.deleteIfExists(input);
			} catch (Exception e) {
				Assert.fail("Unable to remove nowhere.txt for testing.");
			}

			int expected = 0;
			int actual = WordCounter.countWords(input);
			Assert.assertEquals(expected, actual);
		}

		@Test
		public void testFileEmpty() {
			Path input = Paths.get("empty.txt");

			try {
				Files.deleteIfExists(input);
				Files.createFile(input);
			} catch (Exception e) {
				Assert.fail("Unable to create empty.txt for testing.");
			}

			int expected = 0;
			int actual = WordCounter.countWords(input);
			Assert.assertEquals(expected, actual);
		}

		@Test
		public void testFilePosition() {
			Path input = Paths.get("position.txt");

			if (!Files.isReadable(input)) {
				Assert.fail("Unable to find file position.txt for testing.");
			}

			int expected = 20;
			int actual = WordCounter.countWords(input);
			Assert.assertEquals(expected, actual);
		}

		@Test
		public void testFileRFC() {
			Path input = Paths.get("rfc6841.txt");

			if (!Files.isReadable(input)) {
				Assert.fail("Unable to find file rfc6841.txt for testing.");
			}

			int expected = 7557;
			int actual = WordCounter.countWords(input);
			Assert.assertEquals(expected, actual);
		}
	}
}