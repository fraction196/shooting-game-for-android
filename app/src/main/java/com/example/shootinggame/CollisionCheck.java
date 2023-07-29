/*
 * 衝突判定を行うクラス
 */

package com.example.shootinggame;

public class CollisionCheck {
    //自機とオブジェクト（敵）との当たり判定
    public void FighterCollisionCheck(Sprite2D obj[],Fighter fighter){
        for(int i=0; i<obj.length; i++) {
            //オブジェクトと自機の座標の差を求める
                float of_x = obj[i]._pos._x - fighter._pos._x;
                float of_y = obj[i]._pos._y - fighter._pos._y;
            //自機が無敵時間でないとき
                if(!fighter.invincible_time) {
                    //オブジェクト同士が重なっているとき
                        if ((of_x <= fighter.fighter_width)&&(of_x >= -obj[i]._width)&&(of_y <= fighter.fighter_height)&&(of_y >= -obj[i]._height)){
                            //自機の体力を減らし無敵時間のフラグを立てる
                                fighter.hp -= 1;
                                fighter.invincible_time = true;
                        }
                }
        }
    }
    //自機とオブジェジェクト（敵弾）との衝突判定
    public void FighterCollisionCheck2(Sprite2D obj1[],Sprite2D obj2[][],Fighter fighter){
        for(int j=0; j<obj1.length; j++) {
            for (int i = 0; i < obj2[j].length; i++) {
                //オブジェクトと自機の座標の差を求める
                    float of_x = obj2[j][i]._pos._x - fighter._pos._x;
                    float of_y = obj2[j][i]._pos._y - fighter._pos._y;
                //自機が無敵時間でないとき
                    if (!fighter.invincible_time) {
                        //オブジェクト同士が重なっているとき
                            if ((of_x <= fighter.fighter_width) && (of_x >= -obj2[j][i]._width) && (of_y <= fighter.fighter_height) && (of_y >= -obj2[j][i]._height)) {
                                //自機の体力を減らし無敵時間のフラグを立てる
                                    fighter.hp -= 1;
                                    fighter.invincible_time = true;
                                //敵弾の体力を減らしフラグを下ろす
                                    obj2[j][i].hp -= 1;
                                    obj2[j][i].hp_flag = false;
                                    obj2[j][i].u_flag = false;
                                    obj2[j][i].d_flag = false;
                                    obj2[j][i].w_flag = false;
                                    obj2[j][i].n_w_flag = false;
                                    obj2[j][i].s_w_flag = false;
                            }
                    }
            }
        }
    }
    //自機弾とオブジェクト(敵)との衝突判定（配列同士の当たり判定）
    public void FighterBulletCollisionCheck(Sprite2D obj1[],Sprite2D obj2){
        for (int i = 0; i < obj1.length; i++) {
            //自機弾とオブジェクトの座標の差を求める
                float o12_x = obj1[i]._pos._x - obj2._pos._x;
                float o12_y = obj1[i]._pos._y - obj2._pos._y;
            //両者のオブジェクトの体力が1以上の時
                if((obj1[i].hp >= 1)&&(obj2.hp >= 1)){
                    //オブジェクト同士が重なっているとき
                        if ((o12_x <= obj2._width) && (o12_x >= -obj1[i]._width) && (o12_y <= obj2._height) && (o12_y >= -obj1[i]._height)) {
                            //無敵時間でないとき
                                if(!obj1[i].invincible_time){
                                    //オブジェクトのHPを1減らす
                                        obj1[i].hp -= 1;
                                    //HPが0になったとき
                                        if (obj1[i].hp == 0) {
                                            //生存フラグをオフに
                                                obj1[i].hp_flag = false;
                                                //obj1[i].score_flag = true;
                                                //bj1[i].invincible_time = true;
                                        }
                                }
                            //無敵時間でないとき
                                if(!obj2.invincible_time) {
                                    //オブジェクトの体力を減らし無敵時間のフラグを立てる
                                        obj2.hp -= 1;
                                        obj2.invincible_time = true;
                                    //HPが0になったとき
                                        if (obj2.hp == 0) {
                                            //生存フラグをオフにしてスコアフラグを立てる
                                                obj2.hp_flag = false;
                                                obj2.score_flag = true;
                                        }
                                }
                    }
                }
        }
    }
}
