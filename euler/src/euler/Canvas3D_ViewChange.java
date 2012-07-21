/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package euler;

//******************************************************************************
//マウスのドラッグで視点移動を行うCanvas3D拡張クラス“Canvas3D_ViewChange”
//
//Canvas3Dを継承
//MouseMotionLisenerを実装
//
//コンストラクタの引数はカメラの距離とマウス感度
//さらに，コンストラクタの引数にはGraphicsConfigurationが必要（Canvas3Dと同じ仕様）
//
//フィールドとして，SimpleUniverse,ViewingPlatformを持つ
//
//球座標を使用
//動径の長さ一定，マウスのX移動はφ，マウスのY移動はθに対応
//動径の長さは“camera_distance”，初期角度は“theta”と“phi”で調節
//マウス感度は“sensitivity”で調節
//******************************************************************************

//全般のためのインポート
import java.awt.*;
import java.awt.event.*;

//Java3D関係のインポート
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;

public class Canvas3D_ViewChange extends Canvas3D
{
 
 //=============================================================================
 //クラスのフィールド

 //マウスの動きに対する感度
 float sensitivity;

 //動径の長さ（座標中心と視点との距離）
 float camera_distance;

 //新規取得するマウスのx,y座標
 int new_x, new_y;
 
 //前に取得したマウスのx,y座標
 int pre_x, pre_y;

 //SimpleUniverseをフィールドとしてもっておく。
 SimpleUniverse universe;

 //視点の座標変換のためのグループ
 TransformGroup Camera;

 //視点（カメラ）の座標
 float camera_x, camera_y, camera_z, camera_xz, camera_xy, camera_yz = 0;

 //極座標のパラメータ
 //（初期位置の設定も行う）
 float phi = (float)Math.PI/8;
 float theta =  (float)Math.PI/8;

 //座標変換クラス
 Transform3D Transform_camera_pos;   //カメラの位置
 Transform3D Transform_camera_phi;   //phiに関する回転
 Transform3D Transform_camera_theta; //thetaに関する回転

 //3次元ベクトル（カメラの位置用）
 Vector3f Vector_camera_pos;

 
 //=============================================================================
 //コンストラクタ
 public Canvas3D_ViewChange(float Distance, float Sensitivity, GraphicsConfiguration config)
 {
  //親クラスのコンストラクタを呼ぶ. 引数はGraphicsConfiuration.   
  super(config);

  //カメラの原点からの距離を設定
  camera_distance = Distance;

  //マウス感度を調節
  sensitivity = Sensitivity;

  //空のSimpleUniverseを生成
  universe = new SimpleUniverse(this);

  //============================================================================
  //視点（カメラ）について設定
  //============================================================================

  //------------------------------------------------------------------  
  //カメラ全般の初期設定

  //SimpleUniverseが生成したViewingPlatformを取得  
  ViewingPlatform vp = universe.getViewingPlatform();

  //ViewingPlatformの座標変換グループとして，“Camera”を割り当てる
  Camera = vp.getViewPlatformTransform();

  //------------------------------------------------------------------  
  //カメラの位置に関する初期設定

  //theta関係の計算（球座標→直交座標）
  camera_y = camera_distance * (float)Math.sin(theta);
  camera_xz = camera_distance * (float)Math.cos(theta);

  //phi関係の計算（球座標→直交座標）
  camera_x =  camera_xz * (float)Math.sin(phi);
  camera_z =  camera_xz * (float)Math.cos(phi);

  //カメラの位置ベクトルを用意
  Vector_camera_pos = new Vector3f(camera_x, camera_y, camera_z);

  //カメラ位置の座標変換クラスを用意
  Transform_camera_pos = new Transform3D();

  //初期位置設定ための座標変換を用意する。
  Transform_camera_pos.setTranslation(Vector_camera_pos);

  //------------------------------------------------------------------  
  //カメラの向きに関する初期設定

  //カメラの向きの座標変換クラスを用意
  Transform_camera_phi = new Transform3D();
  Transform_camera_theta = new Transform3D(); 

  //カメラの向きの初期設定
  Transform_camera_theta.rotX(-theta);
  Transform_camera_phi.rotY(phi);

  //------------------------------------------------------------------  
  //以上の設定をカメラに反映

  //合成する
  Transform_camera_phi.mul(Transform_camera_theta);
  Transform_camera_pos.mul(Transform_camera_phi);

  //座標変換実行
  Camera.setTransform(Transform_camera_pos);

  //============================================================================
  //マウスの設定
  //============================================================================

  //マウス入力用のクラス，mouse_classをインスタンス化
  mouse_ViewChange mouse = new mouse_ViewChange();

  //このフレームにマウス入力クラスを登録
  addMouseMotionListener(mouse);

 }



 //*****************************************************************************
 //mouse_ViewChange：マウス入力用のクラス
 //*****************************************************************************
 class mouse_ViewChange implements MouseMotionListener
 {

  //============================================================================
  //マウスが動いた時に呼ばれるメソッド
  //============================================================================
  public void mouseMoved(MouseEvent event)
  {
    //常にマウス座標を更新しておく
    pre_x = event.getX();
    pre_y = event.getY();
  }


  //============================================================================
  //マウスがドラッグされた時に呼ばれるメソッド
  //============================================================================
  public void mouseDragged(MouseEvent event)
  {

   //現在のx座標を取得
   new_x = event.getX();
   new_y = event.getY();

   //thetaとphiの値を更新
   //（マウスの動きと視点の動きの向きはここで調整する！）
   theta -= sensitivity*(new_y - pre_y);
   phi += sensitivity*(new_x - pre_x);

   //===========================================================================
   //極座標を直交座標へ直す

   //theta関係の計算（球座標→直交座標）
   camera_y = camera_distance * (float)Math.sin(theta);
   camera_xz = camera_distance * (float)Math.cos(theta);

   //phi関係の計算（球座標→直交座標）
   camera_x =  camera_xz * (float)Math.sin(phi);
   camera_z =  camera_xz * (float)Math.cos(phi);

   //===========================================================================
   //座標変換クラスを用意する

   //カメラの位置ベクトルを作る
   Vector_camera_pos.x = camera_x;
   Vector_camera_pos.y = camera_y;
   Vector_camera_pos.z = camera_z;

   //座標変換クラスを初期化（重要！）
   Transform_camera_pos.setIdentity();

   //平行移動の座標変換を用意
   Transform_camera_pos.setTranslation(Vector_camera_pos);
    
   //回転の座標変換を用意
   Transform_camera_theta.rotX(-theta);
   Transform_camera_phi.rotY(phi);

   //===========================================================================
   //カメラの座標変換実行
   
   //合成する
   Transform_camera_phi.mul(Transform_camera_theta);
   Transform_camera_pos.mul(Transform_camera_phi);

   //座標変換実行
   Camera.setTransform(Transform_camera_pos);

   //===========================================================================
   //マウス座標を更新しておく
   pre_x = event.getX();
   pre_y = event.getY();

  }
 }
 //mouse_ViewChangeここまで
 //*****************************************************************************

}

