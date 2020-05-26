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
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
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
    int ano = 0;
    String url = "https://apps.yoko.pet//api/cimaiapi.php?tabela=producoes&ano=";
    String url_base = "https://apps.yoko.pet//api/cimaiapi.php?tabela=producoes&ano=";
    String url_base_tabelas = "https://apps.yoko.pet//api/cimaiapi.php?tabela=producoesDados&";
    TextView periodicos;
    TextView anais;
    TextView capitulos;
    TextView livros;
    TextView atualizacao;
    Spinner spinAno;
    public static final String TIPO = "porTipoProducao";
    public static final String TABELA = "producoesDados";
    public static final String ANO = "2019";
    public static final String TITULO = "GRAFICO";
    RecyclerView recyclerView;
    ArrayList<ProducaoItem> items;
    CustomAdapter adapter;
    TextView tituloTabela;
    ImageView share;
    Ferramenta tools;

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
        spinAno = (Spinner)view.findViewById(R.id.spinAno);
        String sAno = spinAno.getSelectedItem().toString();
        ano = Integer.parseInt(sAno);
        url = url_base + sAno;
        progressoMain = getActivity().findViewById(R.id.progressoPesquisaMain);
        progressoMain.setVisibility(View.VISIBLE);
        tools = new Ferramenta(getContext());
        items = new ArrayList<ProducaoItem>();
        tituloTabela = (TextView)view.findViewById(R.id.txtTituloTabela);
        share = (ImageView)view.findViewById(R.id.imgProducoesShare);
        periodicos = (TextView)view.findViewById(R.id.txtPeriodicos);
        anais = (TextView)view.findViewById(R.id.txtAnais);
        capitulos = (TextView)view.findViewById(R.id.txtCapitulos);
        livros = (TextView)view.findViewById(R.id.txtLivros);
        atualizacao = (TextView)view.findViewById(R.id.txtAtualizacao);

        try {
            run(url_base + sAno);
        } catch (IOException e) {
            e.printStackTrace();
        }

        spinAno.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String anoSelecionado = parentView.getItemAtPosition(position).toString();
                try {
                    TextView anoMain = (TextView)getActivity().findViewById(R.id.txtAno);
                    anoMain.setText(anoSelecionado);
                    run(url_base + anoSelecionado);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String url_data = url_base_tabelas + "ano=" + spinAno.getSelectedItem().toString() + "&tipo=porTipoProducao";
                try {
                    runTabela(url_data);
                    tituloTabela.setText("Por tipo de produção");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        Button porArea = (Button)view.findViewById(R.id.btnArea);
        porArea.setOnClickListener(this);
        Button porProducao = (Button)view.findViewById(R.id.btnPorProducao);
        porProducao.setOnClickListener(this);
        Button porGrandeArea = (Button)view.findViewById(R.id.btnGrandeArea);
        porGrandeArea.setOnClickListener(this);
        Button porBolsistaPq = (Button)view.findViewById(R.id.btnBolsistas);
        porBolsistaPq.setOnClickListener(this);
        periodicos.setOnClickListener(this);

        progresso = (ProgressBar)view.findViewById(R.id.progresso);
        progresso.setVisibility(View.INVISIBLE);

        recyclerView = (RecyclerView) view.findViewById(R.id.tabela);
        adapter = new CustomAdapter(items);
        /*
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        adapter = new CustomAdapter(items);
        recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecor);
        */
        tools.prepararRecycleView(recyclerView,items,adapter);
        tools.setImgProjetosShareClick(getContext(),recyclerView,share,"ProducaoItem","Por tipo de produção - " + spinAno.getSelectedItem().toString());

        return view;
    }

    @Override
    public void onClick(View view) {
        PesquisaActivity activity = (PesquisaActivity) getActivity();
        Intent intent =  new Intent(getContext(),PesquisaChartActivity.class);
        String url_data;
        switch (view.getId()) {
            case R.id.btnArea:

                url_data = url_base_tabelas + "ano=" + spinAno.getSelectedItem().toString() + "&tipo=porArea";
                try {
                    runTabela(url_data);
                    tituloTabela.setText("Por área");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                tools.setImgProjetosShareClick(getContext(),recyclerView,share,"ProducaoItem","Por área - " + spinAno.getSelectedItem().toString());
                break;
            case R.id.btnGrandeArea:
                url_data = url_base_tabelas + "ano=" + spinAno.getSelectedItem().toString() + "&tipo=porGrandeArea";
                try {
                    runTabela(url_data);
                    tituloTabela.setText("Por grande área");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                tools.setImgProjetosShareClick(getContext(),recyclerView,share,"ProducaoItem","Por grande área - " + spinAno.getSelectedItem().toString());
                intent.putExtra(TIPO,"porGrandeArea");
                intent.putExtra(TABELA,"producoesDados");
                intent.putExtra(TITULO,"Por Grande Área");
                intent.putExtra(ANO,spinAno.getSelectedItem().toString());
                startActivity(intent);
                break;
            case R.id.btnBolsistas:
                url_data = url_base_tabelas + "ano=" + spinAno.getSelectedItem().toString() + "&tipo=porBolsistaPq";
                try {
                    runTabela(url_data);
                    tituloTabela.setText("Por bolsista de produtividade");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                tools.setImgProjetosShareClick(getContext(),recyclerView,share,"ProducaoItem","Por bolsista PQ - " + spinAno.getSelectedItem().toString());
                intent.putExtra(TIPO,"porBolsistaPq");
                intent.putExtra(TABELA,"producoesDados");
                intent.putExtra(TITULO,"Por Bolsista PQ");
                intent.putExtra(ANO,spinAno.getSelectedItem().toString());
                startActivity(intent);
                break;
            case R.id.btnPorProducao:
                url_data = url_base_tabelas + "ano=" + spinAno.getSelectedItem().toString() + "&tipo=porTipoProducao";
                try {
                    runTabela(url_data);
                    tituloTabela.setText("Por tipo de produção");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                tools.setImgProjetosShareClick(getContext(),recyclerView,share,"ProducaoItem","Por tipo de produção - " + spinAno.getSelectedItem().toString());
                intent.putExtra(TIPO,"porTipoProducao");
                intent.putExtra(TABELA,"producoesDados");
                intent.putExtra(TITULO,"Por tipo de produção");
                intent.putExtra(ANO,spinAno.getSelectedItem().toString());
                startActivity(intent);
                break;
            case R.id.txtPeriodicos:
                intent.putExtra(TIPO,"porProducaoAno");
                intent.putExtra(TABELA,"producoesDados");
                intent.putExtra(TITULO,"Histórico de produções por ano");
                intent.putExtra(ANO,"0");
                startActivity(intent);
                break;
        }
    }

    void run(String url) throws IOException {
        tools.idle(((PesquisaActivity)getActivity()),false);
        progressoMain.setVisibility(View.VISIBLE);
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                progressoMain.setVisibility(View.GONE);
                tools.idle(((PesquisaActivity)getActivity()),true);
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();
                tools.idle(((PesquisaActivity)getActivity()),true);
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

    void runTabela(String url) throws IOException {
        tools.idle(((PesquisaActivity)getActivity()),false);
        progressoMain.setVisibility(View.VISIBLE);
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                progressoMain.setVisibility(View.GONE);
                tools.idle(((PesquisaActivity)getActivity()),true);
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();
                tools.idle(((PesquisaActivity)getActivity()),true);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(myResponse);
                            MyTable tabela = new MyTable(obj,items,adapter);
                            tabela.makeTable();
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
