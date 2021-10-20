package cpen221.mp1.similarity;

import cpen221.mp1.OurDocument;

import java.util.*;

import static java.lang.Math.pow;
import static java.util.stream.Collectors.toMap;

public class GroupingDocuments {

    /* ------- Task 5 ------- */

    /**
     * Group documents by similarity
     * @param allDocuments the set of all documents to be considered,
     *                     is not null
     * @param numberOfGroups the number of document groups to be generated
     * @return groups of documents, where each group (set) contains similar
     * documents following this rule: if D_i is in P_x, and P_x contains at
     * least one other document, then P_x contains some other D_j such that
     * the divergence between D_i and D_j is smaller than (or at most equal
     * to) the divergence between D_i and any document that is not in P_x.
     */
    public static Set<Set<OurDocument>> groupBySimilarity (Set<OurDocument> allDocuments, int numberOfGroups) {
        final int ASCII_val = 48;
        ArrayList<Double> DocDivVal = new ArrayList<>();
        Set<Set<OurDocument>> groupedDoc = new HashSet<>();

        DocumentSimilarity DocDivMethod = new DocumentSimilarity();

        List<OurDocument> DocArr = new ArrayList<> (allDocuments.stream().toList());
        ArrayList<Integer> DocArrKeys = new ArrayList<>();
        for(int i = 0; i < DocArr.size(); i++){
            DocArrKeys.add(i);
        }

        ArrayList<OurDocument> DummyDocArr = new ArrayList<>(DocArr);
        ArrayList<String> DocDivValKey = new ArrayList<>();


        while(!DummyDocArr.isEmpty()) {
            int countDoc1 = 0;
            for (int countDoc2 = 0; countDoc2 < DummyDocArr.size(); countDoc2++) {
                if(countDoc1 != countDoc2) {
                    DocDivVal.add(DocDivVal.size(), DocDivMethod.documentDivergence(DummyDocArr.get(countDoc1), DummyDocArr.get(countDoc2)));
                    DocDivValKey.add(DocDivValKey.size(), String.valueOf(countDoc1) + " " + String.valueOf(countDoc2));
                }
            }
            DummyDocArr.remove(0);
        }

        LinkedHashMap<String, Double> DocValMap = new LinkedHashMap<>();
        int KeyCount = 0;

        while(KeyCount < DocDivValKey.size()) {

            int ValCount = 0;

            DocValMap.put(DocDivValKey.get(KeyCount), DocDivVal.get(ValCount));

            KeyCount++;
            ValCount++;
        }

        Map<String, Double> sortedDocMap = DocValMap
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Double>comparingByValue())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        ArrayList<String> sortedDocArrKey = new ArrayList<>(sortedDocMap.keySet());



        int count = 0;
        int groups = DocArr.size();

        while (groups != numberOfGroups) {
            Set<OurDocument> dummySetTemp = new HashSet<>();

            String[] sortedKey = sortedDocArrKey.get(count).split(" ");
            char[] firstDocVal = sortedKey[0].toCharArray();
            char[] secondDocVal = sortedKey[1].toCharArray();

            int firstDoc = 0;
            int id1 = 0;
            for (int n = firstDocVal.length - 1; n > -1; n--) {
                firstDoc += firstDocVal[id1] * pow(10, n);
                id1++;
            }

            int secondDoc = 0;
            int id2 = 0;
            for (int n = secondDocVal.length - 1; n > -1; n--) {
                secondDoc += secondDocVal[id2] * pow(10, n);
                id2++;
            }

            firstDoc -= ASCII_val;
            secondDoc -= ASCII_val;

            Iterator<Set<OurDocument>> itr = groupedDoc.iterator();
            while (itr.hasNext()) {
                if (itr.next().contains(DocArr.get(firstDoc)) || itr.next().contains(DocArr.get(secondDoc))) {
                    //dummySetTemp = itr.next();
                    dummySetTemp.add(DocArr.get(firstDoc));
                    dummySetTemp.add(DocArr.get(secondDoc));
                    itr.remove();
                    break;
                }
            }
            if (!dummySetTemp.isEmpty()) {
                groupedDoc.add(dummySetTemp);

                for (int j = 0; j < DocArrKeys.size(); j++) {
                    if (DocArrKeys.get(j) == firstDoc || DocArrKeys.get(j) == secondDoc) {
                        DocArrKeys.remove(j);
                    }
                }

                groups--;
                count++;
            }
            else {
                Set<OurDocument> dummySet = new HashSet<>();
                dummySet.add(DocArr.get(firstDoc));
                dummySet.add(DocArr.get(secondDoc));
                groupedDoc.add(dummySet);

                for (int j = 0; j < DocArrKeys.size(); j++) {
                    if (DocArrKeys.get(j) == firstDoc || DocArrKeys.get(j) == secondDoc) {
                        DocArrKeys.remove(j);
                    }
                }

                groups--;
                count++;
            }
        }


        for (int numCount = 0; numCount < DocArr.size(); numCount++){
            Set<OurDocument> dummySet = new HashSet<>();
            if (DocArrKeys.contains(numCount)){
                dummySet.add(DocArr.get(numCount));
            }
            groupedDoc.add(dummySet);
        }


        return groupedDoc;
    }
}
