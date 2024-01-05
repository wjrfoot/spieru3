package gov.usda.ars.spieru.durum.sandbox;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.ImageWindow;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;

/**
 *
 * @author wjrfo
 */
public class TestMouseListener {

    public static void main(String[] args) {
        new TestMouseListener();
    }

    public TestMouseListener() {

        File file = new File("C:\\Users\\wjrfo\\Documents\\images\\durum scans\\db-11-22-23-11", "15-gram-1-a.tif");

        ImagePlus imagePlus = IJ.openImage(file.getAbsolutePath());
        ImagePlus dupPlus = imagePlus.duplicate();

        ImageWindow.setNextLocation(100, 100);
        imagePlus.show();

        ImageWindow.setNextLocation(900, 100);
        dupPlus.show();

        SetupMouseWheelListerners(imagePlus, dupPlus);
    }
    
    private void SetupMouseWheelListerners(ImagePlus imagePlus, ImagePlus dupPlus) {
        
        imagePlus.getWindow().getCanvas().addMouseWheelListener(new MyMouseWheelListener(dupPlus));

        MouseWheelListener[] mwls = dupPlus.getWindow().getCanvas().getMouseWheelListeners();
        for (MouseWheelListener mwl : mwls) {
            dupPlus.getWindow().getCanvas().removeMouseWheelListener(mwl);
        }
        dupPlus.getWindow().getCanvas().addMouseWheelListener(new MyMouseWheelListener(null));
        
    }

    private class MyMouseWheelListener implements MouseWheelListener {

        ImagePlus dispatchToImp = null;

        public MyMouseWheelListener(ImagePlus imp) {
            dispatchToImp = imp;
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent arg0) {
            if ((arg0.getModifiersEx() & MouseWheelEvent.CTRL_DOWN_MASK) > 0 ) {
                if (arg0.getWheelRotation() < 0) {
                    dispatchToImp.getCanvas().zoomIn(arg0.getX(), arg0.getY());
                } else {
                    dispatchToImp.getCanvas().zoomOut(arg0.getX(), arg0.getY());
//                    dispatchToImp.getCanvas().zoomIn(arg0.getX(), arg0.getY());
                }
            }
        }
    }
}
