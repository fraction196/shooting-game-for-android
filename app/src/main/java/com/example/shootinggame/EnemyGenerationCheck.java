//敵の生成地点の敵の有無を調べるクラス
package com.example.shootinggame;

public class EnemyGenerationCheck {

        //敵の生成に関して
            //生成フラグ
                public boolean enemy_generation_flag = false;
            //敵a1
                //生成フラグ
                    public boolean enemy_generation_flag_a1 = false;
                //y座標の上
                    public float enemy_a1_y_over = 0;
                //y座標の下
                    public float enemy_a1_y_under = 0;
            //敵a2
                //生成フラグ
                    public boolean enemy_generation_flag_a2 = false;
                //y座標の上
                    public float enemy_a2_y_over = 0;
                //y座標の下
                    public float enemy_a2_y_under = 0;
            //敵a3
                //生成フラグ
                public boolean enemy_generation_flag_a3 = false;
                //y座標の上
                public float enemy_a3_y_over = 0;
                //y座標の下
                public float enemy_a3_y_under = 0;
            //敵b1
                //生成フラグ
                public boolean enemy_generation_flag_b1 = false;
                //y座標の上
                public float enemy_b1_y_over = 0;
                //y座標の下
                public float enemy_b1_y_under = 0;
            //敵c1
                //生成フラグ
                public boolean enemy_generation_flag_c1 = false;
                //y座標の上
                public float enemy_c1_y_over = 0;
                //y座標の下
                public float enemy_c1_y_under = 0;
    //敵生成地点にすでに敵が発生しているかを調べる関数
    public void EnemyGenerationCheck1(Enemy_a1 obj1[],Enemy_a2 obj2[],Enemy_a3 obj3[],Enemy_b1 obj4[],Enemy_c1 obj5[],int width){
        //敵a1
            for (int i = 0; i < obj1.length; i++) {
                //生成地点に敵がいたら敵のy座標を記録しフラグを立てる
                if (obj1[i]._pos._x + obj1[i]._width >= width + 400) {
                    enemy_a1_y_over = obj1[i]._pos._y + obj1[i]._height;
                    enemy_a1_y_under = obj1[i]._pos._y;
                    enemy_generation_flag_a1 = true;
                    break;
                } else {
                    //いなかったらフラグを下ろす
                    enemy_a1_y_over = 0;
                    enemy_a1_y_under = 0;
                    enemy_generation_flag_a1 = false;
                }
            }
        //敵a2
            for (int i = 0; i < obj2.length; i++) {
                //生成地点に敵がいたら敵のy座標を記録しフラグを立てる
                if (obj2[i]._pos._x + obj2[i]._width >= width + 400) {
                    enemy_a2_y_over = obj2[i]._pos._y + obj2[i]._height;
                    enemy_a2_y_under = obj2[i]._pos._y;
                    enemy_generation_flag_a2 = true;
                    break;
                } else {
                    //いなかったらフラグを下ろす
                    enemy_a2_y_over = 0;
                    enemy_a2_y_under = 0;
                    enemy_generation_flag_a2 = false;
                }
            }
        //敵a3
            for (int i = 0; i < obj3.length; i++) {
                //生成地点に敵がいたら敵のy座標を記録しフラグを立てる
                if (obj3[i]._pos._x + obj3[i]._width >= width + 400) {
                    enemy_a3_y_over = obj3[i]._pos._y + obj3[i]._height;
                    enemy_a3_y_under = obj3[i]._pos._y;
                    enemy_generation_flag_a3 = true;
                    break;
                } else {
                    //いなかったらフラグを下ろす
                    enemy_a3_y_over = 0;
                    enemy_a3_y_under = 0;
                    enemy_generation_flag_a3 = false;
                }
            }
        //敵b1
            for (int i = 0; i < obj4.length; i++) {
                //生成地点に敵がいたら敵のy座標を記録しフラグを立てる
                if (obj4[i]._pos._x + obj4[i]._width >= width + 400) {
                    enemy_b1_y_over = obj4[i]._pos._y + obj4[i]._height;
                    enemy_b1_y_under = obj4[i]._pos._y;
                    enemy_generation_flag_b1 = true;
                    break;
                } else {
                    //いなかったらフラグを下ろす
                    enemy_b1_y_over = 0;
                    enemy_b1_y_under = 0;
                    enemy_generation_flag_b1 = false;
                }
        }
        //敵c1
            for (int i = 0; i < obj5.length; i++) {
                //生成地点に敵がいたら敵のy座標を記録しフラグを立てる
                if (obj5[i]._pos._x + obj5[i]._width >= width + 400) {
                    enemy_c1_y_over = obj5[i]._pos._y + obj5[i]._height;
                    enemy_c1_y_under = obj5[i]._pos._y;
                    enemy_generation_flag_c1 = true;
                    break;
                } else {
                    //いなかったらフラグを下ろす
                    enemy_c1_y_over = 0;
                    enemy_c1_y_under = 0;
                    enemy_generation_flag_c1 = false;
                }
            }
    }
    //受けっ取ったy座標がほかの敵の生成地点と衝突しているかを調べる関数
    public void EnemyGenerationCheck2(int y){
        //まず生成フラグを立てる
            enemy_generation_flag = true;
        //すでに発生している敵のy座標に衝突していたら生成フラグをおろし乱数を再生成する
            //敵a1
            if(y<=enemy_a1_y_over && y>=enemy_a1_y_under){
                if(enemy_generation_flag_a1)enemy_generation_flag = false;
            }
            //敵a2
            if(y<=enemy_a2_y_over && y>=enemy_a2_y_under){
                if(enemy_generation_flag_a2)enemy_generation_flag = false;
            }
            //敵a3
            if(y<=enemy_a3_y_over && y>=enemy_a3_y_under){
                if(enemy_generation_flag_a3)enemy_generation_flag = false;
            }
            //敵b1
            if(y<=enemy_b1_y_over && y>=enemy_b1_y_under){
                if(enemy_generation_flag_b1)enemy_generation_flag = false;
            }
            //敵c1
            if(y<=enemy_c1_y_over && y>=enemy_c1_y_under){
                if(enemy_generation_flag_c1)enemy_generation_flag = false;
        }
    }
}
