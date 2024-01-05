/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.usda.ars.spieru.durum.sandbox;

import java.awt.event.*;
import javax.swing.*;
import java.net.Socket;

public class LabelUpdateUsingTimer {

    static String hostnameOrIP = "stackoverflow.com";
    int delay = 5000;
    JLabel label = new JLabel("0000");

    LabelUpdateUsingTimer() {
        label.setFont(label.getFont().deriveFont(120f));

        ActionListener timerListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PingWorker().execute();
            }
        };
        Timer timer = new Timer(delay, timerListener);

        timer.start();
        JOptionPane.showMessageDialog(
                null, label, hostnameOrIP, JOptionPane.INFORMATION_MESSAGE);
        timer.stop();
    }

    class PingWorker extends SwingWorker {

        int time;

        @Override
        protected Object doInBackground() throws Exception {
            time = pingTime();
            return new Integer(time);
        }

        @Override
        protected void done() {
            label.setText("" + time);
        }
    };

    public static int pingTime() {
        Socket socket = null;
        long start = System.currentTimeMillis();
        try {
            socket = new Socket(hostnameOrIP, 80);
        } catch (Exception weTried) {
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception weTried) {}
            }
        }
        long end = System.currentTimeMillis();
        return (int) (end - start);
    }

    public static void main(String[] args) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                new LabelUpdateUsingTimer();
            }
        };
        SwingUtilities.invokeLater(r);
    }
}