package com.carswaddle.carswaddlemechanic.extensions

import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView

fun TextView.addLinks(vararg links: Link) {
    val spannableString = SpannableString(this.text)
    for (link in links) {
        val clickableSpan = object : ClickableSpan() {

            override fun updateDrawState(textPaint: TextPaint) {
                textPaint.color = link.linkColor
                textPaint.isUnderlineText = link.underlineText
            }

            override fun onClick(view: View) {
                Selection.setSelection((view as TextView).text as Spannable, 0)
                view.invalidate()
                link.onClick.onClick(view)
            }
        }
        val startIndexOfLink = this.text.toString().indexOf(link.displayText)
        spannableString.setSpan(clickableSpan, startIndexOfLink, startIndexOfLink + link.displayText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    this.movementMethod = LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
    this.setText(spannableString, TextView.BufferType.SPANNABLE)
}

class Link(
    val displayText: String,
    val onClick: View.OnClickListener,
    val linkColor: Int,
    val underlineText: Boolean
)
