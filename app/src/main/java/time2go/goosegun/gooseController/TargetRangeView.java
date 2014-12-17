package time2go.goosegun.gooseController;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class TargetRangeView extends View {

    private Paint paint = new Paint();
    private float eventX = 0 ;
    private float eventY = 0;
    private boolean drawTouchPointer=false;
    private int scannerOriginX=0;
    private int scannerOriginY=0;

    public TargetRangeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = Math.min(getMeasuredWidth(), getMeasuredHeight());
        setMeasuredDimension(size, size/2);
        scannerOriginY=size/2;
        scannerOriginX=size/2;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        if (drawTouchPointer) canvas.drawCircle (eventX, eventY, 50, paint);
        canvas.drawCircle(canvas.getWidth()/2,canvas.getHeight(),canvas.getWidth()/2,paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawTouchPointer=true;
                break;
            case MotionEvent.ACTION_MOVE:
                drawTouchPointer=true;
                break;
            case MotionEvent.ACTION_UP:
                drawTouchPointer=false;
                break;
            default:
                return false;
        }

        // Get the coordinates of the touch event.
        eventX = event.getX();
        eventY = event.getY();
        if (eventY > this.getHeight()) eventY = this.getHeight();
        if (eventY < 0) eventY=0;
        if (eventX > this.getWidth()) eventX = this.getWidth();
        if (eventX < 0) eventX=0;
        int deltaX = scannerOriginX-(int)eventX;
        int deltaY = scannerOriginY-(int)eventY;
        double angleInDegrees = Math.atan2(deltaY, deltaX) * 180 / Math.PI;
        double distance = Math.sqrt( Math.pow(deltaY,2) + Math.pow(deltaX,2));

        TextView txt = (TextView) ((Activity)getContext()).findViewById(R.id.text1);
        txt.setText("angle " + String.format("%.0f", angleInDegrees) + " distance " + String.format("%.0f", distance));

        // Makes our view repaint and call onDraw
        invalidate();
        return true;
    }
}
