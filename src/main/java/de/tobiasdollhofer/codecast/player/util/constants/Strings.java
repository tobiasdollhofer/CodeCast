package de.tobiasdollhofer.codecast.player.util.constants;

import java.util.ResourceBundle;

/**
 * Texts used in UI loaded from ui_text resource bundle (language dependend)
 */
public class Strings {

    private final static ResourceBundle uiTextBundle = ResourceBundle.getBundle("ui_text");

    public final static String EXPLANATION_NO_COMMENTS = uiTextBundle.getString("codecast_description_no_comments");

    public final static String EXPLANATION = uiTextBundle.getString("codecast_description");

    public final static String NO_COMMENT_AVAILABLE = uiTextBundle.getString("no_comment_available");

    public final static String LOADING_COMMENTS = uiTextBundle.getString("comments_loading");

    public final static String PLAYBACK_ZERO = uiTextBundle.getString("playback_zero");

    public final static String FILE_NOT_AVAILABLE = uiTextBundle.getString("file_not_available");

    public final static String DOWNLOAD_CANCELED = uiTextBundle.getString("download_canceled");

    public final static String FOLDING_GROUP_NAME = uiTextBundle.getString("folding_group_name");

    public final static String FOLDING_PLACEHOLDER_TEXT = uiTextBundle.getString("folding_placeholder_text");

    public final static String TOOLTIP_GUTTER_ICON = uiTextBundle.getString("tooltip_gutterIcon");

    public final static String WINDOW_DISPLAY_NAME = uiTextBundle.getString("window_displayName");

    public final static String HELP_DISPLAY_NAME = uiTextBundle.getString("help_displayName");

    public final static String NOTIFICATION_GROUP_NAME = uiTextBundle.getString("notification_group_name");

    public final static String DOWNLOAD_TASK_TITLE = uiTextBundle.getString("download_task_title");

    public final static String FILE_ALREADY_EXIST = uiTextBundle.getString("file_already_exist");

    public final static String DOWNLOAD_FAILED_EXCEPTION = uiTextBundle.getString("download_failed_exception");

}
