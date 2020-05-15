package pet.yoko.apps.cimaiapp;
/*
<com.github.mikephil.charting.charts.HorizontalBarChart
        android:id="@+id/porProducoesChart"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:visibility="visible">

    </com.github.mikephil.charting.charts.HorizontalBarChart>

 */
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PesquisaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PesquisaFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ProgressBar progresso;
    ProgressBar progressoMain;
    //WebView webView;
    int ano = 0;
    String url = "https://apps.yoko.pet//api/cimaiapi.php?tabela=producoes&ano=";
    TextView periodicos;
    TextView anais;
    TextView capitulos;
    TextView livros;
    TextView atualizacao;
    public static final String TIPO = "";
    public static final String TABELA = "producoesDados";
    public static final String ANO = "";
    public static final String TITULO = "";

    public PesquisaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PesquisaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PesquisaFragment newInstance(String param1, String param2) {
        PesquisaFragment fragment = new PesquisaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pesquisa, container, false);
        PesquisaActivity activity = (PesquisaActivity) getActivity();
        ano = activity.getAno();
        String sAno = String.valueOf(ano);
        url = url + sAno;
        progressoMain = getActivity().findViewById(R.id.progressoPesquisaMain);
        progressoMain.setVisibility(View.VISIBLE);
        try {
            run();
        } catch (IOException e) {
            e.printStackTrace();
        }
        periodicos = (TextView)view.findViewById(R.id.txtPeriodicos);
        anais = (TextView)view.findViewById(R.id.txtAnais);
        capitulos = (TextView)view.findViewById(R.id.txtCapitulos);
        livros = (TextView)view.findViewById(R.id.txtLivros);
        atualizacao = (TextView)view.findViewById(R.id.txtAtualizacao);
        Button porArea = (Button)view.findViewById(R.id.btnArea);
        porArea.setOnClickListener(this);
        Button porProducao = (Button)view.findViewById(R.id.btnPorProducao);
        porProducao.setOnClickListener(this);
        progresso = (ProgressBar)view.findViewById(R.id.progresso);
        progresso.setVisibility(View.INVISIBLE);
        //webView = (WebView) view.findViewById(R.id.webview);
        return view;
    }

    @Override
    public void onClick(View view) {
        PesquisaActivity activity = (PesquisaActivity) getActivity();
        switch (view.getId()) {
            case R.id.btnArea:
                //this.ajustarProgresso(webView,progresso,"https://apps.yoko.pet/cimai/pesquisa?ano=" + String.valueOf(ano));
                break;
            case R.id.btnPorProducao:
                Intent intent =  new Intent(getActivity(),PesquisaChartActivity.class);
                intent.putExtra(TIPO,"porProducao");
                intent.putExtra(TABELA,"producoesDados");
                intent.putExtra(TITULO,"Por tipo de produção");
                intent.putExtra(ANO,String.valueOf(activity.getAno()));
                startActivity(intent);
                break;
        }
    }

    public void ajustarProgresso(WebView webView, final ProgressBar progresso, String URL) {
        progresso.setVisibility(View.VISIBLE);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                progresso.setVisibility(View.GONE);
            }
        });
        webView.loadUrl(URL);
    }

    void run() throws IOException {

        progressoMain.setVisibility(View.VISIBLE);
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                progressoMain.setVisibility(View.GONE);
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(myResponse);
                            periodicos.setText(obj.getString("periodicos"));
                            anais.setText(obj.getString("anais"));
                            capitulos.setText(obj.getString("capitulos"));
                            livros.setText(obj.getString("livros"));
                            atualizacao.setText("Atualizado: " + obj.getString("atualizacao"));
                            progressoMain.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

}
