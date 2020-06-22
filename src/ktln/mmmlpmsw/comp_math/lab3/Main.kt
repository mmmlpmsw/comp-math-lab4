package ktln.mmmlpmsw.comp_math.lab3

import ktln.mmmlpmsw.comp_math.lab3.gui.UserInterface

fun main() {
    val userInterface = UserInterface(object : Function {
        override fun getValue(arg: Double): Double {
            return 0.0
        }

        override fun getValue(x: Double, y: Double): Double {
            return -2 * x
        }
    })
    userInterface.draw(1500, 700)
}