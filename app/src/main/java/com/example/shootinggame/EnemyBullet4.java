/*
 *　敵弾の生成、移動、描画を行うクラス
 */

package com.example.shootinggame;

import javax.microedition.khronos.opengles.GL10;
import java.util.Random;

public class EnemyBullet4 extends Sprite2D{
    //敵弾の生成、移動、描画を行う関数
    public void EnemyBullet4_GMD(Enemy_b1 enemy[], EnemyBullet4 enemybullet[][], GL10 gl, int timer){
        //敵弾の生成
            for (int j = 0; j < enemy.length; j++) {
                for (int i = 0; i < enemybullet[j].length; i++) {
                    //対応する敵の体力が1以上の時
                    if (enemy[j].hp >= 1) {
                        //敵弾の体力が0であり、一定の間隔になった時
                            if (((timer % 230) == 0) && (enemybullet[j][i].hp == 0)) {
                                //HPの値を変更する（発射待ち状態へ）
                                enemybullet[j][i].hp = -1;
                            }
                        //発射待ち状態であり、生存フラグが立っていないとき
                            if ((enemybullet[j][i].hp == -1) && (!enemybullet[j][i].hp_flag)) {
                                //初期位置を対応する敵に合わせる
                                    enemybullet[j][i]._pos._x = enemy[j]._pos._x;
                                    enemybullet[j][i]._pos._y = enemy[j]._pos._y + (enemy[j]._height / 2) - (enemybullet[j][i]._height / 2);
                                //HPを1にし、生存フラグを立てる
                                    enemybullet[j][i].hp_flag = true;
                                    enemybullet[j][i].hp = 1;
                                break;
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
                                    //ランダム変数の作成
                                        Random r1 = new Random();
                                    //移動させる
                                        enemybullet[j][i]._pos._x -= 16;
                                        enemybullet[j][i]._pos._y += r1.nextInt(81)-40;
                                } else {
                                    //画面外のときはHPと生存フラグをオフに
                                        enemybullet[j][i].hp = 0;
                                        enemybullet[j][i].hp_flag = false;
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
    public void EnemyBulletInit(Enemy_b1 enemy[], EnemyBullet4 enemybullet[][], int _width, int _height){
        for(int j=0; j<enemy.length; j++) {
            for (int i = 0; i < enemybullet[j].length; i++) {
                //HPと生存フラグを初期化
                    enemybullet[j][i].hp = 0;
                    enemybullet[j][i].hp_flag = false;
                //初期位置の設定
                    enemybullet[j][i]._pos._x = 100 + _width;
                    enemybullet[j][i]._pos._y = 100 + _height;
            }
        }
    }
}
