package time2go.goosegun.gooseController;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MainDrawingView extends View {
    private Paint paint = new Paint();
    private Path path = new Path();

    public MainDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Get the coordinates of the touch event.
        float eventX = event.getX();
        float eventY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Set a new starting point
                path.moveTo(eventX, eventY);
                return true;
            case MotionEvent.ACTION_MOVE:
                // Connect the points
                path.lineTo(eventX, eventY);
                break;
            default:
                return false;
        }
        // Makes our view repaint and call onDraw
        invalidate();
        return true;
    }
}