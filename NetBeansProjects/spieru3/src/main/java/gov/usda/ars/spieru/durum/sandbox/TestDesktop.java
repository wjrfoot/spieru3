/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.usda.ars.spieru.durum.sandbox;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author wjrfo
 */
public class TestDesktop {

    public static void main(String[] args) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop desktop = Desktop.getDesktop();
                File myFile = new File("C:\\Users\\wjrfo\\Documents\\USDA-ARS-SPIERU\\durum2.xlsx");
                desktop.open(myFile);
            } catch (IOException ex) {
            }
            System.out.println("error");
        }
    }
}
