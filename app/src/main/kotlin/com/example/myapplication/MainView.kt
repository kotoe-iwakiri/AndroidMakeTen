package com.example.myapplication

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainView: ConstraintLayout {

    constructor(context: Context?): super(context)
    constructor(context: Context?, attrs: AttributeSet?): super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr)
//    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int): super(context, attrs, defStyleAttr, defStyleRes)

    val numberEditText: EditText by lazy { findViewById(R.id.number_edit_text) as EditText }
    val executeButton: Button by lazy { findViewById(R.id.execute_button) as Button }
    val resultTextView: TextView by lazy { findViewById(R.id.result_text) as TextView }

    init {
        LayoutInflater.from(context).inflate(R.layout.activity_main, this)
    }

    fun getNumberText(): String {
        return numberEditText.text.toString()
    }

    fun setOnExecuteButtonClickListener(onClickListener: (View) -> Unit) {
        executeButton.setOnClickListener(onClickListener)
    }

    fun setResultText(text: String) {
        resultTextView.text = text
    }

}

