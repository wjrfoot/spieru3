/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.usda.ars.spieru.durum;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.PolygonRoi;
import ij.gui.Roi;
import ij.measure.Measurements;
import ij.measure.ResultsTable;
import ij.plugin.filter.BackgroundSubtracter;
import ij.plugin.filter.EDM;
import ij.plugin.filter.ParticleAnalyzer;
import ij.plugin.frame.RoiManager;
import ij.process.ImageConverter;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.text.DecimalFormat;

/**
 *
 * @author wjrfo
 */
public class AnalyzeScan {

    /**
     * subImagePlusList is the main data object. The object contains a list of
     * SubImagePlus instances. The SubImagePlus object contains an ImagePlus
     * instance holds the image and meta-data of an individual kernel, the
     * bounding box for where the individual kernel is located in the input
     * scan, and the result strings from the kernel and chalk analyze particles
     * runs.
     *
     */
    private List<SubImagePlus> subImagePlusList = new ArrayList<>();
    private int lowTH = 60;
    private int hiTH = 185;
    private String fileName = null;
    private Config config = null;

    private double minSize = 800.;
    private double maxSize = 1000000.;
    private double minCirc = 0;
    private double maxCirc = 1.0;
    private double threshold = 160;

    private double[] buckets = {0.1, 0.2, 0.5, 1.0};

    ResultsTable resultsTable = new ResultsTable();

    ParticleAnalyzer particleAnalyzer = new ParticleAnalyzer(ParticleAnalyzer.SHOW_NONE, Measurements.AREA,
            resultsTable, minSize, maxSize, minCirc, maxCirc);

    public static void main(String[] args) {
        System.out.println("starting analyze");
        File dirF = new File(System.getProperty("user.dir"), "images");
        File imageF = new File(dirF, "img014.tif");
        System.out.println("filename " + dirF.getAbsolutePath() + " " + dirF.exists());
        Config config = new Config();
        config.loadProperties();
        AnalyzeScan analyzeScan = new AnalyzeScan(imageF.getAbsolutePath(), config);
        analyzeScan.analyze();
    }

    /**
     * uses the last created file in picture directory
     */
    public AnalyzeScan() {
        File dirF = new File(System.getProperty("user.dir"), "images");
        dirF = new File(dirF, "img002.tif");
        System.out.println("filename " + dirF.getAbsolutePath() + " " + dirF.exists());
        setFileName(dirF.getAbsolutePath());
        setConfig(new Config());
//        setFileName(FindLastPictureFile.getLastFileName());
    }

    /**
     * uses specified file
     *
     * @param fileName
     */
    public AnalyzeScan(String fileName, Config config) {
        setFileName(fileName);
        setConfig(config);
    }

