package euler;

//******************************************************************************
//スライドバー（縦方向）  “SlideBar2.java
//フィールド“volume”の値が、スライドバーの位置 : "volume" = Position of Slide bar
//スライドバーの最大値は 寸法 - 16 : Max value is (horizontal length - 16)
//
//使用時の手順
//1.設置
//2.initial()クラスを呼ぶ（バーのサイズ決定，ウィンドウ表示後）
//3.設置先でもMouseMoitonListenerを実装し，
//  設置先でも bar.addMouseMotionListener(this)  などのように登録
//******************************************************************************

//==============================================================================
//インポート : Imports
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
 //フィールド : Fields
 //=============================================================================

 //スライドバーの値 : Value of slide bar
 int value=0;

 //マウスの現在座標 : Mouse's coordinate
 int x=0;
 int y=0;

 //マウスの１つ前の座標 : Previous mouse coordinate
 int pre_x=0;
 int pre_y=0;

 //バー自体の位置 : Position of the bar
 int bar_pos=0;

 //オフセット : Offset
 int offset= 0;

 //ダブルバッファ用のImageクラス : Image Class object for Double Buffering
 Image iBuffer;

 //ダブルバッファ用のGraphicsクラス : Glaphics Class object for Double Buffering
 Graphics gBuffer;

 //寸法
 Dimension dimension;


 //=============================================================================
 //コンストラクタ
 //=============================================================================
 public SlideBar2()
 {
  //寸法を取得 : use accessor function to find the size of frame
  dimension = getSize();

  //MouseMotionListenerに登録 : Enable Mouse Event Handler
  addMouseMotionListener(this);  
 }

 //=============================================================================
 //外から呼ぶメソッド。スライドバッファを設置し，サイズを決定後に呼ぶ。
 //=============================================================================
 public void initialize()
 {
  bar_pos = getSize().height-16; // Bar position
  getBuffer();
  drawBar();
 }


 //=============================================================================
 //ダブルバッファリング関係 : Double Buffering
 //=============================================================================
 public void getBuffer() // Buffering
 {
   //一応，寸法取得  : Check size again
   dimension = getSize();

   //描画用メモリ領域確保 : Allocate memory for the buffer
   iBuffer = createImage(dimension.width,dimension.height);

   //iBufferからGraphicsインスタンスを生成 : Create Graphics Class Instance from the allocated buffer
   gBuffer = iBuffer.getGraphics();
 }


 //=============================================================================
 //描画 : Drawing bar
 //=============================================================================
 public void drawBar()
 { 
  //寸法取得 : Obtain size again
  dimension = getSize();

  //gBufferの内容をクリア : clear buffer
  gBuffer.clearRect(0,0,dimension.width,dimension.height);

  //縦の線の色 : Set vertical line color (bar rail)
  gBuffer.setColor(Color.gray); 

  //縦の線を書く: Draw the line
  gBuffer.drawLine(dimension.width/2,0,dimension.width/2,dimension.height);

  //バーの色 : set slide bar color
  gBuffer.setColor(Color.lightGray);

  //バーを書く : Draw the slide bar
  gBuffer.fill3DRect(dimension.width/2-16,bar_pos,32,16,true);
   
  //スライドバーの値を更新 : Update the slide bar's value
  value = dimension.height-bar_pos-16;
 }

 //=============================================================================
 //paint()メソッド : Paint method (paint frame)
 //=============================================================================
 public void paint(Graphics g)
 {
  //iBufferの内容を描画 : Draw the content of iBuffer
  g.drawImage(iBuffer,0,0,this);
 }


 //=============================================================================
 //マウスが動いたときに呼ばれる : Event Handler for Mouse Moved
 //=============================================================================
 public void mouseMoved(MouseEvent event)
 {
  //マウス座標を更新 : Mouse Coordinate updated
  pre_x = event.getX();
  pre_y = event.getY();
 }

 //=============================================================================
 //マウスがドラッグされたときに呼ばれる : Event Handler for Mouse Dragging
 //=============================================================================
 public void mouseDragged(MouseEvent event)
 {
  //一応，寸法取得 : get size yet again
  dimension = getSize();

  //マウス座標を読み込み : Read off the mouse coordinate
  x=event.getX();
  y=event.getY();

  //マウス位置がスライドバー範囲外の場合は何もしない - Do nothing outside of range 
  // ... This code does work so commented. Tomo
//  if(ybar_pos + 40)
//  {
//   return;
//  }

  //スライドバーの位置を更新 : Update slide bar position
  //（画面状では上の方がy座標が小さい） -- Higher the slider bar, higher the value
  bar_pos += (y-pre_y);

  //もしバーの位置が負になっていたらゼロに戻す : Do not accept any negative values
  if(bar_pos<0)
  {
   bar_pos=0;
  }
  
  //もしバーの位置が上限を超えていたら最大値に戻す : Do not accept any values above the upper bound
  if((bar_pos+16)>dimension.height)
  {
   bar_pos  = dimension.height-16;
  }

  //バーを書き直す : Redraw slide bar
  drawBar();

  //スライドバーの値を更新 : Update Slide bar value
  value = dimension.height-bar_pos-16;

  //マウス座標を更新 : Update Mouse coordinate
  pre_x = x;
  pre_y = y;

  //再描画 : Force redraw of the window frame
  repaint();
 }
}

