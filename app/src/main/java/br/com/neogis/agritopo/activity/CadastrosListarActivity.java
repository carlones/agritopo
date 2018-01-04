package br.com.neogis.agritopo.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import br.com.neogis.agritopo.R;
import br.com.neogis.agritopo.dao.tabelas.Elemento;
import br.com.neogis.agritopo.dao.tabelas.ElementoDao;
import br.com.neogis.agritopo.dao.tabelas.ElementoDaoImpl;
import br.com.neogis.agritopo.fragment.CadastrosAbaFragment;

import static br.com.neogis.agritopo.dao.Constantes.ALTERAR_ELEMENTO_REQUEST;
import static br.com.neogis.agritopo.dao.Constantes.ARG_ELEMENTOID;

public class CadastrosListarActivity extends AppCompatActivity {

    public static final String ARG_CLASSEID = "ClasseIdAba";
    public ArrayList<Integer> idsSelecionados = new ArrayList<>();
    TabLayout abas;
    AbaAdapter abaAdapter;
    ViewPager viewPager;
    List<Elemento> elementos;
    private String[] titulosAbas = {"Tudo", "Pontos", "Áreas", "Distâncias"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cadastros_listar);

        carregarElementos();
        viewPager = (ViewPager) findViewById(R.id.pager);
        abaAdapter = new AbaAdapter(getSupportFragmentManager());
        viewPager.setAdapter(abaAdapter);
        abas = (TabLayout) findViewById(R.id.tabs);
        abas.setupWithViewPager(viewPager);

        int abrirNaAba = getIntent().getIntExtra(ARG_CLASSEID, 0);
        viewPager.setCurrentItem(abrirNaAba);
    }

    private void carregarElementos() {
        elementos = (new ElementoDaoImpl(getBaseContext()).getAll("elementoid DESC"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cadastros, menu);

        boolean mostrarBotaoRemover = !idsSelecionados.isEmpty();
        MenuItem botaoRemover = menu.findItem(R.id.menu_cadastros_acao_remover);
        botaoRemover.setVisible(mostrarBotaoRemover);
        return true;
    }

    public void removerElementosSelecionados(MenuItem item) {
        if( idsSelecionados.isEmpty() ) return;

        String mensagemAlerta = idsSelecionados.size() == 1
                ? "Remover esse item?"
                : "Remover " + Integer.toString(idsSelecionados.size()) + " itens?"
                ;
        AlertDialog confirmacao = new AlertDialog.Builder(this)
                .setMessage(mensagemAlerta)
                .setIcon(android.R.drawable.ic_menu_delete)
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Remover", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        ElementoDao dao = new ElementoDaoImpl(getBaseContext());
                        for(Integer id: idsSelecionados) {
                            Elemento e = dao.get(id);
                            dao.delete(e);
                        }

                        // Ocultar o botao de remover
                        idsSelecionados.clear();
                        invalidateOptionsMenu();

                        // Reconstruir conteúdo abas
                        carregarElementos();
                        abaAdapter.resetarAbas();

                        dialog.dismiss();
                    }

                })
                .create();
        confirmacao.show();
    }

    // Esses callbacks são acionados pelo fragment
    //
    public void onElementoMarcado(Elemento elemento) {
        idsSelecionados.add(elemento.getElementoid());
        invalidateOptionsMenu(); // exibir ícone remover
    }
    public void onElementoDesmarcado(Elemento elemento) {
        idsSelecionados.remove(Integer.valueOf(elemento.getElementoid()));
        invalidateOptionsMenu(); // ocultar ícone remover
    }
    public void onElementoClicado(Elemento elemento) {
        Intent intent = new Intent(getBaseContext(), ElementoDetailActivity.class);
        intent.putExtra(ARG_ELEMENTOID, elemento.getElementoid());
        startActivityForResult(intent, ALTERAR_ELEMENTO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( resultCode == RESULT_OK )
        {
            if (requestCode == ALTERAR_ELEMENTO_REQUEST)
            {
                int elementoId = data.getIntExtra(ARG_ELEMENTOID, -1);
                if( elementoId > -1 )
                {
                    // Reconstruir conteúdo abas
                    carregarElementos();
                    abaAdapter.resetarAbas();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // Fornece o Fragmento da aba
    //
    class AbaAdapter extends FragmentPagerAdapter {

        private final List<CadastrosAbaFragment> mFragmentList = new ArrayList<>();
        private String[] classePorPosicao = { "Tudo", "Ponto", "Area", "Distancia" };

        private AbaAdapter(FragmentManager manager) {
            super(manager);
            int posicao = 0;
            for (String titulo : titulosAbas) {
                CadastrosAbaFragment f = new CadastrosAbaFragment();
                f.titulo = titulo;
                f.classe = classePorPosicao[posicao];
                mFragmentList.add(f);
                posicao++;
            }
            setarConteudoAbas();
        }

        @Override
        public Fragment getItem(int position) { return mFragmentList.get(position); }

        @Override
        public int getCount() {
            return titulosAbas.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            CadastrosAbaFragment f = mFragmentList.get(position);
            return f.titulo + " (" + Integer.toString(f.elementosDaAba.size()) + ")";
        }

        private void resetarAbas() {
            setarConteudoAbas();
            redesenharAbas();
        }

        private void setarConteudoAbas() {
            for(CadastrosAbaFragment f: mFragmentList) {
                f.elementosDaAba.clear();
                for (Elemento e : elementos) {
                    if (f.classe.equals("Tudo") || e.getClasse().getNome().equals(f.classe))
                        f.elementosDaAba.add(e);
                }
            }
            // Após remover um elemento o título não é recriado automaticamente
            if( abas != null ) {
                for(int i=0; i < abas.getTabCount(); i++) {
                    abas.getTabAt(i).setText(getPageTitle(i));
                }
            }
        }

        private void redesenharAbas() {
            for(CadastrosAbaFragment f: mFragmentList) {
                f.redesenhar();
            }
        }
    }
}