    /**
     * entry point to analyze analysis
     */
    public void analyze() {

        ImagePlus baseIP = IJ.openImage(getFileName());
        baseIP.getProcessor().flipHorizontal();
        baseIP.setTitle("original image");
        baseIP.show();
        baseIP = baseIP.duplicate();

        Roi[] rois = FindROIs(baseIP);

//        findNearPoints(rois);
//
//        rois = combineRois(rois);
        subImagePlusList = makeSubImagePlusList(baseIP, rois);

        PrintWriter pw = null;

        try {
            File dirF = new File(System.getProperty("user.dir"), "data");
            String fileName = System.currentTimeMillis() + ".txt";
            File outF = new File(dirF, fileName);
            pw = new PrintWriter(outF);

            int cnt = 1;

            for (SubImagePlus sip : subImagePlusList) {

                processKernel(sip, cnt);

                processChalk(sip);

                processResults(sip, pw, cnt++);

            }
            System.out.println("done process");
        } catch (IOException ex) {
            Logger.getLogger(AnalyzeScan.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            pw.close();
        }

        // create bar chart output
        EventQueue.invokeLater(() -> {
            ResultsBarChart ex = new ResultsBarChart(subImagePlusList, buckets);
            ex.setVisible(true);
        });

    }

    /**
     * this should be greatly expanded to analyze the results instead of just
     * printing them to a text file
     *
     * @param sip
     * @param pw
     */
    private void processResults(SubImagePlus sip, PrintWriter pw, int idx) {
        processResults(sip, pw, idx, false);
    }

    private void processResults(SubImagePlus sip, PrintWriter pw, int idx, boolean visibleFlg) {
        if (pw != null) {
            pw.println(sip.getKernelResults().split("\\t")[1] + "  ---  " + sip.getChalkResults().split("\\t")[1]);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            SubImagePlusFrame sipf = new SubImagePlusFrame();
            sipf.setTitle("number " + idx);
            sipf.getChalkJTF().setText(sip.getChalkResults().split("\\t")[1]);
            sipf.getKernelJTF().setText(sip.getKernelResults().split("\\t")[1]);
            ImagePlus kip = sip.getKernelIP();
//            ImagePlus kip = sip.getKernelIP().duplicate();
//            sip.getKernelIP().show();
//            kip.resize(50, 50, "none");
            kip.setTitle("kip");
//            kip.show();
            sipf.getKernelPitJL().setIcon(new ImageIcon(kip.getImage()));
//            sipf.getKernelPitJL().setIcon(new ImageIcon(sip.getKernelIP().getImage()));
            ImagePlus cip = sip.getChalkIP();
//            cip.resize(60, 60, "none");
            sipf.getChalkPictJL().setIcon(new ImageIcon(cip.getImage()));
//            sipf.getChalkPictJL().setIcon(new ImageIcon(sip.getChalkIP().getImage()));
            sipf.getOriginalPitJL().setIcon(new ImageIcon(sip.getOriginalIP().getImage()));
            double kernel = sip.getKernelArea();
            double chalk = sip.getChalkArea();
//            double kernel = Double.parseDouble(sip.getKernelResults().split("\\t")[1]);
//            double chalk = Double.parseDouble(sip.getChalkResults().split("\\t")[1]);
            if (new Double(kernel).equals(chalk)) {
                chalk = 0.0;
            }
            String temp = (kernel == chalk) ? "invalid"
                    : new DecimalFormat("##").format((chalk / kernel) * 100.) + "%"; // rounded to 2 decimal places
            sipf.getPercentJTF().setText(temp);
            sipf.setVisible(visibleFlg);
        });

    }

    private void dumpResultsTable(ResultsTable rs) {
        System.out.println("Results Table " + rs.getCounter());
        System.out.println(rs.getColumnHeadings());
        for (int idx = 0; idx < rs.getCounter(); idx++) {
            System.out.println(rs.getRowAsString(idx));

        }
    }

    /**
     * takes the bounding box for each kernel found in analyze particles and
     * splits each kernel into a separate, fairly small, image object
     *
     * @param bIP
     * @param roiList
     */
    private List<SubImagePlus> makeSubImagePlusList(ImagePlus baseImp, Roi[] rois) {

        int cnt = 1;
        List<SubImagePlus> subImagePlusList = new ArrayList<>();

        for (Roi roi : rois) {
//            System.out.println("roi cnt " + roi.getPolygon().npoints);

            baseImp.setRoi(roi);
            Rectangle boundingBox = roi.getBounds();
            baseImp.cut();
            ImagePlus impRoi = IJ.createImage(roi.getName(), "RGB black", 256, 256, 1);
//            ImagePlus impRoi = IJ.createImage(roi.getName(), "RGB black", boundingBox.height, boundingBox.width, 1);

            impRoi.paste();
//            impRoi.paste(0,0);
//            impRoi.show();
//            IJ.run(impRoi, "Find Edges", "");
            impRoi.setTitle("kernel " + cnt++);
//            impRoi.show();
//            System.out.println("roi " + cnt);
            SubImagePlus sip = new SubImagePlus(impRoi, roi);

            sip.setKernelIP(impRoi);
            writeIP(impRoi);
            subImagePlusList.add(sip);

        }

        return subImagePlusList;
    }

