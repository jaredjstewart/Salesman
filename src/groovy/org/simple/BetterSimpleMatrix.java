package org.simple;
/*
 * Copyright (c) 2009-2014, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Efficient Java Matrix Library (EJML).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import groovy.transform.CompileStatic;
import org.ejml.alg.generic.GenericMatrixOps;
import org.ejml.data.DenseMatrix64F;
import org.ejml.data.ReshapeMatrix64F;
import org.ejml.ops.CommonOps;
import org.ejml.ops.RandomMatrices;
import org.ejml.ops.ReadMatrixCsv;
import org.ejml.simple.SimpleBase;

import java.io.InputStream;
import java.util.Random;


/**
 * <p>
 * {@link BetterSimpleMatrix} is a wrapper around {@link org.ejml.data.DenseMatrix64F} that provides an
 * easy to use object oriented interface for performing matrix operations.  It is designed to be
 * more accessible to novice programmers and provide a way to rapidly code up solutions by simplifying
 * memory management and providing easy to use functions.
 * </p>
 *
 * <p>
 * Most functions in BetterSimpleMatrix do not modify the original matrix.  Instead they
 * create a new BetterSimpleMatrix instance which is modified and returned.  This greatly simplifies memory
 * management and writing of code in general. It also allows operations to be chained, as is shown
 * below:<br>
 * <br>
 * BetterSimpleMatrix K = P.mult(H.transpose().mult(S.invert()));
 * </p>
 *
 * <p>
 * Working with both {@link org.ejml.data.DenseMatrix64F} and BetterSimpleMatrix in the same code base is easy.
 * To access the internal DenseMatrix64F in a BetterSimpleMatrix simply call {@link BetterSimpleMatrix#getMatrix()}.
 * To turn a DenseMatrix64F into a BetterSimpleMatrix use {@link BetterSimpleMatrix#wrap(org.ejml.data.DenseMatrix64F)}.  Not
 * all operations in EJML are provided for BetterSimpleMatrix, but can be accessed by extracting the internal
 * DenseMatrix64F.
 * </p>
 *
 * <p>
 * EXTENDING: BetterSimpleMatrix contains a list of narrowly focused functions for linear algebra.  To harness
 * the functionality for another application and to the number of functions it supports it is recommended
 * that one extends {@link org.ejml.simple.SimpleBase} instead.  This way the returned matrix type's of BetterSimpleMatrix functions
 * will be of the appropriate types.  See StatisticsMatrix inside of the examples directory.
 * </p>
 *
 * <p>
 * PERFORMANCE: The disadvantage of using this class is that it is more resource intensive, since
 * it creates a new matrix each time an operation is performed.  This makes the JavaVM work harder and
 * Java automatically initializes the matrix to be all zeros.  Typically operations on small matrices
 * or operations that have a runtime linear with the number of elements are the most affected.  More
 * computationally intensive operations have only a slight unnoticeable performance loss.  MOST PEOPLE
 * SHOULD NOT WORRY ABOUT THE SLIGHT LOSS IN PERFORMANCE.
 * </p>
 *
 * <p>
 * It is hard to judge how significant the performance hit will be in general.  Often the performance
 * hit is insignificant since other parts of the application are more processor intensive or the bottle
 * neck is a more computationally complex operation.  The best approach is benchmark and then optimize the code.
 * </p>
 *
 * <p>
 * If BetterSimpleMatrix is extended then the protected function {link #createMatrix} should be extended and return
 * the child class.  The results of BetterSimpleMatrix operations will then be of the correct matrix type.
 * </p>
 *
 * <p>
 * The object oriented approach used in BetterSimpleMatrix was originally inspired by Jama.
 * http://math.nist.gov/javanumerics/jama/
 * </p>
 *
 * @author Peter Abeles
 */
@CompileStatic
public class BetterSimpleMatrix extends SimpleBase<BetterSimpleMatrix> {

    /**
     * A simplified way to reference the last row or column in the matrix for some functions.
     */
    public static final int END = Integer.MAX_VALUE;

    /**
     * <p>
     * Creates a new matrix which has the same value as the matrix encoded in the
     * provided array.  The input matrix's format can either be row-major or
     * column-major.
     * </p>
     *
     * <p>
     * Note that 'data' is a variable argument type, so either 1D arrays or a set of numbers can be
     * passed in:<br>
     * BetterSimpleMatrix a = new BetterSimpleMatrix(2,2,true,new double[]{1,2,3,4});<br>
     * BetterSimpleMatrix b = new BetterSimpleMatrix(2,2,true,1,2,3,4);<br>
     * <br>
     * Both are equivalent.
     * </p>
     *
     * @see org.ejml.data.DenseMatrix64F#DenseMatrix64F(int, int, boolean, double...)
     *
     * @param numRows The number of rows.
     * @param numCols The number of columns.
     * @param rowMajor If the array is encoded in a row-major or a column-major format.
     * @param data The formatted 1D array. Not modified.
     */
    public BetterSimpleMatrix(int numRows, int numCols, boolean rowMajor, double... data) {
        mat = new DenseMatrix64F(numRows,numCols, rowMajor, data);
    }

    /**
     * <p>
     * Creates a matrix with the values and shape defined by the 2D array 'data'.
     * It is assumed that 'data' has a row-major formatting:<br>
     * <br>
     * data[ row ][ column ]
     * </p>
     *
     * @see org.ejml.data.DenseMatrix64F#DenseMatrix64F(double[][])
     *
     * @param data 2D array representation of the matrix. Not modified.
     */
    public BetterSimpleMatrix(double data[][]) {
        mat = new DenseMatrix64F(data);
    }

