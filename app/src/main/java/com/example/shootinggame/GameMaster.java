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
    public int real_time = -1;
    public int bt1_height = 172;
    public int bt2_width = 266;
    public int bt2_height =122;
    public int bt3_width = 620;
    public int bt3_height = 172;

    private MainActivity main = new MainActivity();

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

    private Sprite2D system1 = new Sprite2D();      //システムウインドウ１
    private Sprite2D system2 = new Sprite2D();      //システムウインドウ２

    //ボタン関係
    private Sprite2D title_bt_exit = new Sprite2D();      //
    private Sprite2D title_bt_start = new Sprite2D();    //
    private Sprite2D system_bt_next = new Sprite2D();      //
    private Sprite2D system_bt_exit = new Sprite2D();    //
    private Sprite2D gameover_bt_restart = new Sprite2D();      //
    private Sprite2D gameover_bt_title = new Sprite2D();    //
    private Sprite2D sys1 = new Sprite2D();      //システム1
    private Sprite2D sys2 = new Sprite2D();      //システム1
    private Sprite2D sys3 = new Sprite2D();      //システム1
    private Sprite2D hp1_png = new Sprite2D();    //HP1
    private Sprite2D hp2_png = new Sprite2D();    //HP2
    private Sprite2D hp3_png = new Sprite2D();    //HP3
    private Sprite2D hp4_png = new Sprite2D();    //HP4


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
                TitleButton(gl);
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

                system_hp_Draw(gl);
                //ゲームクリア判定
                GameClear(real_time);

                break;
            case 2:     //ゲームオーバー画面
                gameover.draw(gl,getRatio());
                Game_O_C_Button(gl);

                break;
            case 3:     //ゲームクリア画面
                gameclear.draw(gl,getRatio());
                Game_O_C_Button(gl);
                break;
            case 4:     //システムウインドウ
                background.draw(gl,getRatio());
                if(!system_bt_next.flag4)system1.draw(gl,getRatio());
                if(system_bt_next.flag4)system2.draw(gl,getRatio());
                SystemButton(gl);

        }
    }
    //画面比率
    private float getRatio(){
        return (float)_height/483.0f;
    }
    //時間を得る
    private void getTime(){
        timer += 1;
        if(timer%60 == 0){
            real_time += 1;
            System.out.println("real_time " + real_time);
        }
        //System.out.println("time " + timer);

    }

    private void GameClear(int real_time){
        if(real_time == 60)gamemode = 3;
    }
    private void system_hp_Draw(GL10 gl){
        sys1._pos._x = 40;
        sys1._pos._y = 860;
        sys2._pos._x = 40;
        sys2._pos._y = 860;
        sys3._pos._x = 40;
        sys3._pos._y = 860;
        switch (fighter.hp){
            case 1:
                sys3.draw(gl);
                hp1_png._pos._x = 360;
                hp1_png._pos._y = 880;
                hp1_png.draw(gl);
                break;
            case 2:
                sys2.draw(gl);
                hp1_png._pos._x = 360;
                hp1_png._pos._y = 880;
                hp1_png.draw(gl);
                hp2_png._pos._x = 445;
                hp2_png._pos._y = 880;
                hp2_png.draw(gl);

                break;
            case 3:
                sys1.draw(gl);
                hp1_png._pos._x = 360;
                hp1_png._pos._y = 880;
                hp1_png.draw(gl);
                hp2_png._pos._x = 445;
                hp2_png._pos._y = 880;
                hp2_png.draw(gl);
                hp3_png._pos._x = 530;
                hp3_png._pos._y = 880;
                hp3_png.draw(gl);
                break;
            case 4:
                sys1.draw(gl);
                hp1_png._pos._x = 360;
                hp1_png._pos._y = 880;
                hp1_png.draw(gl);
                hp2_png._pos._x = 445;
                hp2_png._pos._y = 880;
                hp2_png.draw(gl);
                hp3_png._pos._x = 530;
                hp3_png._pos._y = 880;
                hp3_png.draw(gl);
                hp3_png._pos._x = 615;
                hp3_png._pos._y = 880;
                hp3_png.draw(gl);

                break;
        }
    }
    private void TitleButton(GL10 gl){
        if(gamemode == 0){
            if(!title_bt_exit.flag || title_bt_exit.flag3){
                title_bt_exit._pos._x = 100;
                title_bt_exit._pos._y = 70;
                if(title_bt_exit.flag3)System.exit(0);
            }

            if(title_bt_exit.flag && !title_bt_exit.flag2){
                title_bt_exit._width -= 40;
                title_bt_exit._height -= 40;
                title_bt_exit._pos._x += 20;
                title_bt_exit._pos._y += 10;
                title_bt_exit.flag2 = true;
            }

            if(!title_bt_start.flag || title_bt_start.flag3){
                title_bt_start._pos._x = 690;
                title_bt_start._pos._y = 70;
                if(title_bt_start.flag3){
                    FlagReset();
                    title_bt_start._width += 40;
                    title_bt_start._height += 40;
                    gamemode = 4;
                }
            }
            if(title_bt_start.flag && !title_bt_start.flag2){
                title_bt_start._width -= 40;
                title_bt_start._height -= 40;
                title_bt_start._pos._x += 20;
                title_bt_start._pos._y += 10;
                title_bt_start.flag2 = true;
            }
            title_bt_exit.draw(gl);
            title_bt_start.draw(gl);
        }
    }
    private void Game_O_C_Button(GL10 gl){
        if(!gameover_bt_restart.flag || gameover_bt_restart.flag3){
            gameover_bt_restart._pos._x = 820;
            gameover_bt_restart._pos._y = 120;
            if (gameover_bt_restart.flag3){
                FlagReset();
                gameover_bt_restart._width += 40;
                gameover_bt_restart._height += 40;
                gamemode = 1;
            }
        }

        if(gameover_bt_restart.flag && !gameover_bt_restart.flag2){
            gameover_bt_restart._width -= 40;
            gameover_bt_restart._height -= 40;
            gameover_bt_restart._pos._x += 20;
            gameover_bt_restart._pos._y += 10;
            gameover_bt_restart.flag2 = true;
        }
        if(!gameover_bt_title.flag || gameover_bt_title.flag3){
            gameover_bt_title._pos._x = 1520;
            gameover_bt_title._pos._y = 120;
            if (gameover_bt_title.flag3){
                FlagReset();
                gameover_bt_title._width += 40;
                gameover_bt_title._height += 40;
                gamemode = 0;
            }
        }

        if(gameover_bt_title.flag && !gameover_bt_title.flag2){
            gameover_bt_title._width -= 40;
            gameover_bt_title._height -= 40;
            gameover_bt_title._pos._x += 20;
            gameover_bt_title._pos._y += 10;
            gameover_bt_title.flag2 = true;
        }
        gameover_bt_restart.draw(gl);
        gameover_bt_title.draw(gl);

    }
    private void SystemButton(GL10 gl){
        if(!system_bt_next.flag4) {
            if (!system_bt_next.flag || system_bt_next.flag3) {
                system_bt_next._pos._x = 1740;
                system_bt_next._pos._y = 150;
                if (system_bt_next.flag3){
                    system_bt_next._width += 20;
                    system_bt_next._height += 20;
                    system_bt_next.flag4 = true;
                }
            }

            if (system_bt_next.flag && !system_bt_next.flag2) {
                system_bt_next._width -= 20;
                system_bt_next._height -= 20;
                system_bt_next._pos._x += 10;
                system_bt_next._pos._y += 5;
                system_bt_next.flag2 = true;
            }
            system_bt_next.draw(gl);
            //system_bt_exit.draw(gl);
        }
        if(system_bt_next.flag4){
            if (!system_bt_exit.flag || system_bt_exit.flag3) {
                system_bt_exit._pos._x = 1740;
                system_bt_exit._pos._y = 150;
                if (system_bt_exit.flag3) {
                    FlagReset();
                    system_bt_exit._width += 20;
                    system_bt_exit._height += 20;
                    gamemode = 1;
                }
            }

            if (system_bt_exit.flag && !system_bt_exit.flag2) {
                system_bt_exit._width -= 20;
                system_bt_exit._height -= 20;
                system_bt_exit._pos._x += 10;
                system_bt_exit._pos._y += 5;
                system_bt_exit.flag2 = true;
            }
            //system_bt_next.draw(gl);
            system_bt_exit.draw(gl);
        }

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
        //System.out.println("move " + (int)-background2._pos._x);
        //System.out.println("wid " + _width*2);
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
        title.setTexture(gl,_context.getResources(),R.drawable.taitoru1);  //タイトル画面
        title._texWidth = 1024;
        title._width = 1024 ;
        background.setTexture(gl,_context.getResources(),R.drawable.haikei1_50_2);  //背景
        background._texWidth = 2048;
        background._width = 2048;
        background2.setTexture(gl,_context.getResources(),R.drawable.haikei1_50_2);  //背景
        background2._texWidth = 2048;
        background2._width = 2048;
        gameover.setTexture(gl,_context.getResources(),R.drawable.gameover_kari1);  //ゲームオーバー画面
        gameover._texWidth = 1024;
        gameover._width = 1024 ;
        gameclear.setTexture(gl,_context.getResources(),R.drawable.gameclear_kari1);  //ゲームクリア画面
        gameclear._texWidth = 1024;
        gameclear._width = 1024 ;

        system1.setTexture(gl,_context.getResources(),R.drawable.sys1);   //システム１
        system1._texWidth = 1024;
        system1._width = 1024 ;
        system2.setTexture(gl,_context.getResources(),R.drawable.sys2);   //システム2
        system2._texWidth = 1024;
        system2._width = 1024 ;

        sys1.setTexture(gl,_context.getResources(),R.drawable.sys1_kari2);//ゲーム画面のシステム
        sys2.setTexture(gl,_context.getResources(),R.drawable.sys2_kari);//ゲーム画面のシステム差分
        sys3.setTexture(gl,_context.getResources(),R.drawable.sy3_kari);//ゲーム画面のシステム差分
        hp1_png.setTexture(gl,_context.getResources(),R.drawable.hp1);
        hp1_png._width += 15;
        hp1_png._height += 15;
        hp2_png.setTexture(gl,_context.getResources(),R.drawable.hp1);
        hp2_png._width += 15;
        hp2_png._height += 15;
        hp3_png.setTexture(gl,_context.getResources(),R.drawable.hp1);
        hp3_png._width += 15;
        hp3_png._height += 15;
        hp4_png.setTexture(gl,_context.getResources(),R.drawable.hp1);
        hp4_png._width += 15;
        hp4_png._height += 15;
        //ボタン
        title_bt_exit.setTexture(gl,_context.getResources(),R.drawable.bt2);  //
        title_bt_start.setTexture(gl,_context.getResources(),R.drawable.bt1);  //
        title_bt_exit._texWidth = 512;
        title_bt_exit._width = 512;
        title_bt_start._texWidth = 512;
        title_bt_start._width = 512;

        system_bt_next.setTexture(gl,_context.getResources(),R.drawable.bt3);  //
        system_bt_exit.setTexture(gl,_context.getResources(),R.drawable.bt4);  //

        gameover_bt_restart.setTexture(gl,_context.getResources(),R.drawable.bt5);  //
        gameover_bt_title.setTexture(gl,_context.getResources(),R.drawable.bt6);  //

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
    public void InitAll(){
        init();
        reset();
        fb_1[0].FighterBulletInit(fb_1,_width,_height);
        eb_1[0][0].EnemyBulletInit(enemy1,eb_1,_width,_height);
        fighter.FighterInit(fighter);
        enemy1[0].EnemyInit(enemy1,_width,_height);
    }
    public void init(){
        explode._visible = false;
        timer = -1;
        real_time = -1;
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

    //フラグリセッt
    public void FlagReset(){
        title_bt_start.flag = false;
        title_bt_start.flag2 = false;
        title_bt_start.flag3 = false;
        title_bt_exit.flag = false;
        title_bt_exit.flag2 = false;
        title_bt_exit.flag3 = false;
        system_bt_next.flag = false;
        system_bt_next.flag2 = false;
        system_bt_next.flag3 = false;
        system_bt_next.flag4 = false;
        system_bt_exit.flag  = false;
        system_bt_exit.flag2  = false;
        system_bt_exit.flag3  = false;
        system_bt_exit.flag4  = false;
        gameover_bt_restart.flag = false;
        gameover_bt_restart.flag2 = false;
        gameover_bt_restart.flag3 = false;
        gameover_bt_title.flag = false;
        gameover_bt_title.flag2 = false;
        gameover_bt_title.flag3 = false;

    }
    //タッチ関係
    //タップしたとき
    public void actionDown(float x,float y) {
        float y1 = _height - y -20;
        switch(gamemode){
            case 0:
                if(x > title_bt_exit._pos._x && x < title_bt_exit._pos._x+title_bt_start._width){
                    if(y1 > title_bt_exit._pos._y && y1 < title_bt_exit._pos._y+bt1_height){
                        title_bt_exit.flag = true;
                    }
                }
                /*
                if(x > 600 && x < 1000){
                    title_bt_start.flag = true;
                    System.out.println("ON3 ");
                }

                 */
                if(x > title_bt_start._pos._x && x < title_bt_start._pos._x+title_bt_start._width){
                    if(y1 > title_bt_start._pos._y && y1 < title_bt_start._pos._y+bt1_height){
                        title_bt_start.flag = true;
                    }
                }
                /*
                System.out.println("x "+x);
                System.out.println("y "+y1);
                System.out.println("y1 "+y);
                System.out.println("wid "+_width);
                System.out.println("hei "+_height);
                System.out.println("titlex "+title_bt_start._pos._x);
                System.out.println("titley "+title_bt_start._pos._y);

                 */
                break;
            case 1:
                break;
            case 2:
            case 3:
                if(x > gameover_bt_restart._pos._x && x < gameover_bt_restart._pos._x+bt3_width){
                    if(y1 > gameover_bt_restart._pos._y && y1 < gameover_bt_restart._pos._y+bt3_height){
                        gameover_bt_restart.flag = true;
                    }
                }
                if(x > gameover_bt_title._pos._x && x < gameover_bt_title._pos._x+bt3_width){
                    if(y1 > gameover_bt_title._pos._y && y1 < gameover_bt_title._pos._y+bt3_height){
                        gameover_bt_title.flag = true;
                    }
                }
                break;
            case 4:
                if(x > system_bt_next._pos._x && x < system_bt_next._pos._x+bt2_width){
                    if(y1 > system_bt_next._pos._y && y1 < system_bt_next._pos._y+bt2_height){
                        system_bt_next.flag = true;
                    }
                }
                if(system_bt_next.flag4){
                    if(x > system_bt_exit._pos._x && x < system_bt_exit._pos._x+bt2_width){
                        if(y1 > system_bt_exit._pos._y && y1 < system_bt_exit._pos._y+bt2_height){
                            system_bt_exit.flag = true;
                        }
                    }
                }
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
            case 4:
        }
    }
    public void actionUp(float x,float y) {
        //タップを離した時
        switch(gamemode){
            case 0:
                //初期化
                InitAll();
                if(title_bt_exit.flag)title_bt_exit.flag3 = true;
                if(title_bt_start.flag)title_bt_start.flag3 = true;
                break;
            case 1:
                reset();
                break;
            case 2:
            case 3:
                InitAll();
                if(gameover_bt_restart.flag)gameover_bt_restart.flag3 = true;
                if(gameover_bt_title.flag)gameover_bt_title.flag3 = true;
                break;
            case 4:
                if(system_bt_next.flag)system_bt_next.flag3 = true;
                if(system_bt_exit.flag)system_bt_exit.flag3 = true;
                break;
        }
    }
}