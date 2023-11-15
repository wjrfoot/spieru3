/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.usda.ars.spieru.durum;

    import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.*;

/**
 *
 * @author wjrfo
 */
public class TestWatershedMorphLibJ {

    public static void main(String[] args) {
        
        ImagePlus imp = IJ.openImage("C:\\Users\\wjrfo\\Documents\\images\\durum scans\\wjr durum 2 toching\\mixed\\v850-durum-S33-shake-018.tif");
        IJ.run(imp, "8-bit", "");
//        IJ.setAutoThreshold(imp, "Default dark no-reset");
        imp.getProcessor().threshold(100, 255);
        IJ.run(imp, "Convert to Mask", "");
//        ImagePlus imp = imp.duplicate();
        IJ.run(imp, "Fill Holes", "");
        imp.setTitle("1");
        imp.show();;
        imp = imp.duplicate();
//        IJ.run(imp, "Convert to Mask", "");
//        IJ.run(imp, "Analyze Particles...", "size=500-20000 circularity=0.20-1.00 display composite");
//        IJ.run(imp, "Watershed", "");

        DistanceTransformWatershedMod dtwm = new DistanceTransformWatershedMod();

        dtwm.setup("", imp);
        dtwm.run(imp.getProcessor());

        imp.setTitle("2");
        imp.show();;
        imp = imp.duplicate();
//        IJ.run(imp, "Distance Transform Watershed", "distances=[Chessboard (1,1)] output=[32 bits] normalize dynamic=1 connectivity=4");
        ImageConverter.setDoScaling(true);
        IJ.run(imp, "8-bit", "");
//        IJ.setAutoThreshold(imp, "Default dark no-reset");
        imp.getProcessor().threshold(1, 255);
        imp.setTitle("3");
        imp.show();;
        imp = imp.duplicate();
        imp.getProcessor().invert();
//        imp.getProcessor().erode();
        IJ.run(imp, "Convert to Mask", "");
        IJ.run(imp, "Analyze Particles...", "size=500-20000 circularity=0.20-1.00 display composite");
        imp.setTitle("4");
        imp.show();
    }


}
