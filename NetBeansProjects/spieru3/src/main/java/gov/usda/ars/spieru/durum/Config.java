/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.usda.ars.spieru.durum;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @TODO this class should use reflection to do the loads and save to the
 * properties file
 */
/**
 *
 * @author wjrfo
 */
public class Config {

    //<editor-fold defaultstate="collapsed" desc="getters/setters">
    
    /**
     * @return the lastImageDirectory
     */
    public String getLastImageDirectory() {
        return lastImageDirectory;
    }

    /**
     * @param lastImageDirectory the lastImageDirectory to set
     */
    public void setLastImageDirectory(String lastImageDirectory) {
        this.lastImageDirectory = lastImageDirectory;
    }

    /**
     * @return the minSizeFindParticles
     */
    public String getMinSizeFindParticles() {
        return minSizeFindParticles;
    }

    public double getMinSizeFindParticles(int arg) {
        return Double.parseDouble(minSizeFindParticles);
    }

    /**
     * @param minSizeFindParticles the minSizeFindParticles to set
     */
    public void setMinSizeFindParticles(String minSizeFindParticles) {
        this.minSizeFindParticles = minSizeFindParticles;
    }

    /**
     * @return the maxSizeFindParticles
     */
    public String getMaxSizeFindParticles() {
        return maxSizeFindParticles;
    }

    public double getMaxSizeFindParticles(int arg) {
        return Double.parseDouble(maxSizeFindParticles);
    }

    /**
     * @param maxSizeFindParticles the maxSizeFindParticles to set
     */
    public void setMaxSizeFindParticles(String maxSizeFindParticles) {
        this.maxSizeFindParticles = maxSizeFindParticles;
    }

    /**
     * @return the minCircFindParticles
     */
    public String getMinCircFindParticles() {
        return minCircFindParticles;
    }

    public double getMinCircFindParticles(int arg) {
        return Double.parseDouble(minCircFindParticles);
    }

    /**
     * @param minCircFindParticles the minCircFindParticles to set
     */
    public void setMinCircFindParticles(String minCircFindParticles) {
        this.minCircFindParticles = minCircFindParticles;
    }

    /**
     * @return the maxCircFindParticles
     */
    public String getMaxCircFindParticles() {
        return maxCircFindParticles;
    }

    public double getMaxCircFindParticles(int arg) {
        return Double.parseDouble(maxCircFindParticles);
    }

    /**
     * @param maxCircFindParticles the maxCircFindParticles to set
     */
    public void setMaxCircFindParticles(String maxCircFindParticles) {
        this.maxCircFindParticles = maxCircFindParticles;
    }

    /**
     * @return the lowThresholdKernel
     */
    public String getLowThresholdKernel() {
        return lowThresholdKernel;
    }

    public int getLowThresholdKernel(int arg) {
        return Integer.parseInt(lowThresholdKernel);
    }

    /**
     * @param lowThresholdKernel the lowThresholdKernel to set
     */
    public void setLowThresholdKernel(String lowThresholdKernel) {
        this.lowThresholdKernel = lowThresholdKernel;
    }

    /**
     * @return the hiThresholdKernel
     */
    public String getHiThresholdKernel() {
        return hiThresholdKernel;
    }

    public int getHiThresholdKernel(int arg) {
        return Integer.parseInt(hiThresholdKernel);
    }

    /**
     * @param hiThresholdKernel the hiThresholdKernel to set
     */
    public void setHiThresholdKernel(String hiThresholdKernel) {
        this.hiThresholdKernel = hiThresholdKernel;
    }

    /**
     * @return the minSizeKernel
     */
    public String getMinSizeKernel() {
        return minSizeKernel;
    }

    public double getMinSizeKernel(int arg) {
        return Integer.parseInt(minSizeKernel);
    }

    /**
     * @param minSizeKernel the minSizeKernel to set
     */
    public void setMinSizeKernel(String minSizeKernel) {
        this.minSizeKernel = minSizeKernel;
    }

    /**
     * @return the maxSizeKernel
     */
    public String getMaxSizeKernel() {
        return maxSizeKernel;
    }

    public double getMaxSizeKernel(int arg) {
        return Double.parseDouble(maxSizeKernel);
    }

    /**
     * @param maxSizeKernel the maxSizeKernel to set
     */
    public void setMaxSizeKernel(String maxSizeKernel) {
        this.maxSizeKernel = maxSizeKernel;
    }

    /**
     * @return the minCircKernel
     */
    public String getMinCircKernel() {
        return minCircKernel;
    }

    public double getMinCircKernel(int arg) {
        return Double.parseDouble(minCircKernel);
    }

