/*
 *
 *  * Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
 *  *        Semesterticketbüro der Humboldt-Universität Berlin
 *  *
 *  * Copyright (c) 2015 Michael Mertins (MichaelMertins@gmail.com)
 *  * 2011-2014 Jürgen Schmelzle (j.schmelzle@web.de)
 *  *
 *  *     This program is free software: you can redistribute it and/or modify
 *  *     it under the terms of the GNU Affero General Public License as
 *  *     published by the Free Software Foundation, either version 3 of the
 *  *     License, or (at your option) any later version.
 *  *
 *  *     This program is distributed in the hope that it will be useful,
 *  *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  *     GNU Affero General Public License for more details.
 *  *
 *  *     You should have received a copy of the GNU Affero General Public License
 *  *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */






package org.semtix.gui;

import org.semtix.config.Settings;
import org.semtix.gui.filter.ActionFilterAntraege;
import org.semtix.gui.info.DialogHilfe;
import org.semtix.gui.person.emailsuche.ActionSucheEmail;
import org.semtix.gui.person.personensuche.ActionSuchePerson;
import org.semtix.gui.tabs.ButtonTabComponent;
import org.semtix.gui.tabs.TabView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.HashMap;


/**
 * Klasse mit Hauptframe, Menü, Toolbar und Statuspanel.
 *
 */
public class MainView {
	
	private MainControl mainControl;
	private JFrame mainFrame;
	
	private Menue menue;
	private Toolbar toolbar;
	private StatusPanel statusPanel;
	
	private MainPanel mainPanel;
	
	/**
	 * Erzeugt ein MainView.
	 * @param mc MainControl
	 */
	public MainView(MainControl mc) {

		this.mainControl = mc;
		
		mainFrame = new JFrame();
		
		// Setzt den Titel der Anwendung im Frame (Konstante ist in Klasse Settings festgelegt)
		mainFrame.setTitle(Settings.APP_NAME);
		
		// Icon für Anwendung
		URL url = getClass().getResource("/images/app_logo_16x16.png");
		mainFrame.setIconImage (Toolkit.getDefaultToolkit().getImage(url));

		// Grösse des Bildschirms abfragen
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

		// Framegrösse 95/100 vom kompletten Bildschirm
		int width = d.width; // * 95 / 100;
		int height = d.height * 95 / 100;

		mainFrame.setSize(width, height);
		
		// Frame auf dem Bildschirm zentrieren
		mainFrame.setLocationRelativeTo(null);
		
		// Programm beendet nicht automatisch beim Schließen des Frames.
		// Aufräumarbeiten (wie z.B. DB-Verbindung schließen) werden noch durchgeführt.
		mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		// WindowListener zum Aufräumen bei Programmende
		mainFrame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				// Anwendung aufräumen und schließen
				mainControl.cleanUpBeforeExit();
			}
		});
		
		// Tastenkombinationen für Anwendung setzen
		setKeyBindings();
		
		// Menüleiste anlegen
		menue = new Menue(mainControl);
		
		// Menüleiste dem Hauptfram ehinzufügen
		mainFrame.setJMenuBar(menue);
		
		// ToolBar erstellen
		toolbar = new Toolbar(mainControl);
		
		// MainPanel erstellen
		mainPanel = new MainPanel();
		
		// StatusPanel erstellen
		statusPanel = new StatusPanel();
		
		// ToolBar, MainPanel und StatusPanel dem Hauptframe hinzufügen
		mainFrame.add(BorderLayout.NORTH, toolbar);
		mainFrame.add(BorderLayout.CENTER, mainPanel);
		mainFrame.add(BorderLayout.SOUTH, statusPanel);
		
		mainFrame.setVisible(true);
		
	}
	
	
	
	/**
	 * Fügt der JTabbedPane im MainPanel ein neues Tab hinzu.
	 * @param tabView TabView
	 * @param buttonTabComponent ButtonTabComponent
	 */
	public void addTab(TabView tabView, ButtonTabComponent buttonTabComponent) {
		
		mainPanel.addTab(tabView, buttonTabComponent);	
	}
	
	/**
	 * Liefert das MainPanel. 
	 * @return MainPanel
	 */
	public MainPanel getMainPanel() {
		return mainPanel;
	}

	

	/**
	 * KeyBindings (Hotkeys für die gesamte Anwendung) setzen.
	 */
	private void setKeyBindings() {


        mainFrame.getRootPane().getInputMap().put(KeyStroke.getKeyStroke("F1"), "actionHilfe");
        mainFrame.getRootPane().getActionMap().put("actionHilfe", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new DialogHilfe();
            }
        });


        // Taste F2: Dialog für Personensuche öffnen
        mainFrame.getRootPane().getInputMap().put(KeyStroke.getKeyStroke("F2"), "actionSuche");
		mainFrame.getRootPane().getActionMap().put("actionSuche", new ActionSuchePerson("Person suchen", mainControl));
		
		// Taste F3: Dialog für Mailadressensuche öffnen
		mainFrame.getRootPane().getInputMap().put(KeyStroke.getKeyStroke("F3"), "actionSucheMail");
		mainFrame.getRootPane().getActionMap().put("actionSucheMail", new ActionSucheEmail(mainControl));

        final HashMap<KeyStroke, Action> actionMap = new HashMap<KeyStroke, Action>();
        KeyStroke key1 = KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK);
        actionMap.put(key1, new ActionFilterAntraege("Anträge anzeigen", mainControl));

        KeyStroke key2 = KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK);
        actionMap.put(key2, new ActionSuchePerson("Person suchen", mainControl));


        KeyStroke key4 = KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.SHIFT_DOWN_MASK + KeyEvent.CTRL_DOWN_MASK);
        actionMap.put(key4, new AbstractAction("Neue Person anlegen") {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainControl.addTab("Neuer", "Antragsteller", "123");
            }
        });

        KeyStroke key5 = KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK);
        actionMap.put(key5, new AbstractAction("Close Tab safely") {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.removeTab();
            }
        });

        KeyboardFocusManager kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        kfm.addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                KeyStroke keyStroke = KeyStroke.getKeyStrokeForEvent(e);
                if (actionMap.containsKey(keyStroke)) {
                    final Action a = actionMap.get(keyStroke);
                    final ActionEvent ae = new ActionEvent(e.getSource(), e.getID(), null);
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            a.actionPerformed(ae);
                        }
                    });
                    return true;
                }
                return false;
            }
        });
    }
	
	

	/**
	 * Aktualisiert die Statuszeile im Hauptframe.
	 */
	public void updateStatusPanel() {
		statusPanel.updatePanel();
	}
	
	
	/**
	 * Aktualisiert RadioButtons im Menü, ToggleButton in der ToolBar sowie Text in der Statuszeile, wenn die aktuell
	 * eingestellte Universität wechselt.
	 */
	public void changeUni() {
		updateStatusPanel();
	}
	
	
	/**
	 * Beendet/schliesst den HauptFrame
	 */
	public void close() {
		mainFrame.dispose();
	}

}
