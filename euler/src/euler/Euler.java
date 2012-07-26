package euler;

//******************************************************************************
//exp(jθ)のグラフ  “euler.java”
//
//複素関数グラフを描画
//
//※Canvas3D_ViewChangeクラスが必要 <= Required Classes
//※Axis_classクラスが必要
//※SlideBar2クラスが必要
//******************************************************************************

// Swing 関係のインポート
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

// Java3D 関係のインポート
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;

public class Euler implements MouseMotionListener
{

 //=============================================================================
 //フィールド
 //=============================================================================

 //曲線のパラメータ : Parameter for the spring's curvature
 double radius = 2.0d;  // Radius of Spring
 int z_range = 10;      // Range in Z-direction (Effectively # of turns)
 int data_point=60;     // Data Points per Turn

 //スライドバー : SlideBar
 SlideBar2 bar;
 int slidebar_value_max;
 float bar_value;

 //球の座標変換関係 : Related to Sphere's coordinate conversion
 Transform3D sphere_transform;
 Vector3f sphere_vector;
 TransformGroup sphere_tg;

 //複素ベクトル（赤線）の座標変換関係 : Related to a vector pointing the sphere
 Transform3D line_transform1;
 Transform3D line_transform2;
 TransformGroup line_tg; 
 Vector3f line_vector;

 //=============================================================================
 //メインメソッド - Main Function
 //=============================================================================
 public static void main(String args[])
 {
  Euler frame = new Euler();
 }

