package mmmlpmsw.comp_math.lab3.spline_interpolation

import mmmlpmsw.comp_math.lab3.Function

class SplinesStorage (private val x: DoubleArray, private val y: DoubleArray) {

    private lateinit var splines: Array<Spline?>

    init {
        initSplines(x, y)
    }

    fun initSplines(x: DoubleArray, y: DoubleArray) {
        val n = x.size
        splines = arrayOfNulls(n)
        for (i in 0 until n) {
            splines[i] = Spline()
        }
        for (i in 0 until n) {
            splines[i]!!.x = x[i]
            splines[i]!!.a = y[i]
        }
        splines[0]!!.c = 0.0
        solveByTridiagonalMatrixAlgorithm(x, y, n)
    }

    private fun solveByTridiagonalMatrixAlgorithm(x: DoubleArray, y: DoubleArray, n: Int) {
        val alpha = DoubleArray(n - 1)
        val beta = DoubleArray(n - 1)
        alpha[0] = 0.0
        beta[0] = 0.0
        var hi: Double
        var hi_inc: Double
        var A = 0.0
        var B: Double
        var C = 0.0
        var F = 0.0
        for (i in 1 until n - 1) {
            hi = x[i] - x[i - 1]
            A = hi
            hi_inc = x[i + 1] - x[i]
            B = hi_inc
            C = (hi_inc + hi) * 2
            F = 6 * ((y[i + 1] - y[i]) / hi_inc - (y[i] - y[i - 1]) / hi)
            val t = A * alpha[i - 1] + C
            alpha[i] = -B / t
            beta[i] = (F - A * beta[i - 1]) / t
        }
        splines[n - 1]!!.c = (F - A * beta[n - 2]) / (C + A * alpha[n - 2])
        for (i in n - 2 downTo 1) splines[i]!!.c = alpha[i] * splines[i + 1]!!.c + beta[i]
        for (i in n - 1 downTo 1) {
            hi = x[i] - x[i - 1]
            splines[i]!!.d = (splines[i]!!.c - splines[i - 1]!!.c) / hi
            splines[i]!!.b = hi * (2 * splines[i]!!.c + splines[i - 1]!!.c) / 6 + (y[i] - y[i - 1]) / hi
        }
    }

    fun interpolate() = object : Function {
        override fun getValue(arg: Double): Double {
            return getInterpolatedY(arg)
        }
    }

    private fun getInterpolatedY(x: Double): Double {
        val spline: Spline?
        if (x >= splines[splines.size - 1]!!.x)
            spline = splines[splines.size - 1]
        else if (x <= splines[0]!!.x)
            spline = splines[0]
        else {
            var k: Int
            var left = 0
            var right = splines.size - 1
            while (right > left + 1) {
                k = left + (right - left) / 2
                if (x <= splines[k]!!.x) right = k else left = k
            }
            spline = splines[right]
        }
        val dx = x - spline!!.x
        return spline.a + spline.b * dx + spline.c * dx * dx / 2 + spline.d * dx * dx * dx / 6
    }

    inner class Spline {
        var a = 0.0
        var b = 0.0
        var c = 0.0
        var d = 0.0
        var x = 0.0

    }
}