    /**
     * @param minCircKernel the minCircKernel to set
     */
    public void setMinCircKernel(String minCircKernel) {
        this.minCircKernel = minCircKernel;
    }

    /**
     * @return the maxCircKernel
     */
    public String getMaxCircKernel() {
        return maxCircKernel;
    }

    public double getMaxCircKernel(int arg) {
        return Double.parseDouble(maxCircKernel);
    }

    /**
     * @param maxCircKernel the maxCircKernel to set
     */
    public void setMaxCircKernel(String maxCircKernel) {
        this.maxCircKernel = maxCircKernel;
    }

    /**
     * @return the lowThresholdChalk
     */
    public String getLowThresholdChalk() {
        return lowThresholdChalk;
    }

    public int getLowThresholdChalk(int arg) {
        return Integer.parseInt(lowThresholdChalk);
    }

    /**
     * @param lowThresholdChalk the lowThresholdChalk to set
     */
    public void setLowThresholdChalk(String lowThresholdChalk) {
        this.lowThresholdChalk = lowThresholdChalk;
    }

    /**
     * @return the hiThresholdChalk
     */
    public String getHiThresholdChalk() {
        return hiThresholdChalk;
    }

    public int getHiThresholdChalk(int arg) {
        return Integer.parseInt(hiThresholdChalk);
    }

    /**
     * @param hiThresholdChalk the hiThresholdChalk to set
     */
    public void setHiThresholdChalk(String hiThresholdChalk) {
        this.hiThresholdChalk = hiThresholdChalk;
    }

    /**
     * @return the minSizeChalk
     */
    public String getMinSizeChalk() {
        return minSizeChalk;
    }

    public double getMinSizeChalk(int arg) {
        return Double.parseDouble(minSizeChalk);
    }

    /**
     * @param minSizeChalk the minSizeChalk to set
     */
    public void setMinSizeChalk(String minSizeChalk) {
        this.minSizeChalk = minSizeChalk;
    }

    /**
     * @return the maxSizeChalk
     */
    public String getMaxSizeChalk() {
        return maxSizeChalk;
    }

    public double getMaxSizeChalk(int arg) {
        return Integer.parseInt(maxSizeChalk);
    }

    /**
     * @param maxSizeChalk the maxSizeChalk to set
     */
    public void setMaxSizeChalk(String maxSizeChalk) {
        this.maxSizeChalk = maxSizeChalk;
    }

    /**
     * @return the minCircChalk
     */
    public String getMinCircChalk() {
        return minCircChalk;
    }

    public double getMinCircChalk(int arg) {
        return Double.parseDouble(minCircChalk);
    }

    /**
     * @param minCircChalk the minCircChalk to set
     */
    public void setMinCircChalk(String minCircChalk) {
        this.minCircChalk = minCircChalk;
    }

    /**
     * @return the maxCircChalk
     */
    public String getMaxCircChalk() {
        return maxCircChalk;
    }

    public double getMaxCircChalk(int arg) {
        return Double.parseDouble(maxCircChalk);
    }

    /**
     * @param maxCircChalk the maxCircChalk to set
     */
    public void setMaxCircChalk(String maxCircChalk) {
        this.maxCircChalk = maxCircChalk;
    }
//</editor-fold>

    private Properties properties = new Properties();
    private String propertiesFileName = "durum.xml";

    private String FindParticlesMinSize = "FindParticlesMinSize";
    private String FindParticlesMaxSize = "FindParticlesMaxSize";
    private String FindParticlesMinCirc = "FindParticlesMinCirc";
    private String FindParticlesMaxCirc = "FindParticlesMaxCirc";
    
    private String ChalkThresholdLow = "ChalkThresholdLow";
    private String ChalkThresholdHigh = "ChalkThresholdHigh";
    private String ChalkMinSize = "ChalkMinSize";
    private String ChalkMaxSize = "ChalkMaxSize";
    private String ChalkMinCirc = "ChalkMinCirc";
    private String ChalkMaxCirc = "ChalkMaxCirc";
    
    private String KernelThresholdLow = "KernelThresholdLow";
    private String KernelThresholdHigh = "KernelThresholdHigh";
    private String KernelMinSize = "KernelMinSize";
    private String KernelMaxSize = "KernelMaxSize";
    private String KernelMinCirc = "KernelMinCirc";
    private String KernelMaxCirc = "KernelMaxCirc";
    
    private String LastImageDirectory = "LastInputDirectory";