    /**
     * Creates a new matrix that is initially set to zero with the specified dimensions.
     *
     * @see org.ejml.data.DenseMatrix64F#DenseMatrix64F(int, int)
     *
     * @param numRows The number of rows in the matrix.
     * @param numCols The number of columns in the matrix.
     */
    public BetterSimpleMatrix(int numRows, int numCols) {
        mat = new DenseMatrix64F(numRows, numCols);
    }

    /**
     * Creats a new BetterSimpleMatrix which is identical to the original.
     *
     * @param orig The matrix which is to be copied. Not modified.
     */
    public BetterSimpleMatrix(BetterSimpleMatrix orig) {
        this.mat = orig.mat.copy();
    }

    /**
     * Creates a new BetterSimpleMatrix which is a copy of the DenseMatrix64F.
     *
     * @param orig The original matrix whose value is copied.  Not modified.
     */
    public BetterSimpleMatrix(DenseMatrix64F orig) {
        this.mat = orig.copy();
    }

    /**
     * Creates a new BetterSimpleMatrix which is a copy of the Matrix64F.
     *
     * @param orig The original matrix whose value is copied.  Not modified.
     */
    public BetterSimpleMatrix(ReshapeMatrix64F orig) {
        this.mat = new DenseMatrix64F(orig.numRows,orig.numCols);

        GenericMatrixOps.copy(orig, mat);
    }

    /**
     * Constructor for internal library use only.  Nothing is configured and is intended for serialization.
     */
    public BetterSimpleMatrix(){}

    /**
     * Creates a new BetterSimpleMatrix with the specified DenseMatrix64F used as its internal matrix.  This means
     * that the reference is saved and calls made to the returned BetterSimpleMatrix will modify the passed in DenseMatrix64F.
     *
     * @param internalMat The internal DenseMatrix64F of the returned BetterSimpleMatrix. Will be modified.
     */
    public static BetterSimpleMatrix wrap( DenseMatrix64F internalMat ) {
        BetterSimpleMatrix ret = new BetterSimpleMatrix();
        ret.mat = internalMat;
        return ret;
    }

    /**
     * Creates a new identity matrix with the specified size.
     *
     * @see org.ejml.ops.CommonOps#identity(int)
     *
     * @param width The width and height of the matrix.
     * @return An identity matrix.
     */
    public static BetterSimpleMatrix identity( int width ) {
        BetterSimpleMatrix ret = new BetterSimpleMatrix(width,width);

        CommonOps.setIdentity(ret.mat);

        return ret;
    }

    /**
     * <p>
     * Creates a matrix where all but the diagonal elements are zero.  The values
     * of the diagonal elements are specified by the parameter 'vals'.
     * </p>
     *
     * <p>
     * To extract the diagonal elements from a matrix see {@link #extractDiag()}.
     * </p>
     *
     * @see org.ejml.ops.CommonOps#diag(double...)
     *
     * @param vals The values of the diagonal elements.
     * @return A diagonal matrix.
     */
    public static BetterSimpleMatrix diag( double ...vals ) {
        DenseMatrix64F m = CommonOps.diag(vals);
        BetterSimpleMatrix ret = wrap(m);
        return ret;
    }

    /**
     * <p>
     * Creates a new BetterSimpleMatrix with random elements drawn from a uniform distribution from minValue to maxValue.
     * </p>
     *
     * @see org.ejml.ops.RandomMatrices#setRandom(org.ejml.data.DenseMatrix64F,java.util.Random)
     *
     * @param numRows The number of rows in the new matrix
     * @param numCols The number of columns in the new matrix
     * @param minValue Lower bound
     * @param maxValue Upper bound
     * @param rand The random number generator that's used to fill the matrix.  @return The new random matrix.
     */
    public static BetterSimpleMatrix random(int numRows, int numCols, double minValue, double maxValue, Random rand) {
        BetterSimpleMatrix ret = new BetterSimpleMatrix(numRows,numCols);
        RandomMatrices.setRandom(ret.mat, minValue, maxValue, rand);
        return ret;
    }

    /**
     * @inheritdoc
     */
    @Override
    protected BetterSimpleMatrix createMatrix( int numRows , int numCols ) {
        return new BetterSimpleMatrix(numRows,numCols);
    }

   public static BetterSimpleMatrix loadCSVfromStream (InputStream inputStream)throws Exception  {

        ReadMatrixCsv csv = new ReadMatrixCsv(inputStream);

        DenseMatrix64F ret = csv.read();

        inputStream.close();

        ReshapeMatrix64F mat = ret;

        // see if its a DenseMatrix64F
        if( mat instanceof DenseMatrix64F) {
            return BetterSimpleMatrix.wrap((DenseMatrix64F)mat);
        } else {
            // if not convert it into one and wrap it
            return BetterSimpleMatrix.wrap( new DenseMatrix64F(mat));
        }
    }

    static BetterSimpleMatrix newFilledMatrix(Integer rows, Integer cols, double val){
        BetterSimpleMatrix ret = new BetterSimpleMatrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                ret.set(i,j, val);
            }
        }
        return ret;
    }

    static BetterSimpleMatrix ones( Integer x, Integer y){ return newFilledMatrix(x, y, 1);}
   static BetterSimpleMatrix zeros( Integer x, Integer y){ return newFilledMatrix(x, y, 0);}

}
