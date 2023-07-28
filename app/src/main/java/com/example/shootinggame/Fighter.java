/*
 *　自機の移動、無敵時間の管理を行うクラス
 */

package com.example.shootinggame;

import javax.microedition.khronos.opengles.GL10;

public class Fighter extends Sprite2D{
    //ドラッグの移動量
        public Vector2D amount_of_movement = new Vector2D(0,0);
    //自機のサイズ
        public static final int fighter_width = 190;
        public static final int fighter_height = 190;
    //自機の速さ
        public float fighter_speed = 1.5f;
    //自機の体力
        public int hp = 3;
    //タップされたかを判定するフラグ
        public boolean touch_switch = true;

    //無敵時間を制御する関数
    public void InvincibleTime(Fighter fighter,GL10 gl){
        //無敵時間を長さを決める変数が一定値未満の時
            if(fighter.invincible_count < 10){
                //変数を使用し、点滅の間隔を決める
                    if(fighter.invincible_switch < 5){
                        //描画されていない時
                            fighter.invincible_switch += 1;
                    }else if (fighter.invincible_switch < 10){
                        //描画されている時
                            fighter.draw(gl);
                            fighter.invincible_switch += 1;
                    }else{
                        //点滅の数値が一定値に達したら、変数を初期化
                            fighter.invincible_switch = 0;
                        //無敵時間の長さを決める変数を加算
                            fighter.invincible_count += 1;
                    }
            }else{//無敵時間が一定値以上のとき（一定時間たったら）フラグとカウントを初期化
                    fighter.invincible_time = false;
                    fighter.invincible_count = 0;
            }
    }

    //自機の移動を行う関数
    void FighterMove(Fighter fighter,int _width, int _height){
        //ドラッグの移動量の分だけ自機を移動
            fighter._pos._x += fighter.amount_of_movement._x;
            fighter._pos._y -= fighter.amount_of_movement._y;

        //画面の左側にはみ出る時
            if(fighter._pos._x < 0){
                //x座標をゼロにし、移動量をマイナスにすることではみ出ないようにする
                    fighter._pos._x = 0;
                    fighter.amount_of_movement._x *= -1;
            }
        //画面の右側にはみ出る時
            else if(fighter._pos._x > _width-fighter.fighter_width){
                //x座標を画面幅から自機幅の差にし、移動量をマイナスにすることではみ出ないようにする
                    fighter._pos._x = _width-fighter.fighter_width;
                    fighter.amount_of_movement._x *= -1;
            }
        //画面の下側にはみ出る時
            else if(fighter._pos._y < 0){
                //y座標をゼロにし、移動量をマイナスにすることではみ出ないようにする
                    fighter._pos._y = 0;
                    fighter.amount_of_movement._y *= -1;
            }
        //画面の上側にはみ出る時
            else if(fighter._pos._y > _height-fighter.fighter_height){
                //y座標を画面高さから自機高さの差にし、移動量をマイナスにすることではみ出ないようにする
                    fighter._pos._y = _height-fighter.fighter_height;
                    fighter.amount_of_movement._y *= -1;
            }
    }

    //初期化関連
    public void FighterInit(Fighter fighter){
        //ドラッグの移動量の初期化
            fighter.amount_of_movement = new Vector2D(0,0);
        //自機座標の初期化
            fighter._pos._x = 0;
            fighter._pos._y = 0;
        //HPの初期化
            fighter.hp = 4;
        //無敵時間に関する変数の初期化
            fighter.invincible_time = false;
            fighter.invincible_count = 0;
            fighter.invincible_switch = 0;

    }
}


