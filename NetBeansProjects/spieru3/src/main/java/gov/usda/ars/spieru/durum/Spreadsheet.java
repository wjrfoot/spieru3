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

    private ResultsTable labTable;
    private ResultsTable analyzeParticle;
    private int numPixels;

    private String seriesID;
    private String sampleID;
    private String rotationID;
    private String side;
    private XSSFWorkbook workbook = null;

    public static void main(String[] args) {

        Spreadsheet spreadsheet = new Spreadsheet();

        spreadsheet.workbook = spreadsheet.openXLS();

        spreadsheet.writeXLSFile();

        System.exit(0);
    }

    private XSSFWorkbook initNewWorkbook() {
        workbook = new XSSFWorkbook();
        XSSFSheet sheet = getSheet("summary");
        addSummaryTitles(sheet);
        return workbook;
    }

    /**
     * Opens the existing xls file or creates a new one if it doesn't exist
     *
     * @param fileName
     * @return
     */
    public XSSFWorkbook openXLS() {

        Config config = new Config();
        config.loadProperties();
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
            XSSFSheet sheet = getSheet("summary");
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

        String[] items = {"Sample", "Count", "Chalk", "Kernel", "Lo Chalk", "Hi Chalk", "Lo Kernel", "hi Kernel", "DateTime"};
        XSSFRow row = sheet.createRow(1);

        int cellid = 0;

        for (String obj : items) {
            Cell cell = row.createCell(cellid++);
            cell.setCellValue((String) obj);
//                cell.setCellValue((String) obj);
        }

    }

    void addSummaryLine(SummaryOutput summaryOutput) {
        XSSFSheet sheet = getSheet("summary");
        addSummaryLine(sheet, summaryOutput);

    }

    void addSummaryLine(XSSFSheet sheet, SummaryOutput summaryOutput) {

        int lastRowNum = sheet.getLastRowNum();
        XSSFRow row = sheet.createRow(lastRowNum + 1);

        int cellid = 0;

        Cell cell = row.createCell(cellid++);
        cell.setCellValue(summaryOutput.getSampleFileName());
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
        cell = row.createCell(cellid++);
        cell.setCellValue(summaryOutput.getDateStr());
//                cell.setCellValue((String) obj);

    }

    public void addDetailLines(SummaryOutput summaryOutput, List<DetailOutput> detailOutputList) {

        String inputFileName = summaryOutput.getSampleFileName();
        XSSFSheet sheet = getSheet(inputFileName);
        addSummaryLine(sheet, summaryOutput);
        addDetailTitles(sheet);
        addDetailLines(sheet, detailOutputList);
    }

    void addDetailTitles(XSSFSheet sheet) {

        String[] items = {"#", "Kernel", "Chalk"};
        int rowNum = sheet.getLastRowNum() + 2;
        XSSFRow row = sheet.createRow(rowNum);

        int cellid = 0;

        for (String obj : items) {
            Cell cell = row.createCell(cellid++);
            cell.setCellValue((String) obj);
//                cell.setCellValue((String) obj);
        }

    }

    void addDetailLines(XSSFSheet sheet, List<DetailOutput> detailOutputList) {

        int rowNum = sheet.getLastRowNum() + 2;
        int rowID = 1;

        for (DetailOutput detailOutput : detailOutputList) {
            
            int cellid = 0;
            XSSFRow row = sheet.createRow(rowNum++);
            Cell cell = row.createCell(cellid++);
            cell.setCellValue(rowID++);
            cell = row.createCell(cellid++);
            cell.setCellValue(detailOutput.getKernelArea());
            cell = row.createCell(cellid++);
            cell.setCellValue(detailOutput.getChalkArea());

//                cell.setCellValue((String) obj);
        }

    }

