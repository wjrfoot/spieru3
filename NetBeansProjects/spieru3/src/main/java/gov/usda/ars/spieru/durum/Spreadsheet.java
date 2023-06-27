/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.usda.ars.spieru.durum;

import ij.measure.ResultsTable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.WorkbookUtil;

/**
 *
 * @author wjrfo
 */
public class Spreadsheet {

    private String fileName = "durum.xlsx";
    private String fileDir = System.getProperty("user.home") + "\\Documents\\USDA-ARS-SPIERU";
    private static final String SummarySheetName = "sample log";
    private static final String DataSummarySheetName = "data summary";

    private ResultsTable labTable;
    private ResultsTable analyzeParticle;
    private int numPixels;

    private String seriesID;
    private String sampleID;
    private String rotationID;
    private String side;
    private XSSFWorkbook workbook = null;

    private Config config = null;

    public static void main(String[] args) {

        Spreadsheet spreadsheet = new Spreadsheet();

        spreadsheet.workbook = spreadsheet.openXLS();

        spreadsheet.writeXLSFile();

        System.exit(0);
    }

    public Spreadsheet() {
        this(new Config());
    }

    public Spreadsheet(Config config) {
        this.config = config;
    }

    private XSSFWorkbook initNewWorkbook() {
        workbook = new XSSFWorkbook();
        XSSFSheet sheet = getSheet(SummarySheetName);
        addSummaryTitles(sheet);
        sheet = getSheet(DataSummarySheetName);
        addDataLogTitles(sheet);
        return workbook;
    }

