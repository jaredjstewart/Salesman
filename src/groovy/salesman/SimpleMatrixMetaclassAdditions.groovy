package salesman

import org.ejml.simple.SimpleMatrix


class SimpleMatrixMetaclassAdditions {

    static void apply() {
        println "metaclassing"
        SimpleMatrix.metaClass.getAt = { Integer row, Integer col -> delegate.get(row, col) }
        SimpleMatrix.metaClass.putAt = { ArrayList coords, Double x -> delegate.set(coords[0], coords[1], x) }
        SimpleMatrix.metaClass.div = { Double x -> delegate.divide(x) }
        SimpleMatrix.metaClass.multiply = { Double x -> delegate.scale(x) }

        SimpleMatrix.metaClass.static.newFilledMatrix = { Integer rows, Integer cols, double val ->
            SimpleMatrix ret = new SimpleMatrix(rows, cols)
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    ret[i, j] = val
                }
            }
            return ret
        }
        SimpleMatrix.metaClass.static.ones = { Integer x, Integer y -> SimpleMatrix.newFilledMatrix(x, y, 1) }
        SimpleMatrix.metaClass.static.zeros = { Integer x, Integer y -> SimpleMatrix.newFilledMatrix(x, y, 0) }
        SimpleMatrix.metaClass.setRow = { Integer row, ArrayList vals -> delegate.setRow(row, 0, *vals)
        }
        SimpleMatrix.metaClass.setColumn = { Integer col, ArrayList vals -> delegate.setColumn(col, 0, *vals) }
        SimpleMatrix.metaClass.getRowAsList = { Integer row ->
            def ret = []
            for (i in 0..<delegate.numCols()) {
                ret << delegate[row, i]
            }
            return ret.collect {it as Integer}
        }

        SimpleMatrix.metaClass.asType = { Class aClass ->
            return delegate.getRowAsList(0)
        }
    }
}


