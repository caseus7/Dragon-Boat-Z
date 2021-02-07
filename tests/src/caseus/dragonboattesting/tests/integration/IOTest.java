package caseus.dragonboattesting.tests.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;

import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.lang.reflect.Field;

import caseus.dragonboattesting.GdxTestRunner;

import com.dragonboat.game.*;

/**
 * --ASSESSMENT 2--
 * Integration test for the methods within the
 * IO class.
 */
public class IOTest {
	private String testsPath = "../../tests/src/caseus/dragonboattesting/";
	// Do not modify these files
	private String exampleSaveGamePath = testsPath + "gameSaveExample.json";
	private String exampleSimpleFilePath = testsPath + "simpleExample.txt";
	private String exampleSimpleJSONFilePath = testsPath + "simpleJSONExample.json";
	// You can modify this file
	private String testFilePath = testsPath + "testing.txt";

	private String simpleFileContent = "Some text here.\n";


	@BeforeEach
	public void setup() {

	}

	/**
	 * Tests that the <i>readFile</i> method
	 * returns a string with the file's content.
	 */
	@Test
	public void testReadFileReturnsFilesContent() {
		String result = IO.readFile(exampleSimpleFilePath);
		Assertions.assertEquals(simpleFileContent, result);
	}

	/**
	 * Tests that the <i>readFile</i> method
	 * returns null when the file doesn't exist.
	 */
	@Test
	public void testReadFileDoesntExist() {
		String path = "./path/that/doesnt/exist";
		String result = IO.readFile(path);
		Assertions.assertEquals(null, result);
	}

	/**
	 * Tests that the <i>writeFile</i> method
	 * returns null when the file doesn't exist.
	 */
	@Test
	public void testWriteFileWritesFile() {
		String path = testFilePath;
		// Use random number so that the data changes and it can be checked that a
		// write actually happend
		String text = String.valueOf(Math.random());
		IO.writeFile(path, text);
		Assertions.assertEquals(text, IO.readFile(path));
	}


	/**
	 * Tests that the <i>fromJSON</i> method
	 * returns the JSON converted into the correct
	 * class type.
	 */
	@Test
	public void testFromJSONCreatesObject() {
		ArrayList<String> expected = new ArrayList<>();
		expected.add("Item 1");
		expected.add("Item 2");
		expected.add("Item 3");

		String path = exampleSimpleJSONFilePath;
		String data = IO.readFile(path);
		ArrayList<String> result = (ArrayList) IO.fromJSON(ArrayList.class, data);
		Assertions.assertEquals(expected, result);
	}

	/**
	 * Tests that the <i>toJSON</i> method
	 * correctly converts data to JSON.
	 */
	 @Test
	public void testToJSON() {
		HashMap<String, float[]> data = new HashMap<>();
		data.put("Numbers", new float[] {1.4f, 4f, 7.3f, 2.8f});

		String expected = "{Numbers:[1.4,4,7.3,2.8]}";
		String result = IO.toJSON(data);
		Assertions.assertEquals(expected, result);
	}
}
