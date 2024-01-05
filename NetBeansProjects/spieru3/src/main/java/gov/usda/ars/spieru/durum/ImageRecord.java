/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.usda.ars.spieru.durum;

import ij.ImagePlus;
import ij.measure.ResultsTable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author wjrfo
 */
public class ImageRecord {

    /**
     * @return the roiImage
     */
    public ImagePlus getRoiImage() {
        return roiImage;
    }

    /**
     * @param roiImage the roiImage to set
     */
    public void setRoiImage(ImagePlus roiImage) {
        this.roiImage = roiImage;
    }

    /**
     * @return the summaryOutput
     */
    public SummaryOutput getSummaryOutput() {
        return summaryOutput;
    }

    /**
     * @param summaryOutput the summaryOutput to set
     */
    public void setSummaryOutput(SummaryOutput summaryOutput) {
        this.summaryOutput = summaryOutput;
    }

    private final static List<ImageRecord> imageRecords = new ArrayList<>();

    private File file;
    private ImagePlus image;
    private ImagePlus roiImage;
    private int[] bucketCounts;
    private ResultsTable kernelResultsTable;
    private ResultsTable chalkResultsTable;
    private final List<ImagePlus> subImages = new ArrayList<>();
    private List<DetailOutput> detailOutputList = new ArrayList<>();
    private SummaryOutput summaryOutput;
    private static String[] bucketTitles;
    

    public ImageRecord() {
        imageRecords.add(this);

    }

    public ImageRecord(File file) {
        this.file = file;
        imageRecords.add(this);
    }

    public static void clearAll() {
        getImageRecords().clear();
    }
    
    /**
     * @param image the image to set
     */
    public void setImage(ImagePlus image) {
        this.image = image;
    }
    
    public void addSubImage(ImagePlus subimage) {
        getSubImages().add(subimage);
    }

    //<editor-fold defaultstate="collapsed" desc="getters/setters">
    /**
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * @return the image
     */
    public ImagePlus getImage() {
        return image;
    }

    /**
     * @return the imageRecords
     */
    public static List<ImageRecord> getImageRecords() {
        return imageRecords;
    }

    /**
     * @return the kernelResultsTable
     */
    public ResultsTable getKernelResultsTable() {
        return kernelResultsTable;
    }

    /**
     * @param kernelResultsTable the kernelResultsTable to set
     */
    public void setKernelResultsTable(ResultsTable kernelResultsTable) {
        this.kernelResultsTable = kernelResultsTable;
    }

    /**
     * @return the chalkResultsTable
     */
    public ResultsTable getChalkResultsTable() {
        return chalkResultsTable;
    }

    /**
     * @param chalkResultsTable the chalkResultsTable to set
     */
    public void setChalkResultsTable(ResultsTable chalkResultsTable) {
        this.chalkResultsTable = chalkResultsTable;
    }

    /**
     * @return the subImages
     */
    public List<ImagePlus> getSubImages() {
        return subImages;
    }

//</editor-fold>

    /**
     * @return the detailOutputList
     */
    public List<DetailOutput> getDetailOutputList() {
        return detailOutputList;
    }

    /**
     * @param detailOutputList the detailOutputList to set
     */
    public void setDetailOutputList(List<DetailOutput> detailOutputList) {
        this.detailOutputList = detailOutputList;
    }

    /**
     * @return the bucketTitles
     */
    public static String[] getBucketTitles() {
        return bucketTitles;
    }

    /**
     * @param aBucketTitles the bucketTitles to set
     */
    public static void setBucketTitles(String[] aBucketTitles) {
        bucketTitles = aBucketTitles;
    }

}
