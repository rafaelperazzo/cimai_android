package pet.yoko.apps.cimaiapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

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
 * Use the {@link PesquisaProjetosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PesquisaProjetosFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String url_dados = "https://apps.yoko.pet/webapi/cimaiapi.php?tabela=projetosDados&ano=";
    String url = "https://apps.yoko.pet/webapi/cimaiapi.php?tabela=projetos&ano=";
    TextView total_projetos;
    TextView coordenadores;
    TextView discentes;
    TextView atualizacao;
    ProgressBar progressoMain;
    Spinner spinAno;
    TextView tituloTabela;
    public static final String TIPO = "porTipoProducao";
    public static final String TABELA = "producoesDados";
    public static final String ANO = "2019";
    public static final String TITULO = "GRAFICO";

    RecyclerView recyclerView;
    ArrayList<ProducaoItem> items;
    CustomAdapter adapter;

    ArrayList<GrupoItem> listaProjetos;
    GrupoAdapter listaProjetosAdapter;

    Ferramenta tools;
    SearchView pesquisar;

    public PesquisaProjetosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PesquisaProjetosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PesquisaProjetosFragment newInstance(String param1, String param2) {
        PesquisaProjetosFragment fragment = new PesquisaProjetosFragment();
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
        View view = inflater.inflate(R.layout.fragment_pesquisa_projetos, container, false);
        tools = new Ferramenta(getContext());
        total_projetos = (TextView)view.findViewById(R.id.txtProjetos);
        coordenadores = (TextView)view.findViewById(R.id.txtProjetoCoordenadores);
        discentes = (TextView)view.findViewById(R.id.txtProjetoDiscentes);
        atualizacao = (TextView)view.findViewById(R.id.txtProjetoAtualizacao);
        progressoMain = getActivity().findViewById(R.id.progressoPesquisaMain);
        progressoMain.setVisibility(View.VISIBLE);
        tituloTabela = (TextView)view.findViewById(R.id.txtProjetoTituloTabela);
        spinAno = (Spinner)view.findViewById(R.id.spinProjetoAno);
        Button btnPorArea = (Button)view.findViewById(R.id.btnProjetoArea);
        btnPorArea.setOnClickListener(this);
        Button btnPorGrandeArea = (Button)view.findViewById(R.id.btnProjetoGrandeArea);
        btnPorGrandeArea.setOnClickListener(this);
        Button btnPorAno = (Button)view.findViewById(R.id.btnProjetoPorAno);
        btnPorAno.setOnClickListener(this);
        Button btnListaProjetos = (Button)view.findViewById(R.id.btnProjetoLista);
        btnListaProjetos.setOnClickListener(this);
        try {
            run(url + spinAno.getSelectedItem().toString());
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
                    run(url + anoSelecionado);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String url_data = url_dados + spinAno.getSelectedItem().toString() + "&tipo=porAno";
                try {
                    tools.prepararRecycleView(recyclerView,items,adapter);
                    runTabela(url_data,"ProducaoItem");
                    tituloTabela.setText("Por ano");
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        recyclerView = (RecyclerView)view.findViewById(R.id.tabelaProjeto);
        items = new ArrayList<ProducaoItem>();
        adapter = new CustomAdapter(items);
        listaProjetos = new ArrayList<GrupoItem>();
        listaProjetosAdapter = new GrupoAdapter(listaProjetos);
        tools.prepararRecycleView(recyclerView,items,adapter);
        pesquisar = (SearchView)view.findViewById(R.id.searchProjetos);
        pesquisar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                listaProjetosAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listaProjetosAdapter.getFilter().filter(newText);
                ArrayList<GrupoItem> filtrada = new ArrayList<>();
                for (GrupoItem linha: listaProjetos) {
                    if (linha.lider.toLowerCase().contains(newText)) {
                        filtrada.add(linha);
                    }
                }
                listaProjetosAdapter.setItems(filtrada);
                return false;
            }
        });

        return view;

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
                            total_projetos.setText(obj.getString("total"));
                            coordenadores.setText(obj.getString("coordenadores"));
                            discentes.setText(obj.getString("discentes"));
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

    void runTabela(String url, final String tipoItem) throws IOException {

        progressoMain.setVisibility(View.VISIBLE);
        tools.idle(((PesquisaActivity)getActivity()),false);
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
                            if (tipoItem.equals("ProducaoItem")) {
                                MyTable tabela = new MyTable(obj,items,adapter);
                                tabela.makeTable();
                            }
                            else if (tipoItem.equals("GrupoItem")) {
                                TabelaListaGrupos tabela = new TabelaListaGrupos(obj,listaProjetos,listaProjetosAdapter);
                                tabela.makeTable();
                            }

                            progressoMain.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }


    @Override
    public void onClick(View v) {
        Intent intent =  new Intent(getContext(),PesquisaChartActivity.class);
        switch (v.getId()) {
            case R.id.btnProjetoArea:
                try {
                    tools.prepararRecycleView(recyclerView,items,adapter);
                    this.runTabela(url_dados + spinAno.getSelectedItem().toString() + "&tipo=porArea","ProducaoItem");
                    tituloTabela.setText("Projetos por área");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btnProjetoGrandeArea:
                try {
                    tools.prepararRecycleView(recyclerView,items,adapter);
                    this.runTabela(url_dados + spinAno.getSelectedItem().toString() + "&tipo=porGrandeArea","ProducaoItem");
                    tituloTabela.setText("Projetos por grande área");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //TODO: AUTOMATIZAR NA CLASSE FERRAMENTAS
                intent.putExtra(TIPO,"porGrandeArea");
                intent.putExtra(TABELA,"projetosDados");
                intent.putExtra(TITULO,"Por Grande Área");
                intent.putExtra(ANO,spinAno.getSelectedItem().toString());
                startActivity(intent);
                break;
            case R.id.btnProjetoPorAno:
                try {
                    tools.prepararRecycleView(recyclerView,items,adapter);
                    this.runTabela(url_dados + spinAno.getSelectedItem().toString() + "&tipo=porAno","ProducaoItem");
                    tituloTabela.setText("Projetos por ano");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //TODO: AUTOMATIZAR NA CLASSE FERRAMENTAS
                intent.putExtra(TIPO,"porAno");
                intent.putExtra(TABELA,"projetosDados");
                intent.putExtra(TITULO,"Projetos de Pesquisa Por Ano");
                intent.putExtra(ANO,"0");
                startActivity(intent);
                break;
            case R.id.btnProjetoLista:
                tools.prepararRecycleView(recyclerView,listaProjetos,listaProjetosAdapter);
                try {
                    this.runTabela(url_dados + spinAno.getSelectedItem().toString() + "&tipo=listaProjetos","GrupoItem");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                tituloTabela.setText("Lista de Projetos");
                break;

        }
    }

}
