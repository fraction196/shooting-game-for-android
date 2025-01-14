/*
 * ゲームの進行を行うクラス
 * 画面描画やゲームの処理を行う
 */
package com.example.shootinggame;

import android.content.Context;
import android.media.AudioManager;
import android.opengl.GLSurfaceView;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.media.SoundPool;
import android.media.MediaPlayer;

public class GameMaster implements GLSurfaceView.Renderer
{
    //コンテクスト
        private Context _context;
    //画面サイズ
        public int _width;
        public int _height;

    //ゲームモード
        public int gamemode = 0; //0でタイトル画面、1でゲーム画面、2でゲームオーバー画面、3でゲームクリア画面、4でシステム画面

    //タッチした座標を保存する変数
        public float touch_first_x = 0;
        public float touch_first_y = 0;
        public float touch_move_x = 0;
        public float touch_move_y = 0;

    //時間をカウントする変数
        public int timer = -1;
        public int real_time = -1;//実際の時間

    //自機
        private Fighter fighter = new Fighter();
    //自機弾１
        private static final int fighterbullet_number = 15;  //自機弾の数
        private FighterBullet1[] fb_1 = new FighterBullet1[fighterbullet_number];  //自機の弾
        //public int fighterbullet_on = 1;

    //敵全体
        private static final int enemybullet_number = 100;  //敵弾数
    //敵の生成の衝突判定
        private EnemyGenerationCheck EGC = new EnemyGenerationCheck();
    //敵a1
        private static final int enemy_a1_number = 10;  //a1の数
        private Enemy_a1 enemy_a1[] = new Enemy_a1[enemy_a1_number];  //敵
        private EnemyBullet1[][] eb_1 = new EnemyBullet1[enemy_a1_number][enemybullet_number];  //敵a1の弾
    //敵a2
        private static final int enemy_a2_number = 10;  //敵a2の数
        private Enemy_a2 enemy_a2[] = new Enemy_a2[enemy_a2_number];  //敵
        private EnemyBullet2[][] eb_2 = new EnemyBullet2[enemy_a2_number][enemybullet_number];  //敵a2の弾
    //敵a3
        private static final int enemy_a3_number = 10;  //a3の数
        private Enemy_a3 enemy_a3[] = new Enemy_a3[enemy_a3_number];  //敵
        private EnemyBullet3[][] eb_3 = new EnemyBullet3[enemy_a3_number][enemybullet_number];  //敵a3の弾
    //敵b1
        private static final int enemy_b1_number = 10;  //敵b1の数
        private Enemy_b1 enemy_b1[] = new Enemy_b1[enemy_b1_number];  //敵
        private EnemyBullet4[][] eb_4 = new EnemyBullet4[enemy_b1_number][enemybullet_number];  //敵b1の弾
    //敵c1
        private static final int enemy_c1_number = 10;  //敵c1の数
        private Enemy_c1 enemy_c1[] = new Enemy_c1[enemy_c1_number];  //敵
        private EnemyBullet5[][] eb_5 = new EnemyBullet5[enemy_c1_number][enemybullet_number];  //敵c1の弾
    //画面画像
        private Sprite2D title = new Sprite2D();        //タイトル画面
        private Sprite2D background = new Sprite2D();   //背景画面1
        private Sprite2D background2 = new Sprite2D();   //背景画面2
        private Sprite2D gameover = new Sprite2D();     //ゲームオーバー画面
        private Sprite2D gameclear = new Sprite2D();    //ゲームクリア画面
        private Sprite2D system1 = new Sprite2D();      //システムウインドウ１
        private Sprite2D system2 = new Sprite2D();      //システムウインドウ２

    //ボタン関係
        //タイトル画面
            private Sprite2D title_bt_exit = new Sprite2D();        //「おわる」ボタン
            private Sprite2D title_bt_start = new Sprite2D();       //「はじめる」ボタン
        //システム画面
            private Sprite2D system_bt_next = new Sprite2D();       //「次へ」ボタン
            private Sprite2D system_bt_exit = new Sprite2D();       //「閉じる」ボタン
        //ゲームクリア＆ゲームオーバー画面
            private Sprite2D gameover_bt_restart = new Sprite2D();  //「やりなおす」ボタン
            private Sprite2D gameover_bt_title = new Sprite2D();    //「タイトルへ」ボタン
        //ボタンサイズ
            public int bt1_height = 172;
            public int bt2_width = 266;
            public int bt2_height =122;
            public int bt3_width = 620;
            public int bt3_height = 172;

    //ゲーム中のシステム画像（左上の表示）
        private Sprite2D sys1 = new Sprite2D();      //システム1
        private Sprite2D sys2 = new Sprite2D();      //システム2
        private Sprite2D sys3 = new Sprite2D();      //システム3
    //HP画像
        private Sprite2D hp1_png = new Sprite2D();    //HP1
        private Sprite2D hp2_png = new Sprite2D();    //HP2
        private Sprite2D hp3_png = new Sprite2D();    //HP3
        private Sprite2D hp4_png = new Sprite2D();    //HP4

