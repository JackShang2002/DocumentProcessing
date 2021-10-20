package cpen221.mp1;

import cpen221.mp1.exceptions.NoSuitableSentenceException;
import cpen221.mp1.sentiments.SentimentAnalysis;


import java.io.*;
import java.net.URL;
import java.text.BreakIterator;
import java.util.*;

public class Document {

    private String docuId;
    private StringBuilder docuContent = new StringBuilder();

    /* ------- Task 0 ------- */
    /*  all the basic things  */

    /**
     * Create a new document using a URL
     *
     * @param docId  the document identifier
     * @param docURL the URL with the contents of the document
     */
    public Document(String docId, URL docURL) {

        docuId = docId;

        try {
            Scanner urlScanner = new Scanner(docURL.openStream());
            while (urlScanner.hasNext()) {
                docuContent.append(urlScanner.nextLine());
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
    public Document(String docId, String fileName) {

        docuId = docId;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            for (String fileLine = reader.readLine();
                 fileLine != null;
                 fileLine = reader.readLine()) {
                docuContent.append(fileLine);
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

    /* ------- Task 1 ------- */

    public double averageWordLength() {

        double wordCount = 0.0;
        int letterCount = 0;
        double wordLength = 0.0;

        for (int indexSent = 1; indexSent < numSentences(); indexSent++) {
            String[] splitter = getSentence(indexSent).split("[\\s+ \n]");
            for (int index = 0; index < splitter.length; index++) {
                if (!Objects.equals(splitter[index], "\\s+")) {
                    for (int indexWord = 0; indexWord < splitter[index].length(); indexWord++) {
                        wordCount++;
                    }
                }
                for (int indexWord = 0; indexWord < splitter[index].length() - 1; indexWord++) {
                    if ((splitter[index].charAt(indexWord) >= 'A' && splitter[index].charAt(indexWord) <= 'z') || (splitter[index].charAt(indexWord) == '#' && splitter[index + 1].charAt(indexWord) >= 'A' && splitter[index + 1].charAt(indexWord) <= 'z')) {
                        for (int indexChar = indexWord; indexChar < splitter[index].length() - 2; indexChar++) {
                            if ((splitter[index].charAt(indexChar + 1) >= 'A' && splitter[index].charAt(indexChar + 1) <= 'z') && (splitter[index].charAt(indexChar + 2) >= 'A' && splitter[index].charAt(indexChar + 2) <= 'z')) {
                                letterCount++;
                            }
                        }
                        break;
                    }
                }
            }
        }
        wordLength = (double) letterCount / wordCount;
        return wordLength;
    }

    /**
     * Find the unique word ratio
     *
     * @return the number of unique words to the total number of words
     */
    public double uniqueWordRatio() {
        HashSet<String> uniqueWords = new HashSet<>();
        int wordcount = 0;
        double ratio = 0.0;

        for (int indexSent = 1; indexSent < numSentences(); indexSent++) {
            String[] splitter = getSentence(indexSent).split("[\\s+ \n]");

            for (int index = 0; index < splitter.length; index++) {
                if (!Objects.equals(splitter[index], "\\s+")) {
                    uniqueWords.add(splitter[index].toLowerCase());
                    wordcount++;
                }
            }
        }

        ratio = (double) uniqueWords.size() / wordcount;
        return ratio;
    }

    /**
     * Find the Hapax Legomana ratio
     *
     * @return the number of words occurring exactly once to the total number of words
     */
    public double hapaxLegomanaRatio() {
        // TODO: Implement this method
        HashSet<String> uniqueWords = new HashSet<>();
        int wordcount = 0;
        double ratio = 0.0;

        for (int indexSent = 1; indexSent < numSentences(); indexSent++) {
            String[] splitter = getSentence(indexSent).split("[\\s+ \n]");

            for (int index = 0; index < splitter.length; index++) {
                if (!Objects.equals(splitter[index], "\\s+")) {
                    uniqueWords.add(splitter[index]);
                    wordcount++;
                }
            }
        }

        Iterator<String> iterator = uniqueWords.iterator();
        while(iterator.hasNext()) {
            String currentWord = iterator.next();

            for (int indexSent = 1; indexSent < numSentences(); indexSent++) {
                String[] splitter = getSentence(indexSent).split("[\\s+ \n]");

                for (int index = 0; index < splitter.length; index++) {
                    String word = splitter[index];
                    if (!Objects.equals(word, "\\s+")) {
                        if (Objects.equals(currentWord, word)) {
                            iterator.remove();
                        }
                    }
                }
            }
        }

        int onceWords = uniqueWords.size();
        ratio = (double) onceWords / wordcount;
        return ratio;
    }

    /* ------- Task 2 ------- */

    /**
     * Obtain the number of sentences in the document
     *
     * @return the number of sentences in the document
     */
    public int numSentences() {

        int numSent = 1; //TEMPORARILY CHANGED TO 1 TO FIX GETSENTENCES

        BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
        iterator.setText(String.valueOf(docuContent));
        int start = iterator.first();
        for (int end = iterator.next();
             end != BreakIterator.DONE;
             start = end, end = iterator.next()) {
            String sentence = docuContent.substring(start, end);
            numSent++;
        }

        return numSent;
    }

    /**
     * Obtain a specific sentence from the document.
     * Sentences are numbered starting from 1.
     *
     * @param sentence_number the position of the sentence to retrieve,
     *                        {@code 1 <= sentence_number <= this.getSentenceCount()}
     * @return the sentence indexed by {@code sentence_number}
     */
    public String getSentence(int sentence_number) {

        String sentence = "";
        int counter = 0;

        BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
        iterator.setText(String.valueOf(docuContent));
        int start = iterator.first();
        for (int end = iterator.next();
             end != BreakIterator.DONE;
             start = end, end = iterator.next()) {

            sentence = docuContent.substring(start, end);
            counter++;
            if(counter == sentence_number){
                break;
            }
        }

        return sentence;
    }

    /**
     * Find the average sentence length of a document
     *
     * @return the average number of words in a sentence
     */
    public double averageSentenceLength() {

        double wordCount = 0.0;
        double avgSent = 0.0;

        String[] splitter = docuContent.toString().split("[\\s+ \n]");
        for (int index = 0; index < splitter.length; index++) {
            if (!Objects.equals(splitter[index], "\\s+")) {
                wordCount++;
            }
        }

        avgSent = (double) wordCount / numSentences();
        return avgSent;
    }

    /**
     * Find the sentence complexity, or the average number of phrases per sentence
     *
     * @return the average number of phrases per sentence
     */
    public double averageSentenceComplexity() {
        double phraseCount = 0.0;
        double avgSentComp = 0.0;

        for (int index = 1; index < numSentences(); index++) {

            String[] eachWord = getSentence(index).split("[,;:]");
            phraseCount += eachWord.length;

        }
        avgSentComp = (double) phraseCount / numSentences();
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
     * Obtain the sentence with the most positive sentiment in the document
     *
     * @return the sentence with the most positive sentiment in the
     * document; when multiple sentences share the same sentiment value
     * returns the sentence that appears later in the document
     * @throws NoSuitableSentenceException if there is no sentence that
     *                                     expresses a positive sentiment
     */
    public String getMostPositiveSentence() throws NoSuitableSentenceException {
        // TODO: Implement this method
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
        // TODO: Implement this method
        return null;
    }

}
