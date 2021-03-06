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
 * BoardWindowsMenu.java
 *
 * Created on 12. Februar 2005, 06:08
 */
package net.freerouting.freeroute;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Creates the parameter menu of a board frame.
 *
 * @author Alfons Wirtz
 */
@SuppressWarnings("serial")
public class BoardMenuParameter extends javax.swing.JMenu {

    private final BoardFrame board_frame;
    private final ResourceBundle resources;

    /**
     * Creates a new instance of BoardSelectMenu
     */
    private BoardMenuParameter(BoardFrame p_board_frame, Locale p_locale) {
        board_frame = p_board_frame;
        resources = java.util.ResourceBundle.getBundle("net.freerouting.freeroute.resources.BoardMenuParameter", p_locale);
    }

    /**
     * Returns a new windows menu for the board frame.
     */
    public static BoardMenuParameter get_instance(BoardFrame p_board_frame, Locale p_locale) {
        BoardMenuParameter parameter_menu = new BoardMenuParameter(p_board_frame, p_locale);

        parameter_menu.setText(parameter_menu.resources.getString("parameter"));

        javax.swing.JMenuItem selectwindow = new javax.swing.JMenuItem();
        selectwindow.setText(parameter_menu.resources.getString("select"));
        selectwindow.addActionListener((java.awt.event.ActionEvent evt) -> {
            parameter_menu.board_frame.select_parameter_window.setVisible(true);
        });

        parameter_menu.add(selectwindow);

        javax.swing.JMenuItem routewindow = new javax.swing.JMenuItem();
        routewindow.setText(parameter_menu.resources.getString("route"));
        routewindow.addActionListener((java.awt.event.ActionEvent evt) -> {
            parameter_menu.board_frame.route_parameter_window.setVisible(true);
        });

        parameter_menu.add(routewindow);

        javax.swing.JMenuItem autoroutewindow = new javax.swing.JMenuItem();
        autoroutewindow.setText(parameter_menu.resources.getString("autoroute"));
        autoroutewindow.addActionListener((java.awt.event.ActionEvent evt) -> {
            parameter_menu.board_frame.autoroute_parameter_window.setVisible(true);
        });

        parameter_menu.add(autoroutewindow);

        javax.swing.JMenuItem movewindow = new javax.swing.JMenuItem();
        movewindow.setText(parameter_menu.resources.getString("move"));
        movewindow.addActionListener((java.awt.event.ActionEvent evt) -> {
            parameter_menu.board_frame.move_parameter_window.setVisible(true);
        });

        parameter_menu.add(movewindow);

        return parameter_menu;
    }
}
