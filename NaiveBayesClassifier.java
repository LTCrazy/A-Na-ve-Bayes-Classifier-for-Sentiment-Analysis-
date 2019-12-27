import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Your implementation of a naive bayes classifier. Please implement all four methods.
 */

public class NaiveBayesClassifier implements Classifier {

    /**
     * Trains the classifier with the provided training data and vocabulary size
     */
	int v;
	int totalDoc;
	Map<Label, Integer> docCountPair;
	Map<Label, Integer> wordCountPair;
	Map<String, Integer> posDict;
	Map<String, Integer> negDict;
	Map<String, Integer> totalDict;
//	List<Instance> trainData;
    @Override
    public void train(List<Instance> trainData, int v) {
    	
//    	this.trainData = trainData;
		this.v = v;
		wordCountPair = getWordsCountPerLabel(trainData);
		docCountPair = getDocumentsCountPerLabel(trainData);
		createDictPerLabel(trainData);
		
        // TODO : Implement
        // Hint: First, calculate the documents and words counts per label and store them. 
        // Then, for all the words in the documents of each label, count the number of occurrences of each word.
        // Save these information as you will need them to calculate the log probabilities later.
        //
        // e.g.
        // Assume m_map is the map that stores the occurrences per word for positive documents
        // m_map.get("catch") should return the number of "catch" es, in the documents labeled positive
        // m_map.get("asdasd") would return null, when the word has not appeared before.
        // Use m_map.put(word,1) to put the first count in.
        // Use m_map.replace(word, count+1) to update the value
    }

    /**
     * Counts the number of words for each label
     */
    @Override
    public Map<Label, Integer> getWordsCountPerLabel(List<Instance> trainData) {
        // TODO : Implement
       	int pos = 0;
    	int neg = 0;
    	// count words #
    	for (int i = 0; i < trainData.size(); ++i) {
    		if (trainData.get(i).label == Label.POSITIVE) {
    			pos += trainData.get(i).words.size();
    		}
    		else if (trainData.get(i).label == Label.NEGATIVE) {
    			neg += trainData.get(i).words.size();
    		}
    	}
    	Map<Label, Integer> Pair = new HashMap<Label, Integer>();
    	Pair.put(Label.POSITIVE, pos);
    	Pair.put(Label.NEGATIVE, neg);
        return Pair;
    }


    /**
     * Counts the total number of documents for each label
     */
    @Override
    public Map<Label, Integer> getDocumentsCountPerLabel(List<Instance> trainData) {
        // TODO : Implement
    	int pos = 0;
    	int neg = 0;
    	//count doc #
    	for (int i = 0; i < trainData.size(); ++i) {
    		if (trainData.get(i).label == Label.POSITIVE) pos += 1;
    		else if (trainData.get(i).label == Label.NEGATIVE) neg += 1;
    	}
    	Map<Label, Integer> Pair = new HashMap<Label, Integer>();
    	Pair.put(Label.POSITIVE,pos);
    	Pair.put(Label.NEGATIVE,neg);
    	totalDoc = trainData.size();
        return Pair;
    }

    private void createDictPerLabel (List<Instance> trainData){
    	posDict = new HashMap<String, Integer> ();
    	negDict = new HashMap<String, Integer> ();
    	totalDict = new HashMap<String, Integer> ();
    	for (int i = 0; i < trainData.size(); ++i) {
    		// Iterate through positive document
    		if (trainData.get(i).label == Label.POSITIVE) {
    			for (int j = 0; j < trainData.get(i).words.size(); ++j) {
    				// Update existing word
    				if (posDict.containsKey(trainData.get(i).words.get(j))) {
    					posDict.replace(trainData.get(i).words.get(j), posDict.get(trainData.get(i).words.get(j))+1);
    				}
    				// Create an entry for new word
    				else {
    					posDict.put(trainData.get(i).words.get(j), 1);
    				}
    				// Same for the total dictionary
    				if (totalDict.containsKey(trainData.get(i).words.get(j))) {
    					totalDict.replace(trainData.get(i).words.get(j), totalDict.get(trainData.get(i).words.get(j))+1);
    				}
    				else {
    					totalDict.put(trainData.get(i).words.get(j), 1);
    				}
    			}
    		}
    		// Iterate through negative document
    		else {
    			for (int j = 0; j < trainData.get(i).words.size(); ++j) {
    				// Update existing word
    				if (negDict.containsKey(trainData.get(i).words.get(j))) {
    					negDict.replace(trainData.get(i).words.get(j), negDict.get(trainData.get(i).words.get(j))+1);
    				}
    				// Create an entry for new word
    				else {
    					negDict.put(trainData.get(i).words.get(j), 1);
    				}
    				// Same for the total dictionary
    				if (totalDict.containsKey(trainData.get(i).words.get(j))) {
    					totalDict.replace(trainData.get(i).words.get(j), totalDict.get(trainData.get(i).words.get(j))+1);
    				}
    				else {
    					totalDict.put(trainData.get(i).words.get(j), 1);
    				}
    			}
    		}
    	}
    }
    
