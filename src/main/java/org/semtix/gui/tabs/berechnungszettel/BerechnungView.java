/*

  Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
         Semesterticketbüro der Humboldt-Universität Berlin

  Copyright (c) 2015 Michael Mertins (MichaelMertins@gmail.com)
  2011-2014 Jürgen Schmelzle (j.schmelzle@web.de)

    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */

package org.semtix.gui.tabs.berechnungszettel;

import org.semtix.config.Settings;
import org.semtix.config.UserConf;
import org.semtix.gui.tabs.berechnungszettel.otherpanels.ZettelPanel1;
import org.semtix.gui.tabs.berechnungszettel.otherpanels.ZettelPanel2;
import org.semtix.shared.daten.enums.Vorgangsart;
import org.semtix.shared.elements.SForm;
import org.semtix.shared.print.PrintableBerechnungszettel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Die BerechnungView ist enthält die beiden BerechnungsZettel 1 und 2 sowie
 * die Steuerungbuttons zum Drucken, Schließen und Punkteübernahmeq
 */
public class BerechnungView {

	private BerechnungControl berechnungControl;

	private JScrollPane berechnungPanel;

	private JScrollPane mainScrollPane;

	private ZettelPanel1 zettel1;
	private ZettelPanel2 zettel2;


	/**
	 * erstellt eine neue BerechnungView
	 *
	 * @param bControl BerechnungControl
	 */
	public BerechnungView(BerechnungControl bControl) {

		this.berechnungControl = bControl;

		// ***** Panel für beide Berechnungszettel erstellen *****

		SForm mainPanel = new SForm();

		zettel1 = new ZettelPanel1(berechnungControl);

		zettel2 = new ZettelPanel2(berechnungControl);


		// ZettelPanel hinzufügen
		mainPanel.add(zettel1, 0, 0, 1, 1, 0.0, 0.0, 0, 18, new Insets(5, 5, 5, 5));
		mainPanel.add(zettel2, 1, 0, 1, 1, 1.0, 1.0, 0, 18, new Insets(5, 5, 5, 5));


		// Liste mit druckbaren Panel zusammenstellen (nur Seite 1)
		final List<Component> printList1 = new ArrayList<Component>();
		printList1.add(zettel1);

		// Liste mit druckbaren Panel zusammenstellen (Seite 1+2)
		final List<Component> printList2 = new ArrayList<Component>();
		printList2.add(zettel1);
		printList2.add(zettel2);

		// ***** ButtonPanel erstellen *****

		// Seite 1 + 2 drucken (Punkte übernehmen, kein Vorgang anlegen)
		JButton button1 = new JButton("Seite 1 + 2");
		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "Beide Seiten werden gedruckt. (" + printList2.size() + ")", "Drucken", JOptionPane.INFORMATION_MESSAGE);

