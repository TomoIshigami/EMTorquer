/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package euler;

//******************************************************************************
//exp(jθ)のグラフ  “euler.java”
//
//複素関数グラフを描画
//
//※Canvas3D_ViewChangeクラスが必要
//※Axis_classクラスが必要
//※SlideBar2クラスが必要
//******************************************************************************

//Swing関係のインポート
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

//Java3D関係のインポート
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;

public class Euler implements MouseMotionListener
{

 //=============================================================================
 //フィールド
 //=============================================================================

 //曲線のパラメータ
 double radius = 5.0d;
 int z_range = 10;
 int data_point=60;

 //スライドバー
 SlideBar2 bar;
 int slidebar_value_max;
 float bar_value;

 //球の座標変換関係
 Transform3D sphere_transform;
 Vector3f sphere_vector;
 TransformGroup sphere_tg;

 //複素ベクトル（赤線）の座標変換関係
 Transform3D line_transform1;
 Transform3D line_transform2;
 TransformGroup line_tg; 
 Vector3f line_vector;

 //=============================================================================
 //メインメソッド
 //=============================================================================
 public static void main(String args[])
 {
  Euler frame = new Euler();
 }



 //=============================================================================
 //コンストラクタ
 //=============================================================================
 public Euler()
 {

  //============================================================================
  //まずは、基礎フレームの設定。
  //============================================================================
  //メイン・ウィンドウ作成
  JFrame frame = new JFrame();
  //ウィンドウのサイズ設定
  frame.setSize(650+10,600+35);
  //ウィンドウのタイトル設定
  frame.setTitle("exp(jθ)");
  //ウィンドウを閉じる動作の登録
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  //コンテントペインを作成
  JPanel cp = new JPanel();
  //コンテントペイン上のレイアウトは全て手動で行う
  cp.setLayout(null);
  //フレームに、コンテントペインを登録
  frame.add(cp);
  //ウィンドウを可視化
  frame.setVisible(true);

  //============================================================================
  //スライドバー
  //============================================================================ 
  //スライドバーを作成
  bar = new SlideBar2();
  //コンテントペイン
  cp.add(bar);
  //サイズ決定
  bar.setBounds(600,0,50,600);
  //Slidebarのinitial()はサイズ決定，ウィンドウ表示後に呼ぶ。
  bar.initialize();
  //バーの動きを，このクラスのマウス動作と結びつける
  bar.addMouseMotionListener(this);
  //バーの最大値
  slidebar_value_max = bar.getSize().height - 16;


  //============================================================================
  //次にJava3D関係の設定。
  //============================================================================
  //現在使用している画面の、ハードウェア情報を取得する
  GraphicsConfiguration g_config = SimpleUniverse.getPreferredConfiguration();
  //Cnavas3D_ViewChangeクラスを用意する（距離は40, マウス感度は0.05）
  Canvas3D_ViewChange canvas = new Canvas3D_ViewChange(40.0f, 0.02f, g_config);
  //3D表示領域の大きさを設定。今回はウィンドウいっぱいに表示する
  canvas.setBounds(0,0,600,600);
  //コンテントペインにCanvas3Dを登録
  cp.add(canvas);


  //============================================================================
  //3D空間を構築していきます
  //============================================================================

  //============================================================================
  //全般
  //Canvas3DクラスのSimpleUniverseを利用。
  SimpleUniverse universe = canvas.universe; 
  //座標軸クラスをインスタンス化
  complex_Axis_class axis = new complex_Axis_class(20.0f);
  //universeへ登録
  universe.addBranchGraph(axis.Axis_group);


  //============================================================================
  //らせん状の補助線を書く

  //らせん状に頂点を打って，短い線分で曲線を表現
  Point3d[] line_vertex = new Point3d[data_point*z_range];
  for(int i=0;i < data_point*z_range;i++)
  {
   line_vertex[i] = new Point3d(radius*Math.cos(2*Math.PI*i/data_point), radius*Math.sin(2*Math.PI*i/data_point), radius*(i - z_range*data_point/2)/data_point);
  } 
  //連続する点を指定（小区間に分割することも可能）
  int[] strip = {data_point*z_range};
  //折れ線のオブジェクトを作成
  LineStripArray line = new LineStripArray(line_vertex.length, GeometryArray.COORDINATES, strip);
  //折れ線オブジェクトに頂点座標を登録
  line.setCoordinates(0, line_vertex);
  //Shape3Dとして図形オブジェト化
  Shape3D line_shape = new Shape3D(line);
  //「枝」を作成
  BranchGroup line_group = new BranchGroup();
  //折れ線を「枝」に登録
  line_group.addChild(line_shape);
  //「枝」をuniverseに登録
  universe.addBranchGraph(line_group);  


  //============================================================================
  //球を書く

  //質感，色などの設定
  Appearance appearance = new Appearance();
  Material material = new Material();
  material.setDiffuseColor(0.0f,1.0f,1.0f);
  appearance.setMaterial(material);
  //球を生成
  Sphere sphere = new Sphere(0.6f,appearance);
  //「枝」を作成
  BranchGroup sphere_group = new BranchGroup();
  //座標変換グループ
  sphere_tg = new TransformGroup();
  //座標変換許可設定
  sphere_tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
  //球を座標変換グループに登録
  sphere_tg.addChild(sphere);
  //座標変換グループを「枝」に登録
  sphere_group.addChild(sphere_tg);
  //「枝」をuniverseに登録
  universe.addBranchGraph(sphere_group);
  //初期位置へ座標変換する
  sphere_transform = new Transform3D();
  sphere_vector = new Vector3f((float)radius,0,0);
  sphere_transform.setTranslation(sphere_vector);
  sphere_tg.setTransform(sphere_transform);


  //============================================================================
  //複素ベクトルを書く
 
  //線分の頂点を定義
  Point3d[] line_vertex2 = new Point3d[2];
  line_vertex2[0] = new Point3d(0, 0, 0);
  line_vertex2[1] = new Point3d(radius, 0, 0);  
  //連続する点を指定（小区間に分割することも可能）
  int[] strip2 = {2};
  //折れ線オブジェクトに座標を登録。（色をつけるためのには引数にGeometryArray.COLOR_3が必要） 
  LineStripArray line2 = new LineStripArray(line_vertex2.length, GeometryArray.COORDINATES | GeometryArray.COLOR_3, strip2);
  line2.setCoordinates(0, line_vertex2);
  //色の設定
  Color3f red = new Color3f(1.0f, 0.0f, 0.0f);
  line2.setColor(0, red);
  line2.setColor(1, red);
  //Shape3Dを作成
  Shape3D line_shape2 = new Shape3D(line2);
  //「枝」を作成
  BranchGroup line_group2 = new BranchGroup();
  //座標変換クラスを作成
  line_tg = new TransformGroup();
  line_tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);  
  //線分を座標変換クラスに登録
  line_tg.addChild(line_shape2);
  //座標変換クラスを「枝」に登録
  line_group2.addChild(line_tg);
  //「枝」をuniverseに登録
  universe.addBranchGraph(line_group2);  
  //初期位置への座標変換準備
  line_transform1 = new Transform3D();
  line_transform2 = new Transform3D();
  line_vector = new Vector3f();
  

