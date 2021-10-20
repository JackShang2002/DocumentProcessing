package cpen221.mp1;


import cpen221.mp1.exceptions.NoSuitableSentenceException;
import cpen221.mp1.sentiments.SentimentAnalysis;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.cloud.language.v1.AnalyzeSentimentResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class Task3SentimentTests {
    private static OurDocument sentimentDocument1;
    private static OurDocument sentimentDocument2;

    @BeforeAll
    public static void setupTests() throws MalformedURLException {
        sentimentDocument1 = new OurDocument("Group Member Sentiment", "resources/membersentiment.txt");
        sentimentDocument2 = new OurDocument("Mundane Sentences", "resources/mundanesentences.txt");
    }
    @Test
    public void testGetMostPositiveSentence() throws NoSuitableSentenceException {
        Assertions.assertEquals(sentimentDocument1.getSentence(3), SentimentAnalysis.getMostPositiveSentence(sentimentDocument1));
    }

    @Test
    public void testGetMostNegativeSentence() throws NoSuitableSentenceException {
        Assertions.assertEquals(sentimentDocument1.getSentence(4), SentimentAnalysis.getMostNegativeSentence(sentimentDocument1));
    }
    @Test
    public void testMundaneSentences(){
        NoSuitableSentenceException thrown = assertThrows(
                NoSuitableSentenceException.class,
                () -> SentimentAnalysis.getMostNegativeSentence(sentimentDocument2),
                "There were no expressions in the document with a negative sentiment"
        );
    }


    @Test
    public void testSentences() {
        String text = "..."; // the text for analysis
        try (LanguageServiceClient language = LanguageServiceClient.create()) {
            Document doc = Document.newBuilder().setContent(text).setType(Type.PLAIN_TEXT).build();
            AnalyzeSentimentResponse response = language.analyzeSentiment(doc);
            Sentiment sentiment = response.getDocumentSentiment();
            if (sentiment != null) {
                System.out.println(sentiment.getScore());
                System.out.println(sentiment.getMagnitude());
            }
        }
        catch (IOException ioe) {
            System.out.println(ioe);
            throw new RuntimeException("Unable to communicate with Sentiment Analyzer!");
        }
    }

}
