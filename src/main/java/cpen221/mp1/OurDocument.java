package cpen221.mp1;

import com.google.cloud.language.v1.*;
import cpen221.mp1.exceptions.NoSuitableSentenceException;

import java.io.*;
import java.net.URL;
import java.text.BreakIterator;
import java.util.*;

import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.LanguageServiceClient;

public class OurDocument {

    private String docuId;
    private StringBuilder docuContent = new StringBuilder();
    public static ArrayList<Double> scores = new ArrayList<>();
    private static ArrayList<String> docArray = new ArrayList<>();
    private static ArrayList<String> allWords = new ArrayList<>();


    /* ------- Task 0 ------- */
    /*  all the basic things  */

    /**
     * Create a new document using a URL
     *
     * @param docId  the document identifier
     * @param docURL the URL with the contents of the document
     */
    public OurDocument(String docId, URL docURL) {

        docuId = docId;

        try {
            Scanner urlScanner = new Scanner(docURL.openStream());
            while (urlScanner.hasNext()) {
                docuContent.append(" " + urlScanner.nextLine());
            }
        } catch (IOException ioe) {
            System.out.println("Problem reading from URL!");
        }
    }

    /**
     * @param docId    the document identifier
     * @param fileName the name of the file with the contents of
     *                 the document
     */
    public OurDocument(String docId, String fileName) {

        docuId = docId;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            for (String fileLine = reader.readLine();
                 fileLine != null;
                 fileLine = reader.readLine()) {
                docuContent.append(" " + fileLine);
            }

            reader.close();
        } catch (IOException ioe) {
            System.out.println("Problem reading file!");
        }
    }


    /**
     * Obtain the identifier for this document
     *
     * @return the identifier for this document
     */

    public String getDocId() {

        String id = docuId;

        return id;
    }

    /**
     * Store each word with punctuation and whitespace removed from start and end inside a list,
     * where each word is one element of the list
     *
     * @param doc document containing the words
     * @return List of words with punctuation removed
     */
    public ArrayList<String> getDocumentWords(OurDocument doc) {

        if (!docArray.isEmpty()) {
            docArray.clear();
        }

        doc.DocToArrayList();

        ArrayList<String> allWords = new ArrayList<>();

        for (int indexSent = 0; indexSent < doc.docArray.size(); indexSent++) {
            Scanner scanner = new Scanner(doc.docArray.get(indexSent));
            scanner.useDelimiter("\\s");

            while (scanner.hasNext()) {
                StringBuilder word = new StringBuilder(scanner.next());
                String newWord = removePunctuation(word);
                if (!newWord.isEmpty()) {
                    allWords.add(newWord);
                }
            }
        }

        ArrayList<String> docArrayCopy = new ArrayList<String>(allWords);
        return docArrayCopy;
    }


    /* ------- Task 1 ------- */

    /**
     * Removes punctuation, excluding hashtag, from the beginning and all punctuation from the end of a word
     *
     * @param word the word to remove punctuation from
     * @return the word without punctuation at the start and end (hashtags may be included at the start of the word)
     */

    public String removePunctuation(StringBuilder word) {
        String stringWord = word.toString();

        for (int i = 0; i < word.length(); i++) {
            if (stringWord.toLowerCase().charAt(i) >= 'a' && stringWord.toLowerCase().charAt(i) <= 'z' || stringWord.charAt(i) == '#') {
                word.substring(i);

                if(stringWord.toLowerCase().charAt(0) < 'a' || stringWord.toLowerCase().charAt(0) > 'z') {
                    if(word.length() <= 1) {
                        return "";
                    }

                    else if (stringWord.toLowerCase(Locale.ROOT).charAt(0) != '#') {
                        word.deleteCharAt(0);
                        break;
                    }
                }

                break;
            } else if (stringWord.length() <= 1) {
                return "";
            }
        }

        String newWord = word.toString().toLowerCase(Locale.ROOT);

        for (int j = word.length() - 1; j >= 0; j--) {

            if (newWord.charAt(j) >= 'a' && newWord.charAt(j) <= 'z') {
                if (j == word.length() - 1) {
                    break;
                }

                word.delete(j + 1, word.length());

                if (stringWord.toLowerCase().charAt(stringWord.length() - 1) < 'a' || stringWord.toLowerCase().charAt(stringWord.length() - 1) > 'z') {
                    if (stringWord.length() <= 1) {
                        return "";
                    }

                    word.substring(0, j - 1);
                }

                break;
            }
        }

        if (stringWord.isEmpty()) {
            return "";
        }

        return word.toString();
    }

    /**
     * Find the average length of a word
     *
     * @return the total number of letters divided by the total number of words
     */
    public double averageWordLength() {

        double wordCount = 0;
        int letterCount = 0;
        int start = 0;
        double wordLength = 0;

        if (!docArray.isEmpty()) {
            docArray.clear();
        }

        DocToArrayList();

        for (int indexSent = start; indexSent < docArray.size(); indexSent++) {
            Scanner scanner = new Scanner(docArray.get(indexSent));
            scanner.useDelimiter("\\s");

            while (scanner.hasNext()) {
                StringBuilder word = new StringBuilder(scanner.next());
                String newWord = removePunctuation(word);
                if (!newWord.isEmpty()) {
                    letterCount += newWord.length();
                    wordCount++;
                }
            }
        }

        if (wordCount == 0) {
            return 0;
        }

        wordLength = letterCount / wordCount;
        return wordLength;
    }

    /**
     * Find the unique word ratio
     *
     * @return the number of unique words to the total number of words
     * or zero if there are no words
     */
    public double uniqueWordRatio() {
        HashSet<String> uniqueWords = new HashSet<>();
        int wordCount = 0;
        double ratio;
        int start = 0;

        if (!docArray.isEmpty()) {
            docArray.clear();
        }

        DocToArrayList();

        if(docArray.isEmpty()) {
            return 0;
        }

        for (int indexSent = start; indexSent < docArray.size(); indexSent++) {
            Scanner scanner = new Scanner(docArray.get(indexSent));
            scanner.useDelimiter("\\s");

            while (scanner.hasNext()) {
                StringBuilder word = new StringBuilder(scanner.next());
                String newWord = removePunctuation(word);
                if (!newWord.isEmpty()) {
                    uniqueWords.add(newWord.toLowerCase());
                    wordCount++;
                }
            }
        }

        ratio = (double) uniqueWords.size() / wordCount;
        return ratio;
    }

    /**
     * Find the Hapax Legomana ratio
     *
     * @return the number of words occurring exactly once to the total number of words
     * or zero if there are no words in the document
     */
    public double hapaxLegomanaRatio() {
        HashSet<String> uniqueWords = new HashSet<>();
        ArrayList<String> allWords = new ArrayList<>();
        int wordCount = 0;
        double ratio = 0.0;
        int start = 0;
        int onceWords = 0;

        if (!docArray.isEmpty()) {
            docArray.clear();
        }

        DocToArrayList();

        for (int indexSent = start; indexSent < docArray.size(); indexSent++) {
            Scanner scanner = new Scanner(docArray.get(indexSent));
            scanner.useDelimiter("\\s");

            while (scanner.hasNext()) {
                StringBuilder word = new StringBuilder(scanner.next());
                String newWord = removePunctuation(word);
                if (!newWord.isEmpty()) {
                    uniqueWords.add(newWord.toLowerCase());
                    allWords.add(newWord.toLowerCase(Locale.ROOT));
                    wordCount++;
                }
            }
        }

        if (wordCount == 0) {
            return 0;
        }

        ArrayList<String> uniqueWordsArray = new ArrayList<>(uniqueWords);

        for (int i = 0; i < uniqueWordsArray.size(); i++) {
            int count = 0;
            for (int j = 0; j < allWords.size(); j++) {
                if (uniqueWordsArray.get(i).equals(allWords.get(j))) {
                    count++;
                }
            }
            if (count == 1) {
                onceWords++;
            }
        }

        ratio = (double) onceWords / wordCount;
        return ratio;

    }

    /* ------- Task 2 ------- */

    /**
     * Obtain the number of sentences in the document
     *
     * @return the number of sentences in the document
     */
    public int numSentences() {
        if (!docArray.isEmpty()) {
            docArray.clear();
        }
        DocToArrayList();
        int numSent = docArray.size();
        return numSent;
    }

    /**
     * Store each sentence of document as an element in an array list
     *
     * @Modifies - docArray by adding sentences in order to each index
     */
    public void DocToArrayList() {

        String sentence = "";

        BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
        iterator.setText(String.valueOf(docuContent));
        int start = iterator.first();

        for (int end = iterator.next();
             end != BreakIterator.DONE;
             start = end, end = iterator.next()) {
            sentence = docuContent.substring(start, end);
            Scanner scanner = new Scanner(sentence);
            scanner.useDelimiter("\n");

            while (scanner.hasNextLine()) {
                sentence = scanner.next().trim();
            }

            docArray.add(sentence);
        }
    }

    /**
     * Obtain a specific sentence from the document.
     * Sentences are numbered starting from 1.
     *
     * @param sentence_number the position of the sentence to retrieve,
     *                        {@code 1 <= sentence_number <= this.getSentenceCount()}
     * @return the sentence indexed by {@code sentence_number} or an empty string if
     * the document does not contain any sentences
     */

    public String getSentence(int sentence_number) {
        docArray.clear();

        DocToArrayList();
        if (docArray.isEmpty()) {
            return "";
        }

        String sentence = docArray.get(sentence_number - 1);
        return sentence;
    }

    /**
     * Find the average sentence length of a document
     *
     * @return the average number of words in a sentence or zero if there are no words
     */
    public double averageSentenceLength() {
        ArrayList<String> allWords = new ArrayList<>();
        double ratio;
        int start = 0;

        if (!docArray.isEmpty()) {
            docArray.clear();
        }

        DocToArrayList();
        int sentenceCount = docArray.size();
        if (sentenceCount == 0) {
            return 0;
        }

        for (int indexSent = start; indexSent < sentenceCount; indexSent++) {
            Scanner scanner = new Scanner(docArray.get(indexSent));
            scanner.useDelimiter("\\s");

            while (scanner.hasNext()) {
                StringBuilder word = new StringBuilder(scanner.next());
                String newWord = removePunctuation(word);
                if (!newWord.isEmpty()) {
                    allWords.add(newWord);
                }
            }
        }

        ratio = (double) allWords.size() / sentenceCount;
        return ratio;
    }

    /**
     * Find the sentence complexity, or the average number of phrases per sentence
     *
     * @return the average number of phrases per sentence or zero if there are no sentences
     */
    public double averageSentenceComplexity() {
        double phraseCount = 0.0;
        double avgSentComp = 0.0;
        int periodCounter = 0;
        int uppercaseCounter = 0;
        String semicolon = ";";
        String comma = ",";
        String colon = ":";
        String period = ".";
        String question = "?";
        String exclaim = "!";

        BreakIterator iterator = BreakIterator.getWordInstance(Locale.US);
        iterator.setText(String.valueOf(docuContent));
        int start = iterator.first();
        for (int end = iterator.next();
             end != BreakIterator.DONE;
             start = end, end = iterator.next()) {

            String phrase = docuContent.substring(start, end);

            if (uppercaseCounter == 3) {
                if (Character.isUpperCase(phrase.charAt(0))) {
                    phraseCount++;
                }
                uppercaseCounter = 0;
            }

            if (uppercaseCounter == 2) {
                if (Character.isUpperCase(phrase.charAt(0))) {
                    phraseCount++;
                }
                uppercaseCounter = 3;
            }
            if (uppercaseCounter == 1) {
                uppercaseCounter++;
            }

            if (phrase.equals(period)) {
                periodCounter++;
            } else {
                periodCounter = 0;
            }

            if (phrase.equals(comma) || phrase.equals(semicolon) || phrase.equals(colon) || phrase.equals(period) || phrase.equals(question) || phrase.equals(exclaim)) {
                phraseCount++;
            }

            if (periodCounter == 3) {
                phraseCount = phraseCount - 3;
                uppercaseCounter++;
            }
        }

        if (numSentences() == 0) {
            return 0;
        }

        avgSentComp = phraseCount / numSentences();
        return avgSentComp;
    }

    /* ------- Task 3 ------- */

