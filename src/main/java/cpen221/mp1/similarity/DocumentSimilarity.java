package cpen221.mp1.similarity;

import cpen221.mp1.OurDocument;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Scanner;

import static java.lang.Math.abs;

public class DocumentSimilarity {

    /* DO NOT CHANGE THESE WEIGHTS */
    private final int WT_AVG_WORD_LENGTH      = 5;
    private final int WT_UNIQUE_WORD_RATIO    = 15;
    private final int WT_HAPAX_LEGOMANA_RATIO = 25;
    private final int WT_AVG_SENTENCE_LENGTH  = 1;
    private final int WT_AVG_SENTENCE_CPLXTY  = 4;
    private final int WT_JS_DIVERGENCE        = 50;

    /* ---- END OF WEIGHTS ------ */

    /* ------- Task 4 ------- */

    /**
     * Compute the Jensen-Shannon Divergence between the given documents
     * @param doc1 the first document, is not null
     * @param doc2 the second document, is not null
     * @return the Jensen-Shannon Divergence between the given documents
     */
    public double jsDivergence(OurDocument doc1, OurDocument doc2) {
        double JSD = 0.0;
        HashSet<String> uniqueWords = new HashSet<>();

        ArrayList<String> doc1Words = new ArrayList<>(doc1.getDocumentWords(doc1));
        ArrayList<String> doc2Words = new ArrayList<>(doc2.getDocumentWords(doc2));
        ArrayList<Double> pCount = new ArrayList<>();
        ArrayList<Double> qCount = new ArrayList<>();
        int totalWords1 = doc1Words.size();
        int totalWords2 = doc2Words.size();

        for(int i = 0; i < totalWords1; i++) {
            uniqueWords.add(doc1Words.get(i).toLowerCase(Locale.ROOT));
        }
        for(int i = 0; i < totalWords2; i++) {
            uniqueWords.add(doc2Words.get(i).toLowerCase(Locale.ROOT));
        }

        String[] uniqueWordsArray = uniqueWords.toArray(new String[uniqueWords.size()]);

        for(int i = 0; i < uniqueWordsArray.length; i++) {
            double p = 0;
            double q = 0;

            for(int j = 0; j < totalWords1; j++) {
                if(uniqueWordsArray[i].equals(doc1Words.get(j).toLowerCase(Locale.ROOT))) {
                    for(String s : doc1Words) {
                        if(s.toLowerCase(Locale.ROOT).equals(uniqueWordsArray[i])) {
                            p++;
                        }
                    }
                    break;
                }
            }

            for(int j = 0; j < totalWords2; j++) {
                if(uniqueWordsArray[i].equals(doc2Words.get(j).toLowerCase(Locale.ROOT))) {
                    for(String s : doc2Words) {
                        if(s.toLowerCase(Locale.ROOT).equals(uniqueWordsArray[i])) {
                            q++;
                        }
                    }
                    break;
                }
            }

            pCount.add(i, p);
            qCount.add(i, q);
           //System.out.println(uniqueWordsArray[i] + " " + pCount.get(i) + " and " + qCount.get(i));
        }

        for(int i = 0; i < uniqueWordsArray.length; i++) {
            double P_i = 0.0;
            double Q_i = 0.0;

            if(totalWords1 > 0 && totalWords2 > 0) {
                P_i = pCount.get(i) / totalWords1;
                Q_i = qCount.get(i) / totalWords2;
            }

            else if(totalWords1 > 0) {
                P_i = pCount.get(i) / totalWords1;
            }

            else {
                Q_i = qCount.get(i) / totalWords2;
            }

            double M_i = (P_i + Q_i) / 2;

            JSD += getTerm(P_i, M_i) + getTerm(Q_i, M_i);
            System.out.println(JSD);
        }

        System.out.println(uniqueWordsArray.length);
        System.out.println(JSD);
        return JSD / 2;
    }

    /**
     * Calculate the log base two of a number
     * @param x number to take the log of
     * @return the log of x to base two
     */
    public static double log2(double x)
    {
        return (double) (Math.log(x) / Math.log(2));
    }

    /**
     * Get the result of a multiplied by the log (base 2) of a divided by b.
     * If a equals zero, return zero.
     * @param a number in the equation that appears in the numerator of log calculation.
     *          Represents the probability of a word appearing in a document
     *          a > 0
     * @param b number that appears in the denominator of log calculation;
     *          Represents the sum of probability that a word appears in two documents
     *          divided bby 2.
     *          b > 0
     * @return the result of a multiplied by the log (base 2) of a divided by b, or zero if a or b is zero
     */
    public static double getTerm(double a, double b) {
        if(a == 0 || b == 0) {
            return 0.0;
        }

        return a * log2(a / b);
    }
    /**
     * Compute the Document Divergence between the given documents
     * @param doc1 the first document, is not null
     * @param doc2 the second document, is not null
     * @return the Document Divergence between the given documents
     */
    public double documentDivergence(OurDocument doc1, OurDocument doc2) {
        double DocDiv = 0.0;

        //AvSentLeng.
        DocDiv += WT_AVG_SENTENCE_LENGTH * abs((doc1.averageSentenceLength() - doc2.averageSentenceLength()));

        //SentCoplx.
        DocDiv += WT_AVG_SENTENCE_CPLXTY * abs((doc1.averageSentenceComplexity() - doc2.averageSentenceComplexity()));

        //AvWordLeng.
        DocDiv += WT_AVG_WORD_LENGTH * abs(doc1.averageWordLength() - doc2.averageWordLength());

        //UniWord.
        DocDiv += WT_UNIQUE_WORD_RATIO * abs((doc1.uniqueWordRatio() - doc2.uniqueWordRatio()));

        //HapLego.
        DocDiv += WT_HAPAX_LEGOMANA_RATIO * abs((doc1.hapaxLegomanaRatio() - doc2.hapaxLegomanaRatio()));

        DocDiv += (WT_JS_DIVERGENCE * jsDivergence(doc1, doc2));

        return DocDiv;
    }

}
