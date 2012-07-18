// Java 3D Test Applet
// ColorCubeApplet.java
//   Copyright (c) 1999 ENDO Yasuyuki
//                      mailto:yasuyuki@javaopen.org
//                      http://www.javaopen.org/j3dbook/index.html

import java.applet.*;
import java.awt.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.behaviors.picking.PickRotateBehavior;
import com.sun.j3d.utils.behaviors.picking.PickTranslateBehavior;
import com.sun.j3d.utils.behaviors.picking.PickZoomBehavior;

public class ColorCubeApplet extends Applet {
  private Canvas3D canvas = null;

  public ColorCubeApplet() {
    GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
    this.canvas = new Canvas3D(config);
    this.setLayout(new BorderLayout());
    this.add(this.canvas, BorderLayout.CENTER);
    
    SimpleUniverse universe = new SimpleUniverse(canvas);
    universe.getViewingPlatform().setNominalViewingTransform();
    
    universe.addBranchGraph(createSceneGraph());
  }
  
  private BranchGroup createSceneGraph() {
    BranchGroup root = new BranchGroup();
    
    TransformGroup trans = new TransformGroup();
    trans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
    trans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    trans.setCapability(TransformGroup.ENABLE_PICK_REPORTING);

    BoundingSphere bounds = new BoundingSphere(new Point3d(), 100.0);
    
    PickRotateBehavior rotator =
      new PickRotateBehavior(root, this.canvas, bounds);
    root.addChild(rotator);
    
    PickTranslateBehavior translator =
      new PickTranslateBehavior(root, this.canvas, bounds);
    root.addChild(translator);
    
    PickZoomBehavior zoomer =
      new PickZoomBehavior(root, this.canvas, bounds);
    root.addChild(zoomer);
    
    ColorCube colorCube = new ColorCube(0.4f);
    trans.addChild(colorCube);
    
    root.addChild(trans);
    
    return root;
  }
  
  public static void main(String[] args) {
    ColorCubeApplet applet = new ColorCubeApplet();
    Frame frame = new MainFrame(applet, 250, 250);
  }
}
