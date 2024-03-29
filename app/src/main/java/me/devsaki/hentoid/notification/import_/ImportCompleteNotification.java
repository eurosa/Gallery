package me.devsaki.hentoid.notification.import_;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import me.devsaki.hentoid.R;
import me.devsaki.hentoid.util.notification.Notification;

public class ImportCompleteNotification implements Notification {

    private final int booksOK, booksKO;

    public ImportCompleteNotification(int booksOK, int booksKO)
    {
        this.booksOK = booksOK;
        this.booksKO = booksKO;
    }

    @NonNull
    @Override
    public android.app.Notification onCreateNotification(Context context) {
        return new NotificationCompat.Builder(context, ImportNotificationChannel.ID)
                .setSmallIcon(R.drawable.ic_cherry_icon)
                .setContentTitle("Import complete")
                .setContentText(booksOK + " imported successfuly; " + booksKO +" failed")
                .build();
    }
}
