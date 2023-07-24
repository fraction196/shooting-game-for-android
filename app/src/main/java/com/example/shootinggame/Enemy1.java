package com.example.shootinggame;
import android.content.Context;
import android.content.res.Resources;
import android.media.AudioManager;
import android.opengl.GLSurfaceView;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.media.SoundPool;
import java.util.Random;

public class Enemy1 extends Sprite2D {
    //private Sprite2D[] enemy = new Sprite2D[enemy_number];  //敵
    private static final int enemy_number = 10;  //敵１の数
    public int number_of_enemies = 0;//一画面に出てくる敵の量
    public int teki1_x_speed = 5;               //敵１の速さ
    public int teki_angle[] = new int[enemy_number];   //敵の角度
    public int teki_first_y[] = new int[enemy_number];   //敵の初期のy座標
    private Vector2D[] teki_movement = new Vector2D[enemy_number];
    private int enemy_frequency = 0;
    private int enemy_frequency_time = 0;
    private int enemy_stoptime = 0;
    private int enemy_frequency_time_first = 0;
    private boolean enemyflag = false;
    private int teki_y;

    public void enemyDraw(Enemy1 enemy[], GL10 gl){
        for(int i = 0;i< enemy.length; i++){
            if(enemy[i].hp == 1)enemy[i].draw(gl);
        }
    }

    public void enemyMove(Enemy1 enemy[]){
        for(int i=0; i<enemy.length; i++){
            if(enemy[i].hp == 1) {
                if (enemy[i]._pos._x + enemy[i]._width > 0) {
                    enemy[i]._pos._x -= teki1_x_speed;
                    //enemy[i]._pos._y += teki_angle[i] + teki_movement[i]._y;
                    enemy[i]._pos._y += 0;
                } else {
                    enemy[i].hp = 0;
                    enemy[i].hp_flag = false;
                }
            }
        }
    }

    public void EnemyGeneration(Enemy1 enemy[], int _width, int _height, int timer) {
        number_of_enemies = 0;
        for (int i = 0; i < enemy.length; i++) {
            if (enemy[i].hp == 1) number_of_enemies += 1;
        }
        //System.out.println("num " + number_of_enemies);
        if (number_of_enemies < 6) {
            for (int i = 0; i < enemy.length; i++) {
                Random r1 = new Random();
                enemy_frequency_time = timer - (enemy_frequency_time_first + enemy_stoptime);

                if ((enemy_frequency_time == enemy_frequency) && (enemy[i].hp == 0))
                    enemy[i].hp = -1;
                if ((enemy[i].hp == -1) && (!enemy[i].hp_flag)) {
                    teki_first_y[i] = r1.nextInt(_height - (int) enemy[i]._height);
                    enemy[i]._pos._x = 400 + _width;
                    enemy[i]._pos._y = teki_first_y[i];
                    enemy_frequency_time_first = timer;
                    enemy[i].hp = 1;
                    enemy[i].hp_flag = true;
                    enemy_stoptime = 0;
                    Random r2 = new Random();
                    enemy_frequency = r2.nextInt(81) + 85;
                    break;
                }

            }
        } else {
            enemy_stoptime += 1;
        }

    }

    public void EnemyInit(Enemy1 enemy[], int _width, int _height) {
        for (int i = 0; i < enemy.length; i++) {
            enemy[i].hp = 0;
            enemy[i].hp_flag = false;
            enemy[i]._pos._x = 100 + _width;
            enemy[i]._pos._y = 100 + _height;
        }
        number_of_enemies = 0;//一画面に出てくる敵の量
        teki1_x_speed = 5;               //敵１の速さ
        enemy_frequency = 0;
        enemy_frequency_time = 0;
        enemy_stoptime = 0;
        enemy_frequency_time_first = 0;
        enemyflag = false;
    }
}