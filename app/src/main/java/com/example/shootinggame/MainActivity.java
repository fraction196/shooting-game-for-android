/*
 *メインとして呼び出されるクラス
 */

package com.example.shootinggame;

import android.os.Bundle;
import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.WindowInsets;
import android.view.WindowMetrics;
import android.graphics.Insets;
import android.view.WindowInsetsController;

public class MainActivity extends Activity
{
    private GLSurfaceView _glSurfaceView;
    private GameMaster _renderer;

    @Override
    //クラスを作成
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        _glSurfaceView = new GLSurfaceView(this);
        _renderer = new GameMaster(this);
        _glSurfaceView.setRenderer(_renderer);
        setContentView(_glSurfaceView);

        //画面サイズを取得する
        WindowMetrics windowMetrics = getWindowManager().getCurrentWindowMetrics();
        Insets insets = windowMetrics.getWindowInsets().getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());

        //ステータスバーのサイズを取得
        int statusBarHeight = getStatusBarHeight();

        //取得した画面サイズを代入
        _renderer._width = windowMetrics.getBounds().width() - statusBarHeight;
        _renderer._height = windowMetrics.getBounds().height();

        //ステータスバーとナビゲーションバーの両方を非表示にする
        WindowInsetsController windowInsetsController = getWindow().getInsetsController();
        if (windowInsetsController != null) {
            windowInsetsController.hide(WindowInsets.Type.systemBars());
            windowInsetsController.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        }


    }

    //ステータスバーのサイズ（高さ）を取得する関数
    public int getStatusBarHeight() {
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    @Override
    //フォーカスが再開したときに呼び出される関数
    protected void onResume()
    {
        //再開する
        super.onResume();
        _glSurfaceView.onResume();

    }

    @Override
    //フォーカスを失ったときに呼び出される関数
    protected void onPause()
    {
        //一時停止する
        super.onPause();
        _glSurfaceView.onPause();
    }

    @Override
    //画面がタッチされたときに呼び出される関数
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