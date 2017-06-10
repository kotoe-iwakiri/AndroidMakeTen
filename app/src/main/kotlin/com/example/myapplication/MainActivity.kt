package com.example.myapplication

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = MainView(applicationContext)
        view.setOnExecuteButtonClickListener {
            val number = view.getNumberText()
            val numbers = number.toCharArray().map { it.toString().toInt() }
            if (numbers.size == 4) {
                view.setResultText("calculating...")
                val (n1, n2, n3, n4) = numbers
                val result = MakeTen.canMake(n1, n2, n3, n4)
                view.setResultText(result.toString())
            } else {
                view.setResultText("input 4 digits!")
            }
        }
        setContentView(view)
    }
}