    private Roi[] combineRois(Roi[] rois) {

        List<Roi> rtnRois = new ArrayList<>();

        System.out.println("combineRois");
        for (int idx = 0; idx < rois.length; idx++) {
            if (rois[idx] == null) {
                continue;
            }
            if (idx == 7) {
                System.out.println("bp");
            }
            Rectangle rect1 = rois[idx].getBounds();
            for (int jdx = idx + 1; jdx < rois.length; jdx++) {
                if (rois[jdx] == null) {
                    continue;
                }
                Rectangle rect2 = rois[jdx].getBounds();
                if (rect1.intersects(rect2)) {
                    System.out.println(jdx + " intersects " + idx);
                    Polygon poly1 = rois[idx].getPolygon();
                    Polygon poly2 = rois[jdx].getPolygon();
                    int numNewPoints = poly1.npoints + poly2.npoints;
                    int[] xPoints = new int[numNewPoints];
                    int[] yPoints = new int[numNewPoints];
                    for (int pdx = 0; pdx < poly1.npoints; pdx++) {
                        xPoints[pdx] = poly1.xpoints[pdx];
                        yPoints[pdx] = poly1.ypoints[pdx];
                    }
                    for (int pdx = 0; pdx < poly2.npoints; pdx++) {
                        xPoints[pdx + poly1.npoints] = poly2.xpoints[pdx];
                        yPoints[pdx + poly1.npoints] = poly2.ypoints[pdx];
                    }
                    Roi newRoi = new PolygonRoi(xPoints, yPoints, numNewPoints, Roi.POLYGON);
                    rois[idx] = newRoi;
                    rois[jdx] = null;
                }
            }
        }

        for (int idx = 0; idx < rois.length; idx++) {
            if (rois[idx] != null) {
                rtnRois.add(rois[idx]);
            }
        }

        Roi[] rtnRoisArr = new Roi[rtnRois.size()];
        for (int rdx = 0; rdx < rtnRois.size(); rdx++) {
            rtnRoisArr[rdx] = rtnRois.get(rdx);
        }
        return rtnRoisArr;
    }

    private Roi[] findNearPoints(Roi[] rois) {

        List<Roi> rtnRois = new ArrayList<>();

        System.out.println("findNearPoints");
        for (int idx = 0; idx < rois.length; idx++) {
            if (rois[idx] == null) {
                continue;
            }
            if (idx == 7) {
                System.out.println("bp");
            }
            Rectangle rect1 = rois[idx].getBounds();
            for (int jdx = idx + 1; jdx < rois.length; jdx++) {
                if (rois[jdx] == null) {
                    continue;
                }
                Rectangle rect2 = rois[jdx].getBounds();
                if (rect1.intersects(rect2)) {
                    System.out.println(jdx + " intersects " + idx);
                    Polygon poly1 = rois[idx].getPolygon();
                    Polygon poly2 = rois[jdx].getPolygon();
                    System.out.printf("Intersect %d %d  p1 len %d  p2 len %d\n",
                            idx, jdx, poly1.npoints, poly2.npoints);

                    // find the first and last points
                    int p1start = -1;
                    int p2start = -1;
                    int p1end = 0;
                    int p2end = 0;

                    for (int p1x = 0; p1x < poly1.npoints; p1x++) {

                        for (int p2x = poly2.npoints - 1; p2x >= 0; p2x--) {

                            int deltaX = poly1.xpoints[p1x] - poly2.xpoints[p2x];
                            int deltaY = poly1.ypoints[p1x] - poly2.ypoints[p2x];

                            if (deltaX < 3 && deltaX > -3 && deltaY < 3 && deltaY > -3) {
                                System.out.printf("p1 %4d x %4d y %4d   p2 %4d x %4d y %4d\n",
                                        p1x, poly1.xpoints[p1x], poly1.ypoints[p1x],
                                        p2x, poly2.xpoints[p2x], poly2.ypoints[p2x]);
                                if (p1start == -1) {
                                    p1start = p1x;
                                    p2start = p2x;
                                }
                                p1end = p1x;
                                p2end = p2x;
                            }
                        }
                    }

//                    int numNewPoints = poly1.npoints + poly2.npoints;
//                    int[] xPoints = new int[numNewPoints];
//                    int[] yPoints = new int[numNewPoints];
//                    for (int pdx = 0; pdx < poly1.npoints; pdx++) {
//                        xPoints[pdx] = poly1.xpoints[pdx];
//                        yPoints[pdx] = poly1.ypoints[pdx];
//                    }
//                    for (int pdx = 0; pdx < poly2.npoints; pdx++) {
//                        xPoints[pdx + poly1.npoints] = poly2.xpoints[pdx];
//                        yPoints[pdx + poly1.npoints] = poly2.ypoints[pdx];
//                    }
//                    Roi newRoi = new PolygonRoi(xPoints, yPoints, numNewPoints, Roi.POLYGON);
//                    rois[idx] = newRoi;
//                    rois[jdx] = null;
                }
            }
        }

////        for (int idx = 0; idx < rois.length; idx++) {
////            if (rois[idx] != null) {
////                rtnRois.add(rois[idx]);
////            }
////        }
////
////        Roi[] rtnRoisArr = new Roi[rtnRois.size()];
////        for (int rdx = 0; rdx < rtnRois.size(); rdx++) {
////            rtnRoisArr[rdx] = rtnRois.get(rdx);
////        }
//        return rtnRoisArr;
        return null;
    }

