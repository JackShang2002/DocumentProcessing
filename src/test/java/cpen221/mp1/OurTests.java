package cpen221.mp1;

import cpen221.mp1.similarity.DocumentSimilarity;
import cpen221.mp1.similarity.GroupingDocuments;
import cpen221.mp1.cryptanalysis.ComplexNumber;
import cpen221.mp1.cryptanalysis.DFT;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static cpen221.mp1.cryptanalysis.Cryptography.decrypt;
import static cpen221.mp1.cryptanalysis.Cryptography.encrypt;
import static cpen221.mp1.cryptanalysis.DFT.dft;
import static cpen221.mp1.cryptanalysis.Untangler.areTangled;


public class OurTests {

    private static OurDocument testDocument1;
    private static OurDocument testDocument2;
    private static OurDocument testDocument3;
    private static OurDocument testDocument4;
    private static OurDocument emptyDocument;
    private static DocumentSimilarity documentSimilarity;
    private static GroupingDocuments groupingDocuments;
    private static OurDocument testDocument5_1a;
    private static OurDocument testDocument5_1b;
    private static OurDocument testDocument5_2a;
    private static OurDocument testDocument5_2b;
    private static OurDocument testDocument5_2c;
    private static OurDocument testDocument5_3;
    private static OurDocument testDocument5_4a;
    private static OurDocument testDocument5_4b;
    private static Set<OurDocument> allDocuments;
    private final int WT_AVG_WORD_LENGTH      = 5;
    private final int WT_UNIQUE_WORD_RATIO    = 15;
    private final int WT_HAPAX_LEGOMANA_RATIO = 25;
    private final int WT_AVG_SENTENCE_LENGTH  = 1;
    private final int WT_AVG_SENTENCE_CPLXTY  = 4;
    private final int WT_JS_DIVERGENCE        = 50;

    @BeforeAll
    public static void setupTests() throws MalformedURLException {

        testDocument1 = new OurDocument("The Ant and The Cricket", "resources/antcrick.txt");
        testDocument2 = new OurDocument("The Ant and The Cricket", new URL("http://textfiles.com/stories/antcrick.txt"));
        testDocument3 = new OurDocument("Friday", "resources/Friday.txt");
        testDocument4 = new OurDocument("Mutability", "resources/Mutability.txt");
        emptyDocument = new OurDocument("", "resources/empty.txt");
        documentSimilarity = new DocumentSimilarity();
        groupingDocuments = new GroupingDocuments();
        testDocument5_1a = new OurDocument("1a", "resources/1a.txt");
        testDocument5_1b = new OurDocument("1b", "resources/1b.txt");
        testDocument5_2a = new OurDocument("2a", "resources/2a.txt");
        testDocument5_2b = new OurDocument("2b", "resources/2b.txt");
        testDocument5_2c = new OurDocument("2c", "resources/2c.txt");
        testDocument5_3 = new OurDocument("3", "resources/3.txt");
        testDocument5_4a = new OurDocument("4a", "resources/4a.txt");
        testDocument5_4b = new OurDocument("4b", "resources/4b.txt");
        allDocuments = new HashSet<OurDocument>(Arrays.asList(testDocument5_1a, testDocument5_1b, testDocument5_2a, testDocument5_2b, testDocument5_2c, testDocument5_3, testDocument5_4a, testDocument5_4b));
    }

    /**Task 1 and 2 Tests**/
    @Test
    public void testSentences1() {
        Assertions.assertEquals("A dream has power to poison sleep; We rise.", testDocument4.getSentence(2));
    }
    @Test
    public void testSentences2() {
        Assertions.assertEquals("One evening, she saw a speck of light in the distance, and trampling through the thick snow, made her way towards it.", testDocument1.getSentence(19));
    }

    @Test
    public void testSentences3() {
        Assertions.assertEquals("said the ant.", testDocument1.getSentence(36));
    }

    @Test
    public void EmptyDocumentTests() {
        Assertions.assertEquals("", emptyDocument.getSentence(1));
        Assertions.assertEquals(0, emptyDocument.averageWordLength());
        Assertions.assertEquals(0, emptyDocument.averageSentenceComplexity());
        Assertions.assertEquals(0, emptyDocument.averageSentenceLength());
        Assertions.assertEquals(0, emptyDocument.uniqueWordRatio());
        Assertions.assertEquals(0, emptyDocument.hapaxLegomanaRatio());
    }

    @Test
    public void ShortDocumentTests() {
        Assertions.assertEquals("Gotta catch my bus, I see my friends (My friends).", testDocument3.getSentence(7));
        Assertions.assertEquals((double) 191/44, testDocument3.averageWordLength(), 0.005);
        Assertions.assertEquals((double) 12/7, testDocument3.averageSentenceComplexity(), 0.005);
        Assertions.assertEquals((double) 44/7, testDocument3.averageSentenceLength(), 0.005);
        Assertions.assertEquals((double) 31/44, testDocument3.uniqueWordRatio(), 0.005);
        Assertions.assertEquals((double) 24/44, testDocument3.hapaxLegomanaRatio(), 0.005);
    }

    @Test
    public void testAverageWordLength() {
        Assertions.assertEquals(4.279, testDocument3.averageWordLength(), 0.005);
    }

    /**Task 4 Tests**/

    @Test
    public void ShannonDivergenceSameDocuments() {
        Assertions.assertEquals(0, documentSimilarity.jsDivergence(testDocument2, testDocument2));
    }

