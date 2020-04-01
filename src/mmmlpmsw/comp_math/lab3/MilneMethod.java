package mmmlpmsw.comp_math.lab3;

import javafx.util.Pair;
import java.util.ArrayList;

public class MilneMethod {

    double step;
    private ArrayList<Pair<Double, Double>> values;
    Function function;

    public MilneMethod(ArrayList<Pair<Double, Double>> answers, double step, Function function) {
        values = answers;
        this.function = function;
        this.step = step;
        for (int i = 4; i < values.size(); i++)
            predictAndCorrect(i);
    }

    private void predictAndCorrect (int i) {
        double f3, f2, f1, f;
        f3 = function.getValue(values.get(i-3).getKey(), values.get(i-3).getValue());
        f2 = function.getValue(values.get(i-2).getKey(), values.get(i-2).getValue());
        f1 = function.getValue(values.get(i-1).getKey(), values.get(i-1).getValue());
        f = function.getValue(values.get(i-1).getKey()+step, values.get(i-4).getValue() + 4*step*(2*f3+f2+2*f1)/3);
        values.set(i, new Pair<>(values.get(i-1).getKey()+step, values.get(i-2).getValue() + step*(f+4*f1+f2)/3));
    }

    public ArrayList<Pair<Double, Double>> getValues() {
        return values;
    }
}