    /**
     *
     * @param filNam
     *
     *
     * Implements the following imagej commands to create an image where there
     * are breaks between the kernels. analyze("8-bit");
     * setAutoThreshold("Default dark no-reset"); setOption("BlackBackground",
     * true); analyze("Convert to Mask"); analyze("Options...", "iterations=1
     * count=1 black do=Close"); analyze("watershed");
     */
    private Roi[] FindROIs(ImagePlus baseImagePlus) {

        baseImagePlus.show();

        ImagePlus imp = baseImagePlus.duplicate();

//        IJ.run(imp, "Gaussian Blur...", "sigma=3");
//        IJ.run(imp, "Convert to Mask", "");
//        imp = imp.resize(842, 1310, "bilinear");
//        IJ.run(imp, "Convert to Mask", "");
//        IJ.run(imp, "Fill Holes", "");
//        IJ.run(imp, "Watershed", "");
//        imp = imp.resize(1684, 2620, "bilinear");
//        IJ.run(imp, "Convert to Mask", "");

        ImageConverter ic = new ImageConverter(imp);
        ic.convertToGray8();
        imp.getProcessor().setAutoThreshold("Default dark no-reset");
//        IJ.run(imp, "8-bit", "");
        ImageConverter imageConverter = new ImageConverter(imp);
        imageConverter.convertToGray8();
//        IJ.run(imp, "Auto Threshold", "method=Default white");
//        imp.getProcessor().setAutoThreshold("Default mask");
        IJ.run(imp, "Convert to Mask", "");
        IJ.run(imp, "Watershed", "iterations=1 count=1 black do=Close");


        EDM edm = new EDM();
        edm.setup("watershed", imp);
        edm.run(imp.getProcessor());

//        IJ.analyze(imp, "Find Edges", "");  // testing
        RoiManager roiManager = new RoiManager(false);
        ResultsTable resultsTable = new ResultsTable();

        System.out.printf(" params for watershed %4.0f %6.0f %3.1f %3.1f\n", config.getMinSizeFindParticles(0), config.getMaxSizeFindParticles(0),
                config.getMinCircFindParticles(0), config.getMaxCircFindParticles(0));
        System.out.printf(" params for watershed %4.0f %6.0f %3.1f %3.1f\n", minSize, maxSize, minCirc, maxCirc);
//      

        ParticleAnalyzer.setRoiManager(roiManager);
        ParticleAnalyzer particleAnalyzer = new ParticleAnalyzer(ParticleAnalyzer.SHOW_NONE, Measurements.AREA + Measurements.CENTROID,
                resultsTable, config.getMinSizeFindParticles(0), config.getMaxSizeFindParticles(0),
                config.getMinCircFindParticles(0), config.getMaxCircFindParticles(0));
//        ParticleAnalyzer particleAnalyzer = new ParticleAnalyzer(ParticleAnalyzer.SHOW_NONE, Measurements.AREA + Measurements.CENTROID,
//                resultsTable, minSize, maxSize, minCirc, maxCirc);

        particleAnalyzer.analyze(imp);

        System.out.println("# of kernels " + resultsTable.getCounter() + " " + roiManager.getRoisAsArray().length);

        imp.show();
        imp.getCanvas().addMouseListener(new MyMouseListner(imp));

        return roiManager.getRoisAsArray();
    }

