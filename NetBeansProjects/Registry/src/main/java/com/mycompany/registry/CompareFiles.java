/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.registry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 *
 * @author wjrfo
 */
public class CompareFiles {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        String dir = System.getProperty("user.dir");

        File file1 = new File(dir, "testRegWithPreview.txt");
        File file2 = new File(dir, "testRegNoPreview.txt");
        BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream(file1)));
        BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream(file2)));

        String line1;
        String line2;
        int lincnt = 1;
        
        System.out.println("starting");

        while (true) {

            line1 = br1.readLine();
            line2 = br2.readLine();
            if (line1 == null) {
                break;
            }
            System.out.print("*");
            if (!line1.equals(line2)) {
                System.out.println("");
                System.out.println(lincnt + " " + line1);
                System.out.println(lincnt + " " + line2);
                System.out.println("");
            }
            lincnt++;

        }

        br1.close();
        br2.close();
    }

}
