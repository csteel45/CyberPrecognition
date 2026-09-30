package com.saggs.cyberprecog.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Verifies that the bundled Iris sample data referenced by the README
 * ("a tiny worked example") is present, non-empty, and well-formed.
 */
public class IrisDataTest {

	private static final File IRIS_DATA_FILE = new File("data/iris.data");

	@Test
	public void irisDataFileExists() {
		assertTrue("data/iris.data should exist", IRIS_DATA_FILE.isFile());
	}

	@Test
	public void irisDataFileIsNotEmpty() {
		assertTrue("data/iris.data should not be empty", IRIS_DATA_FILE.length() > 0);
	}

	@Test
	public void irisDataHasExpectedLineCount() throws IOException {
		List<String> lines = readLines(IRIS_DATA_FILE);
		assertEquals("data/iris.data should contain 150 samples", 151, lines.size());
	}

	@Test
	public void irisDataFirstLineParses() throws IOException {
		List<String> lines = readLines(IRIS_DATA_FILE);
		assertFalse("data/iris.data should have at least one line", lines.isEmpty());

		String[] fields = lines.get(0).split(",");
		assertEquals("each row should have 4 measurements plus a class label", 5, fields.length);

		for (int i = 0; i < 4; i++) {
			Double.parseDouble(fields[i]);
		}
		assertFalse("class label should not be empty", fields[4].trim().isEmpty());
	}

	private static List<String> readLines(File file) throws IOException {
		List<String> lines = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		try {
			String line;
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
		} finally {
			reader.close();
		}
		return lines;
	}
}
