package com.example.shootinggame;

import android.content.Context;
import android.media.AudioManager;
import android.opengl.GLSurfaceView;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.media.SoundPool;
import java.util.Random;

public class GameMaster implements GLSurfaceView.Renderer
{

    private Context _context;
    public int _width;
    public int _height;
    public boolean _touch;
    public int gamemode = 0; //0でタイトル画面、1でゲーム画面、2でゲームオーバー画面、3でゲームクリア画面
    public float touch_first_x = 0;
    public float touch_first_y = 0;
    public float touch_move_x = 0;
    public float touch_move_y = 0;
    public boolean touch_switch = true;
    public Vector2D amount_of_movement = new Vector2D(0,0); //自機の移動量
    //public float amount_of_movement_x  = 0;     //自機のx軸方向の移動量
    //public float amount_of_movement_y  = 0;     //自機のy軸方向の移動量
    public static final int fighter_size = 256;  //自機のサイズ
    public float fighter_speed = 1.5f;           //自機の速さ

    public static final int teki1_size = 128;  //自機のサイズ


    private Sprite2D fighter = new Sprite2D();      //自機

    private Sprite2D[] enemy = new Sprite2D[enemy_number];  //敵
    private static final int enemy_number = 10;  //敵１の数
    public int teki1_x_speed = 5;               //敵１の速さ
    public int teki_angle[] = new int[enemy_number];   //敵の角度
    public int teki_first_y[] = new int[enemy_number];   //敵の初期のy座標
    private Vector2D[] teki_movement = new Vector2D[enemy_number];
    private Sprite2D title = new Sprite2D();        //タイトル画面
    private Sprite2D background = new Sprite2D();   //背景画面
    private Sprite2D gameover = new Sprite2D();     //ゲームオーバー画面

    private Sprite2D gameclear = new Sprite2D();    //ゲームクリア画面

    private static SoundPool se_explosion;
    private int soundID;
    //@Override
    public GameMaster(Context context)
    {
        _context = context;
        se_explosion = new SoundPool(1, AudioManager.STREAM_MUSIC,0);
        soundID = se_explosion.load(context, R.raw.explode,1);
    }

    //@Override
    //描画を行う関数
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