    public static void main(String[] args) {  // quick and dirty instead of using junit
        String dirS = System.getProperty("user.home");
        System.out.println(dirS);
        File localDir = new File(System.getProperty("user.home"), "AppData\\Local\\ARS-SPIERU");
        System.out.println(localDir.getPath() + " " + localDir.exists());
        localDir.mkdir();
        System.out.println(localDir.getPath() + " " + localDir.exists());
        Config config = new Config();
        config.saveProperties();

    }

//    private int lowThreshold1 = 80;      // pass 1 analyze particles threshold 
//    private int hiThreshold1 = 255;
//    private int lowThreshold2 = 185;     // pass 2 analyze particles threshold 
//    private int hiThreshold2 = 255;
    private String minSizeFindParticles = "1000";
    private String maxSizeFindParticles = "10000";
    private String minCircFindParticles = "0.2";
    private String maxCircFindParticles = "1.0";

    private String lowThresholdKernel = "80";
    private String hiThresholdKernel = "255";
    private String minSizeKernel = "1100";
    private String maxSizeKernel = "12000";
    private String minCircKernel = "0.21";
    private String maxCircKernel = "0.99";

    private String lowThresholdChalk = "185";
    private String hiThresholdChalk = "254";
    private String minSizeChalk = "1200";
    private String maxSizeChalk = "13000";
    private String minCircChalk = "0.22";
    private String maxCircChalk = "0.98";
    
    private String lastImageDirectory = System.getProperty("user.home") + "\\Documents";
    
    public void loadProperties() {
        File localDir = new File(System.getProperty("user.home"), "AppData\\Local\\ARS-SPIERU");
        if (localDir.exists()) {
            File localFile = new File(localDir, propertiesFileName);
            if (localFile.exists()) {
                try {
                    InputStream is = new FileInputStream(localFile);
                    properties.loadFromXML(is);
                    
                    setMinCircFindParticles((String) properties.get(FindParticlesMinCirc));
                    setMaxCircFindParticles((String) properties.get(FindParticlesMaxCirc));
                    setMinSizeFindParticles((String) properties.get(FindParticlesMinSize));
                    setMaxSizeFindParticles((String) properties.get(FindParticlesMaxSize));
                    
                    setLowThresholdKernel((String) properties.get(KernelThresholdLow));
                    setHiThresholdKernel((String) properties.get(KernelThresholdHigh));
                    setMinCircKernel((String) properties.get(KernelMinCirc));
                    setMaxCircKernel((String) properties.get(KernelMaxCirc));
                    setMinSizeKernel((String) properties.get(KernelMinSize));
                    setMaxSizeKernel((String) properties.get(KernelMaxSize));
           
                    setLowThresholdChalk((String) properties.get(ChalkThresholdLow));
                    setHiThresholdChalk((String) properties.get(ChalkThresholdHigh));
                    setMinCircChalk((String) properties.get(ChalkMinCirc));
                    setMaxCircChalk((String) properties.get(ChalkMaxCirc));
                    setMinSizeChalk((String) properties.get(ChalkMinSize));
                    setMaxSizeChalk((String) properties.get(ChalkMaxSize));
                    
                    setLastImageDirectory((String) properties.getProperty(LastImageDirectory));

                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }

    public void saveProperties() {
        
        properties.setProperty(FindParticlesMinSize, getMinSizeFindParticles());
        properties.setProperty(FindParticlesMaxSize, getMaxSizeFindParticles());
        properties.setProperty(FindParticlesMinCirc, getMinCircFindParticles());
        properties.setProperty(FindParticlesMaxCirc, getMaxCircFindParticles());
        
        properties.setProperty(KernelThresholdLow, getLowThresholdKernel());
        properties.setProperty(KernelThresholdHigh, getHiThresholdKernel());
        properties.setProperty(KernelMinSize, getMinSizeKernel());
        properties.setProperty(KernelMaxSize, getMaxSizeKernel());
        properties.setProperty(KernelMinCirc, getMinCircKernel());
        properties.setProperty(KernelMaxCirc, getMaxCircKernel());
        
        properties.setProperty(ChalkThresholdLow, getLowThresholdChalk());
        properties.setProperty(ChalkThresholdHigh, getHiThresholdChalk());
        properties.setProperty(ChalkMinSize, getMinSizeChalk());
        properties.setProperty(ChalkMaxSize, getMaxSizeChalk());
        properties.setProperty(ChalkMinCirc, getMinCircChalk());
        properties.setProperty(ChalkMaxCirc, getMaxCircChalk());
        
        properties.setProperty(LastImageDirectory, getLastImageDirectory());
        
        File localDir = new File(System.getProperty("user.home"), "AppData\\Local\\ARS-SPIERU");
        if (!localDir.exists()) {
            localDir.mkdir();
        }
        File localFile = new File(localDir, propertiesFileName);
        OutputStream os;
        try {
            os = new FileOutputStream(localFile);
            properties.storeToXML(os, "hi ho");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}
