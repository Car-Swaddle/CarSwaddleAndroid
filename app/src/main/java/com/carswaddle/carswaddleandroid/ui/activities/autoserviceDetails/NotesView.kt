package com.carswaddle.carswaddleandroid.ui.activities.autoserviceDetails

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import com.carswaddle.carswaddleandroid.R

class NotesView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defStyleRes) {

    var notesDidChange: (text: String?) -> Unit = { }

    private var notesText: String
        get() = notesTextView.text.toString()
        set(value) { notesTextView.setText(value) }

    private val notesTextView: EditText

    init {
        LayoutInflater.from(context).inflate(R.layout.notes_view, this, true)
        orientation = VERTICAL

        notesTextView = findViewById<EditText>(R.id.notesEditText)

        notesTextView.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                notesDidChange(s?.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

}