    //アイテム
        private static final int item_number = 1;  //アイテムの数
        private Item_gameclear item_gameclear[] = new Item_gameclear[item_number];

    //スコア
        private SpriteText number = new SpriteText();
        private Sprite2D score_png = new Sprite2D();
        public int score = 0;

    //衝突判定
        private CollisionCheck CC = new CollisionCheck();

    //BGM
        private MediaPlayer bgm = new MediaPlayer();

    //@Override
    //コンストラクタ
    public GameMaster(Context context)
    {
        _context = context;
        //BGMの再生
        bgm = MediaPlayer.create(context, R.raw.bgm);
    }

    //画面関係
    //描画のために毎フレーム呼び出される関数
    public void onDrawFrame(GL10 gl) {
        //描画用バッファをクリア
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        //画面分岐
        switch(gamemode){
            case 0:     //タイトル画面
                //タイトル画像描画
                    title.draw(gl,getRatio());
                //ボタン描画
                    TitleButton(gl);
                break;

            case 1:     //ゲーム画面
                //背景描画とスクロール
                    background.draw(gl,getRatio());
                    background2.draw(gl,getRatio());
                    BackgroundMove(gl,getRatio());
                //BGMを再生
                    bgm.start();
                //時間のカウント
                    getTime();
                //ゲームクリアアイテムの出現
                if(real_time >= 45){
                    item_gameclear[0].Item_gameclear_GMD(item_gameclear,gl);
                    CC.FighterCollisionCheck(item_gameclear,fighter,1);
                    //ゲームクリア判定
                    if(fighter.hp == 10)gamemode = 3;
                }

                //敵の描画
                    //敵の出現範囲チェック
                        EGC.EnemyGenerationCheck1(enemy_a1,enemy_a2,enemy_a3,enemy_b1,enemy_c1,_width);
                    //敵a1
                        if(timer >= 0)enemy_a1[0].Enemy_a1_GMD(enemy_a1,gl,timer,_width,_height,EGC);
                    //敵の出現範囲チェック
                        EGC.EnemyGenerationCheck1(enemy_a1,enemy_a2,enemy_a3,enemy_b1,enemy_c1,_width);
                    //敵a2
                        if(timer >= 400)enemy_a2[0].Enemy_a2_GMD(enemy_a2,gl,timer-400,_width,_height,EGC);
                    //敵の出現範囲チェック
                        EGC.EnemyGenerationCheck1(enemy_a1,enemy_a2,enemy_a3,enemy_b1,enemy_c1,_width);
                    //敵a3
                    if(timer >= 1000)enemy_a3[0].Enemy_a3_GMD(enemy_a3,gl,timer-1000,_width,_height,EGC);
                    //敵の出現範囲チェック
                        EGC.EnemyGenerationCheck1(enemy_a1,enemy_a2,enemy_a3,enemy_b1,enemy_c1,_width);
                    //敵b1
                        if(timer >= 1500)enemy_b1[0].Enemy_b1_GMD(enemy_b1,gl,timer-1500,_width,_height,EGC);
                    //敵の出現範囲チェック
                        EGC.EnemyGenerationCheck1(enemy_a1,enemy_a2,enemy_a3,enemy_b1,enemy_c1,_width);
                    //敵c1
                        if(timer >= 2200)enemy_c1[0].Enemy_c1_GMD(enemy_c1,gl,timer-2200,_width,_height,EGC);
                //自機弾
                    //自機弾１
                        fb_1[0].FighterBullet1_GMD(fighter,fb_1,gl,timer,_width);

                //自機弾との衝突判定
                    //敵a1
                        for(int i = 0; i < enemy_a1.length; i++){
                            //衝突判定を行う
                            CC.FighterBulletCollisionCheck(fb_1, enemy_a1[i]);
                            //スコアフラグが立っているとき
                            if(enemy_a1[i].score_flag){
                                //スコアを加算し、フラグを下ろす
                                score += 200;
                                enemy_a1[i].score_flag = false;
                            }
                        }
                    //敵a2
                        for(int i = 0; i < enemy_a2.length; i++){
                            //衝突判定を行う
                            CC.FighterBulletCollisionCheck(fb_1, enemy_a2[i]);
                            //スコアフラグが立っているとき
                            if(enemy_a2[i].score_flag){
                                //スコアを加算し、フラグを下ろす
                                score += 300;
                                enemy_a2[i].score_flag = false;
                            }
                        }
                    //敵a3
                        for(int i = 0; i < enemy_a3.length; i++){
                            //衝突判定を行う
                            CC.FighterBulletCollisionCheck(fb_1, enemy_a3[i]);
                            //スコアフラグが立っているとき
                                if(enemy_a3[i].score_flag){
                                    //スコアを加算し、フラグを下ろす
                                    score += 500;
                                    enemy_a3[i].score_flag = false;
                                }
                            }
                    //敵b1
                        for(int i = 0; i < enemy_b1.length; i++){
                            //衝突判定を行う
                            CC.FighterBulletCollisionCheck(fb_1, enemy_b1[i]);
                            //スコアフラグが立っているとき
                                if(enemy_b1[i].score_flag){
                                    //スコアを加算し、フラグを下ろす
                                    score += 1000;
                                    enemy_b1[i].score_flag = false;
                                }
                        }
                    //敵c1
                        for(int i = 0; i < enemy_c1.length; i++){
                            //衝突判定を行う
                            CC.FighterBulletCollisionCheck(fb_1, enemy_c1[i]);
                            //スコアフラグが立っているとき
                                if(enemy_c1[i].score_flag){
                                    //スコアを加算し、フラグを下ろす
                                    score += 2000;
                                    enemy_c1[i].score_flag = false;
                                }
                        }
                //敵の弾丸の描画
                    //敵a1-1
                        if(timer>=0)eb_1[0][0].EnemyBullet1_GMD(enemy_a1,eb_1,gl,timer);
                    //敵a2-2
                        if(timer>= 400)eb_2[0][0].EnemyBullet2_GMD(enemy_a2,eb_2,gl,timer-400);
                    //敵a3-3
                        if(timer>= 1000)eb_3[0][0].EnemyBullet3_GMD(enemy_a3,eb_3,gl,timer-1000);
                    //敵b1-4
                        if(timer>= 1500)eb_4[0][0].EnemyBullet4_GMD(enemy_b1,eb_4,gl,timer-1500);
                    //敵c1-5
                        if(timer>= 2200)eb_5[0][0].EnemyBullet5_GMD(enemy_c1,eb_5,gl,timer-2200);

                //自機の描画
                    fighter.FighterMove(fighter,_width,_height);
                //自機との当たり判定
                    //無敵時間の際
                        if(fighter.invincible_time)fighter.InvincibleTime(fighter,gl);
                    //無敵時間でない際
                        if(!fighter.invincible_time){
                            //自機を描画
                            fighter.draw(gl);
                            //衝突判定
                                //敵a1と敵弾1
                                    if(timer>=0){
                                        CC.FighterCollisionCheck(enemy_a1,fighter,0);
                                        CC.FighterCollisionCheck2(enemy_a1,eb_1,fighter);
                                    }
                                //敵a2と敵弾2
                                    if(timer >= 400){
                                        CC.FighterCollisionCheck(enemy_a2,fighter,0);
                                        CC.FighterCollisionCheck2(enemy_a2,eb_2,fighter);
                                    }
                                //敵a3と敵弾3
                                    if(timer >= 1000) {
                                        CC.FighterCollisionCheck(enemy_a3, fighter,0);
                                        CC.FighterCollisionCheck2(enemy_a3, eb_3, fighter);
                                    }
                                //敵b1と敵弾4
                                    if(timer >= 1500) {
                                        CC.FighterCollisionCheck(enemy_b1, fighter,0);
                                        CC.FighterCollisionCheck2(enemy_b1, eb_4, fighter);
                                    }
                                //敵c1と敵弾5
                                    if(timer >= 2200) {
                                        CC.FighterCollisionCheck(enemy_c1, fighter,0);
                                        CC.FighterCollisionCheck2(enemy_c1, eb_5, fighter);
                                    }
                        }
                //自機の生存判定
                    if(fighter.hp == 0) gamemode = 2;

                //スコアとシステムの描画
                    drawScore(gl);
                    system_hp_Draw(gl);
                break;

            case 2:     //ゲームオーバー画面
                //BGMを止め、始めの再生場所にリセットする
                    bgm.pause();
                    bgm.seekTo(0);
                //ゲームオーバー画像描画
                    gameover.draw(gl,getRatio());
                //ボタン描画
                    Game_O_C_Button(gl);
                break;

            case 3:     //ゲームクリア画面
                //BGMを止め、始めの再生場所にリセットする
                    bgm.pause();
                    bgm.seekTo(0);
                //ゲームクリア画面描画
                    gameclear.draw(gl,getRatio());
                //ボタン描画
                    Game_O_C_Button(gl);
                //スコア描画
                    drawScore(gl);
                break;

            case 4:     //システムウインドウ画面
                //システムウインドウ画像描画
                    background.draw(gl,getRatio());
                //システム画面１描画
                    if(!system_bt_next.flag4)system1.draw(gl,getRatio());
                //システム画面２描画
                    if(system_bt_next.flag4)system2.draw(gl,getRatio());
                //ボタン描画
                    SystemButton(gl);
        }
    }
    //画面比率
    private float getRatio(){
        return (float)_height/483.0f;
    }

