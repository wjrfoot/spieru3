/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.usda.ars.spieru.durum.sandbox;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wjrfo
 */
public class TestSSDelete {

    static String fileDir = "C:\\Users\\wjrfo\\Documents\\USDA-ARS-SPIERU";

    public static void main(String[] args) {
        File file = new File(fileDir, "durum6" + ".xlsx");
        Path path = Paths.get(file.getAbsolutePath());
        System.out.println("before " + file.exists());
            try {
                Desktop desktop = Desktop.getDesktop();
                desktop.open(file);
                try {
                    Thread.sleep(1000l);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TestSSDelete.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
                System.out.println("error 2");
            }
        try {
            Files.delete(path);
        } catch (IOException e) {
            System.err.println("Failed to delete file: " + e.getMessage());
        }
        System.out.println("after " + file.exists());

        if (Desktop.isDesktopSupported()) {
//            try {
//                Desktop desktop = Desktop.getDesktop();
//                File myFile = new File(fileDir, "durum16" + ".xlsx");
////                desktop.open(myFile);
//                try {
//                    Thread.sleep(1000l);
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(TestSSDelete.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                try {
//                    myFile.delete();
//                    System.out.println("file still exists " + myFile.exists());
//                } catch (Exception ex) {
//                    System.out.println("delete error");
//                }
//
//            } catch (IOException ex) {
//                System.out.println("error 2");
//            }
        }

    }
}
