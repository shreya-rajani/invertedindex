import org.junit.Assert;
import org.junit.Test;

public class HTMLCleanerExtraTest {

	@Test
	public void testEntities1() {
		String test = "asjhd&jh;jk";
		String expected = "asjhdjk";
		String actual = HTMLCleaner.stripEntities(test);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testEntities2() {
		String test = "3010&#8211;80ab";
		String expected = "301080ab";
		String actual = HTMLCleaner.stripEntities(test);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testEntities3() {
		String test = "2010&#x2013;2011";
		String expected = "20102011";
		String actual = HTMLCleaner.stripEntities(test);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testTags1() {
		String test = "<b>hello</b> world!";
		String expected = "hello world!";
		String actual = HTMLCleaner.stripTags(test);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testTags2() {
		String test = "<b>Hi\n</b> How are you?";
		String expected = "Hi\n How are you?";
		String actual = HTMLCleaner.stripTags(test);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testTags3() {
		String test = "<a \n name=args>arguments</a>";
		String expected = "arguments";
		String actual = HTMLCleaner.stripTags(test);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testElements1() {
		String test = "<style type=\"text/css\">body { font-color: black; }</style>";
		String expected = "";
		String actual = HTMLCleaner.stripElement("style", test);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testElements2() {
		String test = "<style type=\"text/css\">\nbody { font-color: red; }\n</style>";
		String expected = "";
		String actual = HTMLCleaner.stripElement("style", test);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testElements3() {
		String test = "<style \n type=\"text/css\">\nbody { font-color: green; }\n</style>";
		String expected = "";
		String actual = HTMLCleaner.stripElement("style", test);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testElements4() {
		String test = "a<html>b</html>c<html>d</html>e";
		String expected = "ace";
		String actual = HTMLCleaner.stripElement("html", test);

		Assert.assertEquals(expected, actual);
	}
}