    /**
     * Opens the existing xls file or creates a new one if it doesn't exist
     *
     * @param fileName
     * @return
     */
    public XSSFWorkbook openXLS() {

//        Config config = new Config();
//        config.loadProperties();
        setFileDir(config.getOutputDirectory());
        setFileName(config.getOutputFileName() + ".xlsx");
        File parent = new File(getFileDir());
        if (!parent.exists()) {
            parent.mkdirs();
        }

        File file = new File(parent, getFileName());

        System.out.println("output file " + file.getAbsolutePath());

        workbook = null;
        if (file.exists() == false) {
            System.out.println("Creating a new workbook '" + file + "'");
            workbook = initNewWorkbook();
        } else {
            System.out.println("Appending to existing workbook '" + file + "'");
            try {
                workbook = new XSSFWorkbook(file);
            } catch (IOException ex) {
                Logger.getLogger(Spreadsheet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidFormatException ex) {
                Logger.getLogger(Spreadsheet.class.getName()).log(Level.SEVERE, null, ex);
            }
            XSSFSheet sheet = getSheet(SummarySheetName);
            InputStream is = null;
            try {

                is = new FileInputStream(file);
                workbook = new XSSFWorkbook(is);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Spreadsheet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Spreadsheet.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    is.close();
                    file.delete();
                } catch (IOException ex) {
                    Logger.getLogger(Spreadsheet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return workbook;
    }

    void addSummaryTitles(XSSFSheet sheet) {

        String[] items = {"Sample", "Date Time  ", "Count", "Chalk", "Kernel", "Lo Chalk", "Hi Chalk", "Lo Kernel", "hi Kernel"};
        XSSFRow row = sheet.createRow(1);

        int cellid = 0;

        for (String obj : items) {
            Cell cell = row.createCell(cellid++);
            cell.setCellValue((String) obj);
//                cell.setCellValue((String) obj);
        }
        String[] boundLabels = config.getBucketLabels(0);
        for (String obj : boundLabels) {
            Cell cell = row.createCell(cellid++);
            cell.setCellValue((String) obj);
//                cell.setCellValue((String) obj);
        }

    }

    void addDataLogTitles(XSSFSheet sheet) {

        String[] items = {"Sample", "Date Time  "};
        XSSFRow row = sheet.createRow(1);

        int cellid = 0;

        for (String obj : items) {
            Cell cell = row.createCell(cellid++);
            cell.setCellValue((String) obj);
//                cell.setCellValue((String) obj);
        }
        String[] boundLabels = config.getBucketLabels(0);
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);

        cellid++;
        Cell cell = null;
        for (String str : boundLabels) {
            cell = row.createCell(cellid++);
            cell.setCellStyle(style);
            cell.setCellValue((String) str);
        }
        cell = row.createCell(cellid++);
        cell.setCellStyle(style);
        cell.setCellValue("Total");

        cellid++;
        for (String str : boundLabels) {
            cell = row.createCell(cellid++);
            cell.setCellStyle(style);
            cell.setCellValue("%" + str);
        }

    }

    void addSampleLogLine(SummaryOutput summaryOutput) {
        XSSFSheet sheet = getSheet(SummarySheetName);
//        sheet.setColumnWidth(1, 50);  // make wide enough for data string
        addSampleLogLine(sheet, summaryOutput);
//        sheet.setColumnWidth(1, 50);  // make wide enough for data string

    }

    void addSampleLogLine(XSSFSheet sheet, SummaryOutput summaryOutput) {

        int lastRowNum = sheet.getLastRowNum();
        XSSFRow row = sheet.createRow(lastRowNum + 1);

        int cellid = 0;

        Cell cell = row.createCell(cellid++);
        cell.setCellValue(summaryOutput.getSampleFileName());
        cell = row.createCell(cellid++);
        cell.setCellValue(summaryOutput.getDateStr());
        cell = row.createCell(cellid++);
        cell.setCellValue(summaryOutput.getCount());
        cell = row.createCell(cellid++);
        cell.setCellValue(summaryOutput.getChalkArea());
        cell = row.createCell(cellid++);
        cell.setCellValue(summaryOutput.getKernelArea());
        cell = row.createCell(cellid++);
        cell.setCellValue(summaryOutput.getChalkLoThreshold());
        cell = row.createCell(cellid++);
        cell.setCellValue(summaryOutput.getChalkHiThreshold());
        cell = row.createCell(cellid++);
        cell.setCellValue(summaryOutput.getKernelLoThreshold());
        cell = row.createCell(cellid++);
        cell.setCellValue(summaryOutput.getKernelHiThreshold());
//                cell.setCellValue((String) obj);
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("0.00"));
        double[] bounds = config.getBucketBounds(0);
        for (double bound : bounds) {
            cell = row.createCell(cellid++);
            cell.setCellStyle(style);
            cell.setCellValue(bound);

        }

    }

    void addDataSummaryLine(SummaryOutput summaryOutput, List<DetailOutput> detailOutputList) {
        XSSFSheet sheet = getSheet(DataSummarySheetName);
//        sheet.setColumnWidth(1, 50);  // make wide enough for data string
        addDataSummaryLine(sheet, summaryOutput, detailOutputList);
//        sheet.setColumnWidth(1, 50);  // make wide enough for data string

    }

    void addDataSummaryLine(XSSFSheet sheet, SummaryOutput summaryOutput, List<DetailOutput> detailOutputList) {

        int lastRowNum = sheet.getLastRowNum();
        XSSFRow row = sheet.createRow(lastRowNum + 1);

        String[] bucketLabels = config.getBucketLabels(0);
        int[] counts = new int[bucketLabels.length];

        for (DetailOutput detailOutput : detailOutputList) {
            counts[detailOutput.getBucket(config)]++;
        }

        int cellid = 0;

        Cell cell = row.createCell(cellid++);
        cell.setCellValue(summaryOutput.getSampleFileName());
        cell = row.createCell(cellid++);
        cell.setCellValue(summaryOutput.getDateStr());
        cell = row.createCell(cellid++);

        int total = 0;
        for (int count : counts) {
            total += count;
        }

        for (int count : counts) {
            cell = row.createCell(cellid++);
            cell.setCellValue(count);
        }
            cell = row.createCell(cellid++);
            cell.setCellValue(total);

        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("0.00"));
        cellid++; // put space between counts and percents
        for (int count : counts) {
            cell = row.createCell(cellid++);
            cell.setCellStyle(style);
            cell.setCellValue(100. * count / total);

        }

    }

    public void addDetailLines(SummaryOutput summaryOutput, List<DetailOutput> detailOutputList) {

        String inputFileName = summaryOutput.getSampleFileName();
        XSSFSheet sheet = getSheet(inputFileName);
//        sheet.setColumnWidth(1, 500);  // make wide enough for data string
        addSampleLogLine(sheet, summaryOutput);
        addBucketSummary(sheet, detailOutputList);
        addDetailTitles(sheet);
        addDetailLines(sheet, detailOutputList);
    }

    void addDetailTitles(XSSFSheet sheet) {

        String[] items = {"#", "Kernel", "Chalk", "%", "Type"};
        int rowNum = sheet.getLastRowNum() + 2;
        XSSFRow row = sheet.createRow(rowNum);

        int cellid = 2;

        for (String obj : items) {
            Cell cell = row.createCell(cellid++);
            cell.setCellValue((String) obj);
//                cell.setCellValue((String) obj);
        }

    }

    void addBucketSummary(XSSFSheet sheet, List<DetailOutput> detailOutputList) {

        String[] bucketLabels = config.getBucketLabels(0);
        int[] counts = new int[bucketLabels.length];

        for (DetailOutput detailOutput : detailOutputList) {
            counts[detailOutput.getBucket(config)]++;
        }

        int total = 0;
        for (int count : counts) {
            total += count;
        }

        int rowNum = sheet.getLastRowNum() + 2;

        XSSFRow rowLabels = sheet.createRow(rowNum++);
        XSSFRow rowValues = sheet.createRow(rowNum++);

        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("###.%"));

        for (int idx = 0; idx < bucketLabels.length; idx++) {
            Cell cell = rowLabels.createCell(idx + 2);
            cell.setCellValue(bucketLabels[idx]);
            cell = rowValues.createCell(idx + 2);
            cell.setCellValue(counts[idx]);
            cell = rowLabels.createCell(idx + 8);
            cell.setCellValue(bucketLabels[idx]);
            cell = rowValues.createCell(idx + 8);
            cell.setCellStyle(style);
            cell.setCellValue(counts[idx] * 1.0 / total);
        }

        Cell cell = rowLabels.createCell(counts.length + 2);
        cell.setCellValue("Total");
        cell = rowValues.createCell(counts.length + 2);
        cell.setCellValue(total);
        cell = rowLabels.createCell(counts.length + 8);
        cell.setCellValue("Total");
        cell = rowValues.createCell(counts.length + 8);
        cell.setCellStyle(style);
        cell.setCellValue(1.0);

    }

