package br.com.neogis.agritopo.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.com.neogis.agritopo.R;
import br.com.neogis.agritopo.fragment.CamadasFragment;

import static br.com.neogis.agritopo.utils.Constantes.PEGAR_NOME_ARQUIVO_EXPORTAR_REQUEST;

/**
 * Created by carlo on 30/12/2017.
 */

public class SingleFragmentActivity extends AppCompatActivity {
    public final static String FRAGMENT_PARAM = "fragment";
    private CamadasFragment f;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_single_fragment);

        Bundle b = getIntent().getExtras();
        Class<?> fragmentClass = (Class<?>) b.get(FRAGMENT_PARAM);
        f = (CamadasFragment) Fragment.instantiate(this, CamadasFragment.class.getName());
        f.setArguments(b);
        getFragmentManager().beginTransaction().replace(R.id.fragment, f, fragmentClass.getName()).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PEGAR_NOME_ARQUIVO_EXPORTAR_REQUEST) {
            if (resultCode == RESULT_OK) {

            }
        }
    }

    @Override
    public void onBackPressed() {
        f.identificarSelecionados();
        super.onBackPressed();
    }
}