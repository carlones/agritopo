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

import org.osmdroid.util.GeoPoint;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import br.com.neogis.agritopo.BuildConfig;
import br.com.neogis.agritopo.R;
import br.com.neogis.agritopo.activity.ElementoDetailActivity;
import br.com.neogis.agritopo.dao.tabelas.Classe;
import br.com.neogis.agritopo.dao.tabelas.ClasseDao;
import br.com.neogis.agritopo.dao.tabelas.ClasseDaoImpl;
import br.com.neogis.agritopo.dao.tabelas.Elemento;
import br.com.neogis.agritopo.dao.tabelas.ElementoDao;
import br.com.neogis.agritopo.dao.tabelas.ElementoDaoImpl;
import br.com.neogis.agritopo.dao.tabelas.ElementoImagem;
import br.com.neogis.agritopo.dao.tabelas.Fabricas.FabricaClasseDao;
import br.com.neogis.agritopo.dao.tabelas.Fabricas.FabricaElementoDao;
import br.com.neogis.agritopo.dao.tabelas.Fabricas.FabricaTipoElementoDao;
import br.com.neogis.agritopo.dao.tabelas.TipoElemento;
import br.com.neogis.agritopo.dao.tabelas.TipoElementoDao;
import br.com.neogis.agritopo.dao.tabelas.TipoElementoDaoImpl;
import br.com.neogis.agritopo.singleton.Configuration;
import br.com.neogis.agritopo.utils.DateUtils;
import br.com.neogis.agritopo.utils.ImageUtils;

import static android.app.Activity.RESULT_OK;
import static br.com.neogis.agritopo.utils.Constantes.ARG_CLASSEID;
import static br.com.neogis.agritopo.utils.Constantes.ARG_ELEMENTOID;
import static br.com.neogis.agritopo.utils.Constantes.ARG_GEOMETRIA;
import static br.com.neogis.agritopo.utils.Constantes.ARG_TIPOELEMENTOID;

/**
 * A fragment representing a single Elemento detail screen.
 * This fragment is either contained in a {@link br.com.neogis.agritopo.activity.CadastrosListarActivity}
 * in two-pane mode (on tablets) or a {@link ElementoDetailActivity}
 * on handsets.
 */
public class ElementoDetailFragment extends Fragment {
    private static final int REQUEST_IMAGE_MEDIA = 1;
    private static final int REQUEST_TAKE_PICTURE = 2;
    private ArrayList<String> listaImagens;
    private ArrayList<String> listaImagensExcluir;
    private Uri caminhoFoto;
    private Elemento mItem;
    private EditText elementoTitulo;
    private AutoCompleteTextView elementoTipoElemento;
    private EditText elementoDescricao;
    private EditText elementoInformacao;
    private EditText elementoDataCriacao;
    private EditText elementoDataModificacao;
    private LinearLayout elementoListaImagens;
    private ImageView imageTirarFoto;
    private ImageView imageAdicionarImagem;
    private AlertDialog alerta;

