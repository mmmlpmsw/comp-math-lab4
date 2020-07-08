package mmmlpmsw.comp_math.lab3

import mmmlpmsw.comp_math.lab3.gui.UserInterface
import mmmlpmsw.comp_math.lab3.Function

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
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
}