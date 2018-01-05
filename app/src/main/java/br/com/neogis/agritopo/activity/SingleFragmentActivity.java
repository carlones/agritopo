package br.com.neogis.agritopo.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.com.neogis.agritopo.R;

import static br.com.neogis.agritopo.dao.Constantes.PEGAR_NOME_ARQUIVO_KML_REQUEST;

/**
 * Created by carlo on 30/12/2017.
 */

public class SingleFragmentActivity extends AppCompatActivity {
    public final static String FRAGMENT_PARAM = "fragment";

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_single_fragment);

        Bundle b = getIntent().getExtras();
        Class<?> fragmentClass = (Class<?>) b.get(FRAGMENT_PARAM);
        if (bundle == null) {
            Fragment f = Fragment.instantiate(this, fragmentClass.getName());
            f.setArguments(b);
            getFragmentManager().beginTransaction().replace(R.id.fragment, f, fragmentClass.getName()).commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PEGAR_NOME_ARQUIVO_KML_REQUEST) {
            if (resultCode == RESULT_OK) {

            }
        }
    }
}