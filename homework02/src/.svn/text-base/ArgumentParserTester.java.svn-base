import junit.framework.Assert;

import org.junit.Test;

/**
 * Tester for the ArgumentParser class. You may *NOT* make any
 * modifications to this file.
 *
 * @author Sophie Engle
 * @author CS 212 Software Development
 * @author University of San Francisco
 */
public class ArgumentParserTester {

	public static final String[] testCase =
			{"-a", "aaa", "-b", "bbb", "ccc", "-d", "-e", "eee", "-f"};

	@Test
	public void testValidFlag() {
		Assert.assertTrue(ArgumentParser.isFlag("-apple"));
	}

	@Test
	public void testInvalidFlag() {
		Assert.assertFalse(ArgumentParser.isFlag("apple"));
	}

	@Test
	public void testNoNameFlag() {
		Assert.assertFalse(ArgumentParser.isFlag("-"));
	}

	@Test
	public void testEmptyFlag() {
		Assert.assertFalse(ArgumentParser.isFlag(""));
	}

	@Test
	public void testNullFlag() {
		Assert.assertFalse(ArgumentParser.isFlag(null));
	}

	@Test
	public void testValidValue() {
		Assert.assertTrue(ArgumentParser.isValue("apple"));
	}

	@Test
	public void testInvalidValue() {
		Assert.assertFalse(ArgumentParser.isValue("-apple"));
	}

	@Test
	public void testEmptyValue() {
		Assert.assertFalse(ArgumentParser.isValue(""));
	}

	@Test
	public void testNullvalue() {
		Assert.assertFalse(ArgumentParser.isValue(null));
	}

	@Test
	public void testNullArgs() {
		ArgumentParser parser = new ArgumentParser(null);
		Assert.assertTrue(parser.numFlags() == 0);
	}

	@Test
	public void testEmptyArgs() {
		ArgumentParser parser = new ArgumentParser(new String[0]);
		Assert.assertTrue(parser.numFlags() == 0);
	}

	@Test
	public void testNoFlags() {
		ArgumentParser parser = new ArgumentParser(new String[] {"apple"});
		Assert.assertTrue(parser.numFlags() == 0);
	}

	@Test
	public void testSimpleFlags() {
		String[] args = new String[] {"-flag", "value"};
		ArgumentParser parser = new ArgumentParser(args);
		Assert.assertTrue(parser.numFlags() == 1);
	}

	@Test
	public void testSimpleFlagsValue() {
		String[] args = new String[] {"-flag", "value"};
		ArgumentParser parser = new ArgumentParser(args);
		Assert.assertEquals("value", parser.getValue("-flag"));
	}

	@Test
	public void testOnlyFlags() {
		String[] args = new String[] {"-flag"};
		ArgumentParser parser = new ArgumentParser(args);
		Assert.assertTrue(parser.numFlags() == 1);
	}

	@Test
	public void testOnlyFlagsValue() {
		String[] args = new String[] {"-flag"};
		ArgumentParser parser = new ArgumentParser(args);
		Assert.assertNull(parser.getValue("-flag"));
	}

	@Test
	public void testDuplicateFlags() {
		String[] args = new String[] {"-a", "b", "-a", "c"};
		ArgumentParser parser = new ArgumentParser(args);
		Assert.assertTrue(parser.numFlags() == 1);
	}

	@Test
	public void testDuplicateFlagsValue() {
		String[] args = new String[] {"-a", "b", "-a", "c"};
		ArgumentParser parser = new ArgumentParser(args);
		Assert.assertEquals("c", parser.getValue("-a"));
	}

	@Test
	public void testLostValue() {
		String[] args = new String[] {"-a", "apple", "banana"};
		ArgumentParser parser = new ArgumentParser(args);
		Assert.assertEquals("apple", parser.getValue("-a"));
	}

	@Test
	public void testComplexNumFlags() {
		ArgumentParser parser = new ArgumentParser(testCase);
		Assert.assertTrue(parser.numFlags() == 5);
	}

	@Test
	public void testComplexA() {
		ArgumentParser parser = new ArgumentParser(testCase);
		Assert.assertEquals("aaa", parser.getValue("-a"));
	}

	@Test
	public void testComplexB() {
		ArgumentParser parser = new ArgumentParser(testCase);
		Assert.assertEquals("bbb", parser.getValue("-b"));
	}

	@Test
	public void testComplexC() {
		ArgumentParser parser = new ArgumentParser(testCase);
		Assert.assertFalse(parser.hasFlag("ccc"));
	}

	@Test
	public void testComplexD() {
		ArgumentParser parser = new ArgumentParser(testCase);
		Assert.assertFalse(parser.hasValue("-d"));
	}

	@Test
	public void testComplexE() {
		ArgumentParser parser = new ArgumentParser(testCase);
		Assert.assertEquals("eee", parser.getValue("-e"));
	}

	@Test
	public void testComplexF() {
		ArgumentParser parser = new ArgumentParser(testCase);
		Assert.assertTrue(parser.hasFlag("-f"));
	}
}