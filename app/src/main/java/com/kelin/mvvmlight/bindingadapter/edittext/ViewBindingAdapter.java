package com.kelin.mvvmlight.bindingadapter.edittext;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.oom.masterzuo.utils.StringUtils;

import rx.Observable;

/**
 * Created by kelin on 16-3-24.
 */
public final class ViewBindingAdapter {
    public static final String TAG = "YoungDroid";

    @BindingAdapter({ "onSendCommand" })
    public static void setActionListener( EditText editText, ReplyCommand onSendCommand ) {
        editText.setImeOptions( EditorInfo.IME_ACTION_SEND );
        editText.setOnEditorActionListener( ( v, actionId, event ) -> {
            if ( actionId == EditorInfo.IME_ACTION_SEND || ( event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER ) ) {
                //do something;
                String etContent = editText.getText().toString();

                if ( !StringUtils.isBlank( etContent ) && !etContent.equals( "" ) ) {
                    onSendCommand.execute();
                } else {
                    etContent += "\n";
                    editText.setText( etContent );
                    editText.setSelection( etContent.length() );
                }

                return true;
            }
            return false;
        } );
    }
    
    @BindingAdapter({ "onEnterCommand" })
    public static void setEnterListener( EditText editText, boolean onEnterCommand ) {
        if ( onEnterCommand ) {
            editText.setImeOptions( EditorInfo.IME_ACTION_DONE );
            editText.setOnEditorActionListener( ( v, actionId, event ) -> {
                if ( actionId == EditorInfo.IME_ACTION_DONE || ( event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER ) ) {
                    //do something;
                    String etContent = editText.getText().toString();
            
                    etContent += "\n";
                    editText.setText( etContent );
                    editText.setSelection( etContent.length() );
            
                    return true;
                }
                return false;
            } );
        }
    }

    @android.databinding.BindingAdapter({ "requestFocus" })
    public static void requestFocusCommand( EditText editText, final Boolean needRequestFocus ) {
        if ( needRequestFocus ) {
            editText.setFocusableInTouchMode( true );
            editText.setSelection( editText.getText().length() );
            editText.requestFocus();
            InputMethodManager imm = ( InputMethodManager ) editText.getContext().getSystemService( Context.INPUT_METHOD_SERVICE );
            imm.showSoftInput( editText, InputMethodManager.SHOW_IMPLICIT );
        } else {
            editText.setEnabled( false );
            editText.setEnabled( true );
        }

    }


    @android.databinding.BindingAdapter(value = { "beforeTextChangedCommand", "onTextChangedCommand", "afterTextChangedCommand" }, requireAll = false)
    public static void editTextCommand( EditText editText, final ReplyCommand< TextChangeDataWrapper > beforeTextChangedCommand, final ReplyCommand< TextChangeDataWrapper > onTextChangedCommand, final ReplyCommand< String > afterTextChangedCommand ) {
        editText.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged( CharSequence s, int start, int count, int after ) {
                if ( beforeTextChangedCommand != null ) {
                    beforeTextChangedCommand.execute( new TextChangeDataWrapper( s, start, count, count ) );
                }
            }

            @Override
            public void onTextChanged( CharSequence s, int start, int before, int count ) {
                if ( onTextChangedCommand != null ) {
                    onTextChangedCommand.execute( new TextChangeDataWrapper( s, start, before, count ) );
                }
            }

            @Override
            public void afterTextChanged( Editable s ) {
                if ( afterTextChangedCommand != null ) {
                    afterTextChangedCommand.execute( s.toString() );
                }
            }
        } );
    }

    public static class TextChangeDataWrapper {
        public CharSequence s;
        public int start;
        public int before;
        public int count;

        public TextChangeDataWrapper( CharSequence s, int start, int before, int count ) {
            this.s = s;
            this.start = start;
            this.before = before;
            this.count = count;
        }
    }
}

