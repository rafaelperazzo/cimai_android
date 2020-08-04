package pet.yoko.apps.cimaiapp.tasks;

import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pet.yoko.apps.cimaiapp.db.AppDatabase;

public abstract class TaskDownload extends AsyncTask <Void,Void,Void> {

    protected AppDatabase db;
    protected ProgressBar progresso;
    protected OkHttpClient client = new OkHttpClient();
    protected String url;

    protected TaskDownload(AppDatabase db, ProgressBar progresso, String url) {
        this.db = db;
        this.progresso = progresso;
        this.url = url;
    }

    private String run(String url){
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            return ("[]");
        }
    }

    abstract void processarJSON(String conteudo);

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progresso.getLayoutParams().height = LinearLayout.LayoutParams.MATCH_PARENT;
        progresso.setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        String conteudo = run(this.url);
        this.processarJSON(conteudo);
        return null;

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progresso.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
        progresso.setVisibility(View.GONE);
    }
}
