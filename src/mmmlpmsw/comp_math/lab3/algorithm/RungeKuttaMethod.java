package mmmlpmsw.comp_math.lab3.algorithm;

import javafx.util.Pair;
import mmmlpmsw.comp_math.lab3.Function;

import java.util.ArrayList;

public class RungeKuttaMethod {

    private double step;
    private ArrayList<Pair<Double, Double>> values;
    private Function function;

    public RungeKuttaMethod(double x0, double y0, double xn, double accuracy, Function function) {
        this.step = Math.pow(accuracy, 0.25);
//        this.step = acc;
        this.function = function;
        values = new ArrayList<>();
        values.add(new Pair<>(x0, y0));
        double y_next = y0;
        for (double i = x0; i < xn; i += step) {
            y_next = getNextYValue(i, y_next);
            values.add(new Pair<>(i+step, y_next));
        }
    }

    private double getNextYValue(double x, double y) {
        double k0, k1, k2, k3, delta;
        k0 = function.getValue(x, y);
        k1 = function.getValue(x + step/2, y + k0 * step/2);
        k2 = function.getValue(x + step/2, y + k1 * step/2);
        k3 = function.getValue(x + step, y + k2 * step);
        delta = step * (k0 + 2 * k1 + 2 * k2 + k3)/6;
        return y + delta;
    }

    public ArrayList<Pair<Double, Double>> getValues() {
        return values;
    }
    public ArrayList<Pair<Double, Double>> getValues(int count) {
        ArrayList<Pair<Double, Double>> ans = new ArrayList<>();
        for (int i = 0; i < count; i ++) {
            ans.add(values.get(i));
        }
        return ans;
    }



}
