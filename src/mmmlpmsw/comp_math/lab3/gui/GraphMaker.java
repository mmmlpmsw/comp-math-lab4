package mmmlpmsw.comp_math.lab3.gui;

import mmmlpmsw.comp_math.lab3.Function;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.SeriesMarkers;

import javax.swing.*;
import java.awt.*;

public class GraphMaker {

    private Function baseFunction;
    private Function function;
    private double[] xData;

    public GraphMaker (Function baseFunction, Function function, double[] xData) {
        this.baseFunction = baseFunction;
        this.function = function;
        this.xData = xData;
    }

    public JPanel getChart(int width, int height) {
        XYChart chart = new XYChart(width, height);
        chart.getStyler().setXAxisMin(xData[0]);
        chart.getStyler().setXAxisMax(xData[xData.length - 1]);

        double[] yData = new double[xData.length];
        for (int i = 0; i < yData.length; i++)
            yData[i] = function.getValue(xData[i]);
//            yData[i] = baseFunction.getValue(xData[i]);
        XYSeries points = chart.addSeries("Узлы", xData, yData);
        points.setMarker(SeriesMarkers.CIRCLE);
        points.setMarkerColor(Color.BLUE);
        points.setLineColor(Color.WHITE);

        double step = Math.abs(xData[xData.length - 1] - xData[0]) / width;
        double[] xGraphing = new double[width];
        double[] yBaseFunction = new double[width];
        double[] ySpline = new double[width];
        for (int i = 0; i < yBaseFunction.length; i++) {
            double arg = xData[0] + step * i;
            yBaseFunction[i] = baseFunction.getValue(arg);
            ySpline[i] = function.getValue(arg);
            xGraphing[i] = arg;
        }

//        chart.addSeries("Исходная функция", xGraphing, yBaseFunction).
//                setMarker(SeriesMarkers.NONE);
        chart.addSeries("Функция", xGraphing, ySpline).
                setMarker(SeriesMarkers.NONE);

        return new XChartPanel<>(chart);
    }
}