    //時間を得る関数
    private void getTime(){
        //時間を加算
            timer += 1;
        //実際の時間
            if(timer%60 == 0){
                real_time += 1;
                //System.out.println("real_time " + real_time);
            }
    }

    //スコア描画関数
    private void drawScore(GL10 gl){
        //ゲーム画面の時
            if(gamemode == 1) {
                //スコア描画
                    score_png._height = 512;
                    score_png._width = 512;
                    score_png._pos._x = 750;
                    score_png._pos._y = 975;
                    score_png.draw(gl);
                //数字描画
                    number._height = 80;
                    number._width = 60;
                    number._pos._x = 980;
                    number._pos._y = 975;
                    number.draw(gl,score,1);
            }
        //クリア画面の時
            if(gamemode == 3) {
                //スコア描画
                    score_png._height = 1024;
                    score_png._width = 1024;
                    score_png._pos._x = 820;
                    score_png._pos._y = 480;
                    score_png.draw(gl);
                //数字描画
                    number._height = 140;
                    number._width = 100;
                    number._pos._x = 1280;
                    number._pos._y = 485;
                    number.draw(gl,score,1);
            }
    }

    //ゲーム中のシステム画面と自機の体力を表示する関数
    private void system_hp_Draw(GL10 gl){
        //システム画面の位置を設定
            sys1._pos._x = 40;
            sys1._pos._y = 860;
            sys2._pos._x = 40;
            sys2._pos._y = 860;
            sys3._pos._x = 40;
            sys3._pos._y = 860;
        switch (fighter.hp){
            case 1:     //hp==1の時
                //システム画面とhpを描画
                    sys3.draw(gl);
                    hp1_png._pos._x = 360;
                    hp1_png._pos._y = 880;
                    hp1_png.draw(gl);
                break;
            case 2:     //hp==2の時
                //システム画面とhpを描画
                    sys2.draw(gl);
                    hp1_png._pos._x = 360;
                    hp1_png._pos._y = 880;
                    hp1_png.draw(gl);
                    hp2_png._pos._x = 445;
                    hp2_png._pos._y = 880;
                    hp2_png.draw(gl);
                break;
            case 3:     //hp==3の時
                //システム画面とhpを描画
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
            case 4:     //hp==4の時
                //システム画面とhpを描画
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

    //ボタンを描画する関数
    //タイトル画面
    private void TitleButton(GL10 gl){
        //「おわる」ボタンについて
            if(!title_bt_exit.flag || title_bt_exit.flag3){
                //ボタンの位置設定
                    title_bt_exit._pos._x = 100;
                    title_bt_exit._pos._y = 70;
                //ボタンが押されたフラグが立っている時
                    if(title_bt_exit.flag3)System.exit(0);
            }
            //ボタンが押された際に、ボタンサイズを変更し、フラグを立てる
                if(title_bt_exit.flag && !title_bt_exit.flag2){
                    title_bt_exit._width -= 40;
                    title_bt_exit._height -= 40;
                    title_bt_exit._pos._x += 20;
                    title_bt_exit._pos._y += 10;
                    title_bt_exit.flag2 = true;
                }
            //ボタンを描画
                title_bt_exit.draw(gl);

        //「はじめる」ボタンについて
            if(!title_bt_start.flag || title_bt_start.flag3){
                //ボタンの位置設定
                    title_bt_start._pos._x = 690;
                    title_bt_start._pos._y = 70;
                //ボタンが押されたフラグが立っている時
                    if(title_bt_start.flag3){
                        //ボタンフラグとサイズをリセットする
                            FlagReset();
                            title_bt_start._width += 40;
                            title_bt_start._height += 40;
                        //システム画面へ移動する
                            gamemode = 4;
                    }
            }
            //ボタンが押された際に、ボタンサイズを変更し、フラグを立てる
            if(title_bt_start.flag && !title_bt_start.flag2){
                title_bt_start._width -= 40;
                title_bt_start._height -= 40;
                title_bt_start._pos._x += 20;
                title_bt_start._pos._y += 10;
                title_bt_start.flag2 = true;
            }
            //ボタンを描画
                title_bt_start.draw(gl);
    }
    //ゲームクリア＆ゲームオーバー画面
    private void Game_O_C_Button(GL10 gl){
        //「やりなおす」ボタンについて
            if(!gameover_bt_restart.flag || gameover_bt_restart.flag3){
                //ボタンの位置設定
                    gameover_bt_restart._pos._x = 820;
                    gameover_bt_restart._pos._y = 120;
                //ボタンが押されたフラグが立っている時
                    if (gameover_bt_restart.flag3){
                        //スコアとボタンフラグとサイズをリセットする
                            ScoreInit();
                            FlagReset();
                            gameover_bt_restart._width += 40;
                            gameover_bt_restart._height += 40;
                        //ゲーム画面へ移動する
                            gamemode = 1;
                    }
            }
            //ボタンが押された際に、ボタンサイズを変更し、フラグを立てる
                if(gameover_bt_restart.flag && !gameover_bt_restart.flag2){
                    gameover_bt_restart._width -= 40;
                    gameover_bt_restart._height -= 40;
                    gameover_bt_restart._pos._x += 20;
                    gameover_bt_restart._pos._y += 10;
                    gameover_bt_restart.flag2 = true;
                }
            //ボタンを描画
                gameover_bt_restart.draw(gl);

        //「タイトルへ」ボタンについて
            if(!gameover_bt_title.flag || gameover_bt_title.flag3){
                //ボタンの位置設定
                    gameover_bt_title._pos._x = 1520;
                    gameover_bt_title._pos._y = 120;
                //ボタンが押されたフラグが立っている時
                    if (gameover_bt_title.flag3){
                        //ボタンフラグとサイズをリセットする
                            FlagReset();
                            gameover_bt_title._width += 40;
                            gameover_bt_title._height += 40;
                        //タイトル画面へ移動する
                            gamemode = 0;
                    }
            }
            //ボタンが押された際に、ボタンサイズを変更し、フラグを立てる
                if(gameover_bt_title.flag && !gameover_bt_title.flag2){
                    gameover_bt_title._width -= 40;
                    gameover_bt_title._height -= 40;
                    gameover_bt_title._pos._x += 20;
                    gameover_bt_title._pos._y += 10;
                    gameover_bt_title.flag2 = true;
                }
            //ボタンを描画
                gameover_bt_title.draw(gl);
    }
    //システム画面（システムウインドウ）
    private void SystemButton(GL10 gl){
        //「つぎへ」ボタンが押される前の時
            if(!system_bt_next.flag4) {
                //「つぎへ」ボタンについて
                    if (!system_bt_next.flag || system_bt_next.flag3) {
                        //ボタンの位置設定
                            system_bt_next._pos._x = 1740;
                            system_bt_next._pos._y = 150;
                        //ボタンが押されたフラグが立っている時
                            if (system_bt_next.flag3){
                                //ボタンフラグとサイズをリセットする
                                    system_bt_next._width += 20;
                                    system_bt_next._height += 20;
                                //「つぎへ」ボタンが押されたフラグを立てる
                                    system_bt_next.flag4 = true;
                            }
                    }
                    //ボタンが押された際に、ボタンサイズを変更し、フラグを立てる
                        if (system_bt_next.flag && !system_bt_next.flag2) {
                            system_bt_next._width -= 20;
                            system_bt_next._height -= 20;
                            system_bt_next._pos._x += 10;
                            system_bt_next._pos._y += 5;
                            system_bt_next.flag2 = true;
                        }
                    //ボタンを描画
                        system_bt_next.draw(gl);
            }
        //「つぎへ」ボタンが押さた後の時
            if(system_bt_next.flag4){
                //「とじる」ボタンについて
                    if (!system_bt_exit.flag || system_bt_exit.flag3) {
                            //ボタンの位置設定
                            system_bt_exit._pos._x = 1740;
                            system_bt_exit._pos._y = 150;
                        //ボタンが押されたフラグが立っている時
                            if (system_bt_exit.flag3) {
                                //スコアとボタンフラグとサイズをリセットする
                                    FlagReset();
                                    ScoreInit();
                                    system_bt_exit._width += 20;
                                    system_bt_exit._height += 20;
                                //ゲーム画面へ移動する
                                    gamemode = 1;
                            }
                    }
                    //ボタンが押された際に、ボタンサイズを変更し、フラグを立てる
                        if (system_bt_exit.flag && !system_bt_exit.flag2) {
                            system_bt_exit._width -= 20;
                            system_bt_exit._height -= 20;
                            system_bt_exit._pos._x += 10;
                            system_bt_exit._pos._y += 5;
                            system_bt_exit.flag2 = true;
                        }
                    //ボタンを描画
                        system_bt_exit.draw(gl);
        }
    }

    //背景を移動する関数
    private void BackgroundMove(GL10 gl,float getRatio){
        //背景１が端まで来たら背景２の後ろに移動させる
            if((int)-background._pos._x == (_width-1)*2){
                background._pos._x = (_width-1)*2;
            }
        //背景２が端まで来たら背景１の後ろに移動させる
            if((int)-background2._pos._x == (_width-1)*2){
                background2._pos._x = (_width-1)*2;
            }
        //背景１と背景２をスクロールする
            background._pos._x -= 2;
            background2._pos._x -= 2;
    }

    //サーフェイス
    //サーフェイスのサイズ変更した時
    public void onSurfaceChanged(GL10 gl, int width, int height) {}
    //サーフェイスの生成、再生成した時
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(1.0f,1.0f,1.0f,1.0f);              //背景の色
        gl.glDisable(GL10.GL_DITHER);                                    //ティザを無効化
        gl.glEnable(GL10.GL_DEPTH_TEST);                                 //深度テストを有効化
        gl.glEnable(GL10.GL_TEXTURE_2D);                                 //テクスチャ機能ON
        gl.glEnable(GL10.GL_ALPHA_TEST);                                 //透明可能に
        gl.glEnable(GL10.GL_BLEND);                                      //ブレンド可能に
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);  //色のブレンド方法

        //使用する画像のテクスチャ読み込み
        //画面関連
            //タイトル画面とサイズ設定
                title.setTexture(gl,_context.getResources(),R.drawable.taitoru1);
                title._texWidth = 1024;
                title._width = 1024 ;
            //背景１とサイズ設定
                background.setTexture(gl,_context.getResources(),R.drawable.haikei1_50_2);
                background._texWidth = 2048;
                background._width = 2048;
            //背景２とサイズ設定
                background2.setTexture(gl,_context.getResources(),R.drawable.haikei1_50_2);
                background2._texWidth = 2048;
                background2._width = 2048;
            //ゲームオーバー画面とサイズ設定
                gameover.setTexture(gl,_context.getResources(),R.drawable.gameover);
                gameover._texWidth = 1024;
                gameover._width = 1024 ;
            //ゲームクリア画面とサイズ設定
                gameclear.setTexture(gl,_context.getResources(),R.drawable.gameclear);
                gameclear._texWidth = 1024;
                gameclear._width = 1024 ;
            //システム画面１とサイズ設定
                system1.setTexture(gl,_context.getResources(),R.drawable.window_sys1);
                system1._texWidth = 1024;
                system1._width = 1024 ;
            //システム画面２とサイズ設定
                system2.setTexture(gl,_context.getResources(),R.drawable.window_sys2_2);
                system2._texWidth = 1024;
                system2._width = 1024 ;

        //ゲーム画面のシステム画像
            sys1.setTexture(gl,_context.getResources(),R.drawable.sys1_kari2);//差分１
            sys2.setTexture(gl,_context.getResources(),R.drawable.sys2_kari);//差分2
            sys3.setTexture(gl,_context.getResources(),R.drawable.sys3_kari);//差分3

        //HP画像
            //HP1とサイズ設定
                hp1_png.setTexture(gl,_context.getResources(),R.drawable.hp1);
                hp1_png._width += 15;
                hp1_png._height += 15;
            //HP2とサイズ設定
                hp2_png.setTexture(gl,_context.getResources(),R.drawable.hp1);
                hp2_png._width += 15;
                hp2_png._height += 15;
            //HP3とサイズ設定
                hp3_png.setTexture(gl,_context.getResources(),R.drawable.hp1);
                hp3_png._width += 15;
                hp3_png._height += 15;
            //HP4とサイズ設定
                hp4_png.setTexture(gl,_context.getResources(),R.drawable.hp1);
                hp4_png._width += 15;
                hp4_png._height += 15;

        //ボタン関係
            //タイトル画面
                //「おわる」ボタンとサイズ設定
                    title_bt_exit.setTexture(gl,_context.getResources(),R.drawable.bt2);
                    title_bt_exit._texWidth = 512;
                    title_bt_exit._width = 512;
                //「始める」ボタンとサイズ設定
                    title_bt_start.setTexture(gl,_context.getResources(),R.drawable.bt1);
                    title_bt_start._texWidth = 512;
                    title_bt_start._width = 512;
            //ゲームオーバー＆ゲームクリア画面
                //「やりなおす」ボタン
                    gameover_bt_restart.setTexture(gl,_context.getResources(),R.drawable.bt5);
                //「タイトルへ」ボタン
                    gameover_bt_title.setTexture(gl,_context.getResources(),R.drawable.bt6);
            //システム画面
                //「つぎへ」ボタン
                    system_bt_next.setTexture(gl,_context.getResources(),R.drawable.bt3);
                //「閉じる」ボタン
                    system_bt_exit.setTexture(gl,_context.getResources(),R.drawable.bt4);

        //スコア関係
            //スコアの文字画像
                score_png.setTexture(gl,_context.getResources(),R.drawable.score_string);
            //数字画像とサイズ設定
                number.setTexture(gl,_context.getResources(),R.drawable.score_w1);
                number._texWidth = 25;
                number._height = 80;
                number._width = 60;

        //自機関係
            //自機とサイズ設定
                fighter.setTexture(gl,_context.getResources(),R.drawable.fighter);
                fighter._width = 120;
                fighter._height = 120;

        //自機弾関係
            //自機弾１
                for(int i=0; i<fb_1.length; i++){
                    fb_1[i] = new FighterBullet1();
                    fb_1[i].setTexture(gl,_context.getResources(),R.drawable.fb_1);
                }

        //敵関係
            //敵a1
                for(int i=0; i<enemy_a1.length; i++){
                    enemy_a1[i] = new Enemy_a1();
                    enemy_a1[i].setTexture(gl,_context.getResources(),R.drawable.teki_a1);
                }
            //敵a2
                for(int i=0; i<enemy_a2.length; i++){
                    enemy_a2[i] = new Enemy_a2();
                    enemy_a2[i].setTexture(gl,_context.getResources(),R.drawable.teki_a2);
                }
            //敵a3
                for(int i=0; i<enemy_a3.length; i++){
                    enemy_a3[i] = new Enemy_a3();
                    enemy_a3[i].setTexture(gl,_context.getResources(),R.drawable.teki_a3);
                }
            //敵b1
                for(int i=0; i<enemy_b1.length; i++){
                    enemy_b1[i] = new Enemy_b1();
                    enemy_b1[i].setTexture(gl,_context.getResources(),R.drawable.teki_b2);
                }
            //敵c1
                for(int i=0; i<enemy_c1.length; i++){
                    enemy_c1[i] = new Enemy_c1();
                    enemy_c1[i].setTexture(gl,_context.getResources(),R.drawable.teki_c3);
                }

        //敵弾関係
            //敵弾a1_1
                for(int j=0; j<enemy_a1.length; j++) {
                    for (int i = 0; i < eb_1[j].length; i++) {
                        eb_1[j][i] = new EnemyBullet1();
                        eb_1[j][i].setTexture(gl, _context.getResources(), R.drawable.eb_1);
                        eb_1[j][i]._height = 40;
                        eb_1[j][i]._width = 40;
                    }
                }
            //敵弾a2_2
                for(int j=0; j<enemy_a2.length; j++) {
                    for (int i = 0; i < eb_2[j].length; i++) {
                        eb_2[j][i] = new EnemyBullet2();
                        eb_2[j][i].setTexture(gl, _context.getResources(), R.drawable.eb_2);
                        eb_2[j][i]._height = 40;
                        eb_2[j][i]._width = 40;
                    }
                }
            //敵弾a3_3
                for(int j=0; j<enemy_a3.length; j++) {
                    for (int i = 0; i < eb_3[j].length; i++) {
                        eb_3[j][i] = new EnemyBullet3();
                        eb_3[j][i].setTexture(gl, _context.getResources(), R.drawable.eb_3);
                        eb_3[j][i]._height = 70;
                        eb_3[j][i]._width = 70;
                    }
                }
            //敵弾b1_4
                for(int j=0; j<enemy_b1.length; j++) {
                    for (int i = 0; i < eb_4[j].length; i++) {
                        eb_4[j][i] = new EnemyBullet4();
                        eb_4[j][i].setTexture(gl, _context.getResources(), R.drawable.eb_5);
                        eb_4[j][i]._height = 100;
                        eb_4[j][i]._width = 200;
                    }
                }
            //敵弾c1_5
                for(int j=0; j<enemy_c1.length; j++) {
                    for (int i = 0; i < eb_5[j].length; i++) {
                        eb_5[j][i] = new EnemyBullet5();
                        eb_5[j][i].setTexture(gl, _context.getResources(), R.drawable.eb_4);
                        eb_5[j][i]._height = 40;
                        eb_5[j][i]._width = 40;
                    }
                }
        //アイテム
            //ゲームクリア
                for(int i=0; i<item_gameclear.length; i++) {
                    item_gameclear[i] = new Item_gameclear();
                    item_gameclear[i].setTexture(gl, _context.getResources(), R.drawable.item_gameclear);
                    item_gameclear[i]._width = 120;
                    item_gameclear[i]._height = 120;
                }
    }

