package br.com.neogis.agritopo.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import br.com.neogis.agritopo.BuildConfig;
import br.com.neogis.agritopo.R;
import br.com.neogis.agritopo.activity.ElementoDetailActivity;
import br.com.neogis.agritopo.dao.tabelas.Classe;
import br.com.neogis.agritopo.dao.tabelas.ClasseDao;
import br.com.neogis.agritopo.dao.tabelas.Elemento;
import br.com.neogis.agritopo.dao.tabelas.ElementoDao;
import br.com.neogis.agritopo.dao.tabelas.ElementoImagem;
import br.com.neogis.agritopo.dao.tabelas.Fabricas.FabricaClasseDao;
import br.com.neogis.agritopo.dao.tabelas.Fabricas.FabricaElementoDao;
import br.com.neogis.agritopo.dao.tabelas.Fabricas.FabricaTipoElementoDao;
import br.com.neogis.agritopo.dao.tabelas.TipoElemento;
import br.com.neogis.agritopo.dao.tabelas.TipoElementoDao;
import br.com.neogis.agritopo.model.MyGeoPoint;
import br.com.neogis.agritopo.singleton.Configuration;
import br.com.neogis.agritopo.utils.DateUtils;
import br.com.neogis.agritopo.utils.ImageUtils;

import static android.app.Activity.RESULT_OK;
import static br.com.neogis.agritopo.utils.Constantes.ARG_CLASSEID;
import static br.com.neogis.agritopo.utils.Constantes.ARG_ELEMENTOID;
import static br.com.neogis.agritopo.utils.Constantes.ARG_GEOMETRIA;

/**
 * A fragment representing a single Elemento detail screen.
 * This fragment is either contained in a {@link br.com.neogis.agritopo.activity.CadastrosListarActivity}
 * in two-pane mode (on tablets) or a {@link ElementoDetailActivity}
 * on handsets.
 */
public class ElementoDetailGeomFragment extends Fragment {
    private Elemento mItem;
    private EditText geometriaOriginal;
    private EditText geometriaLatitude;
    private EditText geometriaLongitude;
    private EditText geometriaAltitude;

    public ElementoDetailGeomFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ElementoDao elementoDao = FabricaElementoDao.Criar(getActivity().getBaseContext());
        int elementoId = getArguments().getInt(ARG_ELEMENTOID);
        if (elementoId != 0) {
            mItem = elementoDao.get(elementoId);
        } else {
            ClasseDao classeDao = FabricaClasseDao.Criar(getActivity().getBaseContext());
            Classe classe = classeDao.get(getArguments().getInt(ARG_CLASSEID, 0));
            TipoElemento te = new TipoElemento("");
            mItem = new Elemento(te, classe, "", "", getArguments().getString(ARG_GEOMETRIA));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_elemento_geom_detail, container, false);

        geometriaOriginal = (EditText) rootView.findViewById(R.id.geometriaOriginal);
        geometriaLatitude = (EditText) rootView.findViewById(R.id.geometriaLatitude);
        geometriaLongitude = (EditText) rootView.findViewById(R.id.geometriaLongitude);
        geometriaAltitude = (EditText) rootView.findViewById(R.id.geometriaAltitude);
        if (mItem != null) {
            geometriaOriginal.setText(Elemento.getInformacaoExtra(mItem));
            geometriaLatitude.setText(String.valueOf(mItem.getPontoCentral().getLatitude()));
            geometriaLongitude.setText(String.valueOf(mItem.getPontoCentral().getLongitude()));
            geometriaAltitude.setText(String.valueOf(mItem.getPontoCentral().getAltitude()));
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
    }

    public MyGeoPoint getGeometria() {
        MyGeoPoint geoPoint = new MyGeoPoint(
                Double.valueOf(geometriaLatitude.getText().toString()),
                Double.valueOf(geometriaLongitude.getText().toString()),
                Double.valueOf(geometriaAltitude.getText().toString())
        );
        return geoPoint;
    }
}
