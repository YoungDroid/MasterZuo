package com.oom.masterzuo.viewmodel.base.edittext;

import android.databinding.BindingAdapter;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.widget.EditText;

/**
 * Created by YoungDroid on 16-3-24.
 * Email: YoungDroid@163.com
 */
public final class ViewBindingAdapter {
    public static final String TAG = "YoungDroid";

    @BindingAdapter( { "isShowPassword" })
    public static void showPassword( EditText editText, boolean isShow ) {
        if ( !isShow ) {
            if ( editText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD ) ) return;
            editText.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );
        } else {
            if ( editText.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD ) return;
            editText.setInputType( InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD );
        }
        //切换后将EditText光标置于末尾
        CharSequence charSequence = editText.getText();
        if ( charSequence != null ) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
    }
    
    @BindingAdapter( { "editable" })
    public static void setEditable( EditText editText, boolean editable ) {
        if ( !editable ) {
            editText.setOnKeyListener( null );
        }
    }
}

