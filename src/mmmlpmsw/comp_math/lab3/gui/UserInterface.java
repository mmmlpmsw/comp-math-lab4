package mmmlpmsw.comp_math.lab3.gui;

import mmmlpmsw.comp_math.lab3.Function;
import mmmlpmsw.comp_math.lab3.MilneMethod;
import mmmlpmsw.comp_math.lab3.RungeKuttaMethod;
import mmmlpmsw.comp_math.lab3.spline_interpolation.SplinesStorage;

import javax.swing.*;
import java.awt.*;

public class UserInterface {
    private JFrame mainFrame;
    private Function baseFunction;
    private Function interpolatedFunction;
    private JPanel mainPanel, graphPanel, controlPanel;
    private int width, height;
    private final String errorTitle = "Ошибка";
    private MilneMethod milneMethod;

    public UserInterface(Function baseFunction) {
        this.baseFunction = baseFunction;
    }

    public void draw(int width, int height) {
        SwingUtilities.invokeLater(() -> {
            createMainFrame(width, height);
            mainFrame.setVisible(true);
            mainFrame.setResizable(false);
        });
    }

    public void createMainFrame(int width, int height)  {
        this.height = height;
        this.width = width;

        mainFrame = new JFrame("Лабораторная работа №3");
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setSize(width, height);
        mainPanel = new JPanel(new GridLayout(1, 2));
        mainFrame.setContentPane(mainPanel);
        controlPanel = new JPanel(new GridLayout(4, 1, 0, 0));
        mainPanel.add(controlPanel);
        createGraphPanel();
        createSelectFunctionPanel();
        JPanel fieldsPanel = createFields();
        createMakeGraphButton(fieldsPanel);
//        createInterpolatedValueField();
    }

    private JPanel getGraphPanel(int width, int height) {

        double xValues[] = new double[milneMethod.getValues().size()],
                yValues[] = new double[milneMethod.getValues().size()];
        for (int i = 0; i < milneMethod.getValues().size(); i ++) {
            xValues[i] = milneMethod.getValues().get(i).getKey();
            yValues[i] = milneMethod.getValues().get(i).getValue();
        }

        Function interpolatedFunction = new SplinesStorage(xValues, yValues).interpolate();
        this.interpolatedFunction = interpolatedFunction;

        JPanel graphPanel = new GraphMaker(baseFunction, interpolatedFunction, xValues).
                getChart(width, height);
        graphPanel.setLocation(0, 0);
        graphPanel.setSize(width, height);
        return graphPanel;
    }
//    todo
    private void createSelectFunctionPanel() {
        JPanel selectFunctionPanel = new JPanel();
        JLabel label = new JLabel("Выберите уравнение");
        selectFunctionPanel.add(label);
        JComboBox<String> selectedFunction = new JComboBox<>();
        controlPanel.add(selectFunctionPanel);
        selectedFunction.addItem("y' = -2x");
//        selectedFunction.addItem("1/(x^4 + 4)");
//        selectedFunction.addItem("x^2");
        selectFunctionPanel.add(selectedFunction);

        selectedFunction.addActionListener(e -> {
            String function = (String)(((JComboBox)e.getSource()).getSelectedItem());
            switch (function) {
                case "y' = -2x":
                    this.baseFunction = new Function() {
                        @Override
                        public double getValue(double x, double y) {
                            return -2 * x;
                        }
                    };
                    break;
//                case "1/(x^4 + 4)":
//                    this.baseFunction = arg -> 1/(Math.pow(arg, 4) + 4);
//                    break;
//                case "x^2":
//                    this.baseFunction = arg -> arg*arg;
//                    break;
            }
        });
    }

    private void createGraphPanel() {
        graphPanel = new JPanel(null);
        graphPanel.setSize(width, height);
        mainPanel.add(graphPanel);
    }

