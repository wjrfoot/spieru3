/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.usda.ars.spieru.durum;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.SwingWorker;

public class AnalyzeScanWorker extends SwingWorker<Integer, Integer> {

    public AnalyzeScanWorker(DurumFrame durumFrame) {
        this.durumFrame = durumFrame;
    }

    DurumFrame durumFrame;

    ProcessMonitor progressMonitor = new ProcessMonitor(null, false);

    @Override
    protected Integer doInBackground() throws Exception {
        Config config = new Config();

        config.loadProperties();

        progressMonitor.setFileMax(ImageRecord.getImageRecords().size());
        progressMonitor.setVisible(true);
        // Do a time-consuming task.
        int cnt = 1;
        publish(-cnt);
        for (ImageRecord imageRecord : ImageRecord.getImageRecords()) {
            AnalyzeScan analyze = new AnalyzeScan(imageRecord, config, this);
            analyze.analyze();
            cnt++;
            publish(-cnt);
        }
        return null;
    }

    
    
    @Override
    protected void done() {

        durumFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

        durumFrame.displayResultsTables(ImageRecord.getImageRecords());
        progressMonitor.dispose();

    }

    // kludgy way to do this. < 0 is for image, > 0 for subimage
    @Override
    protected void process(List<Integer> numbers) {
        int num = numbers.get(numbers.size() - 1);
        if (num < 0) {
        System.out.println("file idx " + -num);
            progressMonitor.getJBPFile().setValue(-num);
        } else {
            progressMonitor.getJPBKernel().setValue(num);
        }
    }

    public void doPublish(int val) {
        publish(val);
    }

}