  //============================================================================
  //ライトの設定（いつもと同じ平行光源を設定）
  //============================================================================
  //ライトの強さ及び色
  Color3f light_color = new Color3f(3.4f,3.4f,3.4f);
  //ライトの方向
  Vector3f light_direction = new Vector3f(-0.8f,-1.2f,-1.0f);
  //平行光源を用意
  DirectionalLight light = new DirectionalLight(light_color,light_direction);
  //ライトで照らす範囲（球範囲で指定，中心座標とその半径）
  BoundingSphere bounds = new BoundingSphere(new Point3d( 0.0f , 0.0f , 0.0f ) , 1000);
  //範囲を登録
  light.setInfluencingBounds(bounds);
  //ライト用の「枝」を作る
  BranchGroup BranchGroup_Light = new BranchGroup();
  //「枝」にライトを登録
  BranchGroup_Light.addChild(light);
  //仮想空間に「枝」を登録
  universe.addBranchGraph(BranchGroup_Light);
 } 


 //=============================================================================
 //マウスが動いたときに呼ばれる
 //=============================================================================
 public void mouseMoved(MouseEvent event)
 {
  //何もしない
 }


 //=============================================================================
 //マウスがドラッグされたときに呼ばれる
 //============================================================================= 
 public void mouseDragged(MouseEvent event)
 {
   bar_value = bar.value * z_range * data_point / slidebar_value_max;

   //=========================================================================== 
   //球を動かす

   //座標変換クラスのリセット
   sphere_transform.setIdentity();
   //球を移動させる先の位置ベクトル
   sphere_vector.x = (float)radius*(float)Math.cos(2*Math.PI*bar_value/data_point);
   sphere_vector.y = (float)radius*(float)Math.sin(2*Math.PI*bar_value/data_point);
   sphere_vector.z = (float)radius*(bar_value - z_range*data_point/2)/data_point;  
   //平行移動の座標変換
   sphere_transform.setTranslation(sphere_vector);
   //座標変換実行
   sphere_tg.setTransform(sphere_transform);

   //=========================================================================== 
   //複素ベクトルを動かす

   //座標変換クラスのリセット
   line_transform1.setIdentity();
   line_transform2.setIdentity();  
   //線分を回転させる座標変換
   line_transform1.rotZ(2*Math.PI*bar_value/data_point);  
   //線分を移動させる先の位置ベクトル
   line_vector.x = 0.0f; 
   line_vector.y = 0.0f; 
   line_vector.z = (float)radius*(bar_value - z_range*data_point/2)/data_point;
   //平行移動の座標変換
   line_transform2.setTranslation(line_vector);
   //座標変換を合成
   line_transform2.mul(line_transform1);
   //座標変換実行
   line_tg.setTransform(line_transform2); 
 }
}