    /**
     * process full kernel
     *
     * @param sip
     */
    private void processKernel(SubImagePlus sip, int cnt) {

        ImagePlus ip = sip.getKernelIP().duplicate();

        ip.duplicate().show();

        ip.setTitle("process kernel");

//	// analyze particles
//      analyze("Set Measurements...",
//	"area centroid perimeter fit shape redirect=None decimal=2");
//        IJ.run(ip, "Set Measurements...", getConfig().getMeasureParamsBase());
//	// set the threshold
//	analyze("8-bit");
//        IJ.run(ip, "8-bit", "");
        ImageConverter imageConverter = new ImageConverter(ip);
        imageConverter.convertToGray8();

//	setAutoThreshold("Default dark");
        ip.getProcessor().setAutoThreshold("Default Dark");

//	setThreshold(lowTH, 255);
        ip.getProcessor().setThreshold(getConfig().getLowThresholdKernel(hiTH),
                getConfig().getHiThresholdKernel(hiTH), 0);

//	// analyze particles
//	analyze("Analyze Particles...",
//	"size=minSz1-30 circularity=0.1-1.00" + 
//	" show=[Overlay Masks] display");
        // use different size since we are working in pixels instead of mm
//        IJ.run(ip, "Analyze Particles...", getConfig().getAnalyzeKernel());
//        Analyzer analyzer = new Analyzer();
//        analyzer.setup("size=10-30000 circularity=0.1-1.00", ip);
//        analyzer.analyze(ip.getProcessor());
        ResultsTable resultsTable = new ResultsTable();

        ParticleAnalyzer particleAnalyzer = new ParticleAnalyzer(ParticleAnalyzer.SHOW_NONE, Measurements.AREA,
                resultsTable, config.getMinSizeKernel(0), config.getMaxSizeKernel(0),
                config.getMinCircKernel(0), config.getMaxCircKernel(0));
        particleAnalyzer.analyze(ip);

        // save last results, full, table entry into kernel object
        ip.show();
        if (resultsTable.getCounter() == 0) {
            sip.setKernelResults("a\ta\ta");
            sip.setKernelArea(0.0);

        } else {
            String resultsStr = resultsTable.getRowAsString(0);
            System.out.println("kernel " + cnt + " area " + resultsTable.getValue("Area", 0));
//        String resultsStr = ResultsTable.getResultsTable().getRowAsString(ResultsTable.getResultsTable().getCounter() - 1);
            sip.setKernelResults(resultsStr);
            sip.setKernelArea(resultsTable.getValue("Area", 0));
        }
        sip.setKernelIP(ip);
//        ip.show();
    }

    /**
     * processes chalk portion of kernel
     *
     * @param sip
     */
    private void processChalk(SubImagePlus sip) {

        ImagePlus ip = sip.getKernelIP().duplicate();

//	// process the duplicate for chalk
//	analyze("Set Measurements...",
//	"area centroid perimeter fit shape redirect=None decimal=2");
//        IJ.run(ip, "Set Measurements...", getConfig().getMeasureParamsBase());
        // try to smooth image and trim tips
//	analyze("Subtract Background...", "rolling=5 create");
        IJ.run(ip, "Subtract Background...", "rolling=5 create");
//        BackgroundSubtracter bS = new BackgroundSubtracter();
//        bS.setup("rolling=5 create dark", ip);
//        bS.run(ip.getProcessor());

//	// set the threshold for the chalk
//	analyze("8-bit");
//        IJ.run(ip, "8-bit", "");
        ImageConverter imageConverter = new ImageConverter(ip);
        imageConverter.convertToGray8();

//	setAutoThreshold("Default dark");
        ip.getProcessor().setAutoThreshold("Default Dark");

//	setThreshold(lowTH, 255);
        ip.getProcessor().setThreshold(config.getLowThresholdChalk(0),
                config.getHiThresholdChalk(0), 0);

//	analyze("Analyze Particles...",
//	"size=minSz1-30 circularity=0.1-1.00" + 
//	" show=[Overlay Masks] display");
        // changed size because measurements are pixels instead of mm
        ResultsTable resultsTable = new ResultsTable();

        ParticleAnalyzer particleAnalyzer = new ParticleAnalyzer(ParticleAnalyzer.SHOW_NONE, Measurements.AREA,
                resultsTable, config.getMinSizeChalk(0), config.getMaxSizeChalk(0),
                config.getMinCircChalk(0), config.getMaxCircChalk(0));
        particleAnalyzer.analyze(ip);

//        IJ.run(ip, "Analyze Particles...", "size=10-30000 circularity=0.1-1.00");
//                ResultsTable resultsTable = new ResultsTable();
//
//        ParticleAnalyzer particleAnalyzer = new ParticleAnalyzer(ParticleAnalyzer.SHOW_NONE, Measurements.AREA,
//                resultsTable, minSize, maxSize, minCirc, maxCirc);
//        resultsTable.reset();
//        particleAnalyzer.analyze(ip);
        // save last results, chalk< table entry into kernel object
//        String resultsStr = ResultsTable.getResultsTable().getRowAsString(ResultsTable.getResultsTable().getCounter() - 1);
        if (resultsTable.getCounter() <= 0) {
            sip.setChalkResults("a\ta\ta");
            sip.setChalkArea(0.0);
        } else {
//        String resultsStr = resultsTable.getRowAsString(0);
            String resultsStr = resultsTable.getRowAsString(resultsTable.getCounter() - 1);
            sip.setChalkResults(resultsStr);
            double area = resultsTable.getValue("Area", resultsTable.getCounter() - 1);
            sip.setChalkArea(area);
//        sip.setChalkArea(resultsTable.getValue("Area", 0));
        }
        sip.setChalkIP(ip);
//        ip.show();
    }

