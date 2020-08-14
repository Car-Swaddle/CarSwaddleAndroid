package com.carswaddle.carswaddleandroid.ui.activities.autoserviceDetails

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import com.carswaddle.carswaddleandroid.R
import org.w3c.dom.Text
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule

class NotesView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defStyleRes) {

    var notesDidChange: (text: String?) -> Unit = { }

    var notesText: String?
        get() = notesTextView.text.toString()
        set(value) { notesTextView.setTextWithNoListener(value, textWatcher) }

    private val notesTextView: EditText

    private lateinit var textWatcher: TextWatcher

    private var timer: Timer? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.notes_view, this, true)
        orientation = VERTICAL

        notesTextView = findViewById<EditText>(R.id.notesEditText)

        textWatcher = object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                timer?.cancel()
                timer = Timer()
                timer?.schedule(TimeUnit.SECONDS.toMillis(5)) {
                    notesDidChange(s?.toString())
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        notesTextView.addTextChangedListener(textWatcher)
    }

}


fun EditText.setTextWithNoListener(text: String?, textWatcher: TextWatcher) {
    removeTextChangedListener(textWatcher)
    setText(text)
    addTextChangedListener(textWatcher)
}
