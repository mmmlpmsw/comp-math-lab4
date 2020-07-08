package mmmlpmsw.comp_math.lab3.gui

import mmmlpmsw.comp_math.lab3.Function
import mmmlpmsw.comp_math.lab3.algorithm.MilneMethod
import mmmlpmsw.comp_math.lab3.spline_interpolation.SplinesStorage
import java.awt.GridLayout
import java.awt.event.ActionEvent
import javax.swing.*

class UserInterface(var baseFunction: Function) {
    private lateinit var mainFrame: JFrame
    private lateinit var interpolatedFunction: Function
    private lateinit var mainPanel: JPanel
    private lateinit var graphPanel: JPanel
    private lateinit var controlPanel: JPanel
    private var width = 0
    private var height = 0
    private val errorTitle = "Ошибка"
    private lateinit var milneMethod: MilneMethod
    fun draw(width: Int, height: Int) {
        SwingUtilities.invokeLater {
            createMainFrame(width, height)
            mainFrame.isVisible = true
            mainFrame.isResizable = false
        }
    }

    fun createMainFrame(width: Int, height: Int) {
        this.height = height
        this.width = width
        mainFrame = JFrame("Лабораторная работа №3")
        mainFrame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        mainFrame.setSize(width, height)
        mainPanel = JPanel(GridLayout(1, 2))
        mainFrame.contentPane = mainPanel
        controlPanel = JPanel(GridLayout(4, 1, 0, 0))
        mainPanel.add(controlPanel)
        createGraphPanel()
        createSelectFunctionPanel()
        val fieldsPanel = createFields()
        createMakeGraphButton(fieldsPanel)
    }

    private fun getGraphPanel(width: Int, height: Int): JPanel {
        val xValues = DoubleArray(milneMethod!!.values.size)
        val yValues = DoubleArray(milneMethod!!.values.size)
        for (i in milneMethod!!.values.indices) {
            xValues[i] = milneMethod!!.values[i].key
            yValues[i] = milneMethod!!.values[i].value
        }
        val splinesStorage = SplinesStorage(xValues, yValues)
        val interpolatedFunction = splinesStorage.interpolate()
        this.interpolatedFunction = interpolatedFunction
        val graphPanel = GraphMaker(baseFunction, this.interpolatedFunction, xValues).getChart(width, height)
        graphPanel.setLocation(0, 0)
        graphPanel.setSize(width, height)
        return graphPanel
    }

    private fun createSelectFunctionPanel() {
        val selectFunctionPanel = JPanel()
        val label = JLabel("Выберите уравнение")
        selectFunctionPanel.add(label)
        val selectedFunction = JComboBox<String>()
        controlPanel.add(selectFunctionPanel)
        selectedFunction.addItem("y' = -2x")
        selectedFunction.addItem("y' = sin(x) - y")
        selectedFunction.addItem("y' = e^x - y")
        selectedFunction.addItem("y' = 2y - x")
        selectFunctionPanel.add(selectedFunction)
        selectedFunction.addActionListener { e: ActionEvent ->
            val function = (e.source as JComboBox<*>).selectedItem as String
            when (function) {
                "y' = -2x" -> baseFunction = object : Function {
                    override fun getValue(arg: Double) = 0.0
                    override fun getValue(x: Double, y: Double) = -2 * x
                }

                "y' = sin(x) - y" -> baseFunction = object : Function {
                    override fun getValue(x: Double, y: Double) = Math.sin(x) - y
                    override fun getValue(arg: Double) = 0.0
                }

                "y' = e^x - y" -> baseFunction = object : Function {
                    override fun getValue(arg: Double) = 0.0
                    override fun getValue(x: Double, y: Double) = Math.exp(x) - y
                }

                "y' = 2y - x" -> baseFunction = object : Function {
                    override fun getValue(arg: Double) = 0.0
                    override fun getValue(x: Double, y: Double) = 2 * y - x
                }
            }
        }
    }

    private fun createGraphPanel() {
        graphPanel = JPanel(null)
        graphPanel!!.setSize(width, height)
        mainPanel!!.add(graphPanel)
    }

    private fun createFields(): JPanel {
        val fieldsPanel = JPanel(GridLayout(4, 2, 0, 3))
        controlPanel!!.add(fieldsPanel)
        val x0_Label = JLabel("Введите значение х0")
        fieldsPanel.add(x0_Label)
        val x0_Field = JTextField()
        fieldsPanel.add(x0_Field)
        val y0_Label = JLabel("Введите значение y0")
        fieldsPanel.add(y0_Label)
        val y0_Field = JTextField()
        fieldsPanel.add(y0_Field)
        val xn_Label = JLabel("Введите значение xn")
        fieldsPanel.add(xn_Label)
        val xn_Field = JTextField()
        fieldsPanel.add(xn_Field)
        val acc_Label = JLabel("Введите точность")
        fieldsPanel.add(acc_Label)
        val acc_Field = JTextField()
        fieldsPanel.add(acc_Field)
        return fieldsPanel
    }

    private fun createMakeGraphButton(fieldsPanel: JPanel) {
        val buttonPanel = JPanel(GridLayout(3, 0))
        controlPanel.add(buttonPanel)
        buttonPanel.add(JLabel())
        val mainButton = JButton("Решить уравнение")
        buttonPanel.add(mainButton)
        mainButton.addActionListener { event: ActionEvent? ->
            val x0: Double
            val y0: Double
            val xn: Double
            val acc: Double
            try {
                val x0Text = fieldsPanel.getComponent(1) as JTextField
                x0 = x0Text.text.replace(',', '.').toDouble()
                val y0Text = fieldsPanel.getComponent(3) as JTextField
                y0 = y0Text.text.replace(',', '.').toDouble()
                val xnText = fieldsPanel.getComponent(5) as JTextField
                xn = xnText.text.replace(',', '.').toDouble()
                val accText = fieldsPanel.getComponent(7) as JTextField
                acc = accText.text.replace(',', '.').toDouble()
                if (acc == 0.0 || x0 >= xn) throw NumberFormatException()
                val mm = MilneMethod(x0, y0, xn, acc, baseFunction)
                milneMethod = mm
            } catch (e: NumberFormatException) {
                JOptionPane.showMessageDialog(mainFrame, "Проверьте правильность введенных данных",
                        errorTitle, JOptionPane.ERROR_MESSAGE)
                return@addActionListener
            }
            mainPanel.remove(graphPanel)
            graphPanel = getGraphPanel(width, height)
            mainPanel.add(graphPanel)
            mainPanel.revalidate()
            mainPanel.repaint()
        }
        controlPanel.add(buttonPanel)
    }

}