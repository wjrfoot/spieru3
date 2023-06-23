/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.usda.ars.spieru.durum;

/**
 *
 * @author wjrfo
 */
public class DetailOutput {

    //<editor-fold defaultstate="collapsed" desc="getters/setters">
    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the kernelArea
     */
    public double getKernelArea() {
        return kernelArea;
    }

    /**
     * @param kernelArea the kernelArea to set
     */
    public void setKernelArea(double kernelArea) {
        this.kernelArea = kernelArea;
    }

    /**
     * @return the chalkArea
     */
    public double getChalkArea() {
        return chalkArea;
    }

    /**
     * @param chalkArea the chalkArea to set
     */
    public void setChalkArea(double chalkArea) {
        this.chalkArea = chalkArea;
    }
//</editor-fold>

    private double kernelArea = 0;
    private double chalkArea = 0;
    private String fileName = "";

    public double getFraction() {
        try {
            return chalkArea / kernelArea;
        } catch (Exception ex) {
            return Double.NaN;
        }
    }

    public int getBucket(Config config) {
        double[] buckets = config.getBucketBounds(0);
        double fraction = getFraction();
        for (int idx = 0; idx < buckets.length; idx++) {
            if (fraction < buckets[idx]) {
                return idx;
            }
        }
        return buckets.length;
    }
    
    public String getBucketLabel(Config config) {
        try {            
            return config.getBucketLabels(0)[getBucket(config)];
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            return "err";
        }
    }
}
