/*
 *  Copyright (C) 2014  Alfons Wirtz  
 *   website www.freerouting.net
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License at <http://www.gnu.org/licenses/> 
 *   for more details.
 *
 * BoardMenuHelp.java
 *
 * Created on 19. Oktober 2005, 08:15
 *
 */
package net.freerouting.freeroute;

import java.net.URL;
import java.util.Locale;
import javax.help.CSH;
import javax.help.HelpSet;
import javax.help.HelpSetException;

/**
 *
 * @author Alfons Wirtz
 */
@SuppressWarnings("serial")
public class BoardMenuHelp extends BoardMenuHelpReduced {

    private static CSH.DisplayHelpFromSource contents_help = null;
    private static CSH.DisplayHelpAfterTracking direct_help = null;

    /**
     * Creates a new instance of BoardMenuHelp Separated from
     * BoardMenuHelpReduced to avoid ClassNotFound exception when the library
     * jh.jar is not found, which is only used in this extended class.
     */
    public BoardMenuHelp(BoardFrame p_board_frame, Locale p_locale) {
        super(p_board_frame, p_locale);
        this.initialize_help(p_locale);
        javax.swing.JMenuItem direct_help_window = new javax.swing.JMenuItem();
        direct_help_window.setText(this.resources.getString("direct_help"));
        if (direct_help != null) {
            direct_help_window.addActionListener(direct_help);
        }
        this.add(direct_help_window, 0);
        javax.swing.JMenuItem contents_window = new javax.swing.JMenuItem();
        contents_window.setText(this.resources.getString("contents"));
        if (contents_help != null) {
            contents_window.addActionListener(contents_help);
        }
        this.add(contents_window, 0);
    }

    private void initialize_help(Locale p_locale) {
        // try to find the helpset and create a HelpBroker object
        if (BoardFrame.help_broker == null) {
            String language = p_locale.getLanguage();
            String helpset_name;
            helpset_name = "helpset/" + language + "/jhelpset.hs";
            try {
                URL hsURL = HelpSet.findHelpSet(this.getClass().getClassLoader(), helpset_name);
                if (hsURL == null) {
                    System.out.println("HelpSet " + helpset_name + " not found.");
                } else {
                    BoardFrame.help_set = new HelpSet(null, hsURL);
                }
            } catch (HelpSetException ee) {
                System.out.println("HelpSet " + helpset_name + " could not be opened.");
                System.out.println(ee.getMessage());
            }
            if (BoardFrame.help_set != null) {
                BoardFrame.help_broker = BoardFrame.help_set.createHelpBroker();
            }
            if (BoardFrame.help_broker != null) {
                // CSH.DisplayHelpFromSource is a convenience class to display the helpset
                contents_help = new CSH.DisplayHelpFromSource(BoardFrame.help_broker);
                direct_help = new CSH.DisplayHelpAfterTracking(BoardFrame.help_broker);
            }
        }
    }
}
