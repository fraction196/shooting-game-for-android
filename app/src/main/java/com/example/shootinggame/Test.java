package com.example.shootinggame;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.Context;
import android.opengl.GLSurfaceView;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Test implements GLSurfaceView.Renderer
{

    private Context _context;
    public int _width;
    public int _height;
    public boolean _touch;
    private Sprite2D fighter = new Sprite2D();

    //@Override
    public Test(Context context)
    {
        _context = context;
    }

    //@Override
    public void onDrawFrame(GL10 gl)
    {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT
                | GL10.GL_DEPTH_BUFFER_BIT);
        if(_touch){
            fighter._pos._x += 10;
        }
        else{
            fighter._pos._x = 0;
        }
        fighter.draw(gl);
    }

    //@Override
    public void onSurfaceChanged(
            GL10 gl, int width, int height)
    {
    }

    //@Override
    public void onSurfaceCreated(
            GL10 gl, EGLConfig config)
    {

        gl.glClearColor(1.0f,1.0f,1.0f,1.0f);
        gl.glDisable(GL10.GL_DITHER);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glEnable(GL10.GL_ALPHA_TEST);
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        fighter.setTexture(gl,_context.getResources(),R.drawable.fighter);
    }

    public void actionDown(float x,float y)
    {
        _touch = true;
    }

    public void actionMove(float x,float y)
    {
    }

    public void actionUp(float x,float y)
    {
        _touch = false;
    }
}