    void addDetailLines(XSSFSheet sheet, List<DetailOutput> detailOutputList) {

        int rowNum = sheet.getLastRowNum() + 2;
        int rowID = 1;

        for (DetailOutput detailOutput : detailOutputList) {

            int cellid = 2;
            XSSFRow row = sheet.createRow(rowNum++);
            Cell cell = row.createCell(cellid++);
            cell.setCellValue(rowID++);
            cell = row.createCell(cellid++);
            cell.setCellValue(detailOutput.getKernelArea());
            cell = row.createCell(cellid++);
            cell.setCellValue(detailOutput.getChalkArea());

            CellStyle style = workbook.createCellStyle();
//        style.setAlignment(HorizontalAlignment.CENTER);
//        style.setVerticalAlignment(VerticalAlignment.CENTER);
//        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
//        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setDataFormat(workbook.createDataFormat().getFormat("0.00"));

            cell = row.createCell(cellid++);
            cell.setCellStyle(style);
            cell.setCellValue(detailOutput.getFraction());

            style = workbook.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
//        style.setVerticalAlignment(VerticalAlignment.CENTER);
//        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
//        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            cell = row.createCell(cellid++);
            cell.setCellStyle(style);
            cell.setCellValue(detailOutput.getBucketLabel(config));

//                cell.setCellValue((String) obj);
        }

    }

    public static void writeSummaryHeader(PrintWriter printWriter) {
        printWriter.printf("  Sample   Rot  S ParCnt  PixCnt %%Area  LMean   LStd   AMean  AStd   BMean  BStd\n");
    }

    public void writeSummaryLine(PrintWriter printWriter, String lastSampleID) {
        whoAmI(">>");;
        parseSampleName(getFileName());
        printWriter.printf("%4s %4s %4s %2s ", getSeriesID(), getSampleID(), getRotationID(), getSide());
        double[] ap = null;
        try {
            ap = getAnalyzeParticle().getColumn("Area");
        } catch (IllegalArgumentException iae) {
            ap = new double[1];
            ap[0] = 0.0;
        }
        DescriptiveStatistics stats = new DescriptiveStatistics(ap);
        printWriter.printf("%7d %6.0f %6.3f ", stats.getN(), stats.getSum(),
                stats.getSum() * 100 / getNumPixels());
        double[] labMean = getLabTable().getColumn("Mean");
        double[] labStd = getLabTable().getColumn("StdDev");
        printWriter.printf("%6.2f %6.2f %6.2f %6.2f %6.2f %6.2f ", labMean[1], labStd[1],
                labMean[2], labStd[2], labMean[3], labStd[3]);
        printWriter.printf("\n");
        if (lastSampleID.equals(getSampleID().trim())) {
            printWriter.println();
        }
        whoAmI("<<");;
    }

