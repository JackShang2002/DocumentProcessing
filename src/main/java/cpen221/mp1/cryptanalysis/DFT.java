package cpen221.mp1.cryptanalysis;

public abstract class DFT {

    /**Taking an array of objects ComplexNumber as input, it performs the Discrete Fourier Transform
     * and returns it in an array of ComplexNumber objects.
     *
     * @param inSignal The input array of type ComplexNumber that will be transformed. Cannot be empty.
     *
     * @return The transformed data in an array of object ComplexNumber.
     * */
    public static ComplexNumber[] dft(ComplexNumber[] inSignal) {

        ComplexNumber[] tDFT = new ComplexNumber[inSignal.length];

        for (int k = 0; k < tDFT.length; k++){

            ComplexNumber dummyCNum = new ComplexNumber(0.0,0.0);

            for (int j = 0; j < tDFT.length; j++){
                double angle = 2 * Math.PI * k * j / inSignal.length;
                ComplexNumber trMatrix = new ComplexNumber(Math.cos(angle), - Math.sin(angle));
                ComplexNumber tempCNum = inSignal[j];

                tempCNum.multiply(trMatrix);
                dummyCNum.add(tempCNum);

            }
            tDFT[k] = dummyCNum;
        }
        return tDFT;
    }


    /**Taking an int array as input, it performs the Discrete Fourier Transform
     * and returns it in an array of ComplexNumber objects.
     *
     * @param inSignal The input array of type int that will be transformed. Cannot be empty.
     *
     * @return The transformed data in an array of object ComplexNumber.
     * */
    public static ComplexNumber[] dft(int[] inSignal) {

        ComplexNumber[] tDFT = new ComplexNumber[inSignal.length];

        for (int k = 0; k < tDFT.length; k++) {

            ComplexNumber dummyCNum = new ComplexNumber(0.0, 0.0);

            for (int j = 0; j < tDFT.length; j++){
                double angle = 2 * Math.PI * k * j / inSignal.length;
                ComplexNumber trMatrix = new ComplexNumber(Math.cos(angle), Math.sin(angle));
                ComplexNumber tempCNum = new ComplexNumber(inSignal[j], 0.0);
                tempCNum.multiply(trMatrix);
                dummyCNum.add(tempCNum);
            }
            tDFT[k] = dummyCNum;
        }

        return tDFT;
    }

}