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
    public int timer = -1;

    //無敵
    public boolean touch_switch = true;
    public boolean invincible_time = false; //無敵時間
    public int invincible_switch = 0; //無敵時間
    public int invincible_count = 0;

    //自機関係
    public Vector2D amount_of_movement = new Vector2D(0,0); //自機の移動量
    public static final int fighter_width = 256;  //自機の幅
    public static final int fighter_height = 256;  //自機の高さ
    public float fighter_speed = 1.5f;           //自機の速さ
    public static final int teki1_size = 128;  //自機のサイズ
    public  int fighter_hp = 3; //自機の体力
    private Sprite2D fighter = new Sprite2D();      //自機

    //敵関係
    private Sprite2D[] enemy = new Sprite2D[enemy_number];  //敵
    private static final int enemy_number = 10;  //敵１の数
    public int number_of_enemies=0;//一画面に出てくる敵の量
    public int teki1_x_speed = 5;               //敵１の速さ
    public int teki_angle[] = new int[enemy_number];   //敵の角度
    public int teki_first_y[] = new int[enemy_number];   //敵の初期のy座標
    private Vector2D[] teki_movement = new Vector2D[enemy_number];
    private int enemy_frequency = 0;
    private int enemy_frequency_time = 0;
    private int enemy_stoptime = 0;
    private int enemy_frequency_time_first = 0;
    private boolean enemyflag = false;

    //弾関係
    private static final int fighterbullet_number = 15;  //自機弾の数
    private Sprite2D[] fighterbullet = new Sprite2D[fighterbullet_number];  //自機の弾
    public int fighterbullet_on = 1;
    private static final int enemybullet_number = 30;  //敵弾数１
    private Sprite2D[][] enemybullet = new Sprite2D[enemy_number][enemybullet_number];  //敵１の弾

    //画面関係
    private Sprite2D title = new Sprite2D();        //タイトル画面
    private Sprite2D background = new Sprite2D();   //背景画面
    private Sprite2D gameover = new Sprite2D();     //ゲームオーバー画面
    private Sprite2D gameclear = new Sprite2D();    //ゲームクリア画面


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
                getTime();

                EnemyGeneration();
                enemyMove();
                enemyDraw(gl);
                FighterMove();

                if(invincible_time){
                    if(invincible_count < 10){
                        if(invincible_switch < 5){
                            invincible_switch += 1;
                        }else if (invincible_switch < 10){
                            fighter.draw(gl);
                            invincible_switch += 1;
                        }else{
                            invincible_switch = 0;
                            invincible_count += 1;
                        }
                    }else{
                        invincible_time = false;
                        invincible_count = 0;
                    }
                }else{
                    fighter.draw(gl);
                }

                if(!invincible_time){
                    FighterCollisionCheck(enemy);
                    FighterCollisionCheck2(enemy,enemybullet);
                }
                if(fighter_hp == 0) gamemode = 2;

                FighterBulletGeneration();
                FighterBulletDraw(gl);
                FighterBulletMove();

                EnemyBulletGeneration();
                EnemyBulletDraw(gl);
                EnemyBulletMove();
                for(int i = 0; i < enemy.length; i++){
                    ObjectCollisionCheck(fighterbullet, enemy[i]);
                }
                //System.out.println("TIME "+ timer);
                //System.out.println("x座標 "+fighter._pos._x);
                //System.out.println("y座標 "+fighter._pos._y);
                //System.out.println("hp "+fighter_hp);
                //System.out.println("muteki "+invincible_time);
                //System.out.println("count "+invincible_count);

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
        return (float)_height/485.0f;
    }
    //時間を得る
    private void getTime(){
        timer += 1;
    }


    //自機関係
    //自機弾を発射するクラス
    private void FighterBulletMove(){
        for (int i = 0; i < fighterbullet.length; i++) {
            if(fighterbullet[i].hp == 1) {
                if (fighterbullet[i]._pos._x < _width) {
                    fighterbullet[i]._pos._x += 9;
                } else {
                    fighterbullet[i].hp = 0;
                    fighterbullet[i].hp_flag = false;
                }
                //System.out.println("HP"+i+" "+fighterbullet[i].hp);
            }
        }
    }
    //自機弾の描画
    private void FighterBulletDraw(GL10 gl){
        for(int i = 0;i< fighterbullet.length; i++){
            if(fighterbullet[i].hp == 1)fighterbullet[i].draw(gl);
        }
    }
    //自機弾の生成
    public void FighterBulletGeneration() {
        for (int i = 0; i < fighterbullet.length; i++) {
            if(((timer%50)==0)&&(fighterbullet[i].hp==0))fighterbullet[i].hp = -1;
            if((fighterbullet[i].hp == -1)&&(!fighterbullet[i].hp_flag)){
                fighterbullet[i]._pos._x = fighter._pos._x + fighter_width;
                fighterbullet[i]._pos._y = fighter._pos._y + (fighter_height/2);
                fighterbullet[i].hp_flag = true;
                fighterbullet[i].hp = 1;
                break;
            }
        }
    }

    //自機とオブジェクトとの当たり判定
    private void FighterCollisionCheck(Sprite2D obj[]){
        for(int i=0; i<obj.length; i++) {
            float of_x = obj[i]._pos._x - fighter._pos._x;
            float of_y = obj[i]._pos._y - fighter._pos._y;
            if(!invincible_time) {
                if ((of_x <= fighter_width)&&(of_x >= -obj[i]._width)&&(of_y <= fighter_height)&&(of_y >= -obj[i]._height)){
                    se_explosion.play(soundID, 1.0F, 1.0F, 0, 0, 1.0F);
                    fighter_hp -= 1;
                    invincible_time = true;
                }
            }
        }
    }
    private void FighterCollisionCheck2(Sprite2D obj1[],Sprite2D obj2[][]){
        for(int j=0; j<obj1.length; j++) {
            for (int i = 0; i < obj2[j].length; i++) {
                float of_x = obj2[j][i]._pos._x - fighter._pos._x;
                float of_y = obj2[j][i]._pos._y - fighter._pos._y;
                if (!invincible_time) {
                    if ((of_x <= fighter_width) && (of_x >= -obj2[j][i]._width) && (of_y <= fighter_height) && (of_y >= -obj2[j][i]._height)) {
                        se_explosion.play(soundID, 1.0F, 1.0F, 0, 0, 1.0F);
                        fighter_hp -= 1;
                        invincible_time = true;
                    }
                }
            }
        }
    }
    //オブジェ１とオブジェ２（配列同士の当たり判定）
    private void ObjectCollisionCheck(Sprite2D obj1[],Sprite2D obj2){
        for (int i = 0; i < obj1.length; i++) {
            float o12_x = obj1[i]._pos._x - obj2._pos._x;
            float o12_y = obj1[i]._pos._y - obj2._pos._y;
            if((obj1[i].hp >= 1)&&(obj2.hp >= 1)){
                if ((o12_x <= obj2._width) && (o12_x >= -obj1[i]._width) && (o12_y <= obj2._height) && (o12_y >= -obj1[i]._height)) {
                    se_explosion.play(soundID, 1.0F, 1.0F, 0, 0, 1.0F);
                    obj1[i].hp -= 1;
                    if (obj1[i].hp == 0) obj1[i].hp_flag = false;
                    obj2.hp -= 1;
                    if (obj2.hp == 0) obj2.hp_flag = false;
                }
            }
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
        else if(fighter._pos._x > _width-fighter_width){
            fighter._pos._x = _width-fighter_width;
            amount_of_movement._x *= -1;
        }
        else if(fighter._pos._y < 0){
            fighter._pos._y = 0;
            amount_of_movement._y *= -1;
        }
        else if(fighter._pos._y > _height-fighter_height){
            fighter._pos._y = _height-fighter_height;
            amount_of_movement._y *= -1;
        }
    }


    //敵関係
    //敵１の描画を行う関数
    private void enemyDraw(GL10 gl){
        for(int i = 0;i< enemy.length; i++){
            if(enemy[i].hp == 1)enemy[i].draw(gl);
        }
    }
    //敵１の移動を行う関数
    private void enemyMove(){
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
    //敵1の出現
    public void EnemyGeneration() {
        number_of_enemies = 0;
        for(int i=0; i<enemy.length; i++){
            if(enemy[i].hp == 1)number_of_enemies += 1;
        }
        System.out.println("num "+number_of_enemies);
        if(number_of_enemies < 6){
            for(int i=0; i<enemy.length; i++) {
                Random r1 = new Random();
                enemy_frequency_time = timer - (enemy_frequency_time_first+enemy_stoptime);

                if ((enemy_frequency_time == enemy_frequency) && (enemy[i].hp == 0))enemy[i].hp = -1;
                if ((enemy[i].hp == -1)&&(!enemy[i].hp_flag)){
                    teki_first_y[i] = r1.nextInt(_height-(int)enemy[i]._height);
                    enemy[i]._pos._x = 50 + _width;
                    enemy[i]._pos._y = teki_first_y[i];
                    enemy_frequency_time_first = timer;
                    enemy[i].hp = 1;
                    enemy[i].hp_flag = true;
                    enemy_stoptime = 0;
                    Random r2 = new Random();
                    enemy_frequency = r2.nextInt(81) + 55;
                    break;
                }

            }
        }else{
            enemy_stoptime += 1;
        }

    }
    //敵弾を発射するクラス
    private void EnemyBulletMove(){
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
    //自機弾の描画
    private void EnemyBulletDraw(GL10 gl){
        for (int j = 0; j < enemy.length; j++) {
            for (int i = 0; i < enemybullet[j].length; i++) {
                if (enemybullet[j][i].hp == 1) enemybullet[j][i].draw(gl);
            }
        }
    }
    //自機弾の生成
    public void EnemyBulletGeneration() {
        for (int j = 0; j < enemy.length; j++) {
            for (int i = 0; i < enemybullet[j].length; i++) {
                if (enemy[j].hp == 1) {
                    if (((timer % 100) == 0) && (enemybullet[j][i].hp == 0))
                        enemybullet[j][i].hp = -1;
                    if ((enemybullet[j][i].hp == -1) && (!enemybullet[j][i].hp_flag)) {
                        enemybullet[j][i]._pos._x = enemy[j]._pos._x;
                        enemybullet[j][i]._pos._y = enemy[j]._pos._y + (enemy[j]._height / 2);
                        enemybullet[j][i].hp_flag = true;
                        enemybullet[j][i].hp = 1;
                        break;
                    }
                }
            }
        }
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
        background.setTexture(gl,_context.getResources(),R.drawable.haikei1);  //背景
        gameover.setTexture(gl,_context.getResources(),R.drawable.gameover1);  //ゲームオーバー画面
        gameclear.setTexture(gl,_context.getResources(),R.drawable.gameclear1);  //ゲームクリア画面
        //画像サイズ
        //title._texHeight = 460;  //タイトル画像の高さ
        //title._texWidth = 600;  //タイトル画像の幅
        //title._height = 460;     //タイトル画像の描画高さ
        //title._width = 800;     //タイトル画像の描画幅
        for(int i=0; i<enemy.length; i++){
            enemy[i] = new Sprite2D();
            enemy[i].setTexture(gl,_context.getResources(),R.drawable.teki1_50);
        }
        explode.setTexture(gl,_context.getResources(),R.drawable.explode);
        //自機弾
        for(int i=0; i<fighterbullet.length; i++){
            fighterbullet[i] = new Sprite2D();
            fighterbullet[i].setTexture(gl,_context.getResources(),R.drawable.fighterbullet1);
        }

        //敵弾
        for(int j=0; j<enemy.length; j++) {
            for (int i = 0; i < enemybullet[j].length; i++) {
                enemybullet[j][i] = new Sprite2D();
                enemybullet[j][i].setTexture(gl, _context.getResources(), R.drawable.enemybullet1);
            }
        }
    }


    //初期化関連
    public void init(){
        amount_of_movement = new Vector2D(0,0);
        fighter._pos._x = 0;
        fighter._pos._y = 0;
        invincible_time = false;
        invincible_count = 0;
        invincible_switch = 0;
        fighter_hp = 3;
        explode._visible = false;
        timer = -1;
    }
    public void FighterBulletInit(){
        for(int i=0; i<fighterbullet.length; i++){
            fighterbullet[i].hp = 0;
            fighterbullet[i].hp_flag = false;
            fighterbullet[i]._pos._x = 100+_width;
            fighterbullet[i]._pos._y = 100+_height;
        }
    }
    public void EnemyBulletInit(){
        for(int j=0; j<enemy.length; j++) {
            for (int i = 0; i < enemybullet[j].length; i++) {
                enemybullet[j][i].hp = 0;
                enemybullet[j][i].hp_flag = false;
                enemybullet[j][i]._pos._x = 100 + _width;
                enemybullet[j][i]._pos._y = 100 + _height;
            }
        }
    }
    public void EnemyInit(){
        for(int i=0; i<enemy.length; i++){
            enemy[i].hp = 0;
            enemy[i].hp_flag = false;
            enemy[i]._pos._x = 100+_width;
            enemy[i]._pos._y = 100+_height;
        }
        number_of_enemies=0;//一画面に出てくる敵の量
        teki1_x_speed = 5;               //敵１の速さ
        enemy_frequency = 0;
        enemy_frequency_time = 0;
        enemy_stoptime = 0;
        enemy_frequency_time_first = 0;
        enemyflag = false;
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
        if(touch_switch){
            touch_first_x = x;
            touch_first_y = y;
            touch_switch = false;
        }
        //ドラッグしている座標
        touch_move_x = x;
        touch_move_y = y;

        /*
        System.out.println("タップしたx座標 "+touch_first_x);
        System.out.println("タップしたy座標 "+touch_first_y);
        System.out.println("移動したx座標 "+touch_move_x);
        System.out.println("移動したy座標 "+touch_move_y);
        */
        switch(gamemode){
            case 0:
                break;
            case 1:
                amount_of_movement._x = (touch_move_x-touch_first_x)*fighter_speed;
                amount_of_movement._y = (touch_move_y-touch_first_y)*fighter_speed;
                //System.out.println("x座標の移動量 "+amount_of_movement._x);
                //System.out.println("y座標の移動量 "+amount_of_movement._y);
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
                FighterBulletInit();
                EnemyBulletInit();
                EnemyInit();
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