 //=============================================================================
 //コンストラクタ - Constructer
 //=============================================================================
 public Euler()
 {

  //============================================================================
  //まずは、基礎フレームの設定。
  //============================================================================
  //メイン・ウィンドウ作成 : Create a window
  JFrame frame = new JFrame();
  //ウィンドウのサイズ設定 : Set Window Size
  frame.setSize(650+10,600+35);
  //ウィンドウのタイトル設定 : Set Window Title
  frame.setTitle("exp(jθ)");
  //ウィンドウを閉じる動作の登録 : Applet terminates if window is closed
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  //コンテントペインを作成 : Create a window's content pane
  JPanel cp = new JPanel();
  //コンテントペイン上のレイアウトは全て手動で行う : Content's layout is set to Manual
  cp.setLayout(null);
  //フレームに、コンテントペインを登録 : Assign the content pane to the window frame
  frame.add(cp);
  //ウィンドウを可視化 : Make the window and its content visible
  frame.setVisible(true);

  //============================================================================
  //スライドバー : Slide bar
  //============================================================================ 
  //スライドバーを作成 : Create a slide bar
  bar = new SlideBar2();
  //コンテントペイン : Add the slide bar into the content pane
  cp.add(bar);
  //サイズ決定 : Set size of slide bar (Right of Window)
  bar.setBounds(600,0,50,600);
  //Slidebarのinitial()はサイズ決定，ウィンドウ表示後に呼ぶ。
  bar.initialize();
  //バーの動きを，このクラスのマウス動作と結びつける : Link mouse movement event to slide bar
  bar.addMouseMotionListener(this);
  //バーの最大値 : Upper bound of bar's value
  slidebar_value_max = bar.getSize().height - 16;


  //============================================================================
  //次にJava3D関係の設定。 : Java3D Initialization
  //============================================================================
  //現在使用している画面の、ハードウェア情報を取得する : Obtain necessary hardware data
  GraphicsConfiguration g_config = SimpleUniverse.getPreferredConfiguration();
  //Canvas3D_ViewChangeクラスを用意する（距離は40, マウス感度は0.05）
  // Create Canvas3D_ViewChange Class with viewing distance 40 with mouse sensitivity 0.05
  Canvas3D_ViewChange canvas = new Canvas3D_ViewChange(40.0f, 0.02f, g_config);
  //3D表示領域の大きさを設定。今回はウィンドウいっぱいに表示する
  // Set 3D image display area within window frame (In this case entire window frame)
  canvas.setBounds(0,0,600,600);
  //コンテントペインにCanvas3Dを登録 : Add this Canvas3D object to the content pane
  cp.add(canvas);


  //============================================================================
  //3D空間を構築していきます : Create virtual 3D space
  //============================================================================

  //============================================================================
  //全般 : General Setup
  //Canvas3DクラスのSimpleUniverseを利用。: Create an instance of SimpleUniverse
  SimpleUniverse universe = canvas.universe; 
  //座標軸クラスをインスタンス化 : Create an instance of complex-valued coordinate system
  complex_Axis_class axis = new complex_Axis_class(20.0f);
  //universeへ登録 : Add this axis to "universe"
  universe.addBranchGraph(axis.Axis_group);


  //============================================================================
  //らせん状の補助線を書く : Draw Helical Line

  //らせん状に頂点を打って，短い線分で曲線を表現 : Draw points on Helix and connect them with short lines
  Point3d[] line_vertex = new Point3d[data_point*z_range]; // Allocate space to put coordinates of vertices
  for(int i=0;i < data_point*z_range;i++) // Parametrize the vertices with euler eqn
  {
   line_vertex[i] = new Point3d(radius*Math.cos(2*Math.PI*i/data_point), radius*Math.sin(2*Math.PI*i/data_point), radius*(i - z_range*data_point/2)/data_point);
  } 
  //連続する点を指定（小区間に分割することも可能） : # of data points in a strip
  int[] strip = {data_point*z_range};
  //折れ線のオブジェクトを作成 : Create a LineStripArray obj to which we'll store vertices coordinates
  LineStripArray line = new LineStripArray(line_vertex.length, GeometryArray.COORDINATES, strip);
  //折れ線オブジェクトに頂点座標を登録 : Store coordinates
  line.setCoordinates(0, line_vertex);
  //Shape3Dとして図形オブジェト化 : Create Shape3D obj to which we store resulting shape
  Shape3D line_shape = new Shape3D(line);
  //「枝」を作成 : Create a branch group for it.
  BranchGroup line_group = new BranchGroup();
  //折れ線を「枝」に登録 : Store the shape into this branch group
  line_group.addChild(line_shape);
  //「枝」をuniverseに登録 : Insert the branch group to "universe"
  universe.addBranchGraph(line_group);  


  //============================================================================
  //球を書く : Draw Sphere

  //質感，色などの設定 : Setup Material Properties
  Appearance appearance = new Appearance();
  Material material = new Material();
  material.setDiffuseColor(0.0f,1.0f,1.0f);
  appearance.setMaterial(material);
  //球を生成 : Create Sphere Object
  Sphere sphere = new Sphere(0.6f,appearance);
  //「枝」を作成 : Create a branch group for it
  BranchGroup sphere_group = new BranchGroup();
  //座標変換グループ : Create Coordinate Transformation Group
  sphere_tg = new TransformGroup();
  //座標変換許可設定 : Allow modification to the transformation group
  sphere_tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
  //球を座標変換グループに登録 : Add the sphere to the transformation group
  sphere_tg.addChild(sphere);
  //座標変換グループを「枝」に登録 : Add the transformation group to the branch group
  sphere_group.addChild(sphere_tg);
  //「枝」をuniverseに登録 : Insert the sphere branch group to "universe"
  universe.addBranchGraph(sphere_group);
  //初期位置へ座標変換する : Transform the initial coordinate
  sphere_transform = new Transform3D();
  sphere_vector = new Vector3f((float)radius,0,0);
  sphere_transform.setTranslation(sphere_vector); // Translate via specified vector
  sphere_tg.setTransform(sphere_transform);     // Apply translation


  //============================================================================
  //複素ベクトルを書く : Draw a line to the sphere
 
  //線分の頂点を定義 : Define a line connecting (0,0,0) and (r,0,0) (initial position)
  Point3d[] line_vertex2 = new Point3d[2];
  line_vertex2[0] = new Point3d(0, 0, 0);
  line_vertex2[1] = new Point3d(radius, 0, 0);  
  //連続する点を指定（小区間に分割することも可能）: Involve two points
  int[] strip2 = {2};
  //折れ線オブジェクトに座標を登録。（色をつけるためのには引数にGeometryArray.COLOR_3が必要)
  // Create a LineStripArray obj to store point coord. Use COLOR_3 to use "Color3f"
  LineStripArray line2 = new LineStripArray(line_vertex2.length, GeometryArray.COORDINATES | GeometryArray.COLOR_3, strip2);
  line2.setCoordinates(0, line_vertex2);
  //色の設定 : Set Color
  Color3f red = new Color3f(1.0f, 0.0f, 0.0f);
  line2.setColor(0, red);
  line2.setColor(1, red);
  //Shape3Dを作成 : Create Shape3D obj for line2
  Shape3D line_shape2 = new Shape3D(line2);
  //「枝」を作成 : Create a branch group to store it
  BranchGroup line_group2 = new BranchGroup();
  //座標変換クラスを作成 : Transformation Group
  line_tg = new TransformGroup();
  line_tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);  
  //線分を座標変換クラスに登録 : Add the shape into transformation group
  line_tg.addChild(line_shape2);
  //座標変換クラスを「枝」に登録 : Add the transformation group to the branch group
  line_group2.addChild(line_tg);
  //「枝」をuniverseに登録 : Insert this branch group to "universe"
  universe.addBranchGraph(line_group2);  
  //初期位置への座標変換準備 : Coordinate Initialization for the line
  line_transform1 = new Transform3D();
  line_transform2 = new Transform3D();
  line_vector = new Vector3f();
  