    public ElementoDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            listaImagens = savedInstanceState.getStringArrayList("imagens");
            listaImagensExcluir = savedInstanceState.getStringArrayList("imagensExcluir");
        }

        ElementoDao elementoDao = FabricaElementoDao.Criar(getActivity().getBaseContext());
        int elementoId = getArguments().getInt(ARG_ELEMENTOID);
        if (elementoId != 0) {
            mItem = elementoDao.get(elementoId);
            if (listaImagens.size() == 0 && mItem.getImages().size() > 0)
                for (ElementoImagem elementoImagem : mItem.getImages()) {
                    listaImagens.add(elementoImagem.getImagem().getArquivo());
                }
        } else {
            ClasseDao classeDao = FabricaClasseDao.Criar(getActivity().getBaseContext());
            Classe classe = classeDao.get(getArguments().getInt(ARG_CLASSEID, 0));
            //TipoElementoDao ted = FabricaTipoElementoDao.Criar(getActivity().getBaseContext());
            //TipoElemento te = ted.get(getArguments().getInt(ARG_TIPOELEMENTOID, 0));
            TipoElemento te = new TipoElemento("");
            mItem = new Elemento(te, classe, "", "", getArguments().getString(ARG_GEOMETRIA));
        }

        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(mItem.getClasse().getNome());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_elemento_detail, container, false);

        elementoTitulo = (EditText) rootView.findViewById(R.id.elementoTitulo);
        elementoTipoElemento = (AutoCompleteTextView) rootView.findViewById(R.id.elementoTipoElemento);
        elementoDescricao = (EditText) rootView.findViewById(R.id.elementoDescricao);
        elementoInformacao = (EditText) rootView.findViewById(R.id.elementoInformacao);
        elementoDataCriacao = (EditText) rootView.findViewById(R.id.elementoDataCriacao);
        elementoDataModificacao = (EditText) rootView.findViewById(R.id.elementoDataModificacao);
        elementoListaImagens = (LinearLayout) rootView.findViewById(R.id.elementoListaImagens);
        imageTirarFoto = (ImageView) rootView.findViewById(R.id.elemento_botao_tirar_foto);
        imageAdicionarImagem = (ImageView) rootView.findViewById(R.id.elemento_botao_adicionar_foto);

        TipoElementoDao tipoElementoDao = FabricaTipoElementoDao.Criar(rootView.getContext());
        List<TipoElemento> listaTipoElemento = tipoElementoDao.getAll();
        List<String> nomesTipoElemento = new ArrayList<>();
        for (TipoElemento te : listaTipoElemento) {
            nomesTipoElemento.add(te.getNome());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                rootView.getContext(),
                android.R.layout.simple_dropdown_item_1line,
                nomesTipoElemento
        );
        elementoTipoElemento.setAdapter(adapter);
        elementoTipoElemento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                elementoTipoElemento.showDropDown();
            }
        });

        if (mItem != null) {
            elementoTitulo.setText(mItem.getTitulo());
            elementoTipoElemento.setText(mItem.getTipoElemento().getNome());
            elementoDescricao.setText(mItem.getDescricao());
            elementoDataCriacao.setText(mItem.getCreated_at());
            elementoDataModificacao.setText(mItem.getModified_at());
            elementoInformacao.setKeyListener(null);
            elementoInformacao.setText(Elemento.getInformacaoExtra(mItem));

            for (String image : listaImagens) {
                adicionarImagem(Uri.parse(image));
            }
        }

        imageTirarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                caminhoFoto = FileProvider.getUriForFile(getActivity().getBaseContext(),
                        BuildConfig.APPLICATION_ID + ".utils.MyFileProvider",
                        new File(
                        Configuration.getInstance().DiretorioFotos + File.separator +
                                Long.toString(DateUtils.getCurrentDateTime().getTime()) + ".jpg")
                );
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                List<ResolveInfo> resolvedIntentActivities = getActivity().getBaseContext().getPackageManager().queryIntentActivities(takePicture, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
                    String packageName = resolvedIntentInfo.activityInfo.packageName;
                    getActivity().getBaseContext().grantUriPermission(packageName, caminhoFoto, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, caminhoFoto);
                startActivityForResult(takePicture, REQUEST_TAKE_PICTURE);
            }
        });

        imageAdicionarImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , REQUEST_IMAGE_MEDIA);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            caminhoFoto = savedInstanceState.getParcelable("caminhoFoto");
            listaImagens = savedInstanceState.getStringArrayList("imagens");
            listaImagensExcluir = savedInstanceState.getStringArrayList("imagensExcluir");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("caminhoFoto", caminhoFoto);
        outState.putStringArrayList("imagens", listaImagens);
        outState.putStringArrayList("imagensExcluir", listaImagensExcluir);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case REQUEST_IMAGE_MEDIA:{
                if(resultCode == RESULT_OK){
                    Uri uri = imageReturnedIntent.getData();
                    //GeoPoint pontoCentral = mItem.getPontoCentral();
                    //ImageUtils.writeLocation(getActivity().getBaseContext(), uri, pontoCentral.getLatitude(), pontoCentral.getLongitude());
                    adicionarImagem(uri);
                    listaImagens.add(uri.toString());
                }
                break;
            }
            case REQUEST_TAKE_PICTURE:{
                if(resultCode == RESULT_OK){
                   // GeoPoint pontoCentral = mItem.getPontoCentral();
                    //ImageUtils.writeLocation(getActivity().getBaseContext(), caminhoFoto, pontoCentral.getLatitude(), pontoCentral.getLongitude());
                    adicionarImagem(caminhoFoto);
                    listaImagens.add(caminhoFoto.toString());
                }
                break;
            }
        }
    }

    private void adicionarImagem(final Uri uri){
        Bitmap preview_bitmap = ImageUtils.createPreviewImage(getActivity().getBaseContext(), uri,160);
        ImageView image = new ImageView(getActivity().getBaseContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        image.setLayoutParams(params);
        image.setImageBitmap(preview_bitmap);
        image.setPadding(2,2,2,2);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "image/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
            }
        });

        image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getResources().getString(R.string.alerta));
                builder.setMessage(getResources().getString(R.string.pergunta_deseja_excluir));
                builder.setPositiveButton(getResources().getString(R.string.sim), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        v.setVisibility(View.GONE);
                        for (String img : listaImagens)
                            if (img.equals(uri.toString())) {
                                listaImagensExcluir.add(img);
                                listaImagens.remove(img);
                                break;
                            }
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.nao), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
                alerta = builder.create();
                alerta.show();
                return true;
            }
        });

        elementoListaImagens.addView(image);
    }

    private Bitmap decodeFile(Uri selectedImage) {
        // Decode image size
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        try {
            InputStream fis = getActivity().getBaseContext().getContentResolver().openInputStream(selectedImage);
            BitmapFactory.decodeStream(fis, null, options);
            fis.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE=160;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(options.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    options.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            // Decode with inSampleSize
            options = new BitmapFactory.Options();
            options.inSampleSize = scale;
            fis = getActivity().getBaseContext().getContentResolver().openInputStream(selectedImage);
            Bitmap result = BitmapFactory.decodeStream(fis, null, options);
            fis.close();

            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public String getTitulo() {
        return elementoTitulo.getText().toString();
    }

    public String getDescricao() {
        return elementoDescricao.getText().toString();
    }

    public String getNomeTipoElemento() {
        return elementoTipoElemento.getText().toString();
    }

    public Elemento getElemento() {
        return mItem;
    }

    public ArrayList<String> getListaImagens() {
        return listaImagens;
    }

    public void setListaImagens(ArrayList<String> imageList) {
        if(imageList == null)
            return;

        listaImagens = imageList;
    }

    public ArrayList<String> getListaImagensExcluir() {
        return listaImagensExcluir;
    }

    public void setListaImagensExcluir(ArrayList<String> listaImagensExcluir) {
        this.listaImagensExcluir = listaImagensExcluir;
    }
}
