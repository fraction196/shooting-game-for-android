/*
 *　自機弾の生成、移動、描画を行うクラス
 */

package com.example.shootinggame;

import javax.microedition.khronos.opengles.GL10;


public class FighterBullet1 extends Sprite2D{
    //自機弾の生成、移動、描画を行う関数
    public void FighterBullet1_GMD(Fighter fighter,FighterBullet1[] fb_1,GL10 gl,int timer,int _width) {
        //自機弾の生成
            for (int i = 0; i < fb_1.length; i++) {
                //体力が0であり、一定の間隔になった時
                    if(((timer%20)==0)&&(fb_1[i].hp==0)){
                        //HPの値を変更する（発射待ち状態へ）
                            fb_1[i].hp = -1;
                    }
                //発射待ち状態であり、生存フラグが立っているとき
                    if((fb_1[i].hp == -1)&&(!(fb_1[i].hp_flag))){
                        //初期位置を自機に合わせる
                            fb_1[i]._pos._x = fighter._pos._x + fighter.fighter_width;
                            fb_1[i]._pos._y = fighter._pos._y + (fighter.fighter_height/2);
                        //HPを1にし、生存フラグを立てる
                            fb_1[i].hp_flag = true;
                            fb_1[i].hp = 1;
                        break;
                    }
            }
        //自機弾の移動
            for (int i = 0; i < fb_1.length; i++) {
                //体力が1の時
                    if(fb_1[i].hp == 1) {
                        //画面に収まっているとき
                        if (fb_1[i]._pos._x < _width) {
                            //移動させる
                                fb_1[i]._pos._x += 15;
                        } else {
                            //画面外のときはHPと生存フラグをオフに
                                fb_1[i].hp = 0;
                                fb_1[i].hp_flag = false;
                        }
                    }
            }
        //自機弾の描画
            for(int i = 0;i< fb_1.length; i++){
                //体力が1の時、描画を行う
                    if(fb_1[i].hp == 1)fb_1[i].draw(gl);
            }
    }
    //自機弾の初期化
    public void FighterBulletInit(FighterBullet1 fighterbullet[],int _width, int _height){
        for(int i=0; i<fighterbullet.length; i++){
            //HPと生存フラグを初期化
                fighterbullet[i].hp = 0;
                fighterbullet[i].hp_flag = false;
            //初期位置の設定
                fighterbullet[i]._pos._x = 100+_width;
                fighterbullet[i]._pos._y = 100+_height;
        }
    }
}
