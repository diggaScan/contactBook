package com.sunland.contactbook.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.sunland.contactbook.R;

import java.util.HashMap;


public class AlphabetList extends View {

    String TAG = this.getClass().getSimpleName();

    public static String[] words = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};

    private Context mContext;

    public HashMap<String, Integer> hasLettters;

    public void setHasLettters(HashMap<String, Integer> hasLettters) {
        this.hasLettters = hasLettters;
    }

    private int view_height;
    private int view_width;

    private int alpha_size = 25;

    private Paint alpha_paint;
    private Paint highlight_paint;
    private Paint circle_paint;

    private int alpha_height;

    private int pointed_alphabet = -1;
    private Paint.FontMetrics fontMetrics;

    private OnAlphabetTouchListener onAlphabetTouchListener;

    public AlphabetList(Context context) {
        this(context, null);
    }

    public AlphabetList(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlphabetList(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initRes();
    }

    public void initRes() {

        alpha_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        alpha_paint.setStyle(Paint.Style.FILL_AND_STROKE);
        alpha_paint.setTextSize(alpha_size);
        alpha_paint.setTextAlign(Paint.Align.CENTER);
        alpha_paint.setFakeBoldText(true);

        highlight_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        highlight_paint.setStyle(Paint.Style.FILL_AND_STROKE);
        highlight_paint.setTextSize(alpha_size);
        highlight_paint.setTextAlign(Paint.Align.CENTER);
        highlight_paint.setColor(Color.WHITE);

        circle_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circle_paint.setColor(mContext.getResources().getColor(R.color.colorPrimary));
        circle_paint.setStyle(Paint.Style.FILL_AND_STROKE);

        fontMetrics = alpha_paint.getFontMetrics();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        view_height = h;
        view_width = w;
        alpha_height = h / 27;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float cur_y;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                setBackgroundColor(Color.WHITE);

                cur_y = event.getY();
                pointed_alphabet = (int) (cur_y / alpha_height);

                if (pointed_alphabet < 0 || pointed_alphabet >= words.length)
                    return true;

                if (!hasLettters.containsKey(words[pointed_alphabet]))
                    return true;

                if (onAlphabetTouchListener != null) {
                    onAlphabetTouchListener.onTouch(pointed_alphabet, words[pointed_alphabet]);
                }
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                cur_y = event.getY();

                pointed_alphabet = (int) (cur_y / alpha_height);

                if (pointed_alphabet < 0 || pointed_alphabet >= words.length)
                    break;

                if (!hasLettters.containsKey(words[pointed_alphabet]))
                    break;

                if (onAlphabetTouchListener != null) {

                    onAlphabetTouchListener.onTouch(pointed_alphabet, words[pointed_alphabet]);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:

                if (onAlphabetTouchListener != null) {
                    onAlphabetTouchListener.onTouchFinished();
                }
                setBackgroundColor(Color.TRANSPARENT);
                pointed_alphabet = -1;
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < 27; i++) {

            int centerY = i * alpha_height + alpha_height / 2;
            int baseline = (int) (centerY - (fontMetrics.top + fontMetrics.bottom) / 2);

            if (pointed_alphabet == i) {
                float circleY = i * alpha_height + alpha_height / 2;
                canvas.drawCircle(view_width / 2, circleY, alpha_size - 8, circle_paint);
                canvas.drawText(words[i], view_width / 2, baseline, highlight_paint);
                continue;
            }
            canvas.drawText(words[i], view_width / 2, baseline, alpha_paint);
        }
    }

    public void setOnAlphabetTouchListener(OnAlphabetTouchListener onAlphabetTouchListener) {
        this.onAlphabetTouchListener = onAlphabetTouchListener;
    }

    public interface OnAlphabetTouchListener {

        void onTouch(int position, String letter);

        void onTouchFinished();
    }

}
