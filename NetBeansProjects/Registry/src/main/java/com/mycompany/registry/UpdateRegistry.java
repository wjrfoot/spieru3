/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.registry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author wjrfo
 */
public class UpdateRegistry {

    private static final String REGISTRY_EXE = "\\Windows\\System32\\reg.exe";

    public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {

        File inputTextFile = new File(System.getProperty("user.dir"), "EpsonScanRegistry");
        inputTextFile = new File(inputTextFile, "RegWithPreview.txt");
        System.out.println(inputTextFile.toString());

        BufferedReader br = new BufferedReader(new FileReader(inputTextFile));

        ArrayList<Entry> entries = new ArrayList();

        String key = null;
        String line;
        while ((line = br.readLine()) != null) {
//            System.out.print(line + "           ");
            if (line.trim().length() == 0) {
                continue;
            }

            if (line.startsWith("HKEY")) {
                key = line;
                continue;
            }
            Entry entry = new Entry(key, line);
            entries.add(entry);
//            System.out.println(entry.toString());
        }
        br.close();

        processEntries(entries);
    }

    private static void processEntries(ArrayList<Entry> entries) throws IOException, InterruptedException {

        for (Entry entry : entries) {
            System.out.println("REG " + entry.getCommand());
//            System.out.println(entry.getCommand());
//            Process process = new ProcessBuilder("REG", entry.getCommand()).start();
//            InputStream is = process.getInputStream();
//            InputStreamReader isr = new InputStreamReader(is);
//            BufferedReader br = new BufferedReader(isr);
//            String line;
//
//            process.waitFor();
////        System.out.printf("Output of running %s is:", Arrays.toString(args));
//            while ((line = br.readLine()) != null) {
//                System.out.println(">>" + line);
//            }
//
//            is.close();
//            br.close();

            System.out.println(entry.getCommand());
//            ProcessBuilder pb = new ProcessBuilder(REGISTRY_EXE, "ADD", emtry.getCommand());
            ProcessBuilder pb = new ProcessBuilder(REGISTRY_EXE, "ADD", entry.getKey(), "/v", entry.getTag(), "/t", entry.getType(), "/d", entry.getValue());
            pb.redirectErrorStream(true);
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("tasklist: " + line);
            }
            process.waitFor();

        }

    }

    private static class Entry {

        //<editor-fold defaultstate="collapsed" desc="setters/getters">
        /**
         * @return the key
         */
        public String getKey() {
            return key;
        }

        /**
         * @param key the key to set
         */
        public void setKey(String key) {
            this.key = key;
        }

        /**
         * @return the tag
         */
        public String getTag() {
            return tag;
        }

        /**
         * @param tag the tag to set
         */
        public void setTag(String tag) {
            this.tag = tag;
        }

        /**
         * @return the type
         */
        public String getType() {
            return type;
        }

        /**
         * @param type the type to set
         */
        public void setType(String type) {
            this.type = type;
        }

        /**
         * @return the value
         */
        public String getValue() {
            return value;
        }

        /**
         * @param value the value to set
         */
        public void setValue(String value) {
            this.value = value;
        }
//</editor-fold>

        private String key = "";
        private String tag = "";
        private String type = "";
        private String value = "";

        public Entry(String key, String line) {
            this.key = key;

            if (!line.contains("REG")) {
//                System.out.println("");
                return;
            }

            int typeStart = line.indexOf("REG");
            tag = line.substring(0, typeStart);
//            System.out.println(tag);

            int typeEnd = line.substring(typeStart).indexOf(" ");
            type = line.substring(typeStart, typeStart + typeEnd);

            value = line.substring(typeStart + typeEnd).trim();

        }

        public String getCommand() {

            StringBuilder commandLine = new StringBuilder();
//            commandLine.append("ADD ");
//            commandLine.append(" ");
            commandLine.append("\"").append(getKey().trim()).append("\" ");
            commandLine.append("/v \"").append(getTag().trim()).append("\" ");
            commandLine.append("/t ").append(getType().trim()).append(" ");
            if (getType().equals("REG_SZ")) {
                if (getValue().length() > 0) {
                    commandLine.append("/d \"").append(getValue()).append("\" ");
                }
            } else {
                commandLine.append("/d ").append(getValue()).append(" ");

            }
//            commandLine.append("/f");
            return commandLine.toString();
        }

        @Override
        public String toString() {
            return ">" + "\"" + getTag().trim() + "\"" + " " + getType() + " " + getValue() + "<" + getValue().length();
        }
    }

}
