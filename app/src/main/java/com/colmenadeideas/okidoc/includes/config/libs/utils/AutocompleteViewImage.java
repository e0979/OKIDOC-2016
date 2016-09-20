package com.colmenadeideas.okidoc.includes.config.libs.utils;


import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.AutoCompleteTextView;

import java.util.HashMap;

public class AutocompleteViewImage extends AutoCompleteTextView {
    public AutocompleteViewImage(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public AutocompleteViewImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public AutocompleteViewImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    // this is how to disable AutoCompleteTextView filter
    @Override
    protected void performFiltering(final CharSequence text, final int keyCode) {
        String filterText = "";
        super.performFiltering(filterText, keyCode);
    }

}