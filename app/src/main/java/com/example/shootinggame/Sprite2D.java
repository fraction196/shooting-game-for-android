/*
 * テクスチャの読み込みや描画、画像への当たり判定を調べるクラス
 * 敵や自機、弾などに共通の親クラス
 */

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
	//モデルの可視不可視を保持する変数
    public boolean _visible = true;
	//テクスチャID番号
    public int _textureNo;
	//配置する位置
    public Vector3D _pos = new Vector3D(0,0,0);
	//配置する幅
    public float _width;
 	//配置する高さ
    public float _height;
	//テクスチャでのx座標
    public int _texX;
	//テクスチャでのy座標
    public int _texY;
	//テクスチャでの幅
    public int _texWidth;
	//テクスチャでの高さ
    public int _texHeight;

	//移動オブジェクトに使用する変数
	//体力と体力フラグ
	public int hp = 0;
	public boolean hp_flag = false;
	//スコアフラグ
	public boolean score_flag = false;
	//汎用的に用いるフラグ
	public boolean flag = false;
	public boolean flag2 = false;
	public boolean flag3 = false;
	public boolean flag4 = false;
	//無敵時間に関する変数
	public boolean invincible_time = false;
	public int invincible_switch = 0;
	public int invincible_count = 0;
	//public int number = 0;

	//テクスチャを読み込んでセットする関数
    public void setTexture(GL10 gl,Resources res,int id) {
		//テクスチャをresから読み出す
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

		//テクスチャIDを割り当てる
    	int[] textureNo = new int[1];
    	gl.glGenTextures(1, textureNo, 0);
    	_textureNo = textureNo[0];
		//テクスチャIDのバインド
    	gl.glBindTexture(GL10.GL_TEXTURE_2D, _textureNo);
    	GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		//テクスチャ座標が1.0fを超えたとき、S(T)軸方向に繰り返す設定
    	gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT );
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT );
		//テクスチャの色の設定
    	gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR );
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR );
    	
    	setPos(
    		0,bitmap.getHeight(),
    		bitmap.getWidth(),-bitmap.getHeight(),
    		0,0,0,
    		bitmap.getWidth(),bitmap.getHeight());
    }

	//テクスチャ位置をセットする関数
    public void setPos(int texX,int texY,int texW,int texH, float x,float y,float z,float w,float h) {
    	_texX = texX;
    	_texY = texY;
    	_texWidth = texW;
       	_texHeight = texH;
     	_pos = new Vector3D(x,y,z);
     	_width = w;
     	_height = h;
    }

	//テクスチャを描画する関数
    public void draw(GL10 gl)
    {
    	if ( !_visible )
    	{
    		return;
    	}
		//デプステストを不可に
    	gl.glDisable(GL10.GL_DEPTH_TEST);
		//白色セット
   		gl.glColor4x(0x10000, 0x10000, 0x10000, 0x10000);
	    //テクスチャ0版をアクティブに
   		gl.glActiveTexture(GL10.GL_TEXTURE0);
		//テクスチャIDに対応するテクスチャをバインド
   		gl.glBindTexture(GL10.GL_TEXTURE_2D, _textureNo);

	    //テクスチャの座標と幅と高さを指定する
   		int rect[] = {_texX, _texY, _texWidth, _texHeight};
		//どの部分を使うか指定
   		((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D, GL11Ext.GL_TEXTURE_CROP_RECT_OES, rect, 0);
		//2D描画
   		((GL11Ext) gl).glDrawTexfOES(_pos._x, _pos._y, _pos._z, _width, _height);
		//デプステスト可能に
   	   	gl.glEnable(GL10.GL_DEPTH_TEST);
	}

	//2Dスプライト描画
    public void draw(GL10 gl,float ratio)
    {
    	if ( !_visible )
    	{
    		return;
    	}
		//デプステストを不可に
    	gl.glDisable(GL10.GL_DEPTH_TEST);
		//白色セット
   		gl.glColor4x(0x10000, 0x10000, 0x10000, 0x10000);
	    //テクスチャ0版をアクティブに
   		gl.glActiveTexture(GL10.GL_TEXTURE0);
		//テクスチャIDに対応するテクスチャをバインド
   		gl.glBindTexture(GL10.GL_TEXTURE_2D, _textureNo);

		//テクスチャの座標と幅と高さを指定する
   		int rect[] = {_texX, _texY, _texWidth, _texHeight};
		//どの部分を使うか指定
   		((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D, GL11Ext.GL_TEXTURE_CROP_RECT_OES, rect, 0);
		//2D描画
   		((GL11Ext) gl).glDrawTexfOES(_pos._x, _pos._y, _pos._z, _width*ratio, _height*ratio);
		//デプステスト可能に
   	   	gl.glEnable(GL10.GL_DEPTH_TEST);
	}

	//指定したx,y座標がテクスチャ画像に衝突しているかを調べる関数
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
