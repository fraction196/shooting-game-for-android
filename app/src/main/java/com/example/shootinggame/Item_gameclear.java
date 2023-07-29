/*
 *　ゲームクリアアイテム
 */

package com.example.shootinggame;

import javax.microedition.khronos.opengles.GL10;

public class Item_gameclear extends Sprite2D {
    //敵の生成、移動、描画を行う関数
    public void Item_gameclear_GMD(Item_gameclear item_gameclear[],GL10 gl) {
        for (int i = 0; i < item_gameclear.length; i++) {
            if (item_gameclear[i]._pos._x > 1400) item_gameclear[i]._pos._x -= 4;
            item_gameclear[i].draw(gl);
        }
    }
    //敵の初期化
    public void Item_gameclear_Init(Item_gameclear item_gameclear[], int _width, int _height) {
        for (int i = 0; i < item_gameclear.length; i++) {
            item_gameclear[i]._pos._x = _width + 100;
            item_gameclear[i]._pos._y = _height / 2 + item_gameclear[i]._height;
        }
    }
}