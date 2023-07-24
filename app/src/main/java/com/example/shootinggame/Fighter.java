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
public class Fighter extends Sprite2D{

    public Vector2D amount_of_movement = new Vector2D(0,0); //自機の移動量
    public static final int fighter_width = 256;  //自機の幅
    public static final int fighter_height = 256;  //自機の高さ
    public float fighter_speed = 1.5f;           //自機の速さ
    public static final int teki1_size = 128;  //自機のサイズ
    public int hp = 3; //自機の体力

    //無敵
    public boolean touch_switch = true;
    public boolean invincible_time = false; //無敵時間
    public int invincible_switch = 0; //無敵時間
    public int invincible_count = 0;

    //自機の移動を行う関数
    //無敵時間
    public void InvincibleTime(Fighter fighter,GL10 gl){
        if(fighter.invincible_count < 10){
            if(fighter.invincible_switch < 5){
                fighter.invincible_switch += 1;
            }else if (fighter.invincible_switch < 10){
                fighter.draw(gl);
                fighter.invincible_switch += 1;
            }else{
                fighter.invincible_switch = 0;
                fighter.invincible_count += 1;
            }
        }else{
            fighter.invincible_time = false;
            fighter.invincible_count = 0;
        }
    }

    void FighterMove(Fighter fighter,int _width, int _height){
        fighter._pos._x += fighter.amount_of_movement._x;
        fighter._pos._y -= fighter.amount_of_movement._y;

        if(fighter._pos._x < 0){
            fighter._pos._x = 0;
            fighter.amount_of_movement._x *= -1;
        }
        else if(fighter._pos._x > _width-fighter.fighter_width){
            fighter._pos._x = _width-fighter.fighter_width;
            fighter.amount_of_movement._x *= -1;
        }
        else if(fighter._pos._y < 0){
            fighter._pos._y = 0;
            fighter.amount_of_movement._y *= -1;
        }
        else if(fighter._pos._y > _height-fighter.fighter_height){
            fighter._pos._y = _height-fighter.fighter_height;
            fighter.amount_of_movement._y *= -1;
        }
    }
    //初期化関連
    public void FighterInit(Fighter fighter){

        fighter.amount_of_movement = new Vector2D(0,0);
        fighter._pos._x = 0;
        fighter._pos._y = 0;
        fighter.hp = 4;


        fighter.invincible_time = false;
        fighter.invincible_count = 0;
        fighter.invincible_switch = 0;

    }
}


