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
import android.view.WindowInsetsController;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

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
        /*
        Display display = getWindowManager().getDefaultDisplay();
        _renderer._width = display.getWidth();
        _renderer._height = display.getHeight();

         */


        WindowMetrics windowMetrics = getWindowManager().getCurrentWindowMetrics();
        Insets insets = windowMetrics.getWindowInsets().getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
        int statusBarHeight = getStatusBarHeight();

        _renderer._width = windowMetrics.getBounds().width() - statusBarHeight;
        _renderer._height = windowMetrics.getBounds().height();

        WindowInsetsController windowInsetsController = getWindow().getInsetsController();
        if (windowInsetsController != null) {
            // systemBars : ステータスバーとナビゲーションバーの両方を非表示にする
            windowInsetsController.hide(WindowInsets.Type.systemBars());
            //windowInsetsController.hide(WindowInsets.Type.statusBars()); // ステータスバーのみ非表示にする場合
            //windowInsetsController.hide(WindowInsets.Type.navigationBars()); // ナビゲーションバーのみ非表示にする場合
            windowInsetsController.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        }


    }

    private int getStatusBarHeight() {
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        _glSurfaceView.onResume();

    }
    /*
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) hideSystemUI();
    }
    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }

     */
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