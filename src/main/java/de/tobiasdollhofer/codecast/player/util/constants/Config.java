package de.tobiasdollhofer.codecast.player.util.constants;

import java.util.ResourceBundle;

/**
 * some general configurations loaded from config.properties file
 */
public class Config {

    private final static ResourceBundle configBundle = ResourceBundle.getBundle("config");

    public final static String LANGUAGE = configBundle.getString("language");

    public final static String CODECAST_ANNOTATION = configBundle.getString("codecast_annotation");

    public final static String CODECAST_INFO_ANNOTATION = configBundle.getString("codecast_info_annotation");

    public final static String URL_ANNOTATION = configBundle.getString("url_annotation");

}
