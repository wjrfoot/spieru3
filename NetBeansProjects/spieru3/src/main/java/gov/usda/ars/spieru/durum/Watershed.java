/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.usda.ars.spieru.durum;

import ij.IJ;
import ij.ImagePlus;
import ij.Prefs;

/**
 *
 * @author wjrfo
 */
public class Watershed {

    public static void Watershed(ImagePlus imp) {
//        IJ.run(imp, "Convert to Mask", "");
//        IJ.run(imp, "Watershed", "");

        Prefs.blackBackground = false;
        IJ.run(imp, "Convert to Mask", "");
        IJ.run(imp, "Distance Transform Watershed", "distances=[Quasi-Euclidean (1,1.41)] output=[32 bits] normalize dynamic=2.5 connectivity=8");
        IJ.setAutoThreshold(imp, "Default");
        IJ.setThreshold(imp, 3, 10000000000000000000000000000.);
    }
}
