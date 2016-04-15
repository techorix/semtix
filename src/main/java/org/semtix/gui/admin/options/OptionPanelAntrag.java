/*

  Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
         Semesterticketbüro der Humboldt-Universität Berlin

  Copyright (c) 2015 Michael Mertins (MichaelMertins@gmail.com)
  2011-2014 Jürgen Schmelzle (j.schmelzle@web.de)

    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */

package org.semtix.gui.admin.options;

import org.apache.log4j.Logger;
import org.semtix.db.DBHandlerConf;
import org.semtix.shared.elements.SForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel in den AdminTools für Einstellungen für das Person-Formular.
 */
@SuppressWarnings("serial")
public class OptionPanelAntrag
        extends JPanel {

    private final TextField textFieldBetreffNachfragEmail;
    private final TextField textFieldBetreffMahnungEmail;
    private final JTextArea textAreaNachfrageEmail;
    private final JTextArea textAreaMahnungEmail;
    private final DBHandlerConf dbHandlerConf = new DBHandlerConf();
	private Logger logger = Logger.getLogger(OptionPanelAntrag.class);


    public OptionPanelAntrag() {

        this.setLayout(new BorderLayout());

        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        String betreffNachfragEmail = decode(dbHandlerConf.read("betreffNachfragEmail"));
        String betreffMahnungEmail = decode(dbHandlerConf.read("betreffMahnungEmail"));
        String textNachfrageEmail = decode(dbHandlerConf.read("textNachfrageEmail"));
        String textMahnungEmail = decode(dbHandlerConf.read("textMahnungEmail"));

        String tooltiptext = "Hier Text eingeben. Wenn <Name> eingegeben wird, wird <Name> später durch den Vornamen ersetzt.";

        textFieldBetreffNachfragEmail = new TextField(betreffNachfragEmail);
        textFieldBetreffMahnungEmail = new TextField(betreffMahnungEmail);


        textAreaNachfrageEmail = new JTextArea(textNachfrageEmail, 100, 10);
//        textAreaNachfrageEmail.setPreferredSize(new Dimension(700, 140));
        textAreaNachfrageEmail.setLineWrap(true);
        textAreaMahnungEmail = new JTextArea(textMahnungEmail, 100, 10);
//        textAreaMahnungEmail.setPreferredSize(new Dimension(700, 140));
        textAreaMahnungEmail.setLineWrap(true);

        textAreaNachfrageEmail.setToolTipText(tooltiptext);
        textAreaMahnungEmail.setToolTipText(tooltiptext);

        JScrollPane spNachfrageMail = new JScrollPane(textAreaNachfrageEmail);
        spNachfrageMail.setPreferredSize(new Dimension(700, 140));

        JScrollPane spMahnungMail = new JScrollPane(textAreaMahnungEmail);
        spMahnungMail.setPreferredSize(new Dimension(700, 140));


        JLabel labelTitelNF = new JLabel("Nachfrage-E-Mails");
        labelTitelNF.setFont(labelTitelNF.getFont().deriveFont(Font.BOLD));
        JLabel labelTitelMG = new JLabel("Mahnungs-E-Mails");
        labelTitelMG.setFont(labelTitelMG.getFont().deriveFont(Font.BOLD));

        JButton buttonSave = new JButton("Speichern");
        buttonSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                save();
            }
        });

        JButton buttonReset = new JButton("Zurücksetzen");
        buttonReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                textAreaMahnungEmail.setText(decode("Hallo%20<Name>%0A%0AEine%20Mahnung%20ist%20an%20Dich%20verschickt%20worden.%20Bitte%20achte%20in%20den%20nächsten%20Tagen%20auf%20Briefe%20in%20Deinem%20Briefkasten.%0A%0AMit%20freundlichen%20Grüßen%0A%0A%0ADein%20Semesterticketbüro-Team"));
                textAreaNachfrageEmail.setText(decode("Hallo%20<Name>%0A%0AEin%20Nachfragebrief%20ist%20an%20Dich%20verschickt%20worden.%20Bitte%20achte%20in%20den%20nächsten%20Tagen%20auf%20Briefe%20in%20Deinem%20Briefkasten.%0A%0AMit%20freundlichen%20Grüßen%0A%0A%0ADein%20Semesterticketbüro-Team"));
                textFieldBetreffMahnungEmail.setText(decode("Mahnungsschreiben%20unterwegs"));
                textFieldBetreffNachfragEmail.setText(decode("Nachfragebrief%20unterwegs"));
                save();
            }
        });


        SForm mainPanel = new SForm();

        mainPanel.add(labelTitelNF, 0, 0, 1, 1, 0.0, 0.0, 1, 17, new Insets(5, 0, 5, 0));
        mainPanel.add(textFieldBetreffNachfragEmail, 0, 1, 1, 1, 0.0, 0.0, 1, 17, new Insets(0, 0, 5, 0));
        mainPanel.add(spNachfrageMail, 0, 2, 1, 1, 1.0, 1.0, 2, 17, new Insets(0, 0, 0, 0));

        mainPanel.add(labelTitelMG, 0, 3, 1, 1, 0.0, 0.0, 1, 17, new Insets(20, 0, 5, 0));
        mainPanel.add(textFieldBetreffMahnungEmail, 0, 4, 1, 1, 0.0, 0.0, 1, 17, new Insets(0, 0, 5, 0));
        mainPanel.add(spMahnungMail, 0, 5, 1, 1, 1.0, 1.0, 2, 17, new Insets(0, 0, 0, 0));

        add(mainPanel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        buttonPanel.add(buttonSave);
        buttonPanel.add(buttonReset);


        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Encodes! Replaces " " with "%20" and "\n" with "%0A"
     *
     * @param stringToEncode
     * @return
     */
    private String encode(String stringToEncode) {
		if (null != stringToEncode &&
				(!(stringToEncode.contains("%20") || stringToEncode.contains("%0A")))) {
			stringToEncode = stringToEncode.replaceAll(" ", "%20");
            stringToEncode = stringToEncode.replaceAll("\t ", "%20%20%20%20");
            stringToEncode = stringToEncode.replaceAll("\n", "%0A");
        }

		return stringToEncode;
	}

    private String decode(String stringToDecode) {
		if (null != stringToDecode) {
			if (stringToDecode.contains("%20")) {
				stringToDecode = stringToDecode.replaceAll("%20", " ");
			}
			if (stringToDecode.contains("%0A")) {
				stringToDecode = stringToDecode.replaceAll("%0A", "\n");
			}
		}
		return stringToDecode;
    }


    private void save() {
        dbHandlerConf.update("betreffNachfragEmail", encode(textFieldBetreffNachfragEmail.getText()));
        dbHandlerConf.update("betreffMahnungEmail", encode(textFieldBetreffMahnungEmail.getText()));
        dbHandlerConf.update("textNachfrageEmail", encode(textAreaNachfrageEmail.getText()));
        dbHandlerConf.update("textMahnungEmail", encode(textAreaMahnungEmail.getText()));
    }

}
