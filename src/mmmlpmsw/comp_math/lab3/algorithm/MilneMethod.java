package mmmlpmsw.comp_math.lab3.algorithm;

import javafx.util.Pair;
import mmmlpmsw.comp_math.lab3.Function;

import java.util.ArrayList;

public class MilneMethod {

    private double step, currentX, xn;
    private ArrayList<Pair<Double, Double>> values;
    private Function function;

    public MilneMethod(double x0, double y0, double xn, double step, Function function) {
        values = new RungeKuttaMethod(x0, y0, xn, step, function).getValues(4);
        this.function = function;
        this.step = step;
        this.xn = xn;
        this.currentX = x0 + 3 * step;
        while (currentX < xn)
            calculate();
    }

    private void calculate() {
        int i = 3;
        while (currentX < xn) {
            i ++;
            currentX += step;
            double f3, f2, f1, f;
            f3 = function.getValue(values.get(i - 3).getKey(),
                    values.get(i - 3).getValue());
            f2 = function.getValue(values.get(i - 2).getKey(),
                    values.get(i - 2).getValue());
            f1 = function.getValue(values.get(i - 1).getKey(),
                    values.get(i - 1).getValue());
            f = function.getValue(values.get(i-1).getKey() + step,
                    values.get(i - 4).getValue() +
                            4 * step * (2 * f3 + f2 + 2 * f1)/3);
            double y = f;
            do {
                f = y;
                y = values.get(i-2).getValue() + step*(f + 4 * f1 + f2)/3;
            } while (Math.abs(f - y)/29 > step);
            values.add(new Pair<>(values.get(i-1).getKey()+step, y));
        }
    }

    public ArrayList<Pair<Double, Double>> getValues() {
        return values;
    }
}
