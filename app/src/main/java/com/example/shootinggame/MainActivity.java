/*
 *メインのクラス
 */
package com.example.shootinggame;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.PixelFormat;
import android.app.Activity;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.Context;
import android.opengl.GLSurfaceView;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowInsets;
import android.view.WindowMetrics;
import android.graphics.Insets;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.LinearLayout;

public class MainActivity extends Activity
{
    private GLSurfaceView _glSurfaceView;
    private GameMaster _renderer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        _glSurfaceView = new GLSurfaceView(this);
        _renderer = new GameMaster(this);
        _glSurfaceView.setRenderer(_renderer);
        setContentView(_glSurfaceView);
        Display display = getWindowManager().getDefaultDisplay();
        _renderer._width = display.getWidth();
        _renderer._height = display.getHeight();
        /*
        WindowMetrics windowMetrics = getWindowManager().getCurrentWindowMetrics();
        Insets insets = windowMetrics.getWindowInsets().getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
        _renderer._width = windowMetrics.getBounds().width();
        _renderer._height = windowMetrics.getBounds().height();
        */
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        _glSurfaceView.onResume();

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        _glSurfaceView.onPause();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                _renderer.actionDown(x,y);
                break;
            case MotionEvent.ACTION_MOVE:
                _renderer.actionMove(x,y);
                break;
            case MotionEvent.ACTION_UP:
                _renderer.actionUp(x,y);
                break;
        }
        return true;
    }
}