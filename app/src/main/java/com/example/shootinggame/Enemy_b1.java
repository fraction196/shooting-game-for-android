/*
 *　敵の生成、移動、描画を行うクラス
 */

package com.example.shootinggame;

import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

public class Enemy_b1 extends Sprite2D {
    //敵の数
        private static final int enemy_a1_number = 10;
    //一画面に出てくる敵の上限
        public int noe_max = 6;
    //画面に存在する敵の数を数える変数
        public int number_of_enemies  = 0;
    //敵の速さ
        public int enemy_x_speed = 4;
    //敵の角度
        public int enemy_angle[] = new int[enemy_a1_number];
    //敵の初期のy座標
        public int enemy_first_y[] = new int[enemy_a1_number];
    //敵の生成に関して
        //y座標の上
            //public float enemy_y_over = 0;
        //y座標の下
            //public float enemy_y_under = 0;
        //敵の生成フラグ
            //public boolean enemy_generation_flag = false;
        //敵の生成
            //public EnemyGenerationCheck EGC = new EnemyGenerationCheck();

    //敵の出現頻度に関する変数
        //敵の出現間隔
            private int enemy_frequency = 0;
        //前回敵が生成されてからの時間
            private int time_since_last_enemy = 0;
        //敵が生成されていない時間
            private int enemy_stoptime = 0;
        //敵が生成された時刻
            private int enemy_generation_time = 0;
        //出現頻度をランダムにする際のランダムの幅
            private int random_width = 81;
        //出現頻度をランダムにする際の基準値（最低値）
            private int random_MIN = 100;
    //敵の体力
        public int enemy_a1_hp = 5;
    //private Vector2D[] teki_movement = new Vector2D[enemy_number];
    //private boolean enemyflag = false;
    //private int teki_y;

    //敵の生成、移動、描画を行う関数
    public void Enemy_b1_GMD(Enemy_b1 enemy[], GL10 gl, int timer, int _width, int _height, EnemyGenerationCheck EGC){
        //敵の生成
            //画面に存在する敵の数を数える変数を初期化
                number_of_enemies = 0;
            //画面に存在する敵の数を数える
                for (int i = 0; i < enemy.length; i++) {
                    if (enemy[i].hp_flag) number_of_enemies += 1;
                }
            //画面に存在する敵が一定値未満の時
                if (number_of_enemies < noe_max) {
                    for (int i = 0; i < enemy.length; i++) {
                        //ランダム変数の作成
                            //Random r1 = new Random();
                        //敵が前回生成されてからの時間
                            time_since_last_enemy = timer - (enemy_generation_time + enemy_stoptime);
                        //敵の体力が0であり、一定の間隔になった時
                            if ((time_since_last_enemy == enemy_frequency) && (enemy[i].hp == 0))
                                //HPの値を変更する（出現待ち状態へ）
                                    enemy[i].hp = -1;
                        //出現待ち状態であり、生存フラグが立っていないとき
                            if ((enemy[i].hp == -1) && (!enemy[i].hp_flag)) {
                                //敵のy座標をランダムに設定
                                while(!EGC.enemy_generation_flag) {
                                    Random r1 = new Random();
                                    enemy_first_y[i] = r1.nextInt(_height - (int) enemy[i]._height);
                                    EGC.EnemyGenerationCheck2(enemy_first_y[i]);
                                    if(EGC.enemy_generation_flag)break;
                                }
                                //初期位置を設定
                                    enemy[i]._pos._x = 400 + _width;
                                    enemy[i]._pos._y = enemy_first_y[i];
                                //HPを指定の値にし、生存フラグを立てる
                                    enemy[i].hp_flag = true;
                                    enemy[i].hp = enemy_a1_hp;
                                //敵の生成された時間を記録
                                    enemy_generation_time = timer;
                                //敵が生成されていない時間の値を初期化
                                    enemy_stoptime = 0;
                                //ランダムな値を作成
                                    Random r2 = new Random();
                                //敵の出現間隔をランダムに設定
                                    enemy_frequency = r2.nextInt(random_width) + random_MIN;
                                //生成フラグを下ろす
                                    EGC.enemy_generation_flag = false;
                                break;
                            }
                    }
                }
            //画面に存在する敵が一定値になった時
                if (number_of_enemies == noe_max){
                    //敵が生成されていない時間を加算
                        enemy_stoptime += 1;
                }
        //敵の移動
            for(int i=0; i<enemy.length; i++){
                //敵の体力が1以上の時
                    if(enemy[i].hp >= 1) {
                        //画面に収まっているとき
                            if (enemy[i]._pos._x + enemy[i]._width > 0) {
                                //移動させる
                                    enemy[i]._pos._x -= enemy_x_speed;
                                    //enemy[i]._pos._y += teki_angle[i] + teki_movement[i]._y;
                                    enemy[i]._pos._y += 0;
                        } else {
                            //画面外のときはHPと生存フラグをオフに
                                enemy[i].hp = 0;
                                enemy[i].hp_flag = false;
                        }
                    }
            }
        //敵の描画
            for(int i = 0;i< enemy.length; i++){
                //敵の体力が1以上の時
                    if(enemy[i].hp >= 1) {
                        //無敵時間の際
                            if (enemy[i].invincible_time) {
                                //無敵時間を長さを決める変数が一定値未満の時
                                    if (enemy[i].invincible_count < 2) {
                                        //変数を使用し、点滅の間隔を決める
                                            if (enemy[i].invincible_switch < 5) {
                                                //描画されていない時
                                                    enemy[i].invincible_switch += 1;
                                            } else if (enemy[i].invincible_switch < 10) {
                                                //描画されている時
                                                    enemy[i].draw(gl);
                                                    enemy[i].invincible_switch += 1;
                                            } else{
                                                //点滅の数値が一定値に達したら、変数を初期化
                                                    enemy[i].invincible_switch = 0;
                                                //無敵時間の長さを決める変数を加算
                                                    enemy[i].invincible_count += 1;
                                            }
                                    }else{//無敵時間が一定値以上のとき（一定時間たったら）フラグとカウントを初期化
                                        enemy[i].invincible_time = false;
                                        enemy[i].invincible_count = 0;
                                    }
                            }
                        //無敵時間でない際
                            if(!enemy[i].invincible_time){
                                //敵を描画
                                enemy[i].draw(gl);
                            }
                    }
            }
    }
    //敵の初期化
    public void EnemyInit(Enemy_b1 enemy[], int _width, int _height) {
        for (int i = 0; i < enemy.length; i++) {
            //HPと生存フラグを初期化
                enemy[i].hp = 0;
                enemy[i].hp_flag = false;
            //無敵時間関連の初期化
                enemy[i].invincible_time = false;
            //初期位置の設定
                enemy[i]._pos._x = 100 + _width;
                enemy[i]._pos._y = 100 + _height;
        }
        //一画面に出てくる敵の量の初期化
            number_of_enemies = 0;
        //敵の頻度に関する変数の初期化
            enemy_frequency = 0;
            time_since_last_enemy = 0;
            enemy_stoptime = 0;
            enemy_generation_time = 0;

    }
}