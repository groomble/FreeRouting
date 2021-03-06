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
 * DesignFile.java
 *
 * Created on 25. Oktober 2006, 07:48
 *
 */
package net.freerouting.freeroute;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import static net.freerouting.freeroute.Filename.ALL_FILE_EXTENSIONS;
import static net.freerouting.freeroute.Filename.BINARY_FILE_EXTENSIONS;
import static net.freerouting.freeroute.Filename.RULES_FILE_EXTENSION;
import net.freerouting.freeroute.designformats.specctra.DsnFileException;
import net.freerouting.freeroute.interactive.BoardHandlingException;

/**
 * File functionality with security restrictions used, when the application is
 * opened with Java Webstart
 *
 * @author Alfons Wirtz
 */
public class DesignFile {

    private File output_file;
    private File input_file;
    private String design_dir_name;

    private static final CustomFileFilter FILE_FILTER = new CustomFileFilter(ALL_FILE_EXTENSIONS);

    DesignFile(File p_design_file, String p_design_dir_name) {
        this(p_design_file);
        if (!p_design_dir_name.isEmpty()) {
            design_dir_name = p_design_dir_name;
        } else if (input_file != null) {
            design_dir_name = input_file.getParent();
        } else {
            design_dir_name = null;
        }
    }

    DesignFile(File p_design_file) {
        if (p_design_file != null) {
            input_file = p_design_file;
            String file_name = p_design_file.getName();
            String[] name_parts = file_name.split("\\.");
            if (name_parts[name_parts.length - 1].compareToIgnoreCase(BINARY_FILE_EXTENSIONS) != 0) {
                String binfile_name = name_parts[0] + "." + BINARY_FILE_EXTENSIONS;
                output_file = new File(p_design_file.getParent(), binfile_name);
            } else {
                output_file = p_design_file;
            }
        } else {
            input_file = null;
            output_file = null;
        }
    }

    void initialize(File p_design_file) {
        if (p_design_file != null) {
            input_file = p_design_file;
            String file_name = p_design_file.getName();
            String[] name_parts = file_name.split("\\.");
            if (name_parts[name_parts.length - 1].compareToIgnoreCase(BINARY_FILE_EXTENSIONS) != 0) {
                String binfile_name = name_parts[0] + "." + BINARY_FILE_EXTENSIONS;
                output_file = new File(p_design_file.getParent(), binfile_name);
            } else {
                output_file = p_design_file;
            }
        } else {
            input_file = null;
            output_file = null;
        }
    }