//    To implement these methods while keeping the class
//    small in terms of number of lines of code,
//    implement the methods fully in sentiments.SentimentAnalysis
//    and call those methods here. Use the getSentence() method
//    implemented in this class when you implement the methods
//    in the SentimentAnalysis class.

//    Further, avoid using the Google Cloud AI multiple times for
//    the same document. Save the results (cache them) for
//    reuse in this class.

//    This approach illustrates how to keep classes small and yet
//    highly functional.

    /**
     * Helper method that obtains the sentiment scores of each sentence in a document
     * @param document the document whose sentences will be used to get sentiment scores.
     *
     * @return returns a double array containing the sentiment scores of each sentence
     * in a given document. If document is empty, return error message
     * @throws RuntimeException if reader is unable to communicate with the AI
     */
    public static void getSentimentScores(OurDocument document) {
        for (int i = 1; i <= document.numSentences(); i++) {
            String text = document.getSentence(i);
            try (LanguageServiceClient language = LanguageServiceClient.create()) {
                Document docu = Document.newBuilder().setContent(text).setType(Type.PLAIN_TEXT).build();
                AnalyzeSentimentResponse response = language.analyzeSentiment(docu);
                Sentiment sentiment = response.getDocumentSentiment();
                if (sentiment != null) {
                    scores.add((double) sentiment.getScore());
                }
            } catch (IOException ioe) {
                System.out.println(ioe);
                throw new RuntimeException("Unable to communicate with Sentiment Analyzer!");
            }
        }
    }

    /**
     * Obtain the sentence with the most positive sentiment in the document
     *
     * @return the sentence with the most positive sentiment in the
     * document; when multiple sentences share the same sentiment value
     * returns the sentence that appears later in the document
     * @throws NoSuitableSentenceException if there is no sentence that
     *                                     expresses a positive sentiment
     */
    public String getMostPositiveSentence() throws NoSuitableSentenceException {
        String positiveSentence = "";
        try {
            positiveSentence = getMostPositiveSentence();
        }
        catch (NoSuitableSentenceException nse) {
            System.out.println("There were no expressions in the document with a positive sentiment");
        }

         return positiveSentence;
    }

    private static com.google.cloud.language.v1.Document.Builder newBuilder() {
        return null;
    }

    /**
     * Obtain the sentence with the most negative sentiment in the document
     *
     * @return the sentence with the most negative sentiment in the document;
     * when multiple sentences share the same sentiment value, returns the
     * sentence that appears later in the document
     * @throws NoSuitableSentenceException if there is no sentence that
     *                                     expresses a negative sentiment
     */
    public String getMostNegativeSentence() throws NoSuitableSentenceException {
        String negativeSentence = "";
        try {
            negativeSentence = getMostPositiveSentence();
        }
        catch (NoSuitableSentenceException nse) {
            System.out.println("There were no expressions in the document with a negative sentiment");
        }

        return negativeSentence;
    }
}

