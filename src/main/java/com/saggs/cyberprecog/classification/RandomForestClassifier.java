/**
 * This file is part of the Java Machine Learning Library
 * 
 * The Java Machine Learning Library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * The Java Machine Learning Library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with the Java Machine Learning Library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 * Copyright (c) 2006-2012, Thomas Abeel
 * 
 * Project: http://java-ml.sourceforge.net/
 * 
 */
package com.saggs.cyberprecog.classification;

import java.io.File;
import java.util.Map;
import java.util.Random;

import net.sf.javaml.classification.Classifier;
import net.sf.javaml.classification.evaluation.CrossValidation;
import net.sf.javaml.classification.evaluation.PerformanceMeasure;
import net.sf.javaml.classification.tree.RandomForest;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.tools.data.FileHandler;

import com.saggs.cyberprecog.events.CyberEventSequence;

/**
 * Tutorial for the random forest classifier.
 * 
 * @author Thomas Abeel
 * 
 */
public class RandomForestClassifier implements AttackPatternClassifier {

	private Dataset data = null;
	private RandomForest rForest = null;
	public static Random random = new Random(System.currentTimeMillis());
	
	public RandomForestClassifier() {
		
	}
	
	public void train(Dataset dataset) {
		this.data = dataset;
	}
	
	/**
	 * Override abstract classify method.
	 * @see com.saggs.cyberprecog.classification.AttackPatternClassifier#classify(com.saggs.cyberprecog.events.CyberEventSequence)
	 */
	@Override
	public AttackPattern classify(CyberEventSequence sequence) {
		// TODO Auto-generated method stub
		return null;
	}
	
    /**
     * Shows the default usage of the random forest algorithm.
     */
    public static void main(String[] args) throws Exception {
    	RandomForestClassifier classifier = new RandomForestClassifier();
        /* Load a data set */
        Dataset data = FileHandler.loadDataset(new File("data/iris.data"), 4, ",");
        /*
         * Construct a RF classifier that uses 5 neighbors to make a decision.
         */
        Classifier rf = new RandomForest(500, false, 4, new Random());
        rf.buildClassifier(data);
        
        CrossValidation cv = new CrossValidation(rf);
        Map<Object, PerformanceMeasure> p = cv.crossValidation(data,30);

        System.out.println("Accuracy of Iris-setosa = " + p.get("Iris-setosa").getAccuracy());
        System.out.println("Accuracy of Iris-versicolor = " + p.get("Iris-versicolor").getAccuracy());
        System.out.println("Accuracy of Iris-virginica = " + p.get("Iris-virginica").getAccuracy());
        System.out.println(p);

        /*
         * Load a data set, this can be a different one, but we will use the
         * same one.
         */
        Dataset dataForClassification = FileHandler.loadDataset(new File("data/iris.data"), 4, ",");
        /* Counters for correct and wrong predictions. */
        int correct = 0, wrong = 0;
        /* Classify all instances and check with the correct class values */
        for (Instance inst : dataForClassification) {
            Object predictedClassValue = rf.classify(inst);
            Object realClassValue = inst.classValue();
            if (predictedClassValue.equals(realClassValue))
                correct++;
            else
                wrong++;
        }
        System.out.println("Random Forest Correct predictions  " + correct);
        System.out.println("Random Forest Wrong predictions " + wrong);
        double[] newdata = {5.0,2.4,1.5,0.4};
        Instance inst = new DenseInstance(newdata);
        Object predictedClassValue = rf.classify(inst);
        System.out.println("Iris-setos prediction: " +  predictedClassValue);

        data = FileHandler.loadDataset(new File("data/iris-new.data"), 4, ",");
        p = cv.crossValidation(data,30);

//        System.out.println("Accuracy of Iris-setosa = " + p.get("Iris-setosa").getAccuracy());
//        System.out.println("Accuracy of Iris-versicolor = " + p.get("Iris-versicolor").getAccuracy());
//        System.out.println("Accuracy of Iris-virginica = " + p.get("Iris-virginica").getAccuracy());
        System.out.println("Accuracy of Iris-chrisica = " + p.get("Iris-chrisica").getAccuracy());
        System.out.println("Accuracy of Iris-virginica = " + p.get("Iris-virginica").getAccuracy());
        System.out.println(p);

        predictedClassValue = classifier.predictValue(rf, 1.0,7.4,1.5,0.4);
        double[] chrisdata = {1.0,7.4,1.5,0.4};
        inst = new DenseInstance(chrisdata);
        predictedClassValue = rf.classify(inst);
        System.out.println("Iris-chrisica prediction: " +  predictedClassValue);
        
        for(int i=0; i < 5; i++) {
        	double a = random.nextDouble() + random.nextInt(7);
        	double b = random.nextDouble() + (random.nextInt(1) + 2);
        	double c = random.nextDouble() + random.nextInt(2) + 6;
        	double d = random.nextDouble() + random.nextInt(1) + 1;
        	System.out.println("Random Prediction: " + classifier.predictValue(rf, a, b, c, d) + " " + a + " " + b + " " + c + " " + d);
        }

    }
    
    public Object predictValue(Classifier classifier, double a, double b, double c, double d) {
        double[] data = {a,b,c,d};
        Instance inst = new DenseInstance(data);
        return classifier.classify(inst);
   	
    }

}
