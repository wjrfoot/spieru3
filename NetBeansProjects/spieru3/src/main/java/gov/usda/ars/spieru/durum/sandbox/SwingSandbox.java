/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.usda.ars.spieru.durum.sandbox;

import ij.IJ;
import ij.ImagePlus;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SwingSandbox {

    public static void main(String[] args) throws IOException {
        JFrame frame = buildFrame();

//        final BufferedImage image = ImageIO.read(new File("C:\\Users\\wjrfo\\Documents\\images\\durum scans\\db-11-22-23-11", "15-gram-1-a.tif"));

                    File file = new File("C:\\Users\\wjrfo\\Documents\\images\\durum scans\\db-11-22-23-11", "15-gram-1-a.tif");

            System.out.println(file.getAbsolutePath() + " " + file.exists());

            ImagePlus imagePlus = IJ.openImage(file.getAbsolutePath());
//            imagePlus.show();

            final BufferedImage image = imagePlus.getBufferedImage();

        JPanel pane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, null);
            }
        };


        frame.add(pane);
    }


    private static JFrame buildFrame() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(200, 200);
        frame.setVisible(true);
        return frame;
    }


}