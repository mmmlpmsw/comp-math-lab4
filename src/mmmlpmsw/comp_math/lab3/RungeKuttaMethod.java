package mmmlpmsw.comp_math.lab3;

import javafx.util.Pair;
import java.util.ArrayList;

public class RungeKuttaMethod {

    private double step;
    private ArrayList<Pair<Double, Double>> values;
    private Function function;

    public RungeKuttaMethod(double x_start, double y_start, double x_end, double acc, Function function) {
        this.step = acc;
        this.function = function;
        values = new ArrayList<>();
        values.add(new Pair<>(x_start, y_start));
        double y_next = y_start;
        for (double i = x_start; i < x_end; i += step) {
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

}
