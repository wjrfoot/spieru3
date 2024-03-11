/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.registry;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author wjrfo
 */
public class ListEpsonV800Registry {

    static PrintWriter pw = null;
    
    public static void main(String[] args) throws IOException, InterruptedException {

        String REGISTRY_EXE = "\\Windows\\System32\\reg.exe";
        String BASE_KEY = "HKEY_CURRENT_USER\\Software\\EPSON\\EPSON Scan\\ES00FE";
//        String BASE_KEY = "\"HKEY_CURRENT_USER\\Software\\EPSON\\EPSON Scan\\ES00A1\"";
//        String BASE_KEY = "\"HKEY_CURRENT_USER\\Software\\EPSON\\EPSON Scan\\ES00A1\\Configuration\"";
        String QUERY = "query";

        File outputTextFile = new File(System.getProperty("user.dir"), "EpsonScanRegistry");
//        outputTextFile = new File(outputTextFile, "RegWithNoPreview.txt");
//        outputTextFile = new File(outputTextFile, "RegWithPreview.txt");
        outputTextFile = new File(outputTextFile, "RegWithPreviewRestored.txt");
        System.out.println(outputTextFile.toString());
        pw = new PrintWriter(outputTextFile);
        
        ArrayList<ArrayList<String>> rtnVec = new ArrayList<>();

        listReg(rtnVec, REGISTRY_EXE, QUERY, BASE_KEY);

        for (ArrayList<String> vec : rtnVec) {
            for (String outStr : vec) {
                pw.println(outStr);
            }
        }
        pw.close();
    }

    private static void listReg(ArrayList<ArrayList<String>> rtnVec, String regCmd, String regQuery, String regKey) throws IOException, InterruptedException {
        if (regKey.endsWith("Marquee 0")) {
            System.out.println("debug");
            return;
        }
        ArrayList<String> vec = new ArrayList<>();
        rtnVec.add(vec);
        System.out.println("processing " + regKey + "|");
//        Process process = new ProcessBuilder(testCmd, "--help").start();
        Process process = new ProcessBuilder(regCmd, regQuery, "\"" + regKey + "\"").start();
//        Process process = new ProcessBuilder(regCmd, help).start();
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;

        if (regKey.endsWith("Marquee 0")) {
            System.out.println("debug");
        }
        
//        System.out.printf("Output of running %s is:", Arrays.toString(args));
        while ((line = br.readLine()) != null) {
            vec.add(line);
            System.out.println(">>" + line);
        }

        process.waitFor();

        for (String str : vec) {
            if (str.trim().equals(regKey)) {
                continue;
            }
//            System.out.println(str);
            if (str.startsWith("HKEY")) {
                listReg(rtnVec, regCmd, regQuery, str);

            }
        }

    }

}
