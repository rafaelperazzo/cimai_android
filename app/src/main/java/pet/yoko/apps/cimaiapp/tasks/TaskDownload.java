package pet.yoko.apps.cimaiapp.tasks;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pet.yoko.apps.cimaiapp.db.AppDatabase;

public abstract class TaskDownload extends AsyncTask <Void,Void,Void> {

    protected AppDatabase db;
    protected OkHttpClient client = new OkHttpClient();
    protected String url;
    protected LocalBroadcastManager broadcast;

    protected TaskDownload(AppDatabase db, String url,LocalBroadcastManager broadcast) {
        this.db = db;
        this.url = url;
        this.broadcast = broadcast;
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
    protected Void doInBackground(Void... voids) {
        String conteudo = run(this.url);
        this.processarJSON(conteudo);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Intent myIntent = new Intent("broadcast-terminou-download");
        broadcast.sendBroadcast(myIntent);
    }
}
