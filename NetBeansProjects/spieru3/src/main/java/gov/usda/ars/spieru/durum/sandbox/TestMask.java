/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.usda.ars.spieru.durum.sandbox;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;

/**
 *
 * @author wjrfo
 */
public class TestMask {

    public static void main(String[] args) {

        ImageProcessor mask;
        ImageProcessor target;
        ImagePlus maskImage;
        ImagePlus targetImage;

//        targetImage = IJ.openImage("C:\\Users\\wjrfo\\Documents\\images\\durum scans\\wjr durum 2 toching\\mixed\\v850-durum-S33-shake-018.tif");
//ImagePlus        imp = IJ.openImage("C:\\Users\\wjrfo\\Documents\\images\\durum scans\\wjr durum 2 toching\\mixed\\v850-durum-S33-shake-018.tif");
//
//IJ.run(imp, "Duplicate...", " ");
//IJ.run(imp, "8-bit", "");
//IJ.setAutoThreshold(imp, "Default dark no-reset");
//IJ.setRawThreshold(imp, 164, 255);
//IJ.run(imp, "Convert to Mask", "");
//imp.show();
//        targetImage.duplicate().show();
//        maskImage = targetImage.duplicate();
//        maskImage.getProcessor().threshold(1, 255-170);
//        maskImage.show();
////maskImage.getProcessor().setThreshold(0, 255-170);
//        IJ.run(maskImage, "Convert to Mask", "");
//// should be a binary image here
//        mask = maskImage.getProcessor();
//        target = targetImage.getProcessor(); // should be 32-bit (float)
//        target.setValue(Float.NaN);
//        mask.invert();
//                
//        target.fill(mask);
//
//        targetImage.updateAndDraw();
//        targetImage.show();

            ImagePlus imp = IJ.openImage("C:\\Users\\wjrfo\\Documents\\images\\durum scans\\wjr durum 2 toching\\mixed\\v850-durum-S33-shake-018.tif");
 IJ.run(imp, "8-bit", "");
IJ.setAutoThreshold(imp, "Default dark no-reset");
IJ.setRawThreshold(imp, 170, 255);
          imp.show();

    }

}
