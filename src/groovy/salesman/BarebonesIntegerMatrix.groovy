package salesman

import groovy.transform.CompileStatic

/**
 * Created by Jared on 12/19/2014.
 */
@CompileStatic
class BarebonesIntegerMatrix {
    public int[] data;
    public int numRows;
     public int numCols;


    public BarebonesIntegerMatrix(int numRows, int numCols, List<List<Integer>> listOfRows){
        this.numRows = numRows
        this.numCols = numCols

        data = listOfRows.flatten() as int[]
    }

    public BarebonesIntegerMatrix( int numRows  , int numCols ) {
        data = new int[ numRows * numCols ];

        this.numRows = numRows;
        this.numCols = numCols;
    }

    public void setRow(int rowNum, int[] row) {
//        println "Rownum $rowNum"
//        println "Row $row"
//        println "numCols $numCols"
        for (int i=0; i < numCols; i++) {
            set(rowNum,i,row[i])
        }
    }

    public int[] getRow(int row) {
        int[] ret = new int[this.numCols]
        for (int i=0; i < numCols; i++) {
            ret[i] = get(row,i)
        }
        return ret
    }

    public int get( int row , int col ) {
        return data[ row * numCols + col ];
    }

    public void set( int row , int col , int value ) {
        data[ row * numCols + col ] = value;
    }

    public int get( int index ) {
        return data[index];
    }

    /**
     * Sets the element's value at the specified index.  The element at which row and column
     * modified by this function depends upon the matrix's internal structure, e.g. row-major, column-major, or block.
     *
     * @param index Index of element that is to be set.
     * @param val The new value of the index.
     */
    public int set( int index , int val ) {
        // See benchmarkFunctionReturn.  Pointless return does not degrade performance.  Tested on JDK 1.6.0_21
        return data[index] = val;
    }

    public void print() {
        def out = System.out
        out.println("=====Matrix=====")
        for( int y = 0; y < this.numRows; y++ ) {
            for( int x = 0; x < this.numCols; x++ ) {
                out.print(this.get(y,x) + '\t');
            }
            out.println();
        }
        out.println("================")
    }

}
