package mmmlpmsw.comp_math.lab3.algorithm

import javafx.util.Pair
import mmmlpmsw.comp_math.lab3.Function
import java.util.*


class RungeKuttaMethod(x0: Double, y0: Double, xn: Double, private val step: Double, private val function: Function) {
    val values: ArrayList<Pair<Double, Double>> = ArrayList()
    private fun getNextYValue(x: Double, y: Double): Double {
        val k0 = function.getValue(x, y)
        val k1 = function.getValue(x + step / 2, y + k0 * step / 2)
        val k2 = function.getValue(x + step / 2, y + k1 * step / 2)
        val k3 = function.getValue(x + step, y + k2 * step)
        val delta = step * (k0 + 2 * k1 + 2 * k2 + k3) / 6
        return y + delta
    }

    fun getValues(count: Int): ArrayList<Pair<Double, Double>> {
        var ans = ArrayList<Pair<Double, Double>>()
        for (i in 0 until count) {
            ans.add(values[i])
        }
        return ans
    }

    init {
        values.add(Pair(x0, y0))
        var yNext = y0
        var i = x0
        while (i < xn) {
            yNext = getNextYValue(i, yNext)
            values.add(Pair(i + step, yNext))
            i += step
        }
    }
}
