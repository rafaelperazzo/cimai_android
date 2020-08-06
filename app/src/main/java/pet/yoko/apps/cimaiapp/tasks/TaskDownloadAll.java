package pet.yoko.apps.cimaiapp.tasks;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.concurrent.ExecutionException;

import pet.yoko.apps.cimaiapp.db.AppDatabase;

public class TaskDownloadAll extends AsyncTask <Void,Void,Void> {

    AppDatabase db;
    ProgressBar progresso;
    LocalBroadcastManager broadcast;
    private TaskDownloadPrae dPrae;
    private TaskDownloadPrograd dPrograd;

    public TaskDownloadAll(AppDatabase db, ProgressBar progresso, LocalBroadcastManager broadcast) {
        this.db = db;
        this.progresso = progresso;
        this.broadcast = broadcast;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progresso.getLayoutParams().height = LinearLayout.LayoutParams.MATCH_PARENT;
        progresso.setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        dPrae = new TaskDownloadPrae(db,"https://sci02-ter-jne.ufca.edu.br/webapi/anuario/prae",broadcast);
        dPrograd = new TaskDownloadPrograd(db,"https://sci02-ter-jne.ufca.edu.br/webapi/anuario/prograd",broadcast);
        dPrae.execute();
        dPrograd.execute();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progresso.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
        progresso.setVisibility(View.GONE);
        Intent myIntent = new Intent("broadcast-terminou-download");
        broadcast.sendBroadcast(myIntent);
    }
}
