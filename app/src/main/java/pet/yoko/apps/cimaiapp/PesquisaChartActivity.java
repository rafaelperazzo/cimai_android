package pet.yoko.apps.cimaiapp;
//https://weeklycoding.com/mpandroidchart-documentation/
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa_chart);
        Intent intent = getIntent();
        String TIPO = intent.getStringExtra(PesquisaFragment.TIPO);
        String ANO = intent.getStringExtra(PesquisaFragment.ANO);
        String TABELA = intent.getStringExtra(PesquisaFragment.TABELA);
        String TITULO = intent.getStringExtra(PesquisaFragment.TITULO);
        TITULO_GRAFICO = TITULO;
        setTitle(TITULO + " - " + ANO);
        progresso = (ProgressBar)findViewById(R.id.progressoGrafico);
        progresso.setVisibility(View.VISIBLE);
        URL = "https://apps.yoko.pet/webapi/cimaiapi.php?ano=" + ANO + "&tabela=" + TABELA + "&tipo="+ TIPO;

        grafico = findViewById(R.id.porProducoesChart);
        //TODO: Chamar o método run()
        try {
            run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void makeChart2(ArrayList<BarEntry> dados, ArrayList<String> xLabels) {
        BarDataSet barDataSet = new BarDataSet(dados,"Quantidade");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(14f);
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(1);
        grafico.setFitBars(true);
        grafico.setData(barData);
        XAxis xAxis = grafico.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelRotationAngle(-90);
        xAxis.setDrawGridLines(false);
        Random rand = new Random();

        Legend l = grafico.getLegend();
        List<LegendEntry> entries = new ArrayList<>();
        for (int i = 0; i < xLabels.size(); i++) {
            LegendEntry entry = new LegendEntry();
            int r = rand.nextInt(254)+1;
            int g = rand.nextInt(254)+1;
            int b = rand.nextInt(254)+1;
            int randomColor = Color.rgb(r,g,b);
            entry.formColor = randomColor;
            entry.label = xLabels.get(i);
            entries.add(entry);
        }
        l.setCustom(entries);
        l.setWordWrapEnabled(true);
        l.setMaxSizePercent(0.3f);
        //l.resetCustom();
        l.setEnabled(true);


        grafico.animateY(2000);
        grafico.invalidate();

        //grafico.saveToGallery("mychart.jpg", 85);
    }

    public void makeChart(ArrayList<BarEntry> dados, ArrayList<String> xLabels) {
        BarData barData = new BarData();
        for (int i = 0; i<dados.size();i++) {
            BarEntry linha = dados.get(i);
            String label = xLabels.get(i);
            ArrayList<BarEntry> data = new ArrayList<>();
            data.add(linha);
            BarDataSet barDataSet = new BarDataSet(data,label);
            //barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            Random rand = new Random();
            int r = rand.nextInt(254)+1;
            int g = rand.nextInt(254)+1;
            int b = rand.nextInt(254)+1;
            int randomColor = Color.rgb(r,g,b);
            barDataSet.setColors(randomColor);
            barDataSet.setValueTextColor(Color.BLACK);
            barDataSet.setValueTextSize(20f);
            barData.addDataSet(barDataSet);
        }

        barData.setBarWidth(1);
        grafico.setFitBars(true);
        grafico.setData(barData);
        Legend l = grafico.getLegend();
        l.setWordWrapEnabled(true);
        l.setMaxSizePercent(0.3f);
        l.setEnabled(true);
        XAxis xAxis = grafico.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setCenterAxisLabels(true);
        //xAxis.setDrawLabels(true);
        xAxis.setLabelCount(xLabels.size());
        //xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));
        xAxis.setGranularity(1.0f);
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelRotationAngle(-90);
        xAxis.setDrawGridLines(false);
        xAxis.setCenterAxisLabels(true);
        xAxis.setAxisMinimum(0);
        xAxis.setEnabled(false);
        YAxis yAxis = grafico.getAxisLeft();
        yAxis.setDrawGridLines(false);
        yAxis.setAxisMinimum(0);
        yAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                DecimalFormat format = new DecimalFormat("#.####");
                return(format.format(value));
            }
        });
        yAxis.setEnabled(false);
        //yAxis.setDrawLabels(true);
        grafico.getAxisRight().setDrawGridLines(false);
        grafico.getAxisRight().setEnabled(false);
        //grafico.getAxisRight().setDrawLabels(true);
        grafico.animateY(2000);
        grafico.setDrawBarShadow(false);
        grafico.getDescription().setText(TITULO_GRAFICO);
        grafico.getDescription().setEnabled(false);
        grafico.groupBars(0f,0f,0.06f);
        grafico.invalidate();
        grafico.setSaveEnabled(true);
        grafico.saveToGallery("mychart.jpg", 85);
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
                            makeChart(valores,labels);
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
        /*
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
        shareIntent.setType("image/jpeg");
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
        */
    }
}
