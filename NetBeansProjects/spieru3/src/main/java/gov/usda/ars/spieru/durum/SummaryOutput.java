/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.usda.ars.spieru.durum;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author wjrfo
 */
public class SummaryOutput {

    /**
     * @return the buckets
     */
    public int[] getBuckets() {
        return buckets;
    }

    /**
     * @param buckets the buckets to set
     */
    public void setBuckets(int[] buckets) {
        this.buckets = buckets;
    }

    //<editor-fold defaultstate="collapsed" desc="getters/setters">
    
    /**
     * @return the chalkLoThreshold
     */
    public double getChalkLoThreshold() {
        return chalkLoThreshold;
    }

    /**
     * @param chalkLoThreshold the chalkLoThreshold to set
     */
    public void setChalkLoThreshold(double chalkLoThreshold) {
        this.chalkLoThreshold = chalkLoThreshold;
    }

    /**
     * @return the chalkHiThreshold
     */
    public double getChalkHiThreshold() {
        return chalkHiThreshold;
    }

    /**
     * @param chalkHiThreshold the chalkHiThreshold to set
     */
    public void setChalkHiThreshold(double chalkHiThreshold) {
        this.chalkHiThreshold = chalkHiThreshold;
    }

    /**
     * @return the kernelLoThreshold
     */
    public double getKernelLoThreshold() {
        return kernelLoThreshold;
    }

    /**
     * @param kernelLoThreshold the kernelLoThreshold to set
     */
    public void setKernelLoThreshold(double kernelLoThreshold) {
        this.kernelLoThreshold = kernelLoThreshold;
    }

    /**
     * @return the kernelHiThreshold
     */
    public double getKernelHiThreshold() {
        return kernelHiThreshold;
    }

    /**
     * @param kernelHiThreshold the kernelHiThreshold to set
     */
    public void setKernelHiThreshold(double kernelHiThreshold) {
        this.kernelHiThreshold = kernelHiThreshold;
    }

    /**
     * @return the dateStr
     */
    public String getDateStr() {
        return dateStr;
    }

    /**
     * @param dateStr the dateStr to set
     */
    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    /**
     * @return the sampleFileName
     */
    public String getSampleFileName() {
        return sampleFileName;
    }

    /**
     * @param sampleFileName the sampleFileName to set
     */
    public void setSampleFileName(String sampleFileName) {
        this.sampleFileName = sampleFileName;
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(int count) {
        this.count = count;
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
//</editor-fold>
    
    private double chalkArea = 0;
    private double kernelArea = 0;
    private int count = 0;
    private String sampleFileName = "";
    private String dateStr;
    private double chalkLoThreshold = 0.0;
    private double chalkHiThreshold = 0.0;
    private double kernelLoThreshold = 0.0;
    private double kernelHiThreshold = 0.0;
    private int[] buckets;
    
    
    public SummaryOutput() {
        
          Date date = Calendar.getInstance().getTime();

        // Display a date in day, month, year format
        DateFormat formatter = new SimpleDateFormat("dd/MM/yy-hh:mm");
        String today = formatter.format(date);
        setDateStr(today);

    }
    
}
