package eu.ase.ro.ContactManagement.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BarChartView extends View {
    private Paint paint = new Paint();
    private List<Pair<String, Integer>> data = new ArrayList<>();

    public BarChartView(Context context) {
        super(context);
    }

    public BarChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BarChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // This method can be used to set the data that the bar chart will display
    public void setData(List<Pair<String, Integer>> data) {
        this.data = data;
        invalidate(); // This will trigger onDraw to be called
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (data.isEmpty()) {
            return;
        }

        float maxValue = Collections.max(data, new Comparator<Pair<String, Integer>>() {
            @Override
            public int compare(Pair<String, Integer> pair1, Pair<String, Integer> pair2) {
                return pair1.second.compareTo(pair2.second);
            }
        }).second;

        float barWidth = getWidth() / data.size();
        float unitValue = getHeight() / maxValue;

        paint.setTextSize(barWidth / 3);

        for (int i = 0; i < data.size(); i++) {
            Pair<String, Integer> bar = data.get(i);

            float left = i * barWidth;
            float top = getHeight() - bar.second * unitValue;
            float right = left + barWidth;
            float bottom = getHeight();

            // Draw bar
            paint.setColor(Color.BLUE);
            canvas.drawRect(left, top, right, bottom, paint);

            // Draw text
            paint.setColor(Color.BLACK);
            canvas.drawText(bar.first, left, bottom + paint.getTextSize(), paint);
        }
    }
}