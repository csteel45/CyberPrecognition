package com.saggs.cyberprecog.classification;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Random;

import net.sf.javaml.classification.Classifier;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.tools.data.FileHandler;

import org.junit.Test;

/**
 * Verifies that the Iris sample data can be loaded and used to train the
 * classifier code path used by {@link RandomForestClassifier}.
 */
public class IrisDataIntegrationTest {

	private static final File IRIS_DATA_FILE = new File("data/iris.data");

	@Test
	public void irisDataFileExistsAndIsNotEmpty() {
		assertTrue("data/iris.data should exist", IRIS_DATA_FILE.isFile());
		assertTrue("data/iris.data should not be empty", IRIS_DATA_FILE.length() > 0);
	}

	@Test
	public void irisDataLoadsIntoDataset() throws Exception {
		Dataset data = FileHandler.loadDataset(IRIS_DATA_FILE, 4, ",");
		assertNotNull(data);
		assertEquals(150, data.size());
		assertEquals(3, data.classes().size());
		assertEquals(4, data.get(0).noAttributes());
	}

	@Test
	public void classifierTrainsAndPredictsOnIrisData() throws Exception {
		Dataset data = FileHandler.loadDataset(IRIS_DATA_FILE, 4, ",");

		RandomForestClassifier classifier = new RandomForestClassifier();
		classifier.train(data);

		Classifier rf = new net.sf.javaml.classification.tree.RandomForest(10, false, 4, new Random(42));
		rf.buildClassifier(data);

		Object predicted = classifier.predictValue(rf, 5.0, 2.4, 1.5, 0.4);
		assertNotNull(predicted);
		assertTrue(data.classes().contains(predicted));

		for (Instance inst : data) {
			assertNotNull(rf.classify(inst));
		}
	}
}