    //初期化関連
    //一括で初期化する関数
    public void InitAll(){
        //このクラスの変数の初期化
            init();
        //ドラッグの移動量の初期化
            reset();
        //自機関連の初期化
            fighter.FighterInit(fighter);
        //自機弾の初期化
            fb_1[0].FighterBulletInit(fb_1,_width,_height);

        //敵関連の初期化
            //敵a1
                enemy_a1[0].EnemyInit(enemy_a1,_width,_height);
            //敵a2
                enemy_a2[0].EnemyInit(enemy_a2,_width,_height);
            //敵a3
                enemy_a3[0].EnemyInit(enemy_a3,_width,_height);
            //敵b1
                enemy_b1[0].EnemyInit(enemy_b1,_width,_height);
            //敵c1
                enemy_c1[0].EnemyInit(enemy_c1,_width,_height);
        //敵弾関連の初期化
            //敵弾a1_eb1
                eb_1[0][0].EnemyBulletInit(enemy_a1,eb_1,_width,_height);
            //敵弾a2_eb2
                eb_2[0][0].EnemyBulletInit(enemy_a2,eb_2,_width,_height);
            //敵弾a3_eb3
                eb_3[0][0].EnemyBulletInit(enemy_a3,eb_3,_width,_height);
            //敵弾b1_eb3
                eb_4[0][0].EnemyBulletInit(enemy_b1,eb_4,_width,_height);
            //敵弾c1_eb5
                eb_5[0][0].EnemyBulletInit(enemy_c1,eb_5,_width,_height);
        //ゲームクリアアイテムの初期化
            item_gameclear[0].Item_gameclear_Init(item_gameclear,_width,_height);
    }
    //このクラスの変数の初期化
    public void init(){
        //時間
            timer = -1;
            real_time = -1;
        //背景の座標
            background._pos._x = 0;
            background2._pos._x = (_width-1)*2;
    }
    //ドラッグの移動量の初期化
    public void reset(){
        //ドラッグの移動量
            fighter.amount_of_movement._x = 0;
            fighter.amount_of_movement._y = 0;
        //タッチした座標とフラグ関連
            touch_first_x = 0;
            touch_first_y = 0;
            touch_move_x = 0;
            touch_move_y = 0;
            fighter.touch_switch = true;
    }

