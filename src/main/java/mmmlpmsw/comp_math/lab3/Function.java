package mmmlpmsw.comp_math.lab3;

public interface Function {
    default double getValue(double arg) {
        return 0;
    };
    default double getValue(double x, double y) {
        return 0;
    };
}
