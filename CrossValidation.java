import java.util.ArrayList;
import java.util.List;

public class CrossValidation {
    /**
     * Returns the k-fold cross validation score of classifier clf on training data.
     */
    public static double kFoldScore(Classifier clf, List<Instance> trainData, int k, int v) {
        // TODO : Implement
    	// Split into k folds
    	int idx = 0;
    	double score = 0.0;
    	List<Instance> trainFold = new ArrayList<Instance>();
    	List<Instance> testFold;
    	List<List<Instance>> folds = new ArrayList<List<Instance>>();
    	
    	for (int i = 0; i < k; ++i) {
    		folds.add(trainData.subList(idx, idx + trainData.size()/k));
    		idx = trainData.size()/k;
//    		Collection<List<Instance>> lists = new ArrayList<List<Instance>> (); 
//    		lists = trainData.stream().collect(Collectors.partitioningBy(s -> 
//    		trainData.indexOf(s) >= idx & trainData.indexOf(s) < idx + trainData.size()/k * (k - 1))).values();
//    		List<Instance> trainFold = trainData.subList(idx, idx + trainData.size()/k * (k - 1));
//    		List<Instance> testFold = trainData.subList(trainData.size()/k * (k - 1), trainData.size());
//    		score += withInFoldScore(clf, trainFold, testFold, v);
//    		idx = idx + trainData.size()/k;
    	}
    	// Model for each fold
    	for (int i = 0; i < k; ++i) {
    		clf = new NaiveBayesClassifier();
    		trainFold = new ArrayList<Instance>();
    		testFold = new ArrayList<Instance>();
    		
    		for (int j = 0; j < trainData.size(); ++j)
    		{
    			if (i == (j/(trainData.size()/k))) {
    				testFold.add(trainData.get(j));
    			}
    			else {
    				trainFold.add(trainData.get(j));
    			}
    		}
    		
//    		List<List<Instance>> foldsRep = new ArrayList<List<Instance>>();
//    		foldsRep.addAll(folds);
////    		System.out.print("fold size: "+foldsRep.size()+"\n");
////    		System.out.print("i = "+i+"\n");
//    		testFold = foldsRep.get(i);
//    		foldsRep.remove(i);
//    		for (int j = 0; j < foldsRep.size(); ++j) {
//    			//System.out.println(foldsRep.get(j).size());
//    			trainFold.addAll(foldsRep.get(j));
//    		}
    		score += withInFoldScore(clf, trainFold, testFold, v);
    	}
    	score = score/k;
        return score;
    }
    
    private static double withInFoldScore (Classifier clf, List<Instance> trainData, List<Instance> testData, int v) {
    	clf.train(trainData, v);
    	double score = 0.0;
    	for (int i = 0; i < testData.size(); ++i) {
    		ClassifyResult result = clf.classify(testData.get(i).words);
    		if (result.label == testData.get(i).label) {
    			score += 1;
    		}
    	}
    	score = score/testData.size();
    	//System.out.println(score);
    	return score;
    }
}
