package cpen221.mp1.sentiments;

import com.google.cloud.language.v1.AnalyzeSentimentResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import cpen221.mp1.OurDocument;
import cpen221.mp1.exceptions.NoSuitableSentenceException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class SentimentAnalysis extends OurDocument {

    public SentimentAnalysis(String docId, URL docURL) {
        super(docId, docURL);
    }

    public static String getMostPositiveSentence(cpen221.mp1.OurDocument doc)
        throws NoSuitableSentenceException {

        double maxSentimentScore = -10.0; //safe because the sentiment score will ALWAYS be between -1.0 and 1.0
        int maxScoreIndex = 0;

        if (scores.size() != doc.numSentences() || scores.isEmpty()) {
            getSentimentScores(doc);
        }

        for (int i = 1; i <= doc.numSentences(); i++) {
            if (scores.get(i) >= maxSentimentScore) {
                maxScoreIndex = i;
            }
        }


        if (scores.get(maxScoreIndex) < 0.3) {
            throw new NoSuitableSentenceException("There were no expressions in the document with a positive sentiment");
        }
        else {
            return doc.getSentence(maxScoreIndex);
        }
    }

    public static String getMostNegativeSentence(cpen221.mp1.OurDocument doc)
        throws NoSuitableSentenceException {
        double minSentimentScore = 10;
        int minScoreIndex = 0;

        if (scores.size() != doc.numSentences()) {
            getSentimentScores(doc);
        }

        for (int i = 1; i <= doc.numSentences(); i++) {
            if (scores.get(i) <= minSentimentScore) {
                minScoreIndex = i;
            }
        }


        if (scores.get(minScoreIndex) > -0.3) {
            throw new NoSuitableSentenceException("There were no expressions in the document with a negative sentiment");
        }
        else {
            return doc.getSentence(minScoreIndex);
        }
    }

}
