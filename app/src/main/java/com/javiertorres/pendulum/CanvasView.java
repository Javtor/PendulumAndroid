package com.javiertorres.pendulum;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.javiertorres.pendulum.model.Pendulum;
import com.javiertorres.pendulum.threads.TimeThread;

public class CanvasView extends SurfaceView implements Callback {

    public static final float RADIUS = 25;

    private TimeThread thread;
    private MainActivity mainActivity;
    private Pendulum pendulum;


    public CanvasView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        getHolder().addCallback(this);
        setFocusable(true);
    }

    public void initialize() {
        mainActivity.defaultParameters();
        initPendulum();

        Canvas canvas = null;
        try {
            canvas = getHolder().lockCanvas();
            drawPend(0, 0, canvas);
        } catch (Exception e) {
        } finally {
            if (canvas != null) {
                try {
                    getHolder().unlockCanvasAndPost(canvas);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        thread = new TimeThread(getHolder(), this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Canvas canvas = null;
        try {
            canvas = getHolder().lockCanvas();
            draw(canvas);
        } catch (Exception e) {
        } finally {
            if (canvas != null) {
                try {
                    getHolder().unlockCanvasAndPost(canvas);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setPlaying(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    public void step(double deltaTime) {
        pendulum.step(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            float x = (float) pendulum.getX();
            float y = (float) -pendulum.getY();
            drawPend(x, y, canvas);
        }
    }

    private void drawPend(float rawX, float rawY, Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(4);

        float width = (float) getWidth() / 2;
        float height = (float) getHeight() / 2;

        rawX *= height / pendulum.getL();
        rawY *= height / pendulum.getL();

        rawX += width;
        rawY += height;

        float x = rawX;
        float y = rawY;

        canvas.drawLine(width, height * 3 / 4, rawX, rawY + height * 3 / 4, paint);

        paint.setColor(Color.RED);
        canvas.drawCircle(x, y + height * 3 / 4, RADIUS, paint);
    }


    public void start() {
        try {
            stop();
            initPendulum();
            thread = new TimeThread(getHolder(), this);
            thread.start();
        } catch (NumberFormatException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(getContext().getString(R.string.check_inputs))
                    .setCancelable(false)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(android.R.string.dialog_alert_title)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                        }
                    });

            AlertDialog alert = builder.create();
            alert.show();


        }

    }

    private void initPendulum() {
        mainActivity.getData();
    }

    public void stop() {
        thread.setPlaying(false);
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }


    public void initPendulum(double angle, double length, double mass, double gravity, double friction, double velocity) {
        pendulum = new Pendulum(angle, velocity, length, mass, gravity, friction);
    }
}
