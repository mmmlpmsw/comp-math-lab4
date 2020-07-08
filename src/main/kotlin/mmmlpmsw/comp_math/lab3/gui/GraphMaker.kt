package mmmlpmsw.comp_math.lab3.gui

import mmmlpmsw.comp_math.lab3.Function
import org.knowm.xchart.XChartPanel
import org.knowm.xchart.XYChart
import org.knowm.xchart.style.markers.SeriesMarkers
import java.awt.Color
import javax.swing.JPanel

class GraphMaker(private val baseFunction: Function, private val function: Function, private val xData: DoubleArray) {
    fun getChart(width: Int, height: Int): JPanel {
        val chart = XYChart(width, height)
        chart.styler.xAxisMin = xData[0]
        chart.styler.xAxisMax = xData[xData.size - 1]
        val yData = DoubleArray(xData.size)
        for (i in yData.indices)
            yData[i] = function.getValue(xData[i])
        val points = chart.addSeries("Узлы", xData, yData)
        points.marker = SeriesMarkers.CIRCLE
        points.markerColor = Color.BLUE
        points.lineColor = Color.WHITE
        val step = Math.abs(xData[xData.size - 1] - xData[0]) / width
        val xGraphing = DoubleArray(width)
        val yBaseFunction = DoubleArray(width)
        val ySpline = DoubleArray(width)
        for (i in yBaseFunction.indices) {
            val arg = xData[0] + step * i
            yBaseFunction[i] = baseFunction.getValue(arg)
            ySpline[i] = function.getValue(arg)
            xGraphing[i] = arg
        }
        chart.addSeries("Функция", xGraphing, ySpline).marker = SeriesMarkers.NONE
        return XChartPanel(chart)
    }

}