    /**
     * Returns the prior probability of the label parameter, i.e. P(POSITIVE) or P(NEGATIVE)
     */
    private double p_l(Label label) {
        // TODO : Implement
        // Calculate the probability for the label. No smoothing here.
        // Just the number of label counts divided by the number of documents.
    	double p_l;
    	
    	p_l = (double)docCountPair.get(label)/totalDoc;
//    	System.out.print(p_l+"\n");
        return p_l;
    }

    /**
     * Returns the smoothed conditional probability of the word given the label, i.e. P(word|POSITIVE) or
     * P(word|NEGATIVE)
     */
    private double p_w_given_l(String word, Label label) {
//    	System.out.print(posDict+"\n");
//    	System.out.print("Dict for each word has been created\n");
        // TODO : Implement
        // Calculate the probability with Laplace smoothing for word in class(label)
    	double cl_w = 0.0;
    	double sum = 0.0;
    	if (label == Label.POSITIVE) {
    		if (posDict.containsKey(word)) {
        		cl_w = posDict.get(word);
    		}
    		sum = wordCountPair.get(Label.POSITIVE);
    		}
    	else {
    		if (negDict.containsKey(word)) {
    			cl_w = negDict.get(word);
    		}
    		sum = wordCountPair.get(Label.NEGATIVE);
    	}
    	
        return (cl_w + 1)/(v + sum);
    }

    /**
     * Classifies an array of words as either POSITIVE or NEGATIVE.
     */
    @Override
    public ClassifyResult classify(List<String> words) {
        // TODO : Implement
        // Sum up the log probabilities for each word in the input data, and the probability of the label
        // Set the label to the class with larger log probability
    	double p_l;
    	double sum_pos = 0.0;
    	double sum_neg = 0.0;
    	double g_pos;
    	double g_neg;
    	// As positive
    	p_l = p_l(Label.POSITIVE);
//    	System.out.print("Pos p_l: "+p_l+"\n");
    	for (int i = 0; i < words.size(); ++i) {
//    		System.out.print("Pos p(w|l) for the word "+words.get(i)+": "+p_w_given_l(words.get(i),Label.POSITIVE)+"\n");
    		sum_pos += Math.log(p_w_given_l(words.get(i),Label.POSITIVE));
    	}
    	g_pos = Math.log(p_l) + sum_pos;
    	// As negative
    	p_l = p_l(Label.NEGATIVE);
    	for (int i = 0; i < words.size(); ++i) {
    		sum_neg += Math.log(p_w_given_l(words.get(i),Label.NEGATIVE));
    	}
    	g_neg = Math.log(p_l) + sum_neg;
    	
		Map<Label, Double> prob = new HashMap<Label, Double>();
		prob.put(Label.POSITIVE, g_pos);
		prob.put(Label.NEGATIVE, g_neg);
		ClassifyResult result = new ClassifyResult();
   		result.logProbPerLabel = prob;
   		
    	if (g_pos >= g_neg) {
    		result.label = Label.POSITIVE;
    	}
    	else {
    		result.label = Label.NEGATIVE;
    	}
        return result;
    }


}
