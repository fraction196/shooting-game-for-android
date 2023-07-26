/*
 *　敵弾のクラス
 */

package com.example.shootinggame;

import javax.microedition.khronos.opengles.GL10;


public class EnemyBullet1 extends Sprite2D{
    public void EnemyBulletMove(Enemy_a1 enemy[], EnemyBullet1 enemybullet[][]){
        for (int j = 0; j < enemy.length; j++) {
            for (int i = 0; i < enemybullet[j].length; i++) {
                if (enemybullet[j][i].hp == 1) {
                    if (enemybullet[j][i]._pos._x > 0) {
                        enemybullet[j][i]._pos._x -= 9;
                    } else {
                        enemybullet[j][i].hp = 0;
                        enemybullet[j][i].hp_flag = false;
                    }
                    //System.out.println("HP"+i+" "+fighterbullet[i].hp);
                }
            }
        }
    }


    //敵弾の描画
    public void EnemyBulletDraw(Enemy_a1 enemy[], EnemyBullet1 enemybullet[][], GL10 gl){
        for (int j = 0; j < enemy.length; j++) {
            for (int i = 0; i < enemybullet[j].length; i++) {
                if (enemybullet[j][i].hp == 1) enemybullet[j][i].draw(gl);
            }
        }
    }
    //敵弾の生成
    public void EnemyBulletGeneration(Enemy_a1 enemy[], EnemyBullet1 enemybullet[][], int timer) {
        for (int j = 0; j < enemy.length; j++) {
            for (int i = 0; i < enemybullet[j].length; i++) {
                if (enemy[j].hp >= 1) {
                    if (((timer % 230) == 0) && (enemybullet[j][i].hp == 0))
                        enemybullet[j][i].hp = -1;
                    if ((enemybullet[j][i].hp == -1) && (!enemybullet[j][i].hp_flag)) {
                        enemybullet[j][i]._pos._x = enemy[j]._pos._x;
                        enemybullet[j][i]._pos._y = enemy[j]._pos._y + (enemy[j]._height / 2) - (enemybullet[j][i]._height / 2);
                        enemybullet[j][i].hp_flag = true;
                        enemybullet[j][i].hp = 1;
                        break;
                    }
                }
            }
        }
    }
    public void EnemyBulletInit(Enemy_a1 enemy[], EnemyBullet1 enemybullet[][], int _width, int _height){
        for(int j=0; j<enemy.length; j++) {
            for (int i = 0; i < enemybullet[j].length; i++) {
                enemybullet[j][i].hp = 0;
                enemybullet[j][i].hp_flag = false;
                enemybullet[j][i]._pos._x = 100 + _width;
                enemybullet[j][i]._pos._y = 100 + _height;
            }
        }
    }
}
