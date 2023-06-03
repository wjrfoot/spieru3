/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.usda.ars.spieru.durum;

import ij.ImagePlus;
import ij.gui.Roi;
import java.awt.Rectangle;

/**
 *
 * @author wjrfo
 */
public class SubImagePlus {

    private ImagePlus originalIP = null;
    private ImagePlus kernelIP = null;
    private ImagePlus chalkIP = null;
    private Rectangle boundingBox = null;
    private String kernelResults;
    private String chalkResults;
    private Roi roi = null;
    private double kernelArea;
    private double chalkArea;

    public SubImagePlus() {
    }

    public SubImagePlus(ImagePlus originalIP, Roi roi) {
        this.originalIP = originalIP;
        this.roi = roi;
    }
    
    

//<editor-fold defaultstate="collapsed" desc="getters/setters">
    /**
     * @return the kernelIP
     */
    public ImagePlus getKernelIP() {
        return kernelIP;
    }

    /**
     * @param kernelIP the kernelIP to set
     */
    public void setKernelIP(ImagePlus kernelIP) {
        this.kernelIP = kernelIP;
    }

    /**
     * @return the boundingBox
     */
    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    /**
     * @param boundingBox the boundingBox to set
     */
    public void setBoundingBox(Rectangle boundingBox) {
        this.roi = new Roi(boundingBox);
        this.boundingBox = boundingBox;
    }

    /**
     * @return the kernelResults
     */
    public String getKernelResults() {
        return kernelResults;
    }

    /**
     * @param kernelResults the kernelResults to set
     */
    public void setKernelResults(String kernelResults) {
        this.kernelResults = kernelResults;
    }

    /**
     * @return the chalkResults
     */
    public String getChalkResults() {
        return chalkResults;
    }

    /**
     * @param chalkResults the chalkResults to set
     */
    public void setChalkResults(String chalkResults) {
        this.chalkResults = chalkResults;
    }

    /**
     * @return the chalkIP
     */
    public ImagePlus getChalkIP() {
        return chalkIP;
    }

    /**
     * @param chalkIP the chalkIP to set
     */
    public void setChalkIP(ImagePlus chalkIP) {
        this.chalkIP = chalkIP;
    }

    /**
     * @return the originalIP
     */
    public ImagePlus getOriginalIP() {
        return originalIP;
    }

    /**
     * @param originalIP the originalIP to set
     */
    public void setOriginalIP(ImagePlus originalIP) {
        this.originalIP = originalIP;
    }

    /**
     * @return the roi
     */
    public Roi getRoi() {
        return roi;
    }

    /**
     * @param roi the roi to set
     */
    public void setRoi(Roi roi) {
        this.roi = roi;
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
}
