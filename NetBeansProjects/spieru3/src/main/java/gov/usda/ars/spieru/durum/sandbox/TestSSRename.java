/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.usda.ars.spieru.durum.sandbox;

import static gov.usda.ars.spieru.durum.sandbox.TestSSDelete.fileDir;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wjrfo
 */
public class TestSSRename {

    public static void main(String[] args) {
        File file = new File(fileDir, "durum6" + ".xlsx");
        File toFile = new File(fileDir, "durum6a" + ".xlsx");
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
//            file.renameTo(new File(fileDir, "dumum6Temp.xlsx"));
        try {
            Files.move(Paths.get(file.getAbsolutePath()), Paths.get(toFile.getAbsolutePath()), StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            System.err.println("Failed to move file: " + e.getMessage());
        }
        System.out.println("after " + file.exists());

    }
}
