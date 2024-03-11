/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.registry;

import java.io.*;

public class TestAdd {

  private static final String REGQUERY_UTIL = "reg ";

  private static final String QUERY = "query \"HKEY_CURRENT_USER\\Software\\EPSON\\EPSON Scan\\ES00FE\\User Setting\\User Setting 3\"";
  private static final String ADDKEY = "add \"HKEY_CURRENT_USER\\Software\\EPSON\\EPSON Scan\\ES00FE\\FRED\"";
  
  public static String getCurrentUserPersonalFolderPath() {
    try {
//      Process process = Runtime.getRuntime().exec(REGQUERY_UTIL + ADDKEY);
  Process process = new ProcessBuilder(REGQUERY_UTIL, ADDKEY).start();    
  StreamReader reader = new StreamReader(process.getInputStream());

      reader.start();
      process.waitFor();
      reader.join();

      String result = reader.getResult();

      return result;
    }
    catch (Exception e) {
      return null;
    }
  }


  static class StreamReader extends Thread {
    private InputStream is;
    private StringWriter sw;

    StreamReader(InputStream is) {
      this.is = is;
      sw = new StringWriter();
    }

    public void run() {
      try {
        int c;
        while ((c = is.read()) != -1)
          sw.write(c);
        }
        catch (IOException e) { ; }
      }

    String getResult() {
      return sw.toString();
    }
  }

  public static void main(String s[]) {
    System.out.println(getCurrentUserPersonalFolderPath());
  }
}