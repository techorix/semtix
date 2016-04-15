/*
 * Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
 *        Semesterticketbüro der Humboldt-Universität Berlin
 *
 * Copyright (c) 2015 Michael Mertins (MichaelMertins@gmail.com)
 * 2011-2014 Jürgen Schmelzle (j.schmelzle@web.de)
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.semtix.shared.print;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProgressBarDemo {
    public static void main(String[] args) {
        final Timer timer;
        final JProgressBar progressBar = new JProgressBar();
        final JButton button = new JButton("Start");
        final JFrame f = new JFrame();
        f.setLayout(new FlowLayout());
        f.add(progressBar);
        f.add(button);

        ActionListener updateProBar = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                int val = progressBar.getValue();
                if (val >= 100) {
                    //  timer.stop();
                    button.setText("End");
                    return;
                }
                progressBar.setValue(++val);
            }
        };
        timer = new Timer(50, updateProBar);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (timer.isRunning() && button.getText().equals("Stop")) {
                    timer.stop();
                    button.setText("Start");
                } else if (button.getText().equals("Start")) {
                    timer.start();
                    button.setText("Stop");
                } else {
                    f.dispose();
                }
            }
        });
        f.pack();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
