package ktln.mmmlpmsw.comp_math.lab3.algorithm

import javafx.util.Pair
import ktln.mmmlpmsw.comp_math.lab3.Function
import java.util.*
import kotlin.math.abs

class MilneMethod(x0: Double, y0: Double, xn: Double, accuracy: Double, function: Function) {

    private var step = 0.0
    private var currentX = 0.0
    private var xn = 0.0
    private var accuracy = 0.0
    var values: ArrayList<Pair<Double, Double>> = RungeKuttaMethod(x0, y0, xn, accuracy, function).values
    private val function = function

    init {
        this.step = accuracy
        this.accuracy = accuracy
        this.xn = xn
        this.currentX = x0 + 3 * step
        calculate()
    }

    fun calculate() {
        var i = 3
        while (currentX < xn) {
            i ++
            currentX += step
            val f3 = function.getValue(values[i - 3].key, values[i - 3].value)
            val f2 = function.getValue(values[i - 2].key, values[i - 2].value)
            val f1 = function.getValue(values[i - 1].key, values[i - 1].value)
            var f = function.getValue(values[i - 1].key + step,
                    values[i - 4].value +
                            4 * step * (2 * f3 - f2 + 2 * f1) / 3)
            var y = f
            do {
                f = y
                y = values[i - 2].value + step * (f + 4 * f1 + f2) / 3
            } while (abs(f - y) / 29 > accuracy)
            values.add(Pair(values[i - 1].key +step, y))
        }
    }
}