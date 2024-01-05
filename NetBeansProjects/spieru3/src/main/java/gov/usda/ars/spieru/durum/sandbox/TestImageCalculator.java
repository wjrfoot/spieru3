/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.usda.ars.spieru.durum.sandbox;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.ImageCalculator;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;
import java.io.File;

/**
 *
 * @author wjrfo
 */
public class TestImageCalculator {

    public static void main(String[] args) {
        File file = new File("C:\\Users\\wjrfo\\Documents\\images\\durum scans\\db-11-22-23-11", "15-gram-1-a.tif");

        System.out.println(file.getAbsolutePath() + " " + file.exists());

        ImagePlus imagePlus = IJ.openImage(file.getAbsolutePath());
        imagePlus.show();
        ImagePlus maskImage = imagePlus.duplicate();

        ImageConverter imageConverter = new ImageConverter(maskImage);
        imageConverter.convertToGray8();

//	setAutoThreshold("Default dark");
//        maskImage.getProcessor().setAutoThreshold("Default Red");

//                maskImage.getProcessor().threshold(190, 255);
        maskImage.getProcessor().threshold(170, 255);
//maskImage.getProcessor().setThreshold(0, 255-170);
        IJ.run(maskImage, "Convert to Mask", "");
//        maskImage.getProcessor().invert();
        maskImage.show();;
        ImagePlus resultImage = ImageCalculator.run(imagePlus, maskImage, "min create 32-bit");
        resultImage.setTitle("result");
        resultImage.show();
    }
}