    /**
     * writes the image of one kernel into file for later viewing
     *
     * @param ip
     */
    private void writeIP(ImagePlus ip) {
//             System.out.println(new File(System.getProperty("user.dir"), "data"));
        File dirF = new File(System.getProperty("user.dir"), "data\\roiTest");
        if (!dirF.exists()) {
            dirF.mkdirs();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
//        fileName = "fred" + ".jpg";
        File outF = new File(dirF, fileName);
//        System.out.println(outF.getAbsolutePath());

        try {
            ImageIO.write(ip.getBufferedImage(), "jpg", outF);
        } catch (IOException ex) {
            Logger.getLogger(AnalyzeScan.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private class MyMouseListner implements MouseListener {

        private int canvasHeight;
        private int canvasWidth;
        private int imageHeight;
        private int imageWidth;

        public MyMouseListner(ImagePlus ip) {
            canvasHeight = ip.getCanvas().getHeight();
            canvasWidth = ip.getCanvas().getWidth();
            imageHeight = ip.getHeight();
            imageWidth = ip.getWidth();
        }

        @Override
        public void mouseClicked(MouseEvent arg0) {

            int x = arg0.getX();
            int y = arg0.getY();
//            System.out.println("mouseClicked " + arg0.getY() + " " + arg0.getX());
//            System.out.println("canvas H " + canvasHeight + "  W  " + canvasWidth);
//            System.out.println("image H " + imageHeight + "  W  " + imageWidth);
            int x1 = (x * imageWidth) / canvasWidth;
            int y1 = (y * imageHeight) / canvasHeight;
            System.out.println("scaled H " + y1 + "  W  " + x1);
            Point point = new Point(x1, y1);

            int cnt = 1;
            boolean foundFlg = false;
            for (SubImagePlus sip : subImagePlusList) {
                Roi roi = sip.getRoi();
//                Rectangle rect = sip.getBoundingBox();
                System.out.println(roi.toString());
//                processResults(sip, null);
                if (roi.contains(x1, y1)) {
//                if (roi.contains(point)) {
                    processResults(sip, null, cnt, true);
                    foundFlg = true;
                }
                cnt++;
            }
            System.out.println((foundFlg) ? "found" : "not found");
        }

        @Override
        public void mousePressed(MouseEvent arg0) {
            mouseClicked(arg0);
            System.out.println("mousePressed");
        }

        @Override
        public void mouseReleased(MouseEvent arg0) {
            System.out.println("mouseReleased");
        }

        @Override
        public void mouseEntered(MouseEvent arg0) {
            System.out.println("mouseEntered");
        }

        @Override
        public void mouseExited(MouseEvent arg0) {
            System.out.println("mouseExited");
        }

    }

    //<editor-fold defaultstate="collapsed" desc="getters/setters">
    /**
     * @return the lowTH
     */
    public int getLowTH() {
        return lowTH;
    }

    /**
     * @param lowTH the lowTH to set
     */
    public void setLowTH(int lowTH) {
        this.lowTH = lowTH;
    }

    /**
     * @return the hiTH
     */
    public int getHiTH() {
        return hiTH;
    }

    /**
     * @param hiTH the hiTH to set
     */
    public void setHiTH(int hiTH) {
        this.hiTH = hiTH;
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
    public final void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @param config the config to set
     */
    public void setConfig(Config config) {
        this.config = config;
    }

    /**
     * @return the config
     */
    public Config getConfig() {
        return config;
    }

//</editor-fold>
}