//    static void process(String inputDirName) {
//        whoAmI(">>");
//        System.out.println(inputDirName);
//        File dirF = new File(System.getProperty("user.home"), "AppData\\Local\\ARS-SPIERU\\Flour\\images");
//
//        File[] files = dirF.listFiles(new FileFilter() {
//            @Override
//            public boolean accept(File file) {
//                String fileName = file.getAbsolutePath();
//                return fileName.toLowerCase().endsWith("tif")
//                        || fileName.toLowerCase().endsWith("bmp")
//                        || fileName.toLowerCase().endsWith("png");
//            }
//        });
//
//        List<Spreadsheet> results = processFiles(files);
//        System.out.println("length of results " + results.size());
//
//        PrintWriter printWriter = new PrintWriter(System.out);
//        writeSummaryHeader(printWriter);
//        String lastSampleID = "";
//        for (Spreadsheet resultsRecord : results) {
//            resultsRecord.writeSummaryLine(printWriter, lastSampleID);
//            lastSampleID = resultsRecord.getSampleID().trim();
//        }
//        printWriter.close();
//
//        Config config = Config.loadConfig("smut");
//        String outputDirectory = config.getOutputDirectory();
//
//        String outputFileName = new File(System.getProperty("user.home"), "Documents\\SPIERU\\FLOUR.xlsx").getAbsolutePath();
//        System.err.println(outputFileName);
//        // todo make sure name length is less than 32
//        String sheetName = new File(config.getLastInputDirectory()).getName().substring(10) + "-" + config.getHiThreshold();
//        System.err.println(sheetName + " " + sheetName.length());
//        writeXLS(results, outputFileName, sheetName);
////        writeXLS(results, new File(outputDirectory).getAbsolutePath(),
////                new File(config.getLastInputDirectory()).getName() + "-" + config.getHiThreshold());
//    }
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

//    private static int writeHeader(int lineNum, Map< String, Object[]> resultsMap,
//            Map<Integer, Object[]> rMI) {
//
//        Config config = Config.loadConfig("smut");
//
//        // User
//        List<Object> line = new ArrayList<>();
//        line.add("User:");
//        line.add(config.getUserID());
//        String lineID = String.format("%5d", lineNum);
//        String runName = new File(config.getLastInputDirectory()).getName();
//        line.add("Run:");
//        line.add(runName);
//        resultsMap.put(lineID, line.toArray());
//        rMI.put(lineNum, line.toArray());
//        lineNum++;
//
//        // Threshold
//        line = new ArrayList<>();
//        line.add("Low Threshold");
//        line.add(null);
//        line.add(config.getLoThreshold(0.0));
//        line.add("High Threshold");
//        line.add(null);
//        line.add(config.getHiThreshold(0.0));
//        lineID = String.format("%5d", lineNum);
//        resultsMap.put(lineID, line.toArray());
//        rMI.put(lineNum, line.toArray());
//        lineNum++;
//
//        // Size
//        line = new ArrayList<>();
//        line.add("Min Size");
//        line.add(null);
//        line.add(config.getMinSize(0.0));
//        line.add("Max Size");
//        line.add(null);
//        line.add(config.getMaxSize(0.0));
//        lineID = String.format("%5d", lineNum);
//        resultsMap.put(lineID, line.toArray());
//        rMI.put(lineNum, line.toArray());
//        lineNum++;
//
//        // Circularity
//        line = new ArrayList<>();
//        line.add("Min Circularity");
//        line.add(null);
//        line.add(config.getMinCirc(0.0));
//        line.add("Max Circularity");
//        line.add(null);
//        line.add(config.getMaxCirc(0.0));
//        lineID = String.format("%5d", lineNum);
//        resultsMap.put(lineID, line.toArray());
//        rMI.put(lineNum, line.toArray());
//        lineNum++;
//
//        // Blank line
//        line = new ArrayList<>();
//        lineID = String.format("%5d", lineNum);
//        resultsMap.put(lineID, line.toArray());
//        rMI.put(lineNum, line.toArray());
//        lineNum++;
//
//        return lineNum;
//    }
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
