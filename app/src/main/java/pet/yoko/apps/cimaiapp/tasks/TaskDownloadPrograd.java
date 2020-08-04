package pet.yoko.apps.cimaiapp.tasks;

import android.widget.ProgressBar;

import pet.yoko.apps.cimaiapp.db.AppDatabase;

public class TaskDownloadPrograd extends TaskDownload {

    public TaskDownloadPrograd(AppDatabase db, ProgressBar progresso, String url) {
        super(db, progresso, url);
    }

    @Override
    void processarJSON(String conteudo) {

    }
}
