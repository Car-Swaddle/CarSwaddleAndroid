package com.carswaddle.ui

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView

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
    val editText: EditText

    private var textWatcher: TextWatcher

    init {
        LayoutInflater.from(context).inflate(R.layout.labeled_edit_text, this, true)

        val a = context.obtainStyledAttributes(attrs, R.styleable.LabeledEditText)
        
        labelTextView = findViewById(R.id.labeledEditText_textView)
        editText = findViewById(R.id.labeledEditText_editText)

        textWatcher = object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textDidChange(editText.text.toString())
            }
        }

        editText.addTextChangedListener(textWatcher)

        val defaultText = a.getText(R.styleable.LabeledEditText_label) as? String
        if (defaultText != null) {
            labelText = defaultText
        }

        val textValue = a.getText(R.styleable.LabeledEditText_value) as? String
        if (textValue != null) {
            editTextValue = textValue
        }

        if (a.hasValue(R.styleable.LabeledEditText_android_inputType)) {
            val inputType = a.getInt(R.styleable.LabeledEditText_android_inputType, 0)
            editText.inputType = inputType
        } else {
            editText.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
        }
    }

}