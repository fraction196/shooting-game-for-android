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
