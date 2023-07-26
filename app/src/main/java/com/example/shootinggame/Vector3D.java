//三次元ベクトルを扱う際に使用するクラス

package com.example.shootinggame;

public class Vector3D
{
	//三次元ベクトルの座標
	public float _x;
	public float _y;
	public float _z;

	//コンストラクタ
	public Vector3D(float x,float y,float z)
	{
		_x = x;
		_y = y;
		_z = z;
	}
}
