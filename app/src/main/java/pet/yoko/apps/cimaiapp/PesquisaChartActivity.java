package pet.yoko.apps.cimaiapp;
//https://weeklycoding.com/mpandroidchart-documentation/
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PesquisaChartActivity extends AppCompatActivity {
    ProgressBar progresso;
    String URL;
    BarChart grafico;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa_chart);
        Intent intent = getIntent();
        String TIPO = intent.getStringExtra(PesquisaFragment.TIPO);
        String ANO = intent.getStringExtra(PesquisaFragment.ANO);
        String TABELA = intent.getStringExtra(PesquisaFragment.TABELA);
        String TITULO = intent.getStringExtra(PesquisaFragment.TITULO);
        setTitle(TITULO);
        progresso = (ProgressBar)findViewById(R.id.progressoGrafico);
        progresso.setVisibility(View.VISIBLE);
        URL = "https://apps.yoko.pet/webapi/cimaiapi.php?ano=" + ANO + "&tabela=" + TABELA + "&tipo="+ TIPO;

        grafico = findViewById(R.id.porProducoesChart);
        makeChart(null,null);
        //TODO: Chamar o método run()
    }

    public void makeChart(ArrayList<BarEntry> dados, String[] xLabels) {
        ArrayList<BarEntry> porProducoes = new ArrayList<>();
        //TODO: Adptar para receber quaisquer dados e labels!
        porProducoes.add(new BarEntry(0,204));
        porProducoes.add(new BarEntry(1,70));
        porProducoes.add(new BarEntry(2,120));
        porProducoes.add(new BarEntry(3,78));
        porProducoes.add(new BarEntry(4,12));
        Legend legend = grafico.getLegend();
        legend.setEnabled(false);
        ArrayList<String> labels = new ArrayList<>();
        labels.add("LABEL 1");
        labels.add("LABEL 2");
        labels.add("LABEL 3");
        labels.add("LABEL 4");
        labels.add("LABEL 5");

        final String[] xl = new String[]{"LABEL1","LABEL2","LABEL3","LABEL4","LABEL5"};
        BarDataSet barDataSet = new BarDataSet(porProducoes,"Quantidade");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);
        BarData barData = new BarData(barDataSet);

        grafico.setFitBars(true);
        grafico.setData(barData);
        XAxis xAxis = grafico.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelRotationAngle(-90);
        xAxis.setDrawGridLines(false);
        //xAxis.setValueFormatter(formatter);
        //grafico.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        grafico.getDescription().setText("");
        grafico.animateY(2000);
        grafico.invalidate();

        //grafico.saveToGallery("mychart.jpg", 85);
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
                            //TODO: Extrair dados da API e passar para o makechart()
                            
                            progresso.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }
}