    private JPanel createFields() {
        JPanel fieldsPanel = new JPanel(new GridLayout(4, 2, 0, 3));
        controlPanel.add(fieldsPanel);

        JLabel x0_Label = new JLabel("Введите значение х0");
        fieldsPanel.add(x0_Label);
        JTextField x0_Field = new JTextField();
        fieldsPanel.add(x0_Field);


        JLabel y0_Label = new JLabel("Введите значение y0");
        fieldsPanel.add(y0_Label);
        JTextField y0_Field = new JTextField();
        fieldsPanel.add(y0_Field);


        JLabel xn_Label = new JLabel("Введите значение xn");
        fieldsPanel.add(xn_Label);
        JTextField xn_Field = new JTextField();
        fieldsPanel.add(xn_Field);


        JLabel acc_Label = new JLabel("Введите точность");
        fieldsPanel.add(acc_Label);
        JTextField acc_Field = new JTextField();
        fieldsPanel.add(acc_Field);

        return fieldsPanel;
    }


    private void createMakeGraphButton(JPanel fieldsPanel) {
        JPanel buttonPanel = new JPanel(new GridLayout(3, 0));
        controlPanel.add(buttonPanel);
        buttonPanel.add(new JLabel());
        JButton mainButton = new JButton("Решить уравнение");
        buttonPanel.add(mainButton);
        mainButton.addActionListener(event -> {
            double x0, y0, xn, acc;
            try {
                JTextField x0Text = ((JTextField)fieldsPanel.getComponent(1));
                x0 = Double.parseDouble(x0Text.getText().replace(',', '.'));

                JTextField y0Text = ((JTextField)fieldsPanel.getComponent(3));
                y0 = Double.parseDouble(y0Text.getText().replace(',', '.'));

                JTextField xnText = ((JTextField)fieldsPanel.getComponent(5));
                xn = Double.parseDouble(xnText.getText().replace(',', '.'));

                JTextField accText = ((JTextField)fieldsPanel.getComponent(7));
                acc = Double.parseDouble(accText.getText().replace(',', '.'));

                if (acc == 0)
                    throw new NumberFormatException();

                RungeKuttaMethod rkm = new RungeKuttaMethod(x0, y0, xn, acc, baseFunction);
                MilneMethod mm = new MilneMethod(rkm.getValues(), acc, baseFunction);
                this.milneMethod = mm;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(mainFrame, "Проверьте правильность введенных данных",
                        errorTitle, JOptionPane.ERROR_MESSAGE);
                return;
            }
            mainPanel.remove(graphPanel);
            graphPanel = getGraphPanel(width, height);
            mainPanel.add(graphPanel);
            mainPanel.revalidate();
            mainPanel.repaint();
        });
        controlPanel.add(buttonPanel);
    }

    private void createInterpolatedValueField() {
        JPanel findValuePanel = new JPanel();
        controlPanel.add(findValuePanel);
        JLabel findValueLabel = new JLabel("Введите значение х, в котором нужно найти значение функции");
        findValuePanel.add(findValueLabel);
        JTextField findValueField = new JTextField(3);
        findValuePanel.add(findValueField);
        JLabel valueLabel = new JLabel(String.format("f(%s)=%s", "?", "?"));
        JButton findValueButton = new JButton("Найти");
        findValuePanel.add(valueLabel);
        findValuePanel.add(findValueButton);
        findValueButton.addActionListener(e -> {
            if (interpolatedFunction == null)
                JOptionPane.showMessageDialog(mainFrame, "Сначала интерполируйте функцию",
                        errorTitle, JOptionPane.ERROR_MESSAGE);
            else {
                try {
                    double value = interpolatedFunction.getValue(
                            Double.parseDouble(findValueField.getText().replace(',', '.')));
                    valueLabel.setText(String.format("f(%s)=%.3f", findValueField.getText(), value));
                } catch (NumberFormatException e1) {
                    JOptionPane.showMessageDialog(mainFrame, "Значение Х должно быть числом",
                            errorTitle, JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

}
