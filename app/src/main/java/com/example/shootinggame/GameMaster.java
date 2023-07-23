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
    public int _width2;
    public int _height;
    //public boolean _touch;
    public int gamemode = 0; //0でタイトル画面、1でゲーム画面、2でゲームオーバー画面、3でゲームクリア画面
    public float touch_first_x = 0;
    public float touch_first_y = 0;
    public float touch_move_x = 0;
    public float touch_move_y = 0;
    public int timer = -1;

    //自機関係
    private Fighter fighter = new Fighter();      //自機
    //敵関係
    private static final int enemy1_number = 10;  //敵１の数
    private Enemy1 enemy1[] = new Enemy1[enemy1_number];  //敵

    //弾関係
    private static final int fighterbullet_number = 15;  //自機弾の数
    private FighterBullet1[] fb_1 = new FighterBullet1[fighterbullet_number];  //自機の弾
    //public int fighterbullet_on = 1;
    private static final int enemybullet_number = 30;  //敵弾数１
    private EnemyBullet1[][] eb_1 = new EnemyBullet1[enemy1_number][enemybullet_number];  //敵１の弾

    //画面関係
    private Sprite2D title = new Sprite2D();        //タイトル画面
    private Sprite2D background = new Sprite2D();   //背景画面
    private Sprite2D background2 = new Sprite2D();   //背景画面
    private Sprite2D gameover = new Sprite2D();     //ゲームオーバー画面
    private Sprite2D gameclear = new Sprite2D();    //ゲームクリア画面

    //衝突判定
    private CollisionCheck CC = new CollisionCheck();

    public Sprite2D explode = new Sprite2D();   //爆発画像

    //サウンド
    private static SoundPool se_explosion;
    private int soundID;

    //@Override
    public GameMaster(Context context)
    {
        _context = context;
        se_explosion = new SoundPool(1, AudioManager.STREAM_MUSIC,0);
        soundID = se_explosion.load(context, R.raw.explode,1);
    }

    //画面関係
    //描画を行う関数
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        switch(gamemode){
            case 0:     //タイトル画面
                title.draw(gl,getRatio());
                break;
            case 1:     //ゲーム画面
                background.draw(gl,getRatio());
                background2.draw(gl,getRatio());
                BackgroundMove(gl,getRatio());
                getTime();

                //敵１の描画
                enemy1[0].EnemyGeneration(enemy1,_width,_height,timer);
                enemy1[0].enemyMove(enemy1);
                enemy1[0].enemyDraw(enemy1,gl);


                //自機の描画と当たり判定
                fighter.FighterMove(fighter,_width,_height);
                //System.out.println("in_time " + fighter.invincible_time);
                if(fighter.invincible_time)fighter.InvincibleTime(fighter,gl);
                if(!fighter.invincible_time){
                    fighter.draw(gl);
                    CC.FighterCollisionCheck(enemy1,fighter);
                    CC.FighterCollisionCheck2(enemy1,eb_1,fighter);
                }
                if(fighter.hp == 0) gamemode = 2;

                //弾の描画
                fb_1[0].FighterBulletGeneration1(fb_1,timer,gl,_width,fighter);


                //弾の当たり判定
                eb_1[0][0].EnemyBulletGeneration(enemy1,eb_1,timer);
                eb_1[0][0].EnemyBulletDraw(enemy1,eb_1,gl);
                eb_1[0][0].EnemyBulletMove(enemy1,eb_1);
                for(int i = 0; i < enemy1.length; i++){
                    CC.ObjectCollisionCheck(fb_1, enemy1[i]);
                }

                break;
            case 2:     //ゲームオーバー画面
                gameover.draw(gl,getRatio());
                break;
            case 3:     //ゲームクリア画面
                break;
        }
    }
    //画面比率
    private float getRatio(){
        return (float)_height/483.0f;
    }
    //時間を得る
    private void getTime(){
        timer += 1;
    }

    private void BackgroundMove(GL10 gl,float getRatio){

        if((int)-background._pos._x == (_width-1)*2){
            background._pos._x = (_width-1)*2;
        }
        if((int)-background2._pos._x == (_width-1)*2){
            background2._pos._x = (_width-1)*2;
        }
        background._pos._x -= 2;
        background2._pos._x -= 2;
        System.out.println("move " + (int)-background2._pos._x);
        System.out.println("wid " + _width*2);
    }
    //サーフェイス
    //サーフェイスのサイズ変更した時
    public void onSurfaceChanged(GL10 gl, int width, int height) {}
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
        title.setTexture(gl,_context.getResources(),R.drawable.title4);  //タイトル画面
        background.setTexture(gl,_context.getResources(),R.drawable.haikei1_50_2);  //背景
        background._texWidth = 2048;
        background._width = 2048;
        background2.setTexture(gl,_context.getResources(),R.drawable.haikei1_50_2);  //背景
        background2._texWidth = 2048;
        background2._width = 2050;
        gameover.setTexture(gl,_context.getResources(),R.drawable.gameover1);  //ゲームオーバー画面
        gameclear.setTexture(gl,_context.getResources(),R.drawable.gameclear1);  //ゲームクリア画面

        for(int i=0; i<enemy1.length; i++){
            enemy1[i] = new Enemy1();
            enemy1[i].setTexture(gl,_context.getResources(),R.drawable.teki1_50);
        }
        explode.setTexture(gl,_context.getResources(),R.drawable.explode);
        //自機弾
        for(int i=0; i<fb_1.length; i++){
            fb_1[i] = new FighterBullet1();
            fb_1[i].setTexture(gl,_context.getResources(),R.drawable.fighterbullet1);
        }

        //敵弾
        for(int j=0; j<enemy1.length; j++) {
            for (int i = 0; i < eb_1[j].length; i++) {
                eb_1[j][i] = new EnemyBullet1();
                eb_1[j][i].setTexture(gl, _context.getResources(), R.drawable.enemybullet1);
            }
        }

    }


    //初期化関連
    public void init(){
        explode._visible = false;
        timer = -1;
        background._pos._x = 0;
        background2._pos._x = (_width-1)*2;
    }

    //移動量の初期化
    public void reset(){

        fighter.amount_of_movement._x = 0;
        fighter.amount_of_movement._y = 0;

        touch_first_x = 0;
        touch_first_y = 0;
        touch_move_x = 0;
        touch_move_y = 0;
        fighter.touch_switch = true;

    }

    //タッチ関係
    //タップしたとき
    public void actionDown(float x,float y) {
        switch(gamemode){
            case 0:
                break;
            case 1:
                break;
            case 2:
                gamemode = 0;
                break;
            case 3:
                break;
        }
    }

    public void actionMove(float x,float y) {
        //基準座標の更新
        touch_first_x = touch_move_x;
        touch_first_y = touch_move_y;

        //タップした時の座標を初期値とする
        if(fighter.touch_switch){
            touch_first_x = x;
            touch_first_y = y;
            fighter.touch_switch = false;
        }
        //ドラッグしている座標
        touch_move_x = x;
        touch_move_y = y;

        switch(gamemode){
            case 0:
                break;
            case 1:
                fighter.amount_of_movement._x = (touch_move_x-touch_first_x)*fighter.fighter_speed;
                fighter.amount_of_movement._y = (touch_move_y-touch_first_y)*fighter.fighter_speed;
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
                reset();
                fb_1[0].FighterBulletInit(fb_1,_width,_height);
                eb_1[0][0].EnemyBulletInit(enemy1,eb_1,_width,_height);
                fighter.FighterInit(fighter);
                enemy1[0].EnemyInit(enemy1,_width,_height);
                gamemode = 1;
                break;
            case 1:
                reset();
                //System.out.println("x座標の慣性 "+amount_of_movement._x);
                //System.out.println("y座標の慣性 "+amount_of_movement._y);
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }
}