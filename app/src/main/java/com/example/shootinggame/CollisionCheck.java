/*
 * 衝突判定を行うクラス
 */

package com.example.shootinggame;

public class CollisionCheck {
        //private Context _context;

        //サウンド
        //private static SoundPool se_explosion;
        private int soundID;

        /*
        //@Override
        public CollisionCheck(Context context)
        {
            _context = context;
            se_explosion = new SoundPool(1, AudioManager.STREAM_MUSIC,0);
            soundID = se_explosion.load(context, R.raw.explode,1);
        }

         */
    //自機とオブジェクトとの当たり判定
    public void FighterCollisionCheck(Sprite2D obj[],Fighter fighter){
        for(int i=0; i<obj.length; i++) {

            float of_x = obj[i]._pos._x - fighter._pos._x;
            float of_y = obj[i]._pos._y - fighter._pos._y;
            if(!fighter.invincible_time) {
                if ((of_x <= fighter.fighter_width)&&(of_x >= -obj[i]._width)&&(of_y <= fighter.fighter_height)&&(of_y >= -obj[i]._height)){
                    //se_explosion.play(soundID, 1.0F, 1.0F, 0, 0, 1.0F);
                    fighter.hp -= 1;
                    fighter.invincible_time = true;
                }
            }
        }
    }
    public void FighterCollisionCheck2(Sprite2D obj1[],Sprite2D obj2[][],Fighter fighter){
        for(int j=0; j<obj1.length; j++) {
            for (int i = 0; i < obj2[j].length; i++) {
                float of_x = obj2[j][i]._pos._x - fighter._pos._x;
                float of_y = obj2[j][i]._pos._y - fighter._pos._y;
                if (!fighter.invincible_time) {
                    if ((of_x <= fighter.fighter_width) && (of_x >= -obj2[j][i]._width) && (of_y <= fighter.fighter_height) && (of_y >= -obj2[j][i]._height)) {
                        //se_explosion.play(soundID, 1.0F, 1.0F, 0, 0, 1.0F);
                        fighter.hp -= 1;
                        fighter.invincible_time = true;
                    }
                }
            }
        }
    }
    //オブジェ１とオブジェ２（配列同士の当たり判定）
    public void ObjectCollisionCheck(Sprite2D obj1[],Sprite2D obj2,int score){
        for (int i = 0; i < obj1.length; i++) {
            float o12_x = obj1[i]._pos._x - obj2._pos._x;
            float o12_y = obj1[i]._pos._y - obj2._pos._y;
            if((obj1[i].hp >= 1)&&(obj2.hp >= 1)){
                if ((o12_x <= obj2._width) && (o12_x >= -obj1[i]._width) && (o12_y <= obj2._height) && (o12_y >= -obj1[i]._height)) {
                    //se_explosion.play(soundID, 1.0F, 1.0F, 0, 0, 1.0F);
                    if(!obj1[i].invincible_time){
                        obj1[i].hp -= 1;
                        if (obj1[i].hp == 0) {
                            obj1[i].hp_flag = false;
                            obj1[i].score_flag = true;
                            //bj1[i].invincible_time = true;
                            //score += 200;
                        }
                    }
                    if(!obj2.invincible_time) {
                        obj2.hp -= 1;
                        obj2.invincible_time = true;
                        if (obj2.hp == 0) {
                            obj2.hp_flag = false;
                            obj2.score_flag = true;
                            //score += 200;
                        }
                    }
                }
            }
        }
    }
}
