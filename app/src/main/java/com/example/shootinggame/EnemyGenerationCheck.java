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
    public void EnemyGenerationCheck1(Enemy_a1 obj1[],Enemy_a2 obj2[],int width){
        //敵a1
            for (int i = 0; i < obj1.length; i++) {
                if (obj1[i]._pos._x + obj1[i]._width >= width + 400) {
                    enemy_a1_y_over = obj1[i]._pos._y + obj1[i]._height;
                    enemy_a1_y_under = obj1[i]._pos._y;
                    enemy_generation_flag_a1 = true;
                    break;
                } else {
                    enemy_a1_y_over = 0;
                    enemy_a1_y_under = 0;
                    enemy_generation_flag_a1 = false;
                }
            }
        //敵a2
            for (int i = 0; i < obj2.length; i++) {
                if (obj2[i]._pos._x + obj2[i]._width >= width + 400) {
                    enemy_a2_y_over = obj2[i]._pos._y + obj2[i]._height;
                    enemy_a2_y_under = obj2[i]._pos._y;
                    enemy_generation_flag_a2 = true;
                    break;
                } else {
                    enemy_a2_y_over = 0;
                    enemy_a2_y_under = 0;
                    enemy_generation_flag_a2 = false;
                }
            }
    }
    public void EnemyGenerationCheck2(int y){
        //まず生成フラグを立てる
            enemy_generation_flag = true;
        //間に存在したら
            if(y<=enemy_a1_y_over && y>=enemy_a1_y_under){
                if(enemy_generation_flag_a1)enemy_generation_flag = false;
            }
            if(y<=enemy_a2_y_over && y>=enemy_a2_y_under){
                if(enemy_generation_flag_a2)enemy_generation_flag = false;
            }
    }
}