    private void parseSampleName(String inpFileName) {

        String fileName = new File(inpFileName).getName();
        String[] tokens = fileName.split("\\.|-|_");

        setSeriesID(tokens[0]);
        setSampleID(tokens[1]);
        setRotationID(tokens[2]);
        setSide(tokens[3]);
    }

    private XSSFSheet getSheet(String sheetName) {

        XSSFSheet sheet = null;
        int idx = 0;

        whoAmI(">>");
        sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            sheet = workbook.createSheet(sheetName);
        }
//        while (true) {
//            try {
//                if (idx == 0) {
//                    sheet = workbook.createSheet(sheetName);
//                } else {
//                    sheet = workbook.createSheet(sheetName + "(" + idx + ")");
//
//                }
//                break;
//            } catch (IllegalArgumentException iae) {
//                idx++;
//            }
//        }
//        whoAmI("<<");
        return sheet;

    }

    private void writeXLS(List<Spreadsheet> results, String outputFileName,
            String sheetName) {
//    private static void writeXLS(List<ResultsRecord> results, String outfileFileDirectory,
//            String outputFileName) {

        whoAmI(">>");
        //Create blank workbook
//        XSSFWorkbook workbook = openXLS(outputFileName);
        XSSFWorkbook workbook = null;
//        XSSFWorkbook workbook = new XSSFWorkbook();

        //Create a blank sheet
        XSSFSheet spreadsheet = getSheet(sheetName);
//        XSSFSheet spreadsheet = workbook.getSheet(sheetName);
//        XSSFSheet spreadsheet = workbook.getSheet(new File(outfileFileDirectory).getName());

        //Create row object
        XSSFRow row;

        //This data needs to be written (Object[])
        Map< String, Object[]> resultsMap = new TreeMap< String, Object[]>();
        Map< Integer, Object[]> resultsMapI = new TreeMap<>();

        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setDataFormat(workbook.createDataFormat().getFormat("0.00"));

        Collections.sort(results, new Comparator<Spreadsheet>() {
            @Override
            public int compare(Spreadsheet arg0, Spreadsheet arg1) {
                int comp = arg0.getSampleID().compareTo(arg1.getSampleID());
                if (comp != 0) {
                    return comp;
                }
                comp = arg0.getRotationID().compareTo(arg1.getRotationID());
                if (comp != 0) {
                    return comp;
                }
                return arg0.getSide().compareTo(arg1.getSide());
            }
        });

        int lineNum = 0;

//        lineNum = writeHeader(lineNum, resultsMap, resultsMapI);
        resultsMapI.put(lineNum, new Object[]{
            "Series", "Sample", "Rot", "S", "ParCnt", "PixCnt", "%Area", "LMean", "LStd", "AMean", "AStd", "BMean", "BStd"}
        );
        resultsMap.put(String.format("%5d", lineNum++), new Object[]{
            "Series", "Sample", "Rot", "S", "ParCnt", "PixCnt", "%Area", "LMean", "LStd", "AMean", "AStd", "BMean", "BStd"}
        );

        lineNum++;

        int firstDataLine = lineNum;

        for (Spreadsheet result : results) {
            List<Object> line = new ArrayList<>();
            line.add(result.getSeriesID());
            line.add(result.getSampleID());
            line.add(result.getRotationID());
            line.add(result.getSide());
            double[] ap = null;
            try {
                ap = result.getAnalyzeParticle().getColumn("Area");
            } catch (IllegalArgumentException iae) {
                ap = new double[0];
//                ap[0] = -1.0;
            }
            DescriptiveStatistics stats = new DescriptiveStatistics(ap);
            line.add(new Double(stats.getN()));
            line.add(stats.getSum());
            line.add((stats.getSum() * 100 / result.getNumPixels()));
            double[] labMean = result.getLabTable().getColumn("Mean");
            double[] labStd = result.getLabTable().getColumn("StdDev");
            line.add(labMean[1]);
            line.add(labStd[1]);
            line.add(labMean[2]);
            line.add(labStd[2]);
            line.add(labMean[3]);
            line.add(labStd[3]);
            String lineID = String.format("%5d", lineNum);
            resultsMapI.put(lineNum, line.toArray());
            resultsMap.put(lineID, line.toArray());
            lineNum++;
        }
        //Iterate over data and write to sheet
        Set< String> keyid = resultsMap.keySet();
        int rowid = 0;

        int skipCnt = 0;
        String lastSample = "";

//        for (String key : keyid) {
        for (Integer key : resultsMapI.keySet()) {

//            skipCnt++;
//            if ((skipCnt % 4) == 0) {
//                rowid++;
//            }
//            row = spreadsheet.createRow(rowid++);
//            Object[] objectArr = resultsMap.get(key);
            Object[] objectArr = resultsMapI.get(key);
            if (key > firstDataLine) {
                String sampleID = (String) objectArr[1];
                if (!lastSample.equals(sampleID)) {
                    if (lastSample.length() > 0) {
                        skipCnt++;
                    }
                    lastSample = sampleID;
                }
            }
            row = spreadsheet.createRow(key + skipCnt);

            int cellid = 0;

            for (Object obj : objectArr) {
                Cell cell = row.createCell(cellid++);
                if (obj instanceof Double) {
                    cell.setCellValue((double) obj);
                    cell.setCellStyle(style);
                } else {
                    cell.setCellValue((String) obj);
                }
//                cell.setCellValue((String) obj);
            }
        }
        //Write the workbook in file system
        FileOutputStream out;
        try {
//            String fileName = outfileFileDirectory + System.currentTimeMillis() + ".xlsx";
//            out = new FileOutputStream(new File(fileName));
//            out = new FileOutputStream(new File(outfileFileDirectory, outputFileName + "-" + System.currentTimeMillis() + ".xlsx"));
            out = new FileOutputStream(new File(outputFileName));
//            System.out.println(new File(fileName).getAbsolutePath());
            workbook.write(out);
            out.close();
            System.out.println("Writesheet.xlsx written successfully");

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Spreadsheet.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Spreadsheet.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        whoAmI("<<");

    }

    void writeXLSFile() {

        FileOutputStream out;
        try {
//            String fileName = outfileFileDirectory + System.currentTimeMillis() + ".xlsx";
//            out = new FileOutputStream(new File(fileName));
//            out = new FileOutputStream(new File(outfileFileDirectory, outputFileName + "-" + System.currentTimeMillis() + ".xlsx"));
            out = new FileOutputStream(new File(fileDir, fileName));
//            System.out.println(new File(fileName).getAbsolutePath());
            workbook.write(out);
            out.close();
            System.out.println("Writesheet.xlsx written successfully");

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Spreadsheet.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Spreadsheet.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    /*
     */
    public static void whoAmI(String arg) {
        // getStackTrace() method return 
        // current method name at 0th index 
        String nameofCurrMethod = new Throwable()
                .getStackTrace()[1]
                .getMethodName();

        System.out.println(arg + " Name of current method: "
                + nameofCurrMethod);
    }

    //<editor-fold defaultstate="collapsed" desc="getters/setters">
    /**
     * @return the labTable
     */
    public ResultsTable getLabTable() {
        return labTable;
    }

    /**
     * @param labTable the labTable to set
     */
    public void setLabTable(ResultsTable labTable) {
        this.labTable = labTable;
    }

    /**
     * @return the analyzeParticle
     */
    public ResultsTable getAnalyzeParticle() {
        return analyzeParticle;
    }

    /**
     * @param analyzeParticle the analyzeParticle to set
     */
    public void setAnalyzeParticle(ResultsTable analyzeParticle) {
        this.analyzeParticle = analyzeParticle;
    }

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
     * @return the fileDir
     */
    public String getFileDir() {
        return fileDir;
    }

    /**
     * @param fileDir the fileDir to set
     */
    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }

    /**
     * @return the numPixels
     */
    public int getNumPixels() {
        return numPixels;
    }

    /**
     * @param numPixels the numPixels to set
     */
    public void setNumPixels(int numPixels) {
        this.numPixels = numPixels;
    }

    /**
     * @return the seriesID
     */
    public String getSeriesID() {
        return seriesID;
    }

    /**
     * @param seriesID the seriesID to set
     */
    public void setSeriesID(String seriesID) {
        this.seriesID = seriesID;
    }

    /**
     * @return the sampleID
     */
    public String getSampleID() {
        return sampleID;
    }

    /**
     * @param sampleID the sampleID to set
     */
    public void setSampleID(String sampleID) {
        this.sampleID = sampleID;
    }

    /**
     * @return the rotationID
     */
    public String getRotationID() {
        return rotationID;
    }

    /**
     * @param rotationID the rotationID to set
     */
    public void setRotationID(String rotationID) {
        this.rotationID = rotationID;
    }

    /**
     * @return the side
     */
    public String getSide() {
        return side;
    }

    /**
     * @param side the side to set
     */
    public void setSide(String side) {
        this.side = side;
    }

//</editor-fold>
}
