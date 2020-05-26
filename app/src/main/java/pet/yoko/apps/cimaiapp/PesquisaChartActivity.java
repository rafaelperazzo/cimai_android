package pet.yoko.apps.cimaiapp;
//https://weeklycoding.com/mpandroidchart-documentation/
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PesquisaChartActivity extends AppCompatActivity {
    ProgressBar progresso;
    String URL;
    BarChart grafico;
    String TITULO_GRAFICO;
    String ANO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa_chart);
        Intent intent = getIntent();
        String TIPO = intent.getStringExtra(PesquisaFragment.TIPO);
        ANO = intent.getStringExtra(PesquisaFragment.ANO);
        String TABELA = intent.getStringExtra(PesquisaFragment.TABELA);
        String TITULO = intent.getStringExtra(PesquisaFragment.TITULO);
        TITULO_GRAFICO = TITULO;
        if (Integer.parseInt(ANO)!=0) {
            setTitle(TITULO + " - " + ANO);
        }
        else {
            setTitle(TITULO);
        }

        progresso = (ProgressBar)findViewById(R.id.progressoGrafico);
        progresso.setVisibility(View.VISIBLE);
        URL = "https://apps.yoko.pet/webapi/cimaiapi.php?ano=" + ANO + "&tabela=" + TABELA + "&tipo="+ TIPO;
        grafico = findViewById(R.id.porProducoesChart);
        try {
            run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void run() throws IOException {

        progresso.setVisibility(View.VISIBLE);
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(URL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                progresso.setVisibility(View.GONE);
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();

                PesquisaChartActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(myResponse);
                            ArrayList<BarEntry> valores = new ArrayList<>();
                            ArrayList<String> labels = new ArrayList<>();
                            int i = 0;
                            for(Iterator<String> keys=obj.keys(); keys.hasNext();) {
                                String chave = keys.next();
                                int valor = obj.getInt(chave);
                                labels.add(chave);
                                valores.add(new BarEntry(i,Integer.valueOf(valor)));
                                i++;
                            }
                            progresso.setVisibility(View.GONE);

                            if (Integer.parseInt(ANO)!=0) {
                                MyChart chart = new MyChart(grafico,valores,labels,false);
                                chart.makeChart();
                            }
                            else {
                                MyChart chart = new MyChart(grafico,valores,labels,true);
                                chart.makeChart();
                            }

                        } catch (JSONException e) {
                            progresso.setVisibility(View.GONE);
                            Toast toast = Toast.makeText(getApplicationContext(),"Erro no json: " + e.getMessage(),Toast.LENGTH_LONG);
                            toast.show();
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

    public void shareClick(View view) {
        Bitmap image = grafico.getChartBitmap();
        Uri uri = null;
        try {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "grafico.png");
            FileOutputStream stream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.close();
            uri = Uri.fromFile(file);
            /*Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setType("image/png");
            startActivity(Intent.createChooser(shareIntent, "Compartilhar"));*/
        } catch (IOException e) {

        }
    }
}
