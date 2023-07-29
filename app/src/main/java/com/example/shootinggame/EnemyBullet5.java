/*
 *　敵弾の生成、移動、描画を行うクラス
 */

package com.example.shootinggame;

import javax.microedition.khronos.opengles.GL10;


public class EnemyBullet5 extends Sprite2D{
    //敵弾の生成、移動、描画を行う関数
    public void EnemyBullet5_GMD(Enemy_c1 enemy[], EnemyBullet5 enemybullet[][], GL10 gl, int timer){
        //敵弾の生成
            for (int j = 0; j < enemy.length; j++) {
                for (int i = 0; i < enemybullet[j].length; i++) {
                    //対応する敵の体力が1以上の時
                    if (enemy[j].hp >= 1) {
                            //敵弾の体力が0であり、一定の間隔になった時
                            if (((timer % 100) == 0) && (enemybullet[j][i].hp == 0)) {
                                //HPの値を変更する（発射待ち状態へ）
                                enemybullet[j][i].hp = -1;
                            }
                            //同じフレーム内で弾がすでに発射された時、さらにもう一つの弾を発射待ち状態にする
                            if ((enemy[j].eb_count >= 1) && (enemybullet[j][i].hp == 0))enemybullet[j][i].hp = -1;
                            //発射待ち状態であり、生存フラグが立っていないとき
                            if ((enemybullet[j][i].hp == -1) && (!enemybullet[j][i].hp_flag)) {
                                //初期位置を対応する敵に合わせる
                                enemybullet[j][i]._pos._x = enemy[j]._pos._x;
                                enemybullet[j][i]._pos._y = enemy[j]._pos._y + (enemy[j]._height / 2) - (enemybullet[j][i]._height / 2);
                                //HPを1にし、生存フラグを立てる
                                enemybullet[j][i].hp_flag = true;
                                enemybullet[j][i].hp = 1;
                                //カウントが0の時（1発目）
                                if (enemy[j].eb_count == 0) {
                                    //上方向へのフラグを立てる
                                    enemybullet[j][i].u_flag = true;
                                    enemy[j].eb_count += 1;
                                    break;
                                }
                                //カウントが1の時（2発目）
                                if (enemy[j].eb_count == 1) {
                                    //下方向へのフラグを立てる
                                    enemybullet[j][i].d_flag = true;
                                    enemy[j].eb_count += 1;
                                    break;
                                }
                                //カウントが2の時（3発目）
                                if (enemy[j].eb_count == 2) {
                                    //左方向へのフラグを立てる
                                    enemybullet[j][i].w_flag = true;
                                    enemy[j].eb_count += 1;
                                    break;
                                }
                                //カウントが3の時（4発目）
                                if (enemy[j].eb_count == 3) {
                                    //左上方向へのフラグを立てる
                                    enemybullet[j][i].n_w_flag = true;
                                    enemy[j].eb_count += 1;
                                    break;
                                }
                                //カウントが4の時（5発目）
                                if (enemy[j].eb_count == 4) {
                                    //左下方向へのフラグを立てる
                                    enemybullet[j][i].s_w_flag = true;
                                    enemy[j].eb_count = 0;
                                    break;
                                }
                            }
                    }
                }
            }
        //敵弾の移動
            for (int j = 0; j < enemy.length; j++) {
                for (int i = 0; i < enemybullet[j].length; i++) {
                    //敵弾の体力が1の時
                        if (enemybullet[j][i].hp == 1) {
                            //画面に収まっているとき
                                if (enemybullet[j][i]._pos._x > 0) {
                                    //移動させる
                                        enemybullet[j][i]._pos._x -= 6;
                                    //1発目の弾について
                                        if(enemybullet[j][i].w_flag)enemybullet[j][i]._pos._y += 0;
                                    //2発目の弾について
                                        if(enemybullet[j][i].n_w_flag)enemybullet[j][i]._pos._y += 2;
                                    //3発目の弾について
                                        if(enemybullet[j][i].s_w_flag)enemybullet[j][i]._pos._y -= 2;
                                    //4発目の弾について
                                        if(enemybullet[j][i].u_flag)enemybullet[j][i]._pos._y += 5;
                                    //5発目の弾について
                                        if(enemybullet[j][i].d_flag)enemybullet[j][i]._pos._y -= 5;
                                } else {
                                    //画面外のときはHPと生存フラグをオフに
                                        enemybullet[j][i].hp = 0;
                                        enemybullet[j][i].hp_flag = false;
                                        enemybullet[j][i].u_flag = false;
                                        enemybullet[j][i].d_flag = false;
                                }
                        }
                }
            }
        //敵弾の描画
            for (int j = 0; j < enemy.length; j++) {
                for (int i = 0; i < enemybullet[j].length; i++) {
                    //体力が1の時、描画を行う
                    if (enemybullet[j][i].hp == 1) enemybullet[j][i].draw(gl);
                }
            }
    }
    //敵弾の初期化
    public void EnemyBulletInit(Enemy_c1 enemy[], EnemyBullet5 enemybullet[][], int _width, int _height){
        for(int j=0; j<enemy.length; j++) {
            for (int i = 0; i < enemybullet[j].length; i++) {
                //HPと生存フラグを初期化
                    enemybullet[j][i].hp = 0;
                    enemybullet[j][i].hp_flag = false;
                //初期位置の設定
                    enemybullet[j][i]._pos._x = 100 + _width;
                    enemybullet[j][i]._pos._y = 100 + _height;
                //フラグの初期化
                    enemybullet[j][i].u_flag = false;
                    enemybullet[j][i].d_flag = false;
                    enemybullet[j][i].w_flag = false;
                    enemybullet[j][i].n_w_flag = false;
                    enemybullet[j][i].s_w_flag = false;

            }
        }
    }
}