  //============================================================================
  //ライトの設定（いつもと同じ平行光源を設定）: Light source setup
  //============================================================================
  //ライトの強さ及び色 : Strength and Color of Light Source
  Color3f light_color = new Color3f(3.4f,3.4f,3.4f);
  //ライトの方向 : Direction of shorn light with respect to the origin
  Vector3f light_direction = new Vector3f(-0.8f,-1.2f,-1.0f);
  //平行光源を用意 : Prepare the light source
  DirectionalLight light = new DirectionalLight(light_color,light_direction);
  //ライトで照らす範囲（球範囲で指定，中心座標とその半径）: Define light cone/cylinder
  BoundingSphere bounds = new BoundingSphere(new Point3d( 0.0f , 0.0f , 0.0f ) , 1000);
  //範囲を登録 : Shorn regions
  light.setInfluencingBounds(bounds);
  //ライト用の「枝」を作る : create a branch group for light
  BranchGroup BranchGroup_Light = new BranchGroup();
  //「枝」にライトを登録 : Add light to the branch group
  BranchGroup_Light.addChild(light);
  //仮想空間に「枝」を登録 : Insert the branch group to "universe"
  universe.addBranchGraph(BranchGroup_Light);
 } 


 //=============================================================================
 //マウスが動いたときに呼ばれる : Event-Handler for Mouse Moved to Slide bar
 //=============================================================================
 public void mouseMoved(MouseEvent event)
 {
  //何もしない
 }


 //=============================================================================
 //マウスがドラッグされたときに呼ばれる : Event-Handler for Mouse Dragged on Slight bar
 //============================================================================= 
 public void mouseDragged(MouseEvent event)
 {
   bar_value = bar.value * z_range * data_point / slidebar_value_max;

   //=========================================================================== 
   //球を動かす

   //座標変換クラスのリセット : Reset Coordinate Conversion
   sphere_transform.setIdentity();
   //球を移動させる先の位置ベクトル : Compute the next position for the sphere
   sphere_vector.x = (float)radius*(float)Math.cos(2*Math.PI*bar_value/data_point);
   sphere_vector.y = (float)radius*(float)Math.sin(2*Math.PI*bar_value/data_point);
   sphere_vector.z = (float)radius*(bar_value - z_range*data_point/2)/data_point;  
   //平行移動の座標変換 : Translate by specified vector
   sphere_transform.setTranslation(sphere_vector);
   //座標変換実行 : apply transformation
   sphere_tg.setTransform(sphere_transform);

   //=========================================================================== 
   //複素ベクトルを動かす : Move the line to the sphere

   //座標変換クラスのリセット
   line_transform1.setIdentity();
   line_transform2.setIdentity();  
   //線分を回転させる座標変換 : Rotate line_transform1 (one end) around Z-axis
   line_transform1.rotZ(2*Math.PI*bar_value/data_point);  
   //線分を移動させる先の位置ベクトル : Translate line_transform2 (the other end) along Z-axis
   line_vector.x = 0.0f; 
   line_vector.y = 0.0f; 
   line_vector.z = (float)radius*(bar_value - z_range*data_point/2)/data_point;
   //平行移動の座標変換 : Apply translation
   line_transform2.setTranslation(line_vector);
   //座標変換を合成 : Produce the new coordinates (?? Makes no sense to me ??)
   line_transform2.mul(line_transform1);
   //座標変換実行
   line_tg.setTransform(line_transform2); 
 }
}

