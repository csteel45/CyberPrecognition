package com.saggs.cyberprecog.classification;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.tools.data.FileHandler;

import org.junit.Test;

/**
 * Verifies that the bundled Iris sample (referenced in the README as a "tiny
 * worked example") is present, well-formed and ingestible by the same
 * {@link FileHandler} utility that {@link RandomForestClassifier#main(String[])}
 * uses to load it.
 *
 * @author Christopher Steel - Software AG Government Solutions
 * @since Sep 26, 2026
 */
public class IrisDataIntegrityTest {

	private static final File IRIS_DATA_FILE = new File("data/iris.data");

	/** e.g. "5.1,3.5,1.4,0.2,Iris-setosa" - 4 numeric attributes plus a class label. */
	private static final Pattern IRIS_ROW_PATTERN = Pattern
			.compile("^\\d+(\\.\\d+)?,\\d+(\\.\\d+)?,\\d+(\\.\\d+)?,\\d+(\\.\\d+)?,Iris-[a-z]+$");

	@Test
	public void dataFileExistsAndIsNotEmpty() {
		assertTrue("data/iris.data should exist", IRIS_DATA_FILE.exists());
		assertTrue("data/iris.data should be a non-empty file", IRIS_DATA_FILE.length() > 0);
	}

	@Test
	public void firstLineMatchesExpectedRowFormat() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(IRIS_DATA_FILE));
		try {
			String firstLine = reader.readLine();
			assertNotNull("data/iris.data should have a readable first line", firstLine);
			assertTrue("First line '" + firstLine + "' should match the expected "
					+ "'sepalLength,sepalWidth,petalLength,petalWidth,Iris-species' row format",
					IRIS_ROW_PATTERN.matcher(firstLine).matches());
		} finally {
			reader.close();
		}
	}

	/**
	 * Loads the file through the exact ingestion call used by
	 * {@link RandomForestClassifier#main(String[])} - {@code FileHandler.loadDataset(file, 4, ",")} -
	 * and confirms it parses without throwing and yields the full, labeled Iris data set.
	 */
	@Test
	public void loadsCleanlyViaJavaMLFileHandler() throws IOException {
		Dataset dataset = FileHandler.loadDataset(IRIS_DATA_FILE, 4, ",");

		assertEquals("Iris data set should contain 150 instances", 150, dataset.size());
		for (Instance instance : dataset) {
			assertNotNull("Every instance should carry a class label", instance.classValue());
		}
	}
}
