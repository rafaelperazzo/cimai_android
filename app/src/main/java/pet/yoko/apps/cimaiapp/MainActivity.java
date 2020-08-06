package pet.yoko.apps.cimaiapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.DialogFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pet.yoko.apps.cimaiapp.db.DatabaseClient;
import pet.yoko.apps.cimaiapp.prae.PraeActivity;
import pet.yoko.apps.cimaiapp.tasks.TaskDownload;
import pet.yoko.apps.cimaiapp.tasks.TaskDownloadAll;
import pet.yoko.apps.cimaiapp.tasks.TaskDownloadPrae;
import pet.yoko.apps.cimaiapp.tasks.TaskDownloadPrograd;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    public static final String ANO = "";
    public int VERSAO;
    ProgressBar progressoMain;
    LocalBroadcastManager localBroadcastManager;
    BroadcastReceiver myBroadcastReceiver;
    IntentFilter myFilter;
    private final int MAX_TASKS = 2; //número de downloads
    private int TASKS_FINISHED = 0;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        VERSAO = getVersionCode();
        progressoMain = (ProgressBar)findViewById(R.id.progressoMain);
        sharedPref = getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        Ferramenta.setSharedPref(sharedPref);
        atualizarDados();
        iniciarBroadCast();
        TaskDownloadPrae dPrae = new TaskDownloadPrae(DatabaseClient.getInstance(getApplicationContext()).getAppDatabase(),"https://sci02-ter-jne.ufca.edu.br/webapi/anuario/prae",localBroadcastManager);
        TaskDownloadPrograd dPrograd = new TaskDownloadPrograd(DatabaseClient.getInstance(getApplicationContext()).getAppDatabase(),"https://sci02-ter-jne.ufca.edu.br/webapi/anuario/prograd",localBroadcastManager);
        dPrae.execute();
        dPrograd.execute();
        try {
            run("https://play.google.com/store/apps/details?id=pet.yoko.apps.cimaiapp", 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void atualizarDados() {
        //VERIFICANDO SE É NECESSÁRIO ATUALIZAR O BANCO DE DADOS
    }

    private void iniciarBroadCast() {
        progressoMain.getLayoutParams().height = LinearLayout.LayoutParams.MATCH_PARENT;
        progressoMain.setVisibility(View.VISIBLE);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        myBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                TASKS_FINISHED++;
                if (TASKS_FINISHED==MAX_TASKS) {
                    progressoMain.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    progressoMain.setVisibility(View.GONE);
                }
            }
        };
        myFilter = new IntentFilter("broadcast-terminou-download");
        localBroadcastManager.registerReceiver(myBroadcastReceiver, myFilter);
    }

    public int getVersionCode() {
        int versionCode = 0;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return (versionCode);
    }

    public void atualizarClick(View v) {
        Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=pet.yoko.apps.cimaiapp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void pesquisaClick(View view) {
        Intent intent = new Intent(this, PesquisaActivity.class);
        startActivity(intent);
    }

    public void PRAEclick(View view) {
        Intent intent = new Intent(this, PraeActivity.class);
        startActivity(intent);
    }

    public void ensinoClick(View view) {

    }

    void run(String url, final int tipo) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (tipo == 0) {
                            verificarAtualizacao(myResponse);
                        } else {

                        }

                    }
                });

            }
        });
    }

    public void verificarAtualizacao(String myResponse) {
        int versaoNova = Ferramenta.getAppPlayStoreVersion(myResponse);
        if (VERSAO < versaoNova) {
            popupAtualizar();
        }
    }

    public void popupAtualizar() {
        DialogFragment newFragment = new AtualizarDialog();
        newFragment.show(getSupportFragmentManager(), "Atualizar");
    }

    public void showAdministrativoMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);

        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.popup_main_administrativo);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.popup_main_sustentabilidade:
                //intent = new Intent(this, MapaActivity.class);
                //intent.putExtra(TIPO_MAPA,"cidades");
                //startActivity(intent);
                return true;
            case R.id.popup_main_orcamento:
                //intent = new Intent(this, MapaActivity.class);
                //intent.putExtra(TIPO_MAPA,"cidades");
                //startActivity(intent);
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        Intent intent;
        switch (item.getItemId()) {
            case R.id.main_compartilhar:
                String conteudo = "https://play.google.com/store/apps/details?id=pet.yoko.apps.cimaiapp";
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, conteudo);
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, "Compartilhando dados...");
                startActivity(shareIntent);
                return true;
            case R.id.main_avaliar:
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=pet.yoko.apps.cimaiapp");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