    //スコアの初期化
    public void ScoreInit(){
        score = 0;
    }
    //ボタンのフラグの初期化
    public void FlagReset(){
        //タイトル関連
            title_bt_start.flag = false;
            title_bt_start.flag2 = false;
            title_bt_start.flag3 = false;
            title_bt_exit.flag = false;
            title_bt_exit.flag2 = false;
            title_bt_exit.flag3 = false;
        //システム関連
            system_bt_next.flag = false;
            system_bt_next.flag2 = false;
            system_bt_next.flag3 = false;
            system_bt_next.flag4 = false;
            system_bt_exit.flag  = false;
            system_bt_exit.flag2  = false;
            system_bt_exit.flag3  = false;
            system_bt_exit.flag4  = false;
        //ゲームクリア、オーバー関係
            gameover_bt_restart.flag = false;
            gameover_bt_restart.flag2 = false;
            gameover_bt_restart.flag3 = false;
            gameover_bt_title.flag = false;
            gameover_bt_title.flag2 = false;
            gameover_bt_title.flag3 = false;
    }

    //タッチ関連
    //画面をタッチしたときに呼び出される関数
    public void actionDown(float x,float y) {
        //ゲームのy座標と引数のy座標のずれを修正
            float y1 = _height - y -20;
        switch(gamemode){
            case 0://タイトル画面
                //「おわる」ボタンの範囲にタッチされたらフラグを立てる
                    if(x > title_bt_exit._pos._x && x < title_bt_exit._pos._x+title_bt_start._width){
                        if(y1 > title_bt_exit._pos._y && y1 < title_bt_exit._pos._y+bt1_height){
                            title_bt_exit.flag = true;
                        }
                    }
                //「はじめる」ボタンの範囲にタッチされたらフラグを立てる
                    if(x > title_bt_start._pos._x && x < title_bt_start._pos._x+title_bt_start._width){
                        if(y1 > title_bt_start._pos._y && y1 < title_bt_start._pos._y+bt1_height){
                            title_bt_start.flag = true;
                        }
                    }
                break;
            case 1:
                break;
            case 2://ゲームオーバー画面
            case 3://ゲームクリア画面
                //「やりなおす」ボタンの範囲にタッチされたらフラグを立てる
                    if(x > gameover_bt_restart._pos._x && x < gameover_bt_restart._pos._x+bt3_width){
                        if(y1 > gameover_bt_restart._pos._y && y1 < gameover_bt_restart._pos._y+bt3_height){
                            gameover_bt_restart.flag = true;
                        }
                    }
                //「タイトルへ」ボタンの範囲にタッチされたらフラグを立てる
                    if(x > gameover_bt_title._pos._x && x < gameover_bt_title._pos._x+bt3_width){
                        if(y1 > gameover_bt_title._pos._y && y1 < gameover_bt_title._pos._y+bt3_height){
                            gameover_bt_title.flag = true;
                        }
                    }
                break;
            case 4://システム画面
                //「つぎへ」ボタンの範囲にタッチされたらフラグを立てる
                    if(x > system_bt_next._pos._x && x < system_bt_next._pos._x+bt2_width){
                        if(y1 > system_bt_next._pos._y && y1 < system_bt_next._pos._y+bt2_height){
                            system_bt_next.flag = true;
                        }
                    }
                //「つぎへ」ボタンがすでに押されたとき
                    if(system_bt_next.flag4){
                        //「閉じる」ボタンの範囲にタッチされたらフラグを立てる
                        if(x > system_bt_exit._pos._x && x < system_bt_exit._pos._x+bt2_width){
                            if(y1 > system_bt_exit._pos._y && y1 < system_bt_exit._pos._y+bt2_height){
                                system_bt_exit.flag = true;
                            }
                        }
                    }
                break;
        }
    }
    //画面をドラッグしたときに呼び出される関数
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
            case 1://ゲーム中
                //ドラッグの移動量に自機速度を掛けたものを、自機を移動させる変数に代入
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
    //画面タッチが放されたときに呼び出される関数
    public void actionUp(float x,float y) {
        switch(gamemode){
            case 0://タイトル画面
                //変数の初期化
                    InitAll();
                //ボタンが押されていたらフラグを立てる
                    if(title_bt_exit.flag)title_bt_exit.flag3 = true;
                    if(title_bt_start.flag)title_bt_start.flag3 = true;
                break;
            case 1://ゲーム中
                //ドラッグの移動量の初期化
                    reset();
                break;
            case 2://ゲームオーバー画面
            case 3://ゲームクリア画面
                //変数の初期化
                    InitAll();
                //ボタンが押されていたらフラグを立てる
                    if(gameover_bt_restart.flag)gameover_bt_restart.flag3 = true;
                    if(gameover_bt_title.flag)gameover_bt_title.flag3 = true;
                break;
            case 4://システム画面
                //変数の初期化
                    InitAll();
                //ボタンが押されていたらフラグを立てる
                    if(system_bt_next.flag)system_bt_next.flag3 = true;
                    if(system_bt_exit.flag)system_bt_exit.flag3 = true;
                    break;
        }
    }
}