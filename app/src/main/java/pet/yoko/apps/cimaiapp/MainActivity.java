package pet.yoko.apps.cimaiapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pet.yoko.apps.cimaiapp.prae.PraeActivity;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    public static final String ANO = "";
    public int VERSAO;
    ProgressBar progressoMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        VERSAO = getVersionCode();
        progressoMain = (ProgressBar)findViewById(R.id.progressoMain);
        progressoMain.setVisibility(View.GONE);
        try {
            run("https://play.google.com/store/apps/details?id=pet.yoko.apps.cimaiapp", 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
