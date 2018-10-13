package br.com.neogis.agritopo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import br.com.neogis.agritopo.R;
import br.com.neogis.agritopo.utils.RemoverAcentos;

public class SelecionarMunicipioActivity extends FormGeralActivity {

    public static final String PAIS         = "pais";
    public static final String PAIS_SIGLA   = "pais_sigla";
    public static final String ESTADO       = "estado";
    public static final String ESTADO_SIGLA = "estado_sigla";
    public static final String MUNICIPIO    = "municipio";
    public static final String MUNICIPIO_ID = "municipio_id";

    ListView listView;
    EditText inputMunicipio;

    private Adaptador adapter;
    private List<String> linhas; // as linhas carregadas do arquivo
    private List<Registro> resultado; // as linhas que mostramos na tela

    private int tamanhoMaximoLista = 10;
    private String paisSelecionado;
    private String siglaPaisSelecionado;
    private String estadoSelecionado;
    private String siglaEstadoSelecionado;

    @Override
    protected int getLayoutFormulario() {
        return R.layout.activity_selecionar_municipio_form;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Selecione o país");
        botaoConfimar.hide(); // confirmação é feita ao selecionar item da lista

        resultado = new ArrayList<>();
        listView = (ListView) findViewById(R.id.lista);
        inputMunicipio = (EditText) findViewById(R.id.municipio);
        adapter = new Adaptador(this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long itemId) {
                Registro r = resultado.get(position);
                selecionarRegistro(r);
            }
        });
        inputMunicipio.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrarLista(s.toString());
            }
        });

        tamanhoMaximoLista = 0;
        carregarArquivo("paises.txt");
        if( !selecionarUnicaLinha() ) { // caso haja apenas um país selecionar ele automaticamente
            filtrarLista("");
        }
    }

    private void filtrarLista(String pesquisa) {
//        long startMs = SystemClock.currentThreadTimeMillis();

        resultado.clear();

        pesquisa = RemoverAcentos.remover(pesquisa);
        pesquisa = pesquisa.toLowerCase();
//        Utils.info("Pesquisando por: " + pesquisa);

        for(String linha : linhas) {

            String candidato = RemoverAcentos.remover(linha);
            candidato = candidato.toLowerCase();

            if( candidato.contains(pesquisa) ) {
                Registro r = extrairRegistro(linha);
                resultado.add(r);
                if( tamanhoMaximoLista > 0 && resultado.size() == tamanhoMaximoLista )
                    break;
            }
        }
//        long endMs = SystemClock.currentThreadTimeMillis();
//        long ms = endMs - startMs;
//        Utils.info(Integer.toString(resultado.size()) + " resultados em " + Long.toString(ms) + " ms");
        adapter.notifyDataSetChanged();
    }

    private Registro extrairRegistro(String linha) {
        String partes[] = linha.split(",");
        String descr = partes[0];
        String id = partes[1];
        return new Registro(id, descr);
    }

    private void selecionarRegistro(Registro r) {
        if( siglaPaisSelecionado == null ){
            selecionarPais(r);
        }
        else if( siglaEstadoSelecionado == null ){
            selecionarEstado(r);
        }
        else {
            selecionarMunicipio(r);
        }
    }

    // carrega lista de estados e pede para selecionar um
    private void selecionarPais(Registro r) {
        siglaPaisSelecionado = r.id;
        paisSelecionado = r.descr;
        carregarArquivo(siglaPaisSelecionado + "/_estados.txt");
        if( selecionarUnicaLinha() )
            return; // já invocou selecionarEstado();

        setTitle("Selecione o estado");
        filtrarLista("");
    }

    // carrega lista dos municípios e pede para selecionar um
    private void selecionarEstado(Registro r) {
        siglaEstadoSelecionado = r.id;
        estadoSelecionado = r.descr;
        carregarArquivo(siglaPaisSelecionado + "/" + siglaEstadoSelecionado + ".txt");
        if( selecionarUnicaLinha() )
            return; // já invocou selecionarMunicipio();

        setTitle("Selecione o município");
        inputMunicipio.setVisibility(View.VISIBLE);
        tamanhoMaximoLista = 10;
        filtrarLista("");
        listView.smoothScrollToPosition(0);
        inputMunicipio.requestFocus();
    }

    // Devolve o município selecionado para a Activity que nos chamou
    private void selecionarMunicipio(Registro r) {
        Intent result = new Intent();
        result.putExtra(PAIS, paisSelecionado);
        result.putExtra(PAIS_SIGLA, siglaPaisSelecionado);
        result.putExtra(ESTADO, estadoSelecionado);
        result.putExtra(ESTADO_SIGLA, siglaEstadoSelecionado);
        result.putExtra(MUNICIPIO, r.descr);
        result.putExtra(MUNICIPIO_ID, Integer.parseInt(r.id));
        setResult(RESULT_OK, result);
        finish();
    }

    private void carregarArquivo(String arquivo) {
        linhas = new ArrayList<>();
        BufferedReader reader = null;
        try {
            String caminho = "municipios/" + arquivo;
//            Utils.info("Carregando arquivo " + caminho);
            InputStreamReader isr = new InputStreamReader(getAssets().open(caminho), "UTF-8");
            reader = new BufferedReader(isr);

//            long startMs = SystemClock.currentThreadTimeMillis();
            String line;
            while ((line = reader.readLine()) != null) {
                if( !line.isEmpty() )
                    linhas.add(line);
            }
//            long endMs = SystemClock.currentThreadTimeMillis();
//            long ms = endMs - startMs;
//            Utils.info(Integer.toString(linhas.size()) + " linhas carregadas em " + Long.toString(ms) + " ms");
        } catch (IOException e) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
    }

    // Marcar automaticamente quando tem apenas um registro (Países, Distrito Federal)
    private boolean selecionarUnicaLinha() {
        if( linhas.size() != 1 )
            return false;
        Registro r = extrairRegistro(linhas.get(0));
        selecionarRegistro(r);
        return true;
    }

    class Adaptador extends BaseAdapter
    {
        Activity activity;

        Adaptador(Activity activity) { this.activity = activity; }

        public int getCount() { return resultado.size(); }

        public Object getItem(int position) { return resultado.get(position); }

        public long getItemId(int position) { return position; }

        public View getView(final int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater=activity.getLayoutInflater();
            View row=inflater.inflate(R.layout.activity_selecionar_municipio_item, null);

            TextView tv	= (TextView) row.findViewById(R.id.title);

            tv.setText(resultado.get(position).descr);

            return row;
        }
    }

    class Registro {
        public String id;
        public String descr;

        public Registro(String id, String descr) {
            this.id = id;
            this.descr = descr;
        }
    }
}
