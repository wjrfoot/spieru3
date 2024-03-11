/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.registry;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

/**
 *
 * @author wjrfo
 */
public class RunEpsonScan {

    public static void main(String[] args) throws IOException, InterruptedException, AWTException {

        String testCmd = "\\Windows\\System32\\curl.exe";
        String regCmd = "\\Windows\\System32\\reg.exe";
        String regParam = "\"HKEY_CURRENT_USER\\Software\\EPSON\\EPSON Scan\\ES00A1S\"";
        String scanCmd = "C:\\Windows\\twain_32\\escndv\\escndv.exe";
//        String regParam = "\"HKEY_CURRENT_USER\\Software\\EPSON\\EPSON Scan\\ES00A1\\Configuration\"";
        String query = "query";
        String help = "/?";

//        Process process = new ProcessBuilder(testCmd, "--help").start();
        Process process = new ProcessBuilder(scanCmd).start();
//        Process process = new ProcessBuilder(regCmd, help).start();

        Robot robot = new Robot();
        
        Thread.sleep(5000);
        
        robot.keyPress(KeyEvent.VK_ENTER);
        
        Thread.sleep(10000);
        
        robot.keyPress(KeyEvent.VK_S);

    }

}
