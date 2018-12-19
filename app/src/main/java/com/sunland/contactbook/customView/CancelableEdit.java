package com.sunland.contactbook.customView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.lang.reflect.Method;

/**
 * Created by PeitaoYe on 2018/4/25.
 */

public class CancelableEdit extends AppCompatEditText {
    private String TAG="Cancel";
    private Context mContext;
    private Drawable mCancelButton;
    private Drawable mSearchIcon;
    private OnEditTextClickedListener onEditTextClicked;
    private OnTextChangeListener onTextChangeListener;

    public CancelableEdit(Context context) {
        this(context, null);
    }
//    private KeyBoardUtils mKeyboardUtils;

    public CancelableEdit(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CancelableEdit(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initWidget();
    }

    public void setOnEditTextClickedListener(OnEditTextClickedListener listener) {
        this.onEditTextClicked = listener;
    }

    private void initWidget() {
        setFocusableInTouchMode(true);
        initCancelButton();
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(onTextChangeListener!=null){
                    onTextChangeListener.beforeTextChange();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(onTextChangeListener!=null){
                    onTextChangeListener.onTextChange();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(onTextChangeListener!=null){
                    onTextChangeListener.afterTextChange();
                }
                CancelableEdit.this.initCancelButton();
                CancelableEdit.this.setSelection(CancelableEdit.this.length());
            }
        });

        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    setCompoundDrawables(mSearchIcon, null, null, null);
//                    if (mKeyboardUtils != null) {
//                        mKeyboardUtils.hideKeyboard();
//                    }
                } else {
                    if (CancelableEdit.this.getText().toString().length() != 0 && mCancelButton != null) {
                        setCompoundDrawables(mSearchIcon, null, mCancelButton, null);
                    }
                }
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mCancelButton = null;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP) {

            if(event.getX()>=getTotalPaddingLeft()&&event.getX()<=getWidth()-getTotalPaddingRight()&&onEditTextClicked!=null){
                onEditTextClicked.onEditTextClicked();
            }
            if (mCancelButton != null) {
                boolean isClicked = event.getX() > (getWidth() - getTotalPaddingRight()) && (event.getX() < (getWidth() - getPaddingRight()));
                if (isClicked) {
                    setText("");
                }
            }
        }
        return super.onTouchEvent(event);
    }

    private void initCancelButton() {
        if (getText().toString().length() > 0) {
            setCompoundDrawables(mSearchIcon, null, mCancelButton, null);
        } else {
            setCompoundDrawables(mSearchIcon, null, null, null);
        }
    }

    @Override
    public void setCompoundDrawables(@Nullable Drawable left, @Nullable Drawable top, @Nullable Drawable right, @Nullable Drawable bottom) {
        if (right != null) {
            mCancelButton = right;
            mSearchIcon=left;
        }
        super.setCompoundDrawables(left, top, right, bottom);
    }

    public void disableInput() {
        if (Build.VERSION.SDK_INT <= 10) {
            this.setInputType(InputType.TYPE_NULL);
        } else {
            Class ed = this.getClass();
            Method method;
            try {
                method = ed.getMethod("setShowSoftInputOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(this, false);
            } catch (Exception e) {

            }
            try {
                method = ed.getMethod("setSoftInputShownOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(this, false);
            } catch (Exception e) {

            }
        }
    }

    public void setOnTextChangeListener(OnTextChangeListener onTextChangeListener) {
        this.onTextChangeListener = onTextChangeListener;
    }

    public interface OnEditTextClickedListener{
        void onEditTextClicked();
    }

    public interface OnTextChangeListener{
        void beforeTextChange();
        void onTextChange();
        void afterTextChange();
    }
//    public void setKeyBoardUtils(KeyBoardUtils keyBoardUtils) {
//        this.mKeyboardUtils = keyBoardUtils;
//    }
}