/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.usda.ars.spieru.durum.sandbox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wjrfo
 */
public class TestFileOpen {

    public static void main(String[] args) throws IOException {
//        try {
//            File file = new File("C:\\Users\\wjrfo\\Documents\\USDA-ARS-SPIERU", "durum2.xls");
//            FileOutputStream fos = new FileOutputStream(file);
//            System.out.println("file not open");
//            fos.close();
//            // -> file was closed
//        } catch (IOException e) {
//            System.out.println("file open");
//            // -> file still open
//        }

        File file = new File("C:\\Users\\wjrfo\\Documents\\USDA-ARS-SPIERU", "durum2.xls");
        FileChannel channel = null;
        try {
            channel = new RandomAccessFile(file, "rw").getChannel();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TestFileOpen.class.getName()).log(Level.SEVERE, null, ex);
        }
// Get an exclusive lock on the whole file
        FileLock lock = channel.lock();
        try {
            lock = channel.tryLock();
            // Ok. You get the lock
            System.out.println("file not open");
        } catch (OverlappingFileLockException e) {
            // File is open by someone else
            System.out.println("file open");
        } catch (IOException ex) {
            Logger.getLogger(TestFileOpen.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            lock.release();
        }

    }
}
