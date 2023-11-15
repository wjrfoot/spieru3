/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.usda.ars.spieru.test;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageConverter;
import java.io.File;

/**
 *
 * @author wjrfo
 */
public class TestThreshold {

    static String dirName = "C:\\Users\\wjrfo\\Documents\\images\\durham\\clear slotted grid\\v850 11\\4 kernel non-touching 1003x2000";
    static String filName = "img001.tif";

    public static void main(String[] args) {
        ImagePlus imp = IJ.openImage(new File(dirName, filName).getAbsolutePath());
        imp.show();
        imp = imp.duplicate();
        ImageConverter imageConverter = new ImageConverter(imp);
//        imp.getProcessor().setAutoThreshold("Default Dark");
        imp.setTitle("to gray");
        imageConverter.convertToGray8();
        imp.show();

        imp = imp.duplicate();
        imageConverter = new ImageConverter(imp);

        imp.getProcessor().threshold(110, 255);
        

        imp.setTitle("threshold");
        imp.show();
    }

}
