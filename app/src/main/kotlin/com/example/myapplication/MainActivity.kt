package com.example.myapplication

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = MainView(applicationContext)
        view.setOnExecuteButtonClickListener {
            println("start onClickListener")
            val number = view.getNumberText()
            val numbers = number.toCharArray().map { it.toString().toInt() }
            if (numbers.size == 4) {
                val (n1, n2, n3, n4) = numbers
                val result = MakeTen.canMake(n1, n2, n3, n4)
                view.setResultText("${number}: ${result}")
            } else {
                view.setResultText("input 4 digits!")
            }
            println("end onClickListener")
        }
        setContentView(view)
    }
}
