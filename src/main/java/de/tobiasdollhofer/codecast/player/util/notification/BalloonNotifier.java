package de.tobiasdollhofer.codecast.player.util.notification;

import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.project.Project;
import de.tobiasdollhofer.codecast.player.util.constants.Strings;
import org.jetbrains.annotations.Nullable;

public class BalloonNotifier {

    private static final NotificationGroup NOTIFICATION_GROUP =
            new NotificationGroup(Strings.NOTIFICATION_GROUP_NAME, NotificationDisplayType.BALLOON, true);

    /**
     * notify error message with a small popup
     * @param project current project
     * @param content message
     */
    public static void notifyError(@Nullable Project project, String content) {
        NOTIFICATION_GROUP.createNotification(content, NotificationType.ERROR).notify(project);
    }

    /**
     * notify warning with a small popup
     * @param project current project
     * @param content message
     */
    public static void notifyWarning(@Nullable Project project, String content) {
        NOTIFICATION_GROUP.createNotification(content, NotificationType.WARNING).notify(project);
    }

}
