package com.example.mp5;

import android.text.SpannableString;
import android.text.style.ClickableSpan;

abstract class CustomClickableSpan extends ClickableSpan {
    private SpannableString title;
    CustomClickableSpan(SpannableString setTitle) {
        this.title = setTitle;
    }
    String getTitle() {
        return this.title.toString();
    }
}
