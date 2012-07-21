/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package euler;

//******************************************************************************
//スライドバー（縦方向）  “SlideBar2.java”
//フィールド“volume”の値が、スライドバーの位置
//スライドバーの最大値は 寸法 - 16
//
//使用時の手順
//1.設置
//2.initial()クラスを呼ぶ（バーのサイズ決定，ウィンドウ表示後）
//3.設置先でもMouseMoitonListenerを実装し，
//  設置先でも bar.addMouseMotionListener(this)  などのように登録
//******************************************************************************

//==============================================================================
//インポート
//==============================================================================
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.Graphics;
import java.awt.Image;


//==============================================================================
//SlideBarクラス
//==============================================================================
public class SlideBar2 extends Canvas implements MouseMotionListener
{
           
 //=============================================================================
 //フィールド
 //=============================================================================

 //スライドバーの値
 int value=0;

 //マウスの現在座標
 int x=0;
 int y=0;

 //マウスの１つ前の座標
 int pre_x=0;
 int pre_y=0;

 //バー自体の位置
 int bar_pos=0;

 //オフセット
 int offset=0;

 //ダブルバッファ用のImageクラス
 Image iBuffer;

 //ダブルバッファ用のGraphicsクラス
 Graphics gBuffer;

 //寸法
 Dimension dimension;


 //=============================================================================
 //コンストラクタ
 //=============================================================================
 public SlideBar2()
 {
  //寸法を取得
  dimension = getSize();

  //MouseMotionListenerに登録
  addMouseMotionListener(this);  
 }

 //=============================================================================
 //外から呼ぶメソッド。スライドバッファを設置し，サイズを決定後に呼ぶ。
 //=============================================================================
 public void initialize()
 {
  bar_pos = getSize().height-16;
  getBuffer();
  drawBar();
 }


 //=============================================================================
 //ダブルバッファリング関係
 //=============================================================================
 public void getBuffer() 
 {
   //一応，寸法取得
   dimension = getSize();

   //描画用メモリ領域確保
   iBuffer = createImage(dimension.width,dimension.height);

   //iBufferからGraphicsインスタンスを生成
   gBuffer = iBuffer.getGraphics();
 }


 //=============================================================================
 //描画
 //=============================================================================
 public void drawBar()
 { 
  //寸法取得
  dimension = getSize();

  //gBufferの内容をクリア
  gBuffer.clearRect(0,0,dimension.width,dimension.height);

  //縦の線の色
  gBuffer.setColor(Color.gray); 

  //縦の線を書く
  gBuffer.drawLine(dimension.width/2,0,dimension.width/2,dimension.height);

  //バーの色
  gBuffer.setColor(Color.lightGray);

  //バーを書く
  gBuffer.fill3DRect(dimension.width/2-16,bar_pos,32,16,true);
   
  //スライドバーの値を更新
  value = dimension.height-bar_pos-16;
 }

 //=============================================================================
 //paint()メソッド
 //=============================================================================
 public void paint(Graphics g)
 {
  //iBufferの内容を描画
  g.drawImage(iBuffer,0,0,this);
 }


 //=============================================================================
 //マウスが動いたときに呼ばれる
 //=============================================================================
 public void mouseMoved(MouseEvent event)
 {
  //マウス座標を更新
  pre_x = event.getX();
  pre_y = event.getY();
 }

 //=============================================================================
 //マウスがドラッグされたときに呼ばれる
 //=============================================================================
 public void mouseDragged(MouseEvent event)
 {
  //一応，寸法取得
  dimension = getSize();

  //マウス座標を読み込み
  x=event.getX();
  y=event.getY();

  //マウス位置がスライドバー範囲外の場合は何もしない
//  if(ybar_pos + 40)
//  {
//   return;
//  }

  //スライドバーの位置を更新
  //（画面状では上の方がy座標が小さい）
  bar_pos += (y-pre_y);

  //もしバーの位置が負になっていたらゼロに戻す
  if(bar_pos<0)
  {
   bar_pos=0;
  }
  
  //もしバーの位置が上限を超えていたら最大値に戻す
  if((bar_pos+16)>dimension.height)
  {
   bar_pos  = dimension.height-16;
  }

  //バーを書き直す
  drawBar();

  //スライドバーの値を更新
  value = dimension.height-bar_pos-16;

  //マウス座標を更新
  pre_x = x;
  pre_y = y;

  //再描画
  repaint();
 }
}

