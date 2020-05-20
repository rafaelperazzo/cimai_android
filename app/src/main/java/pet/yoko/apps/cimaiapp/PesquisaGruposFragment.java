package pet.yoko.apps.cimaiapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
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
 * Use the {@link PesquisaGruposFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PesquisaGruposFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String url_dados = "https://apps.yoko.pet/webapi/cimaiapi.php?ano=2019&tabela=gruposDados&";
    String url = "https://apps.yoko.pet/webapi/cimaiapi.php?ano=2019&tabela=grupos";
    TextView certificados;
    TextView docentes;
    TextView tecnicos;
    TextView discentes;
    TextView atualizacao;
    ProgressBar progressoMain;
    ArrayList<ProducaoItem> items;
    CustomAdapter adapter;
    TextView tituloTabela;

    ArrayList<GrupoItem> listaGrupos;
    GrupoAdapter listaGruposAdapter;

    RecyclerView recyclerView;

    public PesquisaGruposFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PesquisaGruposFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PesquisaGruposFragment newInstance(String param1, String param2) {
        PesquisaGruposFragment fragment = new PesquisaGruposFragment();
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

        View view = inflater.inflate(R.layout.fragment_pesquisa_grupos, container, false);

        certificados = (TextView)view.findViewById(R.id.txtGrupoCertificados);
        docentes = (TextView)view.findViewById(R.id.txtGrupoDocentes);
        discentes = (TextView)view.findViewById(R.id.txtGrupoDiscentes);
        tecnicos = (TextView)view.findViewById(R.id.txtGrupoTecnicos);
        progressoMain = getActivity().findViewById(R.id.progressoPesquisaMain);
        atualizacao = (TextView)view.findViewById(R.id.txtGrupoAtualizacao);
        progressoMain.setVisibility(View.VISIBLE);
        Button btnPorArea = (Button)view.findViewById(R.id.btnGrupoArea);
        btnPorArea.setOnClickListener(this);
        Button btnListaGrupos = (Button)view.findViewById(R.id.btnGrupoListaProjetos);
        btnListaGrupos.setOnClickListener(this);
        tituloTabela = (TextView)view.findViewById(R.id.txtGrupoTituloTabela);
        //TABELAS
        items = new ArrayList<ProducaoItem>();
        listaGrupos = new ArrayList<GrupoItem>();
        listaGruposAdapter = new GrupoAdapter(listaGrupos);
        recyclerView = (RecyclerView) view.findViewById(R.id.tabelaGrupo);
        adapter = new CustomAdapter(items);
        /*LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecor);*/
        this.prepararRecycleView(recyclerView,items,adapter);

        try {
            this.run(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Inflate the layout for this fragment
        return view;

    }

    public void prepararRecycleView(RecyclerView recyclerView,ArrayList items, RecyclerView.Adapter adapter) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecor);
    }

    void run(String url) throws IOException {

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
                            certificados.setText(obj.getString("certificados"));
                            docentes.setText(obj.getString("docentes"));
                            discentes.setText(obj.getString("discentes"));
                            tecnicos.setText(obj.getString("tecnicos"));
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
                            MyTable tabela = new MyTable(obj,items,adapter);
                            tabela.makeTable(tipoItem);
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
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnGrupoArea:
                try {
                    this.prepararRecycleView(recyclerView,items,adapter);
                    this.runTabela(url_dados + "tipo=porArea","ProducaoItem");
                    tituloTabela.setText("Grupos por área");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btnGrupoListaProjetos:
                this.prepararRecycleView(recyclerView,listaGrupos,listaGruposAdapter);
                tituloTabela.setText("Lista de grupos de pesquisa");
                break;


        }
    }
}
