/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.registry;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

public class LoadJavaReg {

    private static final String BaseKey = "Software\\EPSON\\EPSON Scan\\ES00FE";
    private static final String TestKey = "SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion";
    private static final String TestName = "ProductName";

    private static final String[] types = {"REG_??", "REG_DWORD", "REG_SZ", "REG_DWORD", "REG_BINARY"}; 
    
    private static PrintWriter pw;
    
    public static void main(String[] args) throws FileNotFoundException {
        // Read a string
//        String productName = Advapi32Util.registryGetStringValue(
//            WinReg.HKEY_LOCAL_MACHINE, "SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion", "ProductName");
//        System.out.printf("Product Name: %s\n", productName);

//        System.out.println(getString(TestKey, TestName));
//        System.out.println(getString(BaseKey, "Location"));
//       putString(BaseKey, "FRED", "test2");
//        listKeys(BaseKey, 0);

        File inputTextFile = new File(System.getProperty("user.dir"), "EpsonScanRegistry");
//        outputTextFile = new File(outputTextFile, "RegWithNoPreview.txt");
//        outputTextFile = new File(outputTextFile, "RegWithPreview.txt");
        inputTextFile = new File(inputTextFile, "WithPreview3.txt");
        System.out.println(inputTextFile.toString());
        pw = new PrintWriter(inputTextFile);
        



        saveValues(BaseKey);
        saveKeys(BaseKey, 0);

        System.out.println("");

        for (KeyRec keyRec : keyRecs) {
            if (keyRec.getType() == 0) {
                System.out.println("zero type " + keyRec.getValue().getClass());
            }
            pw.println(keyRec);
        }
        
        pw.close();
//        Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER, BaseKey, "fred", "hiho");

//        TreeMap<String, Object> map = Advapi32Util.registryGetValues(WinReg.HKEY_CURRENT_USER, BaseKey);
//        Set<String> values = map.keySet();
//        for (String value : values) {
//            Object rtnStr = Advapi32Util.registryGetValue(WinReg.HKEY_CURRENT_USER, BaseKey, value);
////            String rtnStr = Advapi32Util.registryGetStringValue(WinReg.HKEY_CURRENT_USER, BaseKey, value);
//            System.out.println(">> " + value + " " + rtnStr + " " + rtnStr.getClass());
//        }
//        // Read an int (& 0xFFFFFFFFL for large unsigned int)
//        int timeout = Advapi32Util.registryGetIntValue(
//                WinReg.HKEY_LOCAL_MACHINE, "SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion\\Windows", "ShutdownWarningDialogTimeout");
//        System.out.printf("Shutdown Warning Dialog Timeout: %d (%d as unsigned long)\n", timeout, timeout & 0xFFFFFFFFL);
//
//        // Create a key and write a string
//        Advapi32Util.registryCreateKey(WinReg.HKEY_CURRENT_USER, "SOFTWARE\\StackOverflow");
//        Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER, "SOFTWARE\\StackOverflow", "url", "http://stackoverflow.com/a/6287763/277307");
//
//        // Delete a key
//        Advapi32Util.registryDeleteKey(WinReg.HKEY_CURRENT_USER, "SOFTWARE\\StackOverflow");
    }

    private static String getString(String key, String name) {
        String productName = Advapi32Util.registryGetStringValue(
                WinReg.HKEY_CURRENT_USER, key, name);
        return productName;
    }

    private static void putString(String key, String name, String value) {
        Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER, key, name, value);
    }

    private static void getKeys(String key) {
        System.out.println("> " + key);
        String keys[] = Advapi32Util.registryGetKeys(WinReg.HKEY_CURRENT_USER, key);
        for (String keyi : keys) {
            System.out.println(keyi);
            getKeys(key + "\\" + keyi);
        }
    }

    private static void listKeys(String key, int ident) {
        for (int ix = 0; ix < ident; ix++) {
            System.out.print("  ");
        }
        System.out.println(key);
        String keys[] = Advapi32Util.registryGetKeys(WinReg.HKEY_CURRENT_USER, key);
        ident++;
        for (String keyi : keys) {
            if (keyi.trim().equals("FFmt")) {
                System.out.println("at FFmt");
            }
            for (int ix = 0; ix < ident; ix++) {
                System.out.print("  ");
            }
            System.out.println(keyi);
            listKeys(key + "\\" + keyi, ident);
        }
    }

    static ArrayList<KeyRec> keyRecs = new ArrayList<>();

    private static void saveKeys(String key, int ident) {
        for (int ix = 0; ix < ident; ix++) {
            System.out.print("  ");
        }
        System.out.println(key);
        String keys[] = Advapi32Util.registryGetKeys(WinReg.HKEY_CURRENT_USER, key);
        ident++;
        for (String keyi : keys) {
            for (int ix = 0; ix < ident; ix++) {
                System.out.print("  ");
            }
            pw.println(keyi);
            String newKey = key + "\\" + keyi;
            saveValues(newKey);
            saveKeys(newKey, ident);
        }
    }

    static void saveValues(String key) {
        TreeMap<String, Object> map = Advapi32Util.registryGetValues(WinReg.HKEY_CURRENT_USER, key);
        Set<String> values = map.keySet();
        for (String value : values) {
//                Object rtnObj = Advapi32Util.registryGetValue(WinReg.HKEY_CURRENT_USER, keyi, value);
            Object rtnObj = Advapi32Util.registryGetValue(WinReg.HKEY_CURRENT_USER, key, value);
//            String rtnStr = Advapi32Util.registryGetStringValue(WinReg.HKEY_CURRENT_USER, BaseKey, value);
            KeyRec keyRec = new KeyRec(key, value, rtnObj);
            keyRecs.add(keyRec);
                      
        }

    }

    private static class KeyRec {

        //<editor-fold defaultstate="collapsed" desc="comment">
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
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return the value
         */
        public Object getValue() {
            return value;
        }

        /**
         * @param value the value to set
         */
        public void setValue(Object value) {
            this.value = value;
        }

        /**
         * @return the type
         */
        public int getType() {
            return type;
        }

        /**
         * @param type the type to set
         */
        public void setType(int type) {
            this.type = type;
        }
//</editor-fold>

        private String key;
        private String name;
        private Object value;
        private int type = 0;

        public KeyRec(String key, String name, Object value) {
            this.key = key;
            this.name = name;
            this.value = value;

            if (value.getClass() == Integer.class) {
                type = 1;
            } else if (value.getClass() == String.class) {
                type = 2;
            } else if (value.getClass() == Long.class) {
                type = 3;
            } else if (value instanceof byte[]) {
                type = 4;
            }
        }

        public String toString() {
            String valStr = value.toString();
            if (type == 4) {
                valStr = "";
                byte[] byt = (byte[]) value;
                for (int idx = 0; idx < byt.length; idx++) {
                    valStr += byt[idx] + " ";
                }
            }
            return key + " <> " + name + " <> " + valStr + " <> " + types[type];
        }
    }

}