    @Test
    public void ShannonDivergenceDifferentDocuments() {
        ArrayList<String> docWords = testDocument4.getDocumentWords(testDocument4);
        double numWords = docWords.size();
        double expected = 0.5 * (2 * (double) 3/numWords * documentSimilarity.log2(2) + (numWords - 6) * ((double) 1/numWords * documentSimilarity.log2(2)));
        Assertions.assertEquals(expected, documentSimilarity.jsDivergence(emptyDocument, testDocument4), 0.005);
    }

    @Test
    public void DocumentDivergenceSameDocuments() {

        ArrayList<Double> doc1 = new ArrayList<>();
        doc1.add(testDocument3.averageSentenceLength());
        doc1.add(testDocument3.averageSentenceComplexity());
        doc1.add(testDocument3.averageWordLength());
        doc1.add(testDocument3.uniqueWordRatio());
        doc1.add(testDocument3.hapaxLegomanaRatio());

        ArrayList<Double> doc2 = new ArrayList<>();
        doc2.add(testDocument3.averageSentenceLength());
        doc2.add(testDocument3.averageSentenceComplexity());
        doc2.add(testDocument3.averageWordLength());
        doc2.add(testDocument3.uniqueWordRatio());
        doc2.add(testDocument3.hapaxLegomanaRatio());

        ArrayList<Integer> constants = new ArrayList<>();
        constants.add(WT_AVG_SENTENCE_LENGTH);
        constants.add(WT_AVG_SENTENCE_CPLXTY);
        constants.add(WT_AVG_WORD_LENGTH);
        constants.add(WT_UNIQUE_WORD_RATIO);
        constants.add(WT_HAPAX_LEGOMANA_RATIO);

        double expected = WT_JS_DIVERGENCE * documentSimilarity.jsDivergence(testDocument3, testDocument3);

        for(int i = 0; i < 5; i++) {
            expected += (double) constants.get(i) * Math.abs(doc1.get(i) - doc2.get(i));
        }

        Assertions.assertEquals(expected, documentSimilarity.documentDivergence(testDocument3, testDocument3));
    }

    @Test
    public void DocumentDivergenceDifferentDocuments() {
        ArrayList<Double> doc1 = new ArrayList<>();
        doc1.add(testDocument1.averageSentenceLength());
        doc1.add(testDocument1.averageSentenceComplexity());
        doc1.add(testDocument1.averageWordLength());
        doc1.add(testDocument1.uniqueWordRatio());
        doc1.add(testDocument1.hapaxLegomanaRatio());

        ArrayList<Double> doc2 = new ArrayList<>();
        doc2.add(emptyDocument.averageSentenceLength());
        doc2.add(emptyDocument.averageSentenceComplexity());
        doc2.add(emptyDocument.averageWordLength());
        doc2.add(emptyDocument.uniqueWordRatio());
        doc2.add(emptyDocument.hapaxLegomanaRatio());

        ArrayList<Integer> constants = new ArrayList<>();
        constants.add(WT_AVG_SENTENCE_LENGTH);
        constants.add(WT_AVG_SENTENCE_CPLXTY);
        constants.add(WT_AVG_WORD_LENGTH);
        constants.add(WT_UNIQUE_WORD_RATIO);
        constants.add(WT_HAPAX_LEGOMANA_RATIO);

        double expected = WT_JS_DIVERGENCE * documentSimilarity.jsDivergence(testDocument1, emptyDocument);

        for(int i = 0; i < 5; i++) {
            expected += (double) constants.get(i) * Math.abs(doc1.get(i) - doc2.get(i));
        }

        Assertions.assertEquals(expected, documentSimilarity.documentDivergence(testDocument1, emptyDocument), 0.5);
    }

    @Test
    public void testLogCalculator() {
        Assertions.assertEquals(-3.1844, documentSimilarity.log2(0.11), 0.005);
    }

    /**Task 5 Tests**/

    @Test
    public void GroupingTest1() {
        Set<Set<OurDocument>> expectedPartitions = new HashSet<>();
        Set<OurDocument> set1 = new HashSet<>();
        set1.add(testDocument5_1a);
        set1.add(testDocument5_1b);
        Set<OurDocument> set2 = new HashSet<>();
        set2.add(testDocument5_2a);
        set2.add(testDocument5_2b);
        set2.add(testDocument5_2c);
        Set<OurDocument> set3 = new HashSet<>();
        set3.add(testDocument5_3);
        Set<OurDocument> set4 = new HashSet<>();
        set4.add(testDocument5_4a);
        set4.add(testDocument5_4b);

        expectedPartitions.add(set1);
        expectedPartitions.add(set2);
        expectedPartitions.add(set3);
        expectedPartitions.add(set4);

        Assertions.assertEquals(expectedPartitions, groupingDocuments.groupBySimilarity(allDocuments, 4));
    }


    /**Task 6 tests**/
    @Test
    public void testDFT(){
        int[] testArr = {0,1,2,3,4,5,6};
        int[] resultRealArr = new int[7];
        int[] corResultArr = {0,1,2,3,4,5,6};

        ComplexNumber[] resultCompArr = dft(dft(dft(dft(testArr))));

        for (int i = 0; i < resultCompArr.length; i++){
            resultRealArr[i] = (int) resultCompArr[i].re();
        }

        Assertions.assertEquals(corResultArr, resultRealArr);
    }

    @Test
    public void testDecrypt(){

        Assertions.assertEquals("THE ANT AND THE CRICKET", decrypt(encrypt(testDocument1,23,3,5)));
    }

    @Test
    public void testUntangle(){

        String superposition = "adefbc";
        String firstSig = "abc";
        String secondSig = "def";

        Assertions.assertTrue(areTangled(superposition, firstSig, secondSig));

    }
}
