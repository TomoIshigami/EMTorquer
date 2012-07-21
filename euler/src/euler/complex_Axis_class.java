/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package euler;

//******************************************************************************
//複素座標軸のクラス “complex_Axis_class.java”
//
//このクラスの“Axis_group”フィールドを，
//別プログラム内のSimpleUniverseのインスタンスへaddBranchGraph()して使用する
//******************************************************************************

//Java3D関係のインポート
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;

//フォント関係のためのインポート
import java.awt.*;

public class complex_Axis_class 
{

 //=============================================================================
 //クラスのフィールド

 BranchGroup Axis_group;


 //=============================================================================
 //コンストラクタ （引数はサイズ）
 public complex_Axis_class(float size)
 {
  //「枝」を作成
  Axis_group = new BranchGroup();

  //============================================================================
  //X軸
  //============================================================================
  
  //材質
  Appearance ap_x1 = new Appearance();
  Material mat_x1 = new Material();
  //表面効果
  mat_x1.setDiffuseColor(1.0f,0.0f,0.0f);
  //その他視覚効果
  ap_x1.setMaterial(mat_x1);
  //座標変換クラス
  TransformGroup tg_x1 = new TransformGroup();
  //座標変換の書き換えを許可
  tg_x1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
  //座標変換内容
  Transform3D transform_x1 = new Transform3D();
  //円柱（引数は半径，長さ，材質）
  Cylinder x1 = new Cylinder(size/100.0f, size, ap_x1);
  //座標変換クラスへ軸を登録
  tg_x1.addChild(x1);
  transform_x1.rotZ(-Math.PI/2);
  tg_x1.setTransform(transform_x1);
  //全体の「枝」へ座標変換クラスを登録
  Axis_group.addChild(tg_x1);

  //材質
  Appearance ap_x2 = new Appearance();
  Material mat_x2 = new Material();
  //表面効果
  mat_x2.setDiffuseColor(1.0f,0.0f,0.0f);
  //その他視覚効果
  ap_x2.setMaterial(mat_x1);
  //座標変換クラス
  TransformGroup tg_x2 = new TransformGroup();
  //座標変換の書き換えを許可
  tg_x2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
  //座標変換内容
  Transform3D transform_x2 = new Transform3D();
  //円錐（引数は半径，長さ，材質）
  Cone x2 = new Cone(size/100.0f*2.5f, size/10.0f, ap_x2);
  //座標変換クラスへ軸を登録
  tg_x2.addChild(x2);
  //全体の「枝」へ座標変換クラスを登録
  Axis_group.addChild(tg_x2);

  Transform3D rot_x2 = new Transform3D();
  rot_x2.rotZ(-Math.PI/2);
  Vector3f x2_vec = new Vector3f(size/2.0f, 0.0f, 0.0f);
  transform_x2.setTranslation(x2_vec);
  transform_x2.mul(rot_x2);
  tg_x2.setTransform(transform_x2);
  

  
  //============================================================================
  //Y軸
  //============================================================================
  
  //材質
  Appearance ap_y1 = new Appearance();
  Material mat_y1 = new Material();
  //表面効果
  mat_y1.setDiffuseColor(0.0f,1.0f,0.0f);
  //その他視覚効果
  ap_y1.setMaterial(mat_y1);
  //座標変換クラス
  TransformGroup tg_y1 = new TransformGroup();
  //座標変換の書き換えを許可
  tg_y1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
  //座標変換内容
  Transform3D transform_y1 = new Transform3D();
  //円柱（引数は半径，長さ，材質）
  Cylinder y1 = new Cylinder(size/100.0f, size, ap_y1);
  //座標変換クラスへ軸を登録
  tg_y1.addChild(y1);
  //全体の「枝」へ座標変換クラスを登録
  Axis_group.addChild(tg_y1);


  //材質
  Appearance ap_y2 = new Appearance();
  Material mat_y2 = new Material();
  //表面効果
  mat_y2.setDiffuseColor(0.0f,1.0f,0.0f);
  //その他視覚効果
  ap_y2.setMaterial(mat_y1);
  //座標変換クラス
  TransformGroup tg_y2 = new TransformGroup();
  //座標変換の書き換えを許可
  tg_y2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
  //座標変換内容
  Transform3D transform_y2 = new Transform3D();
  //円錐（引数は半径，長さ，材質）
  Cone y2 = new Cone(size/100.0f*2.5f, size/10.0f, ap_y2);
  //座標変換クラスへ軸を登録
  tg_y2.addChild(y2);
  //全体の「枝」へ座標変換クラスを登録
  Axis_group.addChild(tg_y2);
  Vector3f y2_vec = new Vector3f(0.0f, size/2.0f, 0.0f);
  transform_y2.setTranslation(y2_vec);
  tg_y2.setTransform(transform_y2);


  //============================================================================
  //Z軸
  //============================================================================
  
  //材質
  Appearance ap_z1 = new Appearance();
  Material mat_z1 = new Material();
  //表面効果
  mat_z1.setDiffuseColor(0.0f,0.0f,1.0f);
  //その他視覚効果
  ap_z1.setMaterial(mat_z1);
  //座標変換クラス
  TransformGroup tg_z1 = new TransformGroup();
  //座標変換の書き換えを許可
  tg_z1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
  //座標変換内容
  Transform3D transform_z1 = new Transform3D();
  //円柱（引数は半径，長さ，材質）
  Cylinder z1 = new Cylinder(size/100.0f, size*2, ap_z1);
  //座標変換クラスへ軸を登録
  tg_z1.addChild(z1);
  transform_z1.rotX(Math.PI/2);
  tg_z1.setTransform(transform_z1);
  //全体の「枝」へ座標変換クラスを登録
  Axis_group.addChild(tg_z1);

  //材質
  Appearance ap_z2 = new Appearance();
  Material mat_z2 = new Material();
  //表面効果
  mat_z2.setDiffuseColor(0.0f,0.0f,1.0f);
  //その他視覚効果
  ap_z2.setMaterial(mat_z2);
  //座標変換クラス
  TransformGroup tg_z2 = new TransformGroup();
  //座標変換の書き換えを許可
  tg_z2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
  //座標変換内容
  Transform3D transform_z2 = new Transform3D();
  //円錐（引数は半径，長さ，材質）
  Cone z2 = new Cone(size/100.0f*2.5f, size/10.0f, ap_z2);
  //座標変換クラスへ軸を登録
  tg_z2.addChild(z2);
  //全体の「枝」へ座標変換クラスを登録
  Axis_group.addChild(tg_z2);

  Transform3D rot_z2 = new Transform3D();
  rot_z2.rotX(Math.PI/2);
  Vector3f z2_vec = new Vector3f(0.0f, 0.0f, size/2.0f*2);
  transform_z2.setTranslation(z2_vec);
  transform_z2.mul(rot_z2);
  tg_z2.setTransform(transform_z2);


  //============================================================================
  //3Dテキスト
  //============================================================================

  //文字は白色
  Material mat_text = new Material();
  mat_text.setDiffuseColor(1.0f, 1.0f, 1.0f);
  Appearance ap_text = new Appearance();
  ap_text.setMaterial(mat_text);
  //フォントはArial，太字，大きさ
  Font basic_font = new Font("Arial", java.awt.Font.BOLD, 2);
  //3Dフォントを作成
  Font3D font = new Font3D(basic_font, new FontExtrusion());

  //実軸のテキスト（位置はX軸の先端）
  Text3D x_text = new Text3D(font, "Re", new Point3f(size/2.0f + size/20.0f, 0.0f, 0.0f));
  Shape3D x_label = new Shape3D();	
  x_label.setGeometry(x_text);
  x_label.setAppearance(ap_text);  
  Axis_group.addChild(x_label);
  //嘘軸のテキスト（位置はY軸の先端）
  Text3D y_text = new Text3D(font, "Im", new Point3f(0.0f, size/2.0f + size/20.0f, 0.0f));
  Shape3D y_label = new Shape3D();	
  y_label.setGeometry(y_text);
  y_label.setAppearance(ap_text);  
  Axis_group.addChild(y_label);
  //θ軸のテキスト（位置はZ軸の先端）
  Text3D z_text = new Text3D(font, "θ", new Point3f(size/20.0f, 0.0f, size/1.0f - size/20.0f));
  Shape3D z_label = new Shape3D();	
  z_label.setGeometry(z_text);
  z_label.setAppearance(ap_text);  
 Axis_group.addChild(z_label);
 } 
}

