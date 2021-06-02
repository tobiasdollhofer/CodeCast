package de.tobiasdollhofer.codecast.player.util;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

public class BalloonNotifier {

    public static void notifyError(@Nullable Project project, String content) {
        NotificationGroupManager.getInstance().getNotificationGroup("CodeCastNotifier")
                .createNotification(content, NotificationType.ERROR)
                .notify(project);
    }

}