            switch(gamemode){
                case 0:     //タイトル画面
                    title.draw(gl,getRatio());
                    break;
                case 1:     //ゲーム画面
                    background.draw(gl,getRatio());
                    //System.out.println("x座標 "+fighter._pos._x);
                    //System.out.println("y座標 "+fighter._pos._y);
                    enemyMove();
                    enemyDraw(gl);
                    FighterMove();
                    fighter.draw(gl);
                    break;
                case 2:     //ゲームオーバー画面
                    gameover.draw(gl,getRatio());
                    enemyDraw(gl);
                    fighter.draw(gl);
                    break;
                case 3:     //ゲームクリア画面
                    break;
            }
    }

    //画面比率
    private float getRatio(){
        return (float)_height/512.5f;
    }

    //敵１の移動を行う関数
    private void enemyMove(){
        int i;
        for(i=0; i<enemy.length; i++){
            enemy[i]._pos._x -= teki1_x_speed + teki_movement[i]._x;
            //enemy[i]._pos._y += teki_angle[i] + teki_movement[i]._y;
            enemy[i]._pos._y += 0 + teki_movement[i]._y;

            teki_movement[i]._x *= 0.9;
            teki_movement[i]._y *= 0.9;
            //画面端に出ないようにする処理
            /*
            if(enemy[i]._pos._x < 0){
                enemy[i]._pos._x = 0;
                teki_movement[i]._x *= -1;
            }
            if(enemy[i]._pos._x < _width - teki1_size){
                enemy[i]._pos._x = _width - teki1_size;
                teki_movement[i]._x *= -1;
            }

             */
            //自機との当たり判定
            float x1 = fighter._pos._x - enemy[i]._pos._x;
            float y1= fighter._pos._y - enemy[i]._pos._y;
            float x2 = amount_of_movement._x;
            float y2 = amount_of_movement._y;
            if(x1*x1 + y1*y1< 50*50){
                if(x2*x2 + y2*y2 > 1*1){
                    teki_movement[i]._x = amount_of_movement._x;
                    teki_movement[i]._y = -amount_of_movement._y;
                    amount_of_movement._x *= -1;
                    amount_of_movement._y *= -1;
                    se_explosion.play(soundID,1.0F,1.0F,0,0,1.0F);
                }
                else{
                    enemy[i]._pos._x += x1/2;
                    enemy[i]._pos._y -= y1/2;
                }
            }
        }
    }
    //敵１の描画を行う関数
    private void enemyDraw(GL10 gl){
        for(int i = 0;i< enemy.length; i++){
            enemy[i].draw(gl);
        }
    }
    //自機の移動を行う関数
    private void FighterMove(){
        fighter._pos._x += amount_of_movement._x;
        fighter._pos._y -= amount_of_movement._y;

        if(fighter._pos._x < 0){
            fighter._pos._x = 0;
            amount_of_movement._x *= -1;
        }
        else if(fighter._pos._x > _width-fighter_size){
            fighter._pos._x = _width-fighter_size;
            amount_of_movement._x *= -1;
        }
        else if(fighter._pos._y < 0){
            fighter._pos._y = 0;
            amount_of_movement._y *= -1;
        }
        else if(fighter._pos._y > _height-fighter_size){
            fighter._pos._y = _height-fighter_size;
            amount_of_movement._y *= -1;
        }
        else{
            //amount_of_movement._x *= 0.9f;
            //amount_of_movement._y *= 0.9f;
        }


    }
    //サーフェイスのサイズ変更した時
    public void onSurfaceChanged(GL10 gl, int width, int height) {}

    //@Override
    //サーフェイスの生成、再生成した時
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(1.0f,1.0f,1.0f,1.0f);   //背景の色
        gl.glDisable(GL10.GL_DITHER);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glEnable(GL10.GL_ALPHA_TEST);
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        //使用する画像
        fighter.setTexture(gl,_context.getResources(),R.drawable.fighter2);  //自機
        title.setTexture(gl,_context.getResources(),R.drawable.title2);  //タイトル画面
        background.setTexture(gl,_context.getResources(),R.drawable.background2);  //背景
        gameover.setTexture(gl,_context.getResources(),R.drawable.gameover);  //ゲームオーバー画面
        gameclear.setTexture(gl,_context.getResources(),R.drawable.gameclear);  //ゲームクリア画面
        //画像サイズ
        //title._texHeight = 460;  //タイトル画像の高さ
        //title._texWidth = 600;  //タイトル画像の幅
        //title._height = 460;     //タイトル画像の描画高さ
        //title._width = 800;     //タイトル画像の描画幅
        //background._texHeight = 460;  //背景画像の高さ
        //background._texWidth = 600;  //背景画像の幅
        //background._height = 460;     //背景画像の描画高さ
        //background._width = 1200;     //背景画像の描画幅
        for(int i=0; i<enemy.length; i++){
            enemy[i] = new Sprite2D();
            enemy[i].setTexture(gl,_context.getResources(),R.drawable.teki1_50);
        }

    }

    //初期化
    public void init(){
        int i;
        for(i=0; i<enemy.length; i++){
            Random r = new Random();
            teki_first_y[i] = r.nextInt(_height);
            enemy[i]._pos._x = (float)Math.random()*(float)Math.random()*_width*5;
            enemy[i]._pos._y = teki_first_y[i];
            teki_movement[i] = new Vector2D(0,0);
            //敵の角度を決定
            Random r2 = new Random();
            teki_angle[i] = r2.nextInt(11)-5;
        }
        amount_of_movement = new Vector2D(0,0);
        fighter._pos._x = 0;
        fighter._pos._y = 0;
    }
    //移動量の初期化
    public void reset(){
        amount_of_movement._x = 0;
        amount_of_movement._y = 0;
        touch_first_x = 0;
        touch_first_y = 0;
        touch_move_x = 0;
        touch_move_y = 0;
        touch_switch = true;
    }
    public void actionDown(float x,float y) {

    }

    public void actionMove(float x,float y) {
        //基準座標の更新
        touch_first_x = touch_move_x;
        touch_first_y = touch_move_y;

        //タップした時の座標を初期値とする
        if(touch_switch){
            touch_first_x = x;
            touch_first_y = y;
            touch_switch = false;
        }
        //ドラッグしている座標
        touch_move_x = x;
        touch_move_y = y;

        System.out.println("タップしたx座標 "+touch_first_x);
        System.out.println("タップしたy座標 "+touch_first_y);
        System.out.println("移動したx座標 "+touch_move_x);
        System.out.println("移動したy座標 "+touch_move_y);
        switch(gamemode){
            case 0:
                break;
            case 1:
                amount_of_movement._x = (touch_move_x-touch_first_x)*fighter_speed;
                amount_of_movement._y = (touch_move_y-touch_first_y)*fighter_speed;
                System.out.println("x座標の移動量 "+amount_of_movement._x);
                System.out.println("y座標の移動量 "+amount_of_movement._y);
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }
    public void actionUp(float x,float y) {
        //タップを離した時
        switch(gamemode){
            case 0:
                init();
                gamemode = 1;
                break;
            case 1:
                reset();
                System.out.println("x座標の慣性 "+amount_of_movement._x);
                System.out.println("y座標の慣性 "+amount_of_movement._y);
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }
}
