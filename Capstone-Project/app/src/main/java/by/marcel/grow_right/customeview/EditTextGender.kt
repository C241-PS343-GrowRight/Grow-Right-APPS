package by.marcel.grow_right.customeview

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import by.marcel.grow_right.R
import com.google.android.material.textfield.TextInputEditText

class EditTextGender : TextInputEditText, View.OnTouchListener {

    private lateinit var clearButton: Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        clearButton = ContextCompat.getDrawable(context, R.drawable.baseline_close_24) as Drawable

        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Do nothing.
            }

            override fun afterTextChanged(s: Editable) {
                if (s.toString().isNotEmpty()) showClearButton() else hideClearButton()
                if (!isGenderValid(s.toString())) {
                    showError()
                } else {
                    hideError()
                }
            }
        })
    }

    private fun isGenderValid(gender: String): Boolean {
        return gender.equals("male", ignoreCase = true) || gender.equals("female", ignoreCase = true)
    }

    private fun showError() {
        error = context.getString(R.string.invalid_gender)
    }

    private fun hideError() {
        error = null
    }

    private fun showClearButton() {
        setButtonDrawables(endOfTheText = clearButton)
    }

    private fun hideClearButton() {
        setButtonDrawables()
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val clearButtonStart: Float
            val clearButtonEnd: Float
            var isClearButtonClicked = false

            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                clearButtonEnd = (clearButton.intrinsicWidth + paddingStart).toFloat()
                if (event.x < clearButtonEnd) isClearButtonClicked = true
            } else {
                clearButtonStart = (width - paddingEnd - clearButton.intrinsicWidth).toFloat()
                if (event.x > clearButtonStart) isClearButtonClicked = true
            }

            if (isClearButtonClicked) {
                return when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        clearButton = ContextCompat.getDrawable(context, R.drawable.baseline_close_24) as Drawable
                        showClearButton()
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        clearButton = ContextCompat.getDrawable(context, R.drawable.baseline_close_24) as Drawable
                        if (text != null) text?.clear()
                        hideClearButton()
                        true
                    }
                    else -> false
                }
            } else return false
        }
        return false
    }
}
