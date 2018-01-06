package br.com.neogis.agritopo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.neogis.agritopo.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class ArvoreCamadasActivityFragment extends Fragment {

    public ArvoreCamadasActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_arvore_camadas, container, false);
    }
}
