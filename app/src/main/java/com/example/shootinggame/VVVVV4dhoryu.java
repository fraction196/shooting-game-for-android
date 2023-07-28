/*
package com.example.shootinggame;

public class horyuu {
if((fighterbullet[i].hp == -1)&&(number_of_enemies<4)){
                teki_first_y[i] = r.nextInt(_height);
                enemy[i]._pos._x = ((float)Math.random()*(float)Math.random()*_width*5)+_width;
                enemy[i]._pos._y = teki_first_y[i];
                teki_movement[i] = new Vector2D(0,0);
                //敵の角度を決定
                Random r2 = new Random();
                teki_angle[i] = r2.nextInt(11)-5;

        }
}
 //オブジェ１とオブジェ２（配列同士の当たり判定）
    private void ObjectCollisionCheck(Sprite2D obj1[],Sprite2D obj2[]){
        for(int j=0; j<1; j++) {
            for (int i = 0; i < obj1.length; i++) {
                float o12_x = obj1[i]._pos._x - obj2[j]._pos._x;
                float o12_y = obj1[i]._pos._y - obj2[j]._pos._y;
                if ((o12_x <= obj2[j]._width)&&(o12_x >= -obj1[i]._width)&&(o12_y <= obj2[j]._height)&&(o12_y >= -obj1[i]._height)){
                    se_explosion.play(soundID, 1.0F, 1.0F, 0, 0, 1.0F);
                    obj1[i].hp -= 1;
                    if(obj1[i].hp == 0)obj1[i].hp_flag = false;
                    obj2[j].hp -= 1;
                    if(obj2[i].hp == 0)obj2[i].hp_flag = false;
                }
            }

        }
    }

*/
//敵1の出現
    /*
    public void EnemyGeneration(Enemy1 enemy,int _width,int _height,int timer) {
        number_of_enemies = 0;
        if(enemy.hp == 1)number_of_enemies += 1;
        System.out.println("eft"+" "+enemy_frequency_time);
        System.out.println("ef"+" "+enemy_frequency);

        if(number_of_enemies < 6){

            Random r1 = new Random();
            enemy_frequency_time = timer - (enemy_frequency_time_first+enemy_stoptime);

            if ((enemy_frequency_time == enemy_frequency) && (enemy.hp == 0))enemy.hp = -1;
            if ((enemy.hp == -1)&&(!enemy.hp_flag)){
                teki_y = r1.nextInt(_height-(int)enemy._height);
                enemy._pos._x = 50 + _width;
                enemy._pos._y = teki_y;
                enemy_frequency_time_first = timer;
                enemy.hp = 1;
                enemy.hp_flag = true;
                enemy_stoptime = 0;
                Random r2 = new Random();
                enemy_frequency = r2.nextInt(81) + 55;
            }
        }else{
            enemy_stoptime += 1;
        }

    }

     */
//弾関係
//private static final int fighterbullet_number = 15;  //自機弾の数
//private Sprite2D[] fighterbullet = new Sprite2D[fighterbullet_number];  //自機の弾
//public int fighterbullet_on = 1;


    /*
    //自機弾を発射するクラス
    public void FighterBulletMove(FighterBullet1 fighterbullet[],int _width){
        for (int i = 0; i < fighterbullet.length; i++) {
            if(fighterbullet[i].hp == 1) {
                if (fighterbullet[i]._pos._x < _width) {
                    fighterbullet[i]._pos._x += 9;
                } else {
                    fighterbullet[i].hp = 0;
                    fighterbullet[i].hp_flag = false;
                }

            }
        }
    }

    //自機弾の描画
    public void FighterBulletDraw(FighterBullet1 fighterbullet[],GL10 gl){
        for(int i = 0;i< fighterbullet.length; i++){
            if(fighterbullet[i].hp == 1)fighterbullet[i].draw(gl);
        }
    }
    //自機弾の生成
    public void FighterBulletGeneration(FighterBullet1 fb_1[],int timer) {
        for (int i = 0; i < fb_1.length; i++) {
            if(((timer%50)==0)&&(fb_1[i].hp==0)){
                fb_1[i].hp = -1;
            }
            if((fb_1[i].hp == -1)&&(!(fb_1[i].hp_flag))){
                //fighterbullet[i]._pos._x = fighter._pos._x + fighter_width;
                //fighterbullet[i]._pos._y = fighter._pos._y + (fighter_height/2);
                fb_1[i].hp_flag = true;
                fb_1[i].hp = 1;
                System.out.println("HP"+i+" "+fb_1[i].hp);
                System.out.println("HP"+i+" "+fb_1[i].hp_flag);

                break;
            }
        }
    }

     */
    /*
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

     */
