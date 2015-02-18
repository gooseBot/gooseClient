package time2go.goosegun.gooseController;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
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
    private int nozzelRange=41;
    private double feetPerPixel=0;
    private Activity callerActivity;
    private double lastAngleInDegrees=0;
    private int lastDistance=0;
    private String lText="err";
    private Matrix matrix = new Matrix();
    private Bitmap goose;
    private Bitmap nozzle;

    public TargetRangeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundResource(R.drawable.grass2);
        callerActivity = (Activity)context;
        paint.setAntiAlias(true);
        paint.setStrokeWidth(10f);
        paint.setColor(Color.CYAN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        goose = BitmapFactory.decodeResource(getResources(), R.drawable.goosemedium);
        nozzle = BitmapFactory.decodeResource(getResources(), R.drawable.nozzel2);
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = Math.min(getMeasuredWidth(), getMeasuredHeight());
        Context context = getContext();
        if(getResources().getDisplayMetrics().widthPixels > getResources().getDisplayMetrics().heightPixels)
        {   //landscape
            setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()/2);
            scannerOriginY=getMeasuredWidth()/2;
            scannerOriginX=getMeasuredWidth()/2;
            feetPerPixel=(double)nozzelRange/(getMeasuredWidth()/2);
        } else  { //Portrait
            setMeasuredDimension(size, size/2);
            scannerOriginY=size/2;
            scannerOriginX=size/2;
            feetPerPixel=(double)nozzelRange/(size/2);
        }
        eventX=getMeasuredWidth()/2;
        eventY=getMeasuredHeight()/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //if (drawTouchPointer) canvas.drawCircle (eventX, eventY, 50, paint);
        canvas.drawCircle(canvas.getWidth()/2,canvas.getHeight(),canvas.getWidth()/2,paint);
        matrix.reset();
        matrix.postTranslate(-nozzle.getWidth() / 2, -nozzle.getHeight() +65); // centers image
        matrix.postRotate((float)lastAngleInDegrees-90);
        matrix.postTranslate(scannerOriginX, scannerOriginY);
        canvas.drawBitmap(nozzle, matrix, null);
        matrix.reset();
        matrix.postTranslate(-goose.getWidth() / 2, -goose.getHeight() / 2); // Centers image
        matrix.postRotate(0);
        matrix.postTranslate(eventX, eventY);
        canvas.drawBitmap(goose, matrix, null);
        if (drawTouchPointer) canvas.drawLine(eventX, eventY + (goose.getHeight() / 2), scannerOriginX, scannerOriginY, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        UDPcommunication UDPcommunicationTask = new UDPcommunication();

        // Get the coordinates of the touch event and convert to polar
        eventX = event.getX();
        eventY = event.getY();
        if (eventY > this.getHeight()) eventY = this.getHeight();
        if (eventY < 0) eventY=0;
        if (eventX > this.getWidth()) eventX = this.getWidth();
        if (eventX < 0) eventX=0;
        int deltaX = scannerOriginX-(int)eventX;
        int deltaY = scannerOriginY-(int)eventY;
        double angleInDegrees = Math.atan2(deltaY, deltaX) * 180 / Math.PI;
        //double angleInDegrees = (eventX/this.getWidth())*180;
        int distance = (int)((Math.sqrt( Math.pow(deltaY,2) + Math.pow(deltaX,2)))*feetPerPixel);
        //int distance = (int)((this.getHeight()-eventY)*feetPerPixel);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawTouchPointer=true;
                UDPcommunicationTask.execute("von", callerActivity);
                break;
            case MotionEvent.ACTION_MOVE:
                drawTouchPointer=true;
                if ((Math.abs(angleInDegrees-lastAngleInDegrees)>.5) || (Math.abs(distance-lastDistance)>1)){
                    lastAngleInDegrees=angleInDegrees;
                    lastDistance=distance;
                    String targetCoordinates = String.format("%05.1f", lastAngleInDegrees) + String.format("%02d", lastDistance);
                    UDPcommunicationTask.execute("trg"+targetCoordinates, callerActivity);
                }
                break;
            case MotionEvent.ACTION_UP:
                drawTouchPointer=false;
                UDPcommunicationTask.execute("vof", callerActivity);
                break;
            default:
                return false;
        }

        TextView txt = (TextView) (callerActivity.findViewById(R.id.udpResponse));
        txt.setText("angle " + String.format("%.0f", angleInDegrees) + " distance " + String.format("%02d", distance));

        // Makes our view repaint and call onDraw
        invalidate();
        return true;
    }
}
