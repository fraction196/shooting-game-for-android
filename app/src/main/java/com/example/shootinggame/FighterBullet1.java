package com.example.shootinggame;
import android.content.Context;
import android.media.AudioManager;
import android.opengl.GLSurfaceView;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.media.SoundPool;
import java.util.Random;

public class FighterBullet1 extends Sprite2D{
    //弾関係
    //private static final int fighterbullet_number = 15;  //自機弾の数
    //private Sprite2D[] fighterbullet = new Sprite2D[fighterbullet_number];  //自機の弾
    //public int fighterbullet_on = 1;


    /*
    //自機弾を発射するクラス
    public void FighterBulletMove(FighterBullet1 fighterbullet[],int _width){
        for (int i = 0; i < fighterbullet.length; i++) {
            if(fighterbullet[i].hp == 1) {
                if (fighterbullet[i]._pos._x < _width) {
                    fighterbullet[i]._pos._x += 9;
                } else {
                    fighterbullet[i].hp = 0;
                    fighterbullet[i].hp_flag = false;
                }

            }
        }
    }

    //自機弾の描画
    public void FighterBulletDraw(FighterBullet1 fighterbullet[],GL10 gl){
        for(int i = 0;i< fighterbullet.length; i++){
            if(fighterbullet[i].hp == 1)fighterbullet[i].draw(gl);
        }
    }
    //自機弾の生成
    public void FighterBulletGeneration(FighterBullet1 fb_1[],int timer) {
        for (int i = 0; i < fb_1.length; i++) {
            if(((timer%50)==0)&&(fb_1[i].hp==0)){
                fb_1[i].hp = -1;
            }
            if((fb_1[i].hp == -1)&&(!(fb_1[i].hp_flag))){
                //fighterbullet[i]._pos._x = fighter._pos._x + fighter_width;
                //fighterbullet[i]._pos._y = fighter._pos._y + (fighter_height/2);
                fb_1[i].hp_flag = true;
                fb_1[i].hp = 1;
                System.out.println("HP"+i+" "+fb_1[i].hp);
                System.out.println("HP"+i+" "+fb_1[i].hp_flag);

                break;
            }
        }
    }

     */
    //自機弾の生成
    public void FighterBulletGeneration1(FighterBullet1[] fb_1,int timer,GL10 gl,int _width,Fighter fighter) {
        for (int i = 0; i < fb_1.length; i++) {
            if(((timer%20)==0)&&(fb_1[i].hp==0)){
                fb_1[i].hp = -1;
            }
            if((fb_1[i].hp == -1)&&(!(fb_1[i].hp_flag))){
                fb_1[i]._pos._x = fighter._pos._x + fighter.fighter_width;
                fb_1[i]._pos._y = fighter._pos._y + (fighter.fighter_height/2);
                fb_1[i].hp_flag = true;
                fb_1[i].hp = 1;
                break;
            }
        }
        for(int i = 0;i< fb_1.length; i++){
            if(fb_1[i].hp == 1)fb_1[i].draw(gl);
        }
        for (int i = 0; i < fb_1.length; i++) {
            if(fb_1[i].hp == 1) {
                if (fb_1[i]._pos._x < _width) {
                    fb_1[i]._pos._x += 15;
                } else {
                    fb_1[i].hp = 0;
                    fb_1[i].hp_flag = false;
                }

            }
        }
    }
    public void FighterBulletInit(FighterBullet1 fighterbullet[],int _width, int _height){
        for(int i=0; i<fighterbullet.length; i++){
            fighterbullet[i].hp = 0;
            fighterbullet[i].hp_flag = false;
            fighterbullet[i]._pos._x = 100+_width;
            fighterbullet[i]._pos._y = 100+_height;
        }
    }
}
