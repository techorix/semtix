package org.semtix.shared.actions;

import javax.swing.*;
import java.awt.*;
import java.net.URI;

/**
 * Sends a mail
 * <p>
 * Created by Mi on 02.10.15.
 */
public class Email {
    public static void send(String emailAdress, String strURI) {
        if (emailAdress == null || emailAdress.isEmpty() || emailAdress.equals("") || !Desktop.isDesktopSupported()
                || !Desktop.getDesktop().isSupported(Desktop.Action.MAIL)) {
            String message = "Es ist keine Mailadresse eingetragen. Oder E-Mails werden nicht unterstützt.";
            JOptionPane.showMessageDialog(null, message, "Fehler", JOptionPane.ERROR_MESSAGE);
        } else {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.mail(new URI(strURI));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "[FEHLER] Konnte Mailprogramm nicht starten. \r\n" + e.getLocalizedMessage());
            }
        }

    }
}