    public static boolean read_rules_file(String p_design_name, String p_parent_name,
            net.freerouting.freeroute.interactive.BoardHandling p_board_handling, String p_confirm_message) throws DsnFileException {
        boolean result = false;
        String rule_file_name = p_design_name + ".rules";
        boolean dsn_file_generated_by_host = p_board_handling.get_routing_board().communication.specctra_parser_info.dsn_file_generated_by_host;
        if (dsn_file_generated_by_host) {
            File rules_file = new File(p_parent_name, rule_file_name);
            if (rules_file.exists()) {
                try (Reader reader = new InputStreamReader(new FileInputStream(rules_file), StandardCharsets.UTF_8)) {
                    if (WindowMessage.confirm(p_confirm_message)) {
                        result = net.freerouting.freeroute.designformats.specctra.RulesFile.read(reader, p_design_name, p_board_handling);
                        if (rules_file.delete() == false) {
                            throw new DesignFileException("Can't delete rules file");
                        }
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(DesignFile.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException | DesignFileException ex) {
                    Logger.getLogger(DesignFile.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    /**
     * Gets an InputStream from the file. Returns null, if the algorithm failed.
     */
    public InputStream get_input_stream() {
        InputStream result;
        if (input_file == null) {
            return null;
        }
        try {
            result = new FileInputStream(input_file);
        } catch (FileNotFoundException e) {
            Logger.getLogger(DesignFile.class.getName()).log(Level.SEVERE, null, e);
            result = null;
        }
        return result;
    }

    /**
     * Gets the file name as a String. Returns null on failure.
     */
    public String get_name() {
        if (input_file != null) {
            return input_file.getName();
        } else {
            return null;
        }
    }

    void save_as(String design_name, File new_file, ResourceBundle resources, BoardFrame p_board_frame) {
        if (new_file == null) {
            p_board_frame.screen_messages.set_status_message(resources.getString("message_1"));
            return;
        }
        String new_file_name = new_file.getName();
        String[] new_name_parts = new_file_name.split("\\.");
        String found_file_extension = new_name_parts[new_name_parts.length - 1];
        if (found_file_extension.compareToIgnoreCase(BINARY_FILE_EXTENSIONS) == 0) {
            p_board_frame.screen_messages.set_status_message(resources.getString("message_2") + " " + new_file.getName());
            output_file = new_file;
            p_board_frame.save();
        } else {
            if (found_file_extension.compareToIgnoreCase("dsn") != 0) {
                p_board_frame.screen_messages.set_status_message(resources.getString("message_3"));
                return;
            }
            try (OutputStream output_stream = new FileOutputStream(new_file)) {
                p_board_frame.board_panel.board_handling.export_to_dsn_file(output_stream, design_name, false);
            } catch (IOException | BoardHandlingException ex) {
                Logger.getLogger(DesignFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Writes a Specctra Session File to update the design file in the host
     * system. Returns false, if the write failed
     */
    public boolean write_specctra_session_file(BoardFrame p_board_frame, Locale p_locale) {
        final java.util.ResourceBundle resources
                = java.util.ResourceBundle.getBundle("net.freerouting.freeroute.resources.BoardMenuFile", p_locale);
        String design_file_name = get_name();
        String[] file_name_parts = design_file_name.split("\\.", 2);
        String design_name = file_name_parts[0];
        String output_file_name = design_name + ".ses";
        File curr_output_file = new File(get_parent(), output_file_name);
        try (OutputStream output_stream = new FileOutputStream(curr_output_file)) {
            if (p_board_frame.board_panel.board_handling.export_specctra_session_file(design_file_name, output_stream)) {
                p_board_frame.screen_messages.set_status_message(resources.getString("message_11") + " "
                        + output_file_name + " " + resources.getString("message_12"));
            } else {
                p_board_frame.screen_messages.set_status_message(resources.getString("message_13") + " "
                        + output_file_name + " " + resources.getString("message_7"));
                return false;
            }
            if (WindowMessage.confirm(resources.getString("confirm"))) {
                return write_rules_file(design_name, p_board_frame.board_panel.board_handling);
            }
        } catch (IOException ex) {
            Logger.getLogger(DesignFile.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    /**
     * Saves the board rule to file, so that they can be reused later on.
     */
    private boolean write_rules_file(String p_design_name, net.freerouting.freeroute.interactive.BoardHandling p_board_handling) {
        String rules_file_name = p_design_name + RULES_FILE_EXTENSION;
        File rules_file = new File(get_parent(), rules_file_name);
        try (OutputStream output_stream = new FileOutputStream(rules_file)) {
            net.freerouting.freeroute.designformats.specctra.RulesFile.write(p_board_handling, output_stream, p_design_name);
        } catch (IOException ex) {
            Logger.getLogger(DesignFile.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public void update_eagle(BoardFrame p_board_frame, Locale p_locale) {
        final java.util.ResourceBundle resources
                = java.util.ResourceBundle.getBundle("net.freerouting.freeroute.resources.BoardMenuFile", p_locale);
        String design_file_name = get_name();
        String[] file_name_parts = design_file_name.split("\\.", 2);
        String design_name = file_name_parts[0];
        String output_file_name = design_name + ".scr";
        File curr_output_file = new File(get_parent(), output_file_name);
        try (ByteArrayOutputStream session_output_stream = new ByteArrayOutputStream();
                Reader reader = new InputStreamReader(new ByteArrayInputStream(session_output_stream.toByteArray()), StandardCharsets.UTF_8);
                OutputStream output_stream = new FileOutputStream(curr_output_file);) {
            if (!p_board_frame.board_panel.board_handling.export_specctra_session_file(design_file_name, session_output_stream)) {
                return;
            }
            if (p_board_frame.board_panel.board_handling.export_eagle_session_file(reader, output_stream)) {
                p_board_frame.screen_messages.set_status_message(resources.getString("message_14") + " " + output_file_name + " " + resources.getString("message_15"));
            } else {
                p_board_frame.screen_messages.set_status_message(resources.getString("message_16") + " " + output_file_name + " " + resources.getString("message_7"));
            }
            if (WindowMessage.confirm(resources.getString("confirm"))) {
                write_rules_file(design_name, p_board_frame.board_panel.board_handling);
            }
        } catch (IOException ex) {
            Logger.getLogger(DesignFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Gets the binary file for saving or null, if the design file is not
     * available because the application is run with Java Web Start.
     */
    public File get_output_file() {
        return output_file;
    }

    public File get_input_file() {
        return input_file;
    }

    public String get_parent() {
        if (input_file != null) {
            return input_file.getParent();
        }
        return null;
    }

    public File get_parent_file() {
        if (input_file != null) {
            return input_file.getParentFile();
        }
        return null;
    }

    public boolean is_created_from_text_file() {
        return input_file != output_file;
    }

    public void dispose() {
    }

    @Override
    protected void finalize() {
        try {
            dispose();
        } finally {
            try {
                super.finalize();
            } catch (Throwable ex) {
                Logger.getLogger(DesignFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