				PrintableBerechnungszettel.printComponents(printList2);
				berechnungControl.updateAntragModel(null);
			}
		});

		// nur Seite 1 drucken (Punkte übernehmen, kein Vorgang anlegen)
		JButton button2 = new JButton("nur Seite 1");
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "Erste Seite wird gedruckt. (" + printList1.size() + ")", "Drucken", JOptionPane.INFORMATION_MESSAGE);

				PrintableBerechnungszettel.printComponents(printList1);
				berechnungControl.updateAntragModel(null);
			}
		});

		// zurück zum Antrag, ohne Punkte zu übernehmen
		JButton button3 = new JButton("Zurück");
		button3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				berechnungControl.setCards("Antrag");
			}
		});

		// zurück zum Antrag, ohne Punkte zu übernehmen
		JButton button4 = new JButton("Speichern und Zurück");
		button4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				berechnungControl.updateAntragModel(null);
			}
		});


		// Erstrechnung: Seite 1 + 2 drucken, zurück zum Antrag und Punkte aus Berechnungszettel übernehmen
		// Vorgang "Erstrechnung" im Bearbeitungsprotokoll setzen
		JButton buttonErstrechnen = new JButton("Erstrechnen");
		buttonErstrechnen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(null, "Wirklich erstrechnen? \nDie Berechnung wird gezeichnet und gedruckt. \nDer Antrag wird erstgerechnet und entschieden.")) {
					getZettel1().getHeadPanel().setZeichnung1(UserConf.CURRENT_USER.getKuerzel());
					PrintableBerechnungszettel.printComponents(printList2);
					berechnungControl.updateAntragModel(Vorgangsart.ERSTRECHNUNG);
				}
			}
		});

		// Zweitrechnung: Seite 1 + 2 drucken, zurück zum Antrag und Punkte aus Berechnungszettel übernehmen
		// Vorgang "Zweitrechnung" im Bearbeitungsprotokoll setzen
		JButton buttonZweitrechnen = new JButton("Zweitrechnen");
		buttonZweitrechnen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(null, "Wirklich zweitrechnen? \nDie Berechnung wird gezeichnet und gedruckt. \nDer Antrag wird zweitgerechnet und entschieden.")) {
					getZettel1().getHeadPanel().setZeichnung2(UserConf.CURRENT_USER.getKuerzel());
					PrintableBerechnungszettel.printComponents(printList2);
					berechnungControl.updateAntragModel(Vorgangsart.ZWEITRECHNUNG);
				}
			}
		});


		// zurück zum Antrag, ohne Punkte zu übernehmen
		JButton button5 = new JButton("Löschen");
		button5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int rueckgabewert = JOptionPane.showConfirmDialog(null, "Wirklich löschen? \nHärten bleiben erhalten.");

				if (rueckgabewert == JOptionPane.OK_OPTION)
					berechnungControl.deleteBRZ();
			}
		});

		SForm buttonPanel = new SForm();
		buttonPanel.add(new JLabel("Drucken:"), 0, 0, 1, 1, 0.0, 0.0, 0, 17, new Insets(5, 2, 5, 2));
		buttonPanel.add(button1, 1, 0, 1, 1, 0.0, 0.0, 0, 18, new Insets(5, 2, 5, 2));
		buttonPanel.add(button2, 2, 0, 1, 1, 0.0, 0.0, 0, 18, new Insets(5, 2, 5, 2));
		buttonPanel.add(button4, 3, 0, 1, 1, 0.0, 0.0, 0, 18, new Insets(5, 2, 5, 2));
		buttonPanel.add(button3, 4, 0, 1, 1, 0.0, 0.0, 0, 18, new Insets(5, 40, 5, 40));
		buttonPanel.add(buttonErstrechnen, 5, 0, 1, 1, 0.0, 0.0, 0, 18, new Insets(5, 2, 5, 2));
		buttonPanel.add(buttonZweitrechnen, 6, 0, 1, 1, 0.0, 0.0, 0, 18, new Insets(5, 2, 5, 2));
		buttonPanel.add(button5, 7, 0, 1, 1, 0.0, 0.0, 0, 18, new Insets(5, 2, 5, 2));


		mainPanel.add(buttonPanel, 0, 1, 1, 1, 1.0, 1.0, 0, 18, new Insets(5, 5, 5, 5));

		// Gesamtpanel in Scrollpane stecken
		mainScrollPane = new JScrollPane(mainPanel);
		mainScrollPane.setBorder(BorderFactory.createEmptyBorder());

		// Scrollgeschwindigkeit festlegen
		mainScrollPane.getVerticalScrollBar().setUnitIncrement(Settings.SCROLL_UNIT);
		mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		//sonst geht mousewheel nicht:
		mainScrollPane.removeMouseWheelListener(mainScrollPane.getMouseWheelListeners()[0]);

		berechnungPanel = new JScrollPane(mainScrollPane);
		berechnungPanel.setBorder(BorderFactory.createEmptyBorder());
		berechnungPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		berechnungPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		JScrollBar vertical = berechnungPanel.getVerticalScrollBar();
		vertical.setUnitIncrement(Settings.SCROLL_UNIT);
		InputMap im = vertical.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		im.put(KeyStroke.getKeyStroke("DOWN"), "positiveUnitIncrement");
		im.put(KeyStroke.getKeyStroke("UP"), "negativeUnitIncrement");
		im.put(KeyStroke.getKeyStroke("PAGE_DOWN"), "positiveBlockIncrement");
		im.put(KeyStroke.getKeyStroke("PAGE_UP"), "negativeBlockIncrement");
//Space geht nicht, weil wird nicht abgefangen        im.put(KeyStroke.getKeyStroke("SPACE"),"positiveBlockIncrement");


		berechnungPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "actionEscapeDialog");
		berechnungPanel.getActionMap().put("actionEscapeDialog", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				berechnungControl.setCards("Antrag");
			}

		});
		berechnungPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int breitenwert = new Double(e.getPoint().getX()).intValue();
				int hoehenwert = new Double(e.getPoint().getY()).intValue();

				int xpos = 0;
				int ypos = 0;
				if (breitenwert > 460) {
					xpos = 1000;
				}

				if (hoehenwert > 300) {
					ypos = 900;
				}

				berechnungPanel.getViewport().setViewPosition(new Point(xpos, ypos));
			}
		});


		setKeyBindings();
	}


	/**
	 * Liefert ZettelPanel1 (Seite 1 des Berechnungszettel)
	 *
	 * @return ZettelPanel1
	 */
	public ZettelPanel1 getZettel1() {
		return zettel1;
	}


	public ZettelPanel2 getZettel2() {
		return zettel2;
	}


	/**
	 * Liefert Scrollpane mit BerechnungPanel über PersonControl an TabView (CardLayout)
	 *
	 * @return Scrollpane
	 */
	public JScrollPane getBerechnungPanel() {
		return berechnungPanel;
	}


	/**
	 * Setzt KeyBindings (Hotkeys für das Antragformular)
	 */
	@SuppressWarnings("serial")
	private void setKeyBindings() {


		// Taste F4: von Antrag in Berechnungszettel wechseln und zurück
		mainScrollPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F4"), "actionZurueckZuAntrag");
		mainScrollPane.getActionMap().put("actionZurueckZuAntrag", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				berechnungControl.setCards("Antrag");
			}

		});


		// Taste STRG + S: BRZ Härten speichern
		mainScrollPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK), "actionSaveBRZ");
		mainScrollPane.getActionMap().put("actionSaveBRZ", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				berechnungControl.updateAntragModel(null);
			}

		});


	}


}
