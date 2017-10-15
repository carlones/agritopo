package br.com.neogis.agritopo;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by carlo on 15/10/2017.
 */

public class PopupPonto extends PopupWindow {
    private final AutoCompleteTextView txtPontoTitulo;
    private final EditText txtPontoDescricao;
    private View customView;
    private String pontoTitulo;
    private String pontoDescricao;

    public PopupPonto(Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

        setCustomView(inflater.inflate(R.layout.popup_layer, null));
        setContentView(customView);

        if (Build.VERSION.SDK_INT >= 21) {
            setElevation(5.0f);
        }
        ImageButton btnPopupPontoFechar = (ImageButton) getCustomView().findViewById(R.id.btnPopupPontoFechar);
        ImageButton btnPopupPontoFecharTop = (ImageButton) getCustomView().findViewById(R.id.btnPopupPontoFecharTop);
        ImageButton btnPopupPontoFecharBottom = (ImageButton) getCustomView().findViewById(R.id.btnPopupPontoFecharBottom);
        ImageButton btnPopupPontoOK = (ImageButton) getCustomView().findViewById(R.id.btnPopupPontoOK);
        txtPontoTitulo = (AutoCompleteTextView) getCustomView().findViewById(R.id.txtPontoTitulo);
        txtPontoDescricao = (EditText) getCustomView().findViewById(R.id.txtPontoDescricao);

        btnPopupPontoFechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        btnPopupPontoFecharTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        btnPopupPontoFecharBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        btnPopupPontoOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPontoTitulo(txtPontoTitulo.getText().toString());
                setPontoDescricao(txtPontoDescricao.getText().toString());
                dismiss();
            }
        });
    }

    public String getPontoTitulo() {
        return pontoTitulo;
    }

    public void setPontoTitulo(String pontoTitulo) {
        this.pontoTitulo = pontoTitulo;
    }

    public String getPontoDescricao() {
        return pontoDescricao;
    }

    public void setPontoDescricao(String pontoDescricao) {
        this.pontoDescricao = pontoDescricao;
    }

    public View getCustomView() {
        return customView;
    }

    public void setCustomView(View customView) {
        this.customView = customView;
    }
}
