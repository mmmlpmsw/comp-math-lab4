package mmmlpmsw.comp_math.lab3;

import mmmlpmsw.comp_math.lab3.gui.UserInterface;

public class Main {

    public static void main(String[] args) {

        UserInterface userInterface = new UserInterface(new Function() {
            @Override
            public double getValue(double x, double y) {
                return -2 * x;
            }
        });
        userInterface.draw(1500, 700);
    }
}
