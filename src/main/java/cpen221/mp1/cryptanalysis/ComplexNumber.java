package cpen221.mp1.cryptanalysis;

import java.util.Arrays;

public class ComplexNumber {
    private Double realNum;
    private Double imagNum;

    /**Constructs a ComplexNumber object with given real and complex part in type double.
     *
     * @param real The real part of the number to be transformed into a ComplexNumber object.
     *
     * @param imaginary The imaginary part of the number to be transformed into a ComplexNumber object.
     * */

    public ComplexNumber(double real, double imaginary) {
        realNum = real;
        imagNum = imaginary;
    }

    /**Constructs a ComplexNumber object for 0.
     *
     * */
    public ComplexNumber() {
        new ComplexNumber(0,0);
    }

    /**Taking another ComplexNumber seed, create a new ComplexNumber object that takes the values of seed fields.
     *
     * @param seed The ComplexNumber object to be duplicated.
     * */
    public ComplexNumber(ComplexNumber seed) {
        this.realNum = seed.re();
        this.imagNum = seed.im();
    }

    /**Adds a ComplexNumber object 'other' to a ComplexNumber object.
     *
     * @param other The ComplexNumber to be added.
     * */
    public void add(ComplexNumber other) {
        this.realNum += other.realNum;
        this.imagNum += other.imagNum;
    }

    /**Multiplies a ComplexNumber object 'other' to a ComplexNumber object.
     *
     * @param other The ComplexNumber to be multiplied.
     * */
    public void multiply(ComplexNumber other) {
        this.realNum = this.realNum * other.realNum;
        this.imagNum = this.imagNum * other.imagNum;
    }

    /**Converts a ComplexNumber object into a string in the form of a+bi, where 'a' is the real part of
     * the ComplexNumber and 'b' is the imaginary part.
     *
     * @return A string in the form of a+bi of a ComplexNumber object.
     * */
    public String toString() {
        String realString = String.valueOf(realNum);
        String imagineString = String.valueOf(imagNum);
        String reImString = realString + "+" + imagineString + "i";
        return reImString;
    }

    /**Gets the real part of a ComplexNumber object.
     *
     * @return The real part of a ComplexNumber object.
     * */
    public double re() {
        double real = realNum;
        return real;
    }

    /**Gets the imaginary part of a ComplexNumber object.
     *
     * @return The imaginary part of a ComplexNumber object.
     * */
    public double im() {
        double imagine = imagNum;
        return imagine;
    }

    /**Checks if one ComplexNumber object is equal to another, and returns a boolean value.
     *
     * @param other The ComplexNumber object to be compared.
     *
     * @return A boolean value true or false.
     * */
    @Override
    public boolean equals(Object other) {
        if (other == null || this.getClass() != other.getClass()){
            return false;
        }
        ComplexNumber that = (ComplexNumber) other;

       return this.realNum == that.realNum && this.imagNum == that.imagNum;
    }

    /**Checks the hashCode of an object and returns it.
     *
     * @return The hashcode of an object in an integer.
     * */
    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.realNum, this.imagNum});
    }

}