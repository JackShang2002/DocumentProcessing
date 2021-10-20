package cpen221.mp1.cryptanalysis;

import java.util.ArrayList;

public abstract class Untangler {

    /**
     * Determine if {@code superposition} is a result of
     * a tangling of {@code src1} and {@code src2}.
     *
     * sr1 and sr2 should have 1 to 1 mapping on characters within the superposition.
     *
     * Prefix cannot be empty.
     *
     * Superposition does not have to end in the end of a signal, but can be any N-1 characters where N is the length of sr1 or sr2.
     *
     * @param superposition the possibly tangled signal
     * @param src1 the first signal
     * @param src2 the second signal
     * @return true is {@code superposition} is a
     * tangling of {@code src1} and {@code src2} and false otherwise.
     */
    public static boolean areTangled(String superposition, String src1, String src2) {

        char[] src1Char = src1.toCharArray();
        char[] src2Char = src2.toCharArray();
        char[] superPos = superposition.toCharArray();
        ArrayList<Character> superPosClean = new ArrayList<Character>();

        //To clean out all the junk characters.
        for (int i = 0; i < superPos.length; i++){
            int contains = 0;

            for (int id1 = 0; id1 < src1Char.length; id1++) {
                if (src1Char[id1] == superPos[i]){
                    superPosClean.add(superPos[i]);
                    contains = 1;
                    break;
                }
            }
            if (contains == 0) {
                for (int id2 = 0; id2 < src2Char.length; id2++) {
                    if (src2Char[id2] == superPos[i]) {
                        superPosClean.add(superPos[i]);
                        break;
                    }
                }
            }
        }

        if (src1.isEmpty() || src2.isEmpty()){
            return false;
        }

        int xCount = 0;
        int xPrefix = 0;
            for (int j = 0; j < src1Char.length; j++){
                for (int i = 0; i < superPosClean.size(); i++){
                    if (superPosClean.get(i) == src1Char[j]){
                        xCount++;
                    break;
                }
            }
        }
        xPrefix++;

        return true;
    }

}