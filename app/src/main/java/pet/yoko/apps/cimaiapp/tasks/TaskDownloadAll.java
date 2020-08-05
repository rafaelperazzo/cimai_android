package pet.yoko.apps.cimaiapp.tasks;

import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.concurrent.ExecutionException;

import pet.yoko.apps.cimaiapp.db.AppDatabase;

public class TaskDownloadAll extends AsyncTask <Void,Void,Void> {

    AppDatabase db;
    ProgressBar progresso;
    private TaskDownloadPrae dPrae;
    private TaskDownloadPrograd dPrograd;

    public TaskDownloadAll(AppDatabase db, ProgressBar progresso) {
        this.db = db;
        this.progresso = progresso;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progresso.getLayoutParams().height = LinearLayout.LayoutParams.MATCH_PARENT;
        progresso.setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        dPrae = new TaskDownloadPrae(db,"https://sci02-ter-jne.ufca.edu.br/webapi/anuario/prae");
        dPrograd = new TaskDownloadPrograd(db,"https://sci02-ter-jne.ufca.edu.br/webapi/anuario/prograd");
        dPrae.execute();
        dPrograd.execute();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progresso.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
        progresso.setVisibility(View.GONE);
    }
}
