/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.registry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author wjrfo
 */
public class SimpleTestReadWriteRegistry {

    public static void main(String[] args) throws IOException, InterruptedException {

        String regCmd = "\\Windows\\System32\\reg.exe";
        String regParam = "HKEY_CURRENT_USER\\Software\\EPSON\\EPSON Scan\\ES00FE\\Home\\Marquee 0";
//        String regParam = "\"HKEY_CURRENT_USER\\Software\\EPSON\\EPSON Scan\\ES00A1\"";
//        String regParam = "\"HKEY_CURRENT_USER\\Software\\EPSON\\EPSON Scan\\ES00A1\\Configuration\"";
        String query = "query";

        ArrayList<ArrayList<String>> rtnVec = new ArrayList<>();

        listReg(rtnVec, regCmd, query, regParam);

        for (ArrayList<String> vec : rtnVec) {
            for (String outStr : vec) {
                System.out.println(">>" + outStr + "<<");
            }
        }
    }

    private static void listReg(ArrayList<ArrayList<String>> rtnVec, String regCmd, String regQuery, String regKey) throws IOException, InterruptedException {

        if (regKey.endsWith("Marquee 0")) {
            System.out.println(regKey);
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
        process.waitFor();

        
//        System.out.printf("Output of running %s is:", Arrays.toString(args));
        while ((line = br.readLine()) != null) {
            vec.add(line);
            System.out.println(">>" + line);
        }
        
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
