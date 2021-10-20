package cpen221.mp1.cryptanalysis;

import cpen221.mp1.OurDocument;

import static cpen221.mp1.cryptanalysis.DFT.dft;

public abstract class Cryptography {


    /**
     * Encrypt a document by replacing the i-th character, c_i, with
     * c_i + A \times \sin{(i \times 2\pi / P + \pi/4)}
     * where A is the amplitude and P is the period for the sine wave.
     * When encrypting text with multiple sentences, exactly one space
     * is used to separate sentences.
     *
     * @param doc       the document to encrypt
     * @param length    the number of characters to encrypt.
     *                  If {@code length} is less than the number of
     *                  characters in the document then only that many
     *                  characters are encrypted.
     *                  If {@code length} is greater than the number
     *                  of characters in the document then additional
     *                  ' ' are added at the end and encrypted.
     * @param period    is the period of the sine wave used for encryption
     *                  and {@code period} must be a factor of
     *                  {@code length} other than 1 and {@code length} itself.
     * @param amplitude is the amplitude of the encrypting sine wave
     *                  and can be 64, 128, 256 or 512.
     * @return the encrypted array, with exactly one encrypted space
     * separating sentences.
     */
    public static int[] encrypt(OurDocument doc, int length, int period, int amplitude) {

        int[] encryptedArr = new int[length];
        char[] docCharArr = new char[length];
        int charNum = 0;
        char[] c = new char[length];

        Outerloop:
        for (int count = 1; count < doc.numSentences(); count++) {

            char[] tempArr = doc.getSentence(count).toCharArray();

            for (int id = 0; id < tempArr.length; id++) {

                c[charNum] = tempArr[id];
                charNum++;

                if (charNum + 1 == length) {
                    break Outerloop;
                }

            }

            c[charNum] = ' ';
            charNum++;
            if (charNum + 1 == length) {
                break;
            }

        }


        if (charNum + 1 < length) {

            while (charNum + 1 < length) {
                c[charNum] = ' ';
                charNum++;
            }

        }


        for (int i = 0; i < length; i++) {

            encryptedArr[i] = (int) ((int) c[i] + (amplitude * Math.sin((i * 2 * Math.PI / period) + (Math.PI / 4))));

        }

        return encryptedArr;
    }

    /**
     * Decrypt a text that has been encrypted using {@code Cryptography#encrypt}.
     * Returns the decoded text in a string.
     *
     * @param codedText An array of type int containing the data to decrypt.
     *
     * @return A string containing the decoded text.
     */
    public static String decrypt(int[] codedText) {

        ComplexNumber[] dftProc = dft(codedText);
        int amplitude = 0;
        double frequency = 0.0;
        String decodedText = null;

        for (int count = 0; count < dftProc.length / 2; count++){

            int modulus = (int) Math.round(Math.sqrt(Math.pow(2 * dftProc[count].re(), 2)+Math.pow(2 * dftProc[count].im(), 2)));

            if (modulus > amplitude){
                amplitude = modulus;
                frequency = (double) count / dftProc.length;

            }

        }

        amplitude = amplitude / dftProc.length;

        for (int j = 0; j < dftProc.length; j++){
            int dummyNum = 0;
            dummyNum = (int) Math.round(codedText[j] - (amplitude * Math.sin(2 * Math.PI * j * frequency) + (Math.PI / 4)));
            decodedText = decodedText + String.valueOf(dummyNum);
        }

        return decodedText;
    }
}

