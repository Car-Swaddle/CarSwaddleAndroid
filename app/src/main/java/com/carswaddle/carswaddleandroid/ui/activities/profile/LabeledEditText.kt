package com.carswaddle.carswaddleandroid.ui.activities.profile

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.carswaddle.carswaddleandroid.R

class LabeledEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var textDidChange: (text: String?) -> Unit = { }


    var labelText: String?
        get() = labelTextView.text?.toString()
        set(value) {
            labelTextView.text = value
        }

    var editTextValue: String?
        get() = editText.text.toString()
        set(value) {
            editText.setText(value)
        }

    private val labelTextView: TextView
    private val editText: EditText

    private lateinit var textWatcher: TextWatcher

    init {
        LayoutInflater.from(context).inflate(R.layout.labeled_edit_text, this, true)

        labelTextView = findViewById<TextView>(R.id.labeledEditText_textView)
        editText = findViewById<EditText>(R.id.labeledEditText_editText)

        textWatcher = object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textDidChange(editText.text.toString())
            }
        }

        editText.addTextChangedListener(textWatcher)
    }

}