package com.example.shootinggame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

public class Sprite2D
{
    //
    public boolean _visible = true;

    public int _textureNo;

    public Vector3D _pos = new Vector3D(0,0,0);

    public float _width;

    public float _height;

    public int _texX;

    public int _texY;

    public int _texWidth;

    public int _texHeight;
	public int hp = 0;
	public int number = 0;
	public boolean score_flag = false;
	public boolean hp_flag = false;
	public boolean flag = false;
	public boolean flag2 = false;
	public boolean flag3 = false;
	public boolean flag4 = false;
    public void setTexture(GL10 gl,Resources res,int id)
    {

    	InputStream is = res.openRawResource(id);

    	Bitmap bitmap;
    	try
    	{
    		bitmap = BitmapFactory.decodeStream(is);
    	}
    	finally
    	{
    		try
    		{
    			is.close();
    		}
    		catch(IOException e)
    		{
    		}
    	}
    	gl.glEnable(GL10.GL_ALPHA_TEST);
    	gl.glEnable(GL10.GL_BLEND);
    	gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
    	gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);

    	int[] textureNo = new int[1];
    	gl.glGenTextures(1, textureNo, 0);
    	_textureNo = textureNo[0];

    	gl.glBindTexture(GL10.GL_TEXTURE_2D, _textureNo);	

    	GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

    	gl.glTexParameterx(GL10.GL_TEXTURE_2D,
    			GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT );

    	gl.glTexParameterx(GL10.GL_TEXTURE_2D,
    			GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT );

    	gl.glTexParameterx(GL10.GL_TEXTURE_2D,
    			GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR );

    	gl.glTexParameterx(GL10.GL_TEXTURE_2D,
    			GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR );
    	
    	setPos(
    		0,bitmap.getHeight(),
    		bitmap.getWidth(),-bitmap.getHeight(),
    		0,0,0,
    		bitmap.getWidth(),bitmap.getHeight());
    }

    public void setPos(int texX,int texY,int texW,int texH,
    		float x,float y,float z,float w,float h)
    {
    	_texX = texX;
    	_texY = texY;
    	_texWidth = texW;
       	_texHeight = texH;
     	_pos = new Vector3D(x,y,z);
     	_width = w;
     	_height = h;
    }

    public void draw(GL10 gl)
    {
    	if ( !_visible )
    	{
    		return;
    	}
    	gl.glDisable(GL10.GL_DEPTH_TEST);
   		gl.glColor4x(0x10000, 0x10000, 0x10000, 0x10000);
   		gl.glActiveTexture(GL10.GL_TEXTURE0);
   		gl.glBindTexture(GL10.GL_TEXTURE_2D, _textureNo);

   		int rect[] = {_texX, _texY, _texWidth, _texHeight};

   		((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D,
   			GL11Ext.GL_TEXTURE_CROP_RECT_OES, rect, 0);

   		((GL11Ext) gl).glDrawTexfOES(
   				_pos._x, _pos._y, _pos._z, _width, _height);

   	   	gl.glEnable(GL10.GL_DEPTH_TEST);
	}

    public void draw(GL10 gl,float ratio)
    {
    	if ( !_visible )
    	{
    		return;
    	}
    	gl.glDisable(GL10.GL_DEPTH_TEST);

   		gl.glColor4x(0x10000, 0x10000, 0x10000, 0x10000);
   		gl.glActiveTexture(GL10.GL_TEXTURE0);
   		gl.glBindTexture(GL10.GL_TEXTURE_2D, _textureNo);

   		int rect[] = {_texX, _texY, _texWidth, _texHeight};

   		((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D,
   			GL11Ext.GL_TEXTURE_CROP_RECT_OES, rect, 0);
   		((GL11Ext) gl).glDrawTexfOES(
   				_pos._x, _pos._y, _pos._z,
   				_width*ratio, _height*ratio);

   	   	gl.glEnable(GL10.GL_DEPTH_TEST);
	}
    
    public boolean hit(float x,float y)
    {
    	if ( x >= _pos._x && x <= _pos._x + _width )
    	{
        	if ( y >= _pos._y && y <= _pos._y + _height )
        	{
        		return true;
        	}
        }
    	return false;
    }
}
