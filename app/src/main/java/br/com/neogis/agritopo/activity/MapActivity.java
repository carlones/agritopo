package br.com.neogis.agritopo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.apache.commons.io.FilenameUtils;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.MapTileProviderArray;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.modules.IArchiveFile;
import org.osmdroid.tileprovider.modules.MBTilesFileArchive;
import org.osmdroid.tileprovider.modules.MapTileFileArchiveProvider;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;

import br.com.neogis.agritopo.BuildConfig;
import br.com.neogis.agritopo.R;
import br.com.neogis.agritopo.controller.MapFile;
import br.com.neogis.agritopo.controller.MapFileController;
import br.com.neogis.agritopo.dao.tabelas.ClasseEnum;
import br.com.neogis.agritopo.dao.tabelas.Elemento;
import br.com.neogis.agritopo.dao.tabelas.ElementoDao;
import br.com.neogis.agritopo.dao.tabelas.Fabricas.FabricaElementoDao;
import br.com.neogis.agritopo.fragment.CamadasFragment;
import br.com.neogis.agritopo.holder.AdicionarAreaHolder;
import br.com.neogis.agritopo.holder.AdicionarDistanciaHolder;
import br.com.neogis.agritopo.holder.AdicionarElementoHolder;
import br.com.neogis.agritopo.holder.AdicionarPontoHolder;
import br.com.neogis.agritopo.holder.MeuLocalHolder;
import br.com.neogis.agritopo.model.Area;
import br.com.neogis.agritopo.model.ArvoreCamada;
import br.com.neogis.agritopo.model.Distancia;
import br.com.neogis.agritopo.model.InfoBox;
import br.com.neogis.agritopo.model.MapaTiles;
import br.com.neogis.agritopo.model.MyGeoPoint;
import br.com.neogis.agritopo.model.MyGpsMyLocationProvider;
import br.com.neogis.agritopo.model.Ponto;
import br.com.neogis.agritopo.runnable.CamadasLoader;
import br.com.neogis.agritopo.singleton.CamadaHolder;
import br.com.neogis.agritopo.singleton.Licenca;
import br.com.neogis.agritopo.utils.AsyncResponse;
import br.com.neogis.agritopo.utils.DateUtils;
import br.com.neogis.agritopo.utils.IGPSListener;
import br.com.neogis.agritopo.utils.Utils;

import static br.com.neogis.agritopo.utils.Constantes.ALTERAR_ELEMENTO_REQUEST;
import static br.com.neogis.agritopo.utils.Constantes.ARG_CLASSEID;
import static br.com.neogis.agritopo.utils.Constantes.ARG_ELEMENTOID;
import static br.com.neogis.agritopo.utils.Constantes.ARG_ELEMENTO_CENTRALIZAR;
import static br.com.neogis.agritopo.utils.Constantes.ARG_EXPORTAR_NOME_ARQUIVO;
import static br.com.neogis.agritopo.utils.Constantes.ARG_EXPORTAR_TIPO_ARQUIVO;
import static br.com.neogis.agritopo.utils.Constantes.ARG_GEOMETRIA;
import static br.com.neogis.agritopo.utils.Constantes.ARG_GPS_POSICAO;
import static br.com.neogis.agritopo.utils.Constantes.ARG_IMPORTAR_NOME_ARQUIVO;
import static br.com.neogis.agritopo.utils.Constantes.ARG_LICENCA_TIPO;
import static br.com.neogis.agritopo.utils.Constantes.ARG_MAPA_ID;
import static br.com.neogis.agritopo.utils.Constantes.ARG_MAPA_LATITUDEATUAL;
import static br.com.neogis.agritopo.utils.Constantes.ARG_MAPA_LONGITUDEATUAL;
import static br.com.neogis.agritopo.utils.Constantes.ARG_MAPA_MODO;
import static br.com.neogis.agritopo.utils.Constantes.ARG_MAPA_ZOOMINICIAL;
import static br.com.neogis.agritopo.utils.Constantes.ARG_TIPOELEMENTOID;
import static br.com.neogis.agritopo.utils.Constantes.LICENCA_GRATUITA_LIMITE_ELEMENTOS;
import static br.com.neogis.agritopo.utils.Constantes.MY_PERMISSIONS_ACCESS_COARSE_LOCATION;
import static br.com.neogis.agritopo.utils.Constantes.MY_PERMISSIONS_ACCESS_FINE_LOCATION;
import static br.com.neogis.agritopo.utils.Constantes.OFFLINE;
import static br.com.neogis.agritopo.utils.Constantes.PEGAR_ELEMENTO_AREA_REQUEST;
import static br.com.neogis.agritopo.utils.Constantes.PEGAR_ELEMENTO_DISTANCIA_REQUEST;
import static br.com.neogis.agritopo.utils.Constantes.PEGAR_ELEMENTO_PONTO_REQUEST;
import static br.com.neogis.agritopo.utils.Constantes.PEGAR_MENU_CADASTROS_REQUEST;
import static br.com.neogis.agritopo.utils.Constantes.PEGAR_MENU_CAMADAS_REQUEST;
import static br.com.neogis.agritopo.utils.Constantes.PEGAR_NOME_ARQUIVO_EXPORTAR_REQUEST;
import static br.com.neogis.agritopo.utils.Constantes.PEGAR_NOME_ARQUIVO_IMPORTAR_REQUEST;
import static br.com.neogis.agritopo.utils.Constantes.RECARREGAR_LICENCA;

public class MapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IGPSListener, MapEventsReceiver, MapView.OnFirstLayoutListener {

    private BoundingBox mInitialBoundingBox = null;
    private AdicionarElementoHolder modalAtivo;
    private MeuLocalHolder meuLocalHolder;

    private List<Area> areaList;
    private List<Distancia> distanciaList;
    private List<Ponto> pontoList;
    private CamadaHolder camadaHolder;
    private MyLocationNewOverlay mMyLocationNewOverlay;
    private CompassOverlay mCompassOverlay;
    private RotationGestureOverlay mRotationGestureOverlay;
    private MapView map;
    //Outros
    private MapFileController mapFileController;
    //Interface
    private FloatingActionMenu famNovo;
    private Context mContext;
    private Activity mActivity;
    private FloatingActionButton fabNovoPonto, fabNovaArea, fabNovaDistancia, fabCamadas, fabGPS, fabCompartilhar, fabRotacao, fabConcluido, fabCancelar, fabDesfazer, fabSeguirGPS;
    private PopupWindow popupLayers;
    private ConstraintLayout layoutTelaPrincipal;
    private boolean exibirPontos;
    private boolean exibirAreas;
    private boolean exibirDistancias;

    private IGeoPoint coordenadasIniciais;
    private int zoomInicial = 0;
    private IGeoPoint mapaPontoCentral;

    private IMapController mapController;

    // Mostra área, perímetro e distância enquando desenha Área e Distância
    private InfoBox infoBox;
    private InfoBox gpsBox;
    private Licenca.LicencaTipo licencaTipo;

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        layoutTelaPrincipal = (ConstraintLayout) findViewById(R.id.content_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        infoBox = new InfoBox((android.widget.TextView) findViewById(R.id.info_box));
        gpsBox = new InfoBox((android.widget.TextView) findViewById(R.id.gps_box));

        br.com.neogis.agritopo.singleton.Configuration.getInstance().LoadConfiguration(getApplicationContext());

        criarDiretorio("agritopo");
        criarDiretorio("OsmDroid");

        licencaTipo = Licenca.LicencaTipo.valueOf(getIntent().getStringExtra(ARG_LICENCA_TIPO));
        mapFileController = new MapFileController(licencaTipo == Licenca.LicencaTipo.Gratuito);
        mapFileController.LoadMaps();

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);
        else
            onRestoreActivity();

        //MyMarker.ENABLE_TEXT_LABELS_WHEN_NO_IMAGE = true;

        mContext = getApplicationContext();
        mActivity = MapActivity.this;

        exibirAreas = true;
        areaList = new ArrayList<>();
        exibirDistancias = true;
        distanciaList = new ArrayList<>();
        exibirPontos = true;
        pontoList = new ArrayList<>();
        camadaHolder = CamadaHolder.getInstance();

        inicializarMapas(getIntent().getIntExtra(ARG_MAPA_MODO, OFFLINE) == OFFLINE);
        inicializarBotoes();

        carregarCamadas();
    }

    private boolean criarDiretorio(String diretorio) {
        File root = Environment.getRootDirectory().getAbsoluteFile();
        File dir = new File(root, diretorio);
        if (!dir.exists())
            dir.mkdirs();
        return dir.exists();
    }

    private void inicializarBotoes() {
        famNovo = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        fabNovoPonto = (FloatingActionButton) findViewById(R.id.action_novo_ponto);
        fabNovaArea = (FloatingActionButton) findViewById(R.id.action_nova_area);
        fabNovaDistancia = (FloatingActionButton) findViewById(R.id.action_nova_distancia);
        fabGPS = (FloatingActionButton) findViewById(R.id.action_gps);
        fabCamadas = (FloatingActionButton) findViewById(R.id.action_layer);
        fabCompartilhar = (FloatingActionButton) findViewById(R.id.action_share);
        //fabRotacao = (FloatingActionButton) findViewById(R.id.action_rotacao);
        fabConcluido = (FloatingActionButton) findViewById(R.id.action_concluido);
        fabCancelar = (FloatingActionButton) findViewById(R.id.action_cancelar);
        fabDesfazer = (FloatingActionButton) findViewById(R.id.action_desfazer);
        fabSeguirGPS = (FloatingActionButton) findViewById(R.id.action_seguir_gps);

        fabNovoPonto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if ((licencaTipo == Licenca.LicencaTipo.Gratuito) && (areaList.size() + distanciaList.size() + pontoList.size() > LICENCA_GRATUITA_LIMITE_ELEMENTOS)) {
                    Utils.alert(MapActivity.this, getString(R.string.ALERTA_VERSAO_GRATUITA_TITULO), "Você atingiu o limite de " + LICENCA_GRATUITA_LIMITE_ELEMENTOS + " elementos da versão gratuita.", getString(R.string.ALERTA_VERSAO_GRATUITA_BOTAO));
                } else {
                    if (modalAtivo != null) return;
                    modalAtivo = new AdicionarPontoHolder(map, mActivity);
                    mostrarBotoesModal();
                }
            }
        });
        fabNovaArea.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if ((licencaTipo == Licenca.LicencaTipo.Gratuito) && (areaList.size() + distanciaList.size() + pontoList.size() > LICENCA_GRATUITA_LIMITE_ELEMENTOS)) {
                    Utils.alert(MapActivity.this, getString(R.string.ALERTA_VERSAO_GRATUITA_TITULO), "Você atingiu o limite de " + LICENCA_GRATUITA_LIMITE_ELEMENTOS + " elementos da versão gratuita.", getString(R.string.ALERTA_VERSAO_GRATUITA_BOTAO));
                } else {
                    if (modalAtivo != null) return;
                    modalAtivo = new AdicionarAreaHolder(map, mActivity, infoBox);
                    mostrarBotoesModal();
                }
            }
        });
        fabNovaDistancia.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if ((licencaTipo == Licenca.LicencaTipo.Gratuito) && (areaList.size() + distanciaList.size() + pontoList.size() > LICENCA_GRATUITA_LIMITE_ELEMENTOS)) {
                    Utils.alert(MapActivity.this, getString(R.string.ALERTA_VERSAO_GRATUITA_TITULO), "Você atingiu o limite de " + LICENCA_GRATUITA_LIMITE_ELEMENTOS + " elementos da versão gratuita.", getString(R.string.ALERTA_VERSAO_GRATUITA_BOTAO));
                } else {
                    if (modalAtivo != null) return;
                    modalAtivo = new AdicionarDistanciaHolder(map, mActivity, infoBox);
                    mostrarBotoesModal();
                }
            }
        });
        fabGPS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                /* Teste chave licenciamento vencida
                LicencaDaoImpl LicencaDao = new LicencaDaoImpl(getBaseContext());
                List<Licenca> list = LicencaDao.getAll();
                for (Licenca Licenca: list) {
                    if ((Licenca.getTipo() == Licenca.LicencaTipo.Pago) || (Licenca.getTipo() == Licenca.LicencaTipo.Pago_Standalone)) {
                        try {
                            Licenca.setDataexpiracao(new SimpleDateFormat( "yyyyMMdd" ).parse( "20181209" ));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        LicencaDao.save(Licenca);
                        Utils.toast(getBaseContext(), Licenca.getChave() + "-" + Licenca.getDataexpiracao().toString());
                        break;
                    }
                }*/

                if (mMyLocationNewOverlay.isFollowLocationEnabled()) {
                    mMyLocationNewOverlay.disableMyLocation();
                    mMyLocationNewOverlay.disableFollowLocation();
                    ((FloatingActionButton) v).setImageResource(R.drawable.ic_gps_not_fixed_white_24dp);
                    if (meuLocalHolder != null) {
                        meuLocalHolder.finalizar();
                        meuLocalHolder = null;
                    }
                } else {
                    if (!mMyLocationNewOverlay.isMyLocationEnabled()) // evitar stop/start do locationProvider só para centrar o mapa
                        mMyLocationNewOverlay.enableMyLocation();
                    mMyLocationNewOverlay.enableFollowLocation();
                    ((FloatingActionButton) v).setImageResource(R.drawable.ic_gps_fixed_white_24dp);
                    if (mMyLocationNewOverlay.getMyLocation() != null)
                        (map.getController()).setCenter(mMyLocationNewOverlay.getMyLocation());
                    if (meuLocalHolder == null) {
                        meuLocalHolder = new MeuLocalHolder(map, mMyLocationNewOverlay, gpsBox);
                    }
                }
            }
        });
        fabCamadas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Initialize a new instance of LayoutInflater service
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

                // Inflate the custom layoutTelaPrincipal/view
                View customView = inflater.inflate(R.layout.popup_layer, null);
                popupLayers = new PopupWindow(
                        customView,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );

                // Get a reference for the custom view close button
                ImageButton btnPopupLayerFechar = (ImageButton) customView.findViewById(R.id.btnPopupLayerFechar);
                ImageButton btnPopupLayerFecharTop = (ImageButton) customView.findViewById(R.id.btnPopupLayerFecharTop);
                ImageButton btnElementoArea = (ImageButton) customView.findViewById(R.id.btnElementoArea);
                ImageButton btnElementoDistancia = (ImageButton) customView.findViewById(R.id.btnElementoDistancia);
                ImageButton btnElementoPonto = (ImageButton) customView.findViewById(R.id.btnElementoPonto);
                CheckBox cbxAreas = (CheckBox) customView.findViewById(R.id.cbxAreas);
                CheckBox cbxDistancias = (CheckBox) customView.findViewById(R.id.cbxDistancias);
                CheckBox cbxPontos = (CheckBox) customView.findViewById(R.id.cbxPontos);

                cbxPontos.setChecked(exibirPontos);
                cbxAreas.setChecked(exibirAreas);
                cbxDistancias.setChecked(exibirDistancias);

                cbxPontos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        exibirPontos = isChecked;
                        for (Ponto ponto : pontoList) {
                            if (isChecked)
                                ponto.desenharEm(map);
                            else
                                ponto.removerDe(map);
                        }
                        map.invalidate();
                    }
                });
                cbxAreas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        exibirAreas = isChecked;
                        for (Area area : areaList) {
                            if (isChecked)
                                area.desenharEm(map);
                            else
                                area.removerDe(map);
                        }
                        map.invalidate();
                    }
                });
                cbxDistancias.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        exibirDistancias = isChecked;
                        for (Distancia distancia : distanciaList) {
                            if (isChecked)
                                distancia.desenharEm(map);
                            else
                                distancia.removerDe(map);
                        }
                        map.invalidate();
                    }
                });
                btnPopupLayerFechar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupLayers.dismiss();
                    }
                });
                btnPopupLayerFecharTop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupLayers.dismiss();
                    }
                });
                btnElementoArea.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupLayers.dismiss();
                        Intent intent = new Intent(getBaseContext(), CadastrosListarActivity.class);
                        intent.putExtra(ARG_CLASSEID, 2);
                        intent.putExtra(ARG_GPS_POSICAO, (new MyGeoPoint(map.getMapCenter())).toString());
                        startActivityForResult(intent, PEGAR_MENU_CADASTROS_REQUEST);
                    }
                });
                btnElementoDistancia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupLayers.dismiss();
                        Intent intent = new Intent(getBaseContext(), CadastrosListarActivity.class);
                        intent.putExtra(ARG_CLASSEID, 3);
                        intent.putExtra(ARG_GPS_POSICAO, (new MyGeoPoint(map.getMapCenter())).toString());
                        startActivityForResult(intent, PEGAR_MENU_CADASTROS_REQUEST);
                    }
                });
                btnElementoPonto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupLayers.dismiss();
                        Intent intent = new Intent(getBaseContext(), CadastrosListarActivity.class);
                        intent.putExtra(ARG_CLASSEID, 1);
                        intent.putExtra(ARG_GPS_POSICAO, (new MyGeoPoint(map.getMapCenter())).toString());
                        startActivityForResult(intent, PEGAR_MENU_CADASTROS_REQUEST);
                    }
                });
                /*
                    public void showAtLocation (View parent, int gravity, int x, int y)
                        Display the content view in a popup window at the specified location. If the
                        popup window cannot fit on screen, it will be clipped.
                        Learn WindowManager.LayoutParams for more information on how gravity and the x
                        and y parameters are related. Specifying a gravity of NO_GRAVITY is similar
                        to specifying Gravity.LEFT | Gravity.TOP.

                    Parameters
                        parent : a parent view to get the getWindowToken() token from
                        gravity : the gravity which controls the placement of the popup window
                        x : the popup's x location offset
                        y : the popup's y location offset
                */
                popupLayers.showAtLocation(layoutTelaPrincipal, Gravity.FILL, 0, 0);
            }
        });
        /*fabRotacao.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mRotationGestureOverlay.isEnabled()) {
                    ((FloatingActionButton) v).setImageResource(R.drawable.ic_rotation_blocked_white_24dp);
                    mRotationGestureOverlay.setEnabled(false);
                } else {
                    ((FloatingActionButton) v).setImageResource(R.drawable.ic_rotation_white_24dp);
                    mRotationGestureOverlay.setEnabled(true);
                }
            }
        });*/
        fabCompartilhar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (licencaTipo == Licenca.LicencaTipo.Gratuito)
                    Utils.alert(MapActivity.this, getString(R.string.ALERTA_VERSAO_GRATUITA_TITULO), getString(R.string.ALERTA_VERSAO_GRATUITA_MENSAGEM), getString(R.string.ALERTA_VERSAO_GRATUITA_BOTAO));
                else {
                    try {
                        File imageFile = new File(br.com.neogis.agritopo.singleton.Configuration.getInstance().DiretorioFotos + File.separator + Long.toString(DateUtils.getCurrentDateTime().getTime()) + ".jpg");
                        Uri caminhoFoto = FileProvider.getUriForFile(getBaseContext(), BuildConfig.APPLICATION_ID + ".utils.MyFileProvider", imageFile);
                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        List<ResolveInfo> resolvedIntentActivities = getBaseContext().getPackageManager().queryIntentActivities(takePicture, PackageManager.MATCH_DEFAULT_ONLY);
                        for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
                            String packageName = resolvedIntentInfo.activityInfo.packageName;
                            getBaseContext().grantUriPermission(packageName, caminhoFoto, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }
                        View v1 = getWindow().getDecorView().getRootView();
                        v1.setDrawingCacheEnabled(true);
                        Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
                        v1.setDrawingCacheEnabled(false);

                        FileOutputStream outputStream = new FileOutputStream(imageFile);
                        int quality = 100;
                        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                        outputStream.flush();
                        outputStream.close();
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("image/jpeg");
                        share.putExtra(Intent.EXTRA_STREAM, caminhoFoto);
                        startActivity(Intent.createChooser(share, "Compartilhar"));
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        fabConcluido.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                modalAtivo.finalizar();
                modalAtivo = null;
                ocultarBotoesModal();
            }
        });
        fabCancelar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                modalAtivo.cancelar();
                modalAtivo = null;
                ocultarBotoesModal();
            }
        });
        fabDesfazer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                modalAtivo.desfazer();
                map.invalidate();
            }
        });
        fabSeguirGPS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (modalAtivo.seguindoGPS()) {
                    modalAtivo.pararSeguirGPS();
                } else {
                    modalAtivo.seguirGPS();
                    if (!mMyLocationNewOverlay.isFollowLocationEnabled()) {
                        fabGPS.callOnClick();
                    }
                }
                setarIconeBotaoSeguirGPS();
            }
        });
    }

    private void setarIconeBotaoSeguirGPS() {
        fabSeguirGPS.setImageResource(modalAtivo.seguindoGPS() ? android.R.drawable.ic_media_pause : R.drawable.ic_near_me_white_24dp);
    }

    private void ocultarBotoesModal() {
        fabCancelar.setVisibility(View.INVISIBLE);
        fabConcluido.setVisibility(View.INVISIBLE);
        fabDesfazer.setVisibility(View.INVISIBLE);
        fabSeguirGPS.setVisibility(View.INVISIBLE);
        famNovo.setVisibility(View.VISIBLE);
        map.invalidate();
    }

    private void mostrarBotoesModal() {
        famNovo.close(false);
        famNovo.setVisibility(View.INVISIBLE);
        fabConcluido.setVisibility(View.VISIBLE);
        fabCancelar.setVisibility(View.VISIBLE);
        if (modalAtivo.aceitaDesfazer) {
            fabDesfazer.setVisibility(View.VISIBLE);
        }
        if (modalAtivo.aceitaSeguirGps) {
            setarIconeBotaoSeguirGPS();
            fabSeguirGPS.setVisibility(View.VISIBLE);
        }
    }

    private void inicializarMapas(boolean modoOffline) {
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        Configuration.getInstance().setMapViewHardwareAccelerated(true);
        map = (MapView) findViewById(R.id.map);

        map.setBuiltInZoomControls(false);
        map.setMultiTouchControls(true);
        map.setTilesScaledToDpi(true);

        mRotationGestureOverlay = new RotationGestureOverlay(map);
        mRotationGestureOverlay.setEnabled(false);
        map.getOverlays().add(mRotationGestureOverlay);

        ScaleBarOverlay mScaleBarOverlay = new ScaleBarOverlay(map);
        mScaleBarOverlay.setCentred(true);
        mScaleBarOverlay.setScaleBarOffset(Utils.getDisplaySize(this).x / 2, 10);
        map.getOverlays().add(mScaleBarOverlay);

        mCompassOverlay = new CompassOverlay(this, map);
        mCompassOverlay.enableCompass();
        map.getOverlays().add(mCompassOverlay);

        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this);
        map.getOverlays().add(0, mapEventsOverlay);

        mMyLocationNewOverlay = new MyLocationNewOverlay(new MyGpsMyLocationProvider(ctx, this), map);
        mMyLocationNewOverlay.enableMyLocation();
        map.getOverlays().add(mMyLocationNewOverlay);

        carregarPontos();
        carregarAreas();
        carregarDistancias();
        Configuration.getInstance().setDebugMode(false);

        if (mapFileController.ContainsMaps() && modoOffline) {
            if (mapFileController.IsOnline())
                mapFileController.SetSelectedMap(1);

            carregarMapaDeArquivo(mapFileController.GetMapFileSelected());
        } else {
            carregarMapaOnline();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_menu_kml:
                onOptionMenuKMLSelected(item);
                break;
            case R.id.action_menu_raster:
                onOptionMenuRasterSelected(item);
                break;
            case R.id.action_recentralizar:
                mapController.setCenter(mapaPontoCentral);
                break;
        }
        map.invalidate();
        return super.onOptionsItemSelected(item);
    }

    private void onOptionMenuKMLSelected(MenuItem item) {
        final View menuItemView = findViewById(R.id.action_menu_kml);
        PopupMenu popupMenu = new PopupMenu(this, menuItemView);
        popupMenu.inflate(R.menu.principal_kml);
        for (ArvoreCamada camada : camadaHolder.camadas) {
            popupMenu.getMenu().add(0, camada.indice, 0, camada.nome)
                    .setCheckable(true)
                    .setChecked(camada.TemAlgumItemSelecionado());
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                item.setChecked(!item.isChecked());
                ArvoreCamada camada = camadaHolder.camadas.get(item.getItemId());
                camadaHolder.marcarDesmarcarSelecaoArvore(camada, item.isChecked());
                camadaHolder.exibirCamadasSelecionadasNoMapa(camada, map);
                item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
                item.setActionView(menuItemView);
                item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        return false;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        return false;
                    }
                });
                return false;
            }
        });
        popupMenu.show();
    }

    private void onOptionMenuRasterSelected(MenuItem item) {
        final View menuItemView = findViewById(R.id.action_menu_raster);
        PopupMenu popupMenu = new PopupMenu(this, menuItemView);
        popupMenu.inflate(R.menu.principal_raster);

        if (mapFileController.ContainsMaps()) {
            for (MapFile mapFile : mapFileController.Maps) {
                popupMenu.getMenu()
                        .add(0, mapFile.getIndex(), 0, mapFile.getName())
                        .setCheckable(true)
                        .setChecked(mapFile.getSelected());
            }
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                item.setChecked(!item.isChecked());
                int id = item.getItemId();
                coordenadasIniciais = map.getMapCenter();
                zoomInicial = map.getZoomLevel();
                if (id == 0) {
//                    recarregarAppComMapaOnline();
                    carregarMapaOnline();
                } else {
                    carregarMapaDeArquivo(mapFileController.GetMapFile(id));
                }
                map.invalidate();
                return true;
            }
        });

        popupMenu.show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_cadastros: {
                Intent intent = new Intent(this, CadastrosListarActivity.class);
                intent.putExtra(ARG_GPS_POSICAO, (new MyGeoPoint(map.getMapCenter())).toString());
                startActivityForResult(intent, PEGAR_MENU_CADASTROS_REQUEST);
                break;
            }
            case R.id.nav_camadas: {
                Class<?> clazz = CamadasFragment.class;
                Intent intent = new Intent(this, SingleFragmentActivity.class);
                intent.putExtra(SingleFragmentActivity.FRAGMENT_PARAM, clazz);
                startActivityForResult(intent, PEGAR_MENU_CAMADAS_REQUEST);
                break;
            }
            case R.id.nav_exportar: {
                if (licencaTipo == Licenca.LicencaTipo.Gratuito)
                    Utils.alert(MapActivity.this, getString(R.string.ALERTA_VERSAO_GRATUITA_TITULO), getString(R.string.ALERTA_VERSAO_GRATUITA_MENSAGEM), getString(R.string.ALERTA_VERSAO_GRATUITA_BOTAO));
                else {
                    Intent intent = new Intent(this, ExportarActivity.class);
                    startActivityForResult(intent, PEGAR_NOME_ARQUIVO_EXPORTAR_REQUEST);
                }
                break;
            }
            case R.id.nav_importar: {
                if (licencaTipo == Licenca.LicencaTipo.Gratuito)
                    Utils.alert(MapActivity.this, getString(R.string.ALERTA_VERSAO_GRATUITA_TITULO), getString(R.string.ALERTA_VERSAO_GRATUITA_MENSAGEM), getString(R.string.ALERTA_VERSAO_GRATUITA_BOTAO));
                else {
                    Intent intent = new Intent(this, ImportarActivity.class);
                    startActivityForResult(intent, PEGAR_NOME_ARQUIVO_IMPORTAR_REQUEST);
                }
                break;
            }
            case R.id.nav_configuracao: {
                Intent intent = new Intent(this, ConfiguracaoActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_sobre: {
                Intent intent = new Intent(this, SobreActivity.class);
                startActivityForResult(intent, RECARREGAR_LICENCA);
                break;
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (mMyLocationNewOverlay.isMyLocationEnabled() && modalAtivo != null) {
            modalAtivo.locationChanged(location);
        }
    }

    @Override
    public void startLocationProvider() {
    }

    @Override
    public void stopLocationProvider() {
        if (modalAtivo != null && modalAtivo.aceitaSeguirGps && modalAtivo.seguindoGPS()) {
            modalAtivo.pararSeguirGPS();
            setarIconeBotaoSeguirGPS();
        }
    }

    private void carregarMapaOnline() {
        mapFileController.SetOnline();
        map.setUseDataConnection(true);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setTileProvider(new MapTileProviderBasic(getBaseContext()));
        if (mMyLocationNewOverlay.getMyLocation() != null)
            mapaPontoCentral = mMyLocationNewOverlay.getMyLocation();
        else
            mapaPontoCentral = map.getMapCenter();
        coordenadasIniciais = (coordenadasIniciais == null ? mapaPontoCentral : coordenadasIniciais);
        mapaPontoCentral = coordenadasIniciais;
        mapController = map.getController();
        mapController.setZoom(zoomInicial);
        mapController.setCenter(coordenadasIniciais);
    }

    public void recarregarAppComMapa(int arg_mapa_modo) {
        Intent intent = new Intent();
        intent.putExtra(ARG_MAPA_MODO, arg_mapa_modo);
        intent.putExtra(ARG_LICENCA_TIPO, licencaTipo.toString());
        intent.putExtra(ARG_MAPA_ZOOMINICIAL, map.getZoomLevel());
        intent.putExtra(ARG_MAPA_ID, mapFileController.GetMapFileSelected().getIndex());
        intent.putExtra(ARG_MAPA_LATITUDEATUAL, coordenadasIniciais.getLatitude());
        intent.putExtra(ARG_MAPA_LONGITUDEATUAL, coordenadasIniciais.getLongitude());
        setResult(RESULT_OK, intent);
        finish();
        try {
            onBackPressed();
        } catch (Exception e) {

        }
    }

    private void carregarMapaDeArquivo(MapFile mapFile) {
        mapFileController.SetSelectedMap(mapFile);
        MapaTiles am = new MapaTiles(mapFile.getFile(), mMyLocationNewOverlay.getMyLocation(), 10, 21);

        SimpleRegisterReceiver simpleReceiver = new SimpleRegisterReceiver(this);
        XYTileSource mbtilesRender = new XYTileSource("mbtiles", am.zoomMin, am.zoomMax, 256, am.formatoImagem, new String[]{});
        IArchiveFile[] files = {MBTilesFileArchive.getDatabaseFileArchive(mapFile.getFile())};
        MapTileModuleProviderBase moduleProvider = new MapTileFileArchiveProvider(simpleReceiver, mbtilesRender, files);
        MapTileProviderArray mProvider = new MapTileProviderArray(mbtilesRender, null, new MapTileModuleProviderBase[]{moduleProvider});
        map.setTileProvider(mProvider);

        mapController = map.getController();
        if (zoomInicial == 0)
            zoomInicial = am.zoomMin;
        mapaPontoCentral = am.pontoCentral;
        coordenadasIniciais = (coordenadasIniciais == null ? am.pontoCentral : coordenadasIniciais);
        mapController.setZoom(zoomInicial);
        mapController.setCenter(coordenadasIniciais);
    }

    void setInitialViewOn(BoundingBox bb) {
        if (map.getScreenRect(null).height() == 0) {
            mInitialBoundingBox = bb;
            map.addOnFirstLayoutListener(this);
        } else
            map.zoomToBoundingBox(bb, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PEGAR_ELEMENTO_AREA_REQUEST) {
            if (resultCode == RESULT_OK) {
                ElementoDao elementoDao = FabricaElementoDao.Criar(mContext);
                Elemento mItem = elementoDao.get(data.getExtras().getInt(ARG_ELEMENTOID));
                Area area = new Area(mItem);
                areaList.add(area);
                if (exibirAreas) {
                    area.desenharEm(map);
                    map.invalidate();
                }
            }
        }
        if (requestCode == PEGAR_ELEMENTO_DISTANCIA_REQUEST) {
            if (resultCode == RESULT_OK) {
                ElementoDao elementoDao = FabricaElementoDao.Criar(mContext);
                Elemento mItem = elementoDao.get(data.getExtras().getInt(ARG_ELEMENTOID));
                Distancia distancia = new Distancia(mItem);
                distanciaList.add(distancia);
                if (exibirDistancias) {
                    distancia.desenharEm(map);
                    map.invalidate();
                }
            }
        }
        if (requestCode == PEGAR_ELEMENTO_PONTO_REQUEST) {
            if (resultCode == RESULT_OK) {
                ElementoDao elementoDao = FabricaElementoDao.Criar(mContext);
                Elemento mItem = elementoDao.get(data.getExtras().getInt(ARG_ELEMENTOID));
                Ponto ponto = new Ponto(mItem);
                pontoList.add(ponto);
                if (exibirPontos) {
                    ponto.desenharEm(map);
                    map.invalidate();
                }
            }
        }
        if (requestCode == PEGAR_MENU_CADASTROS_REQUEST) {
            carregarPontos();
            carregarAreas();
            carregarDistancias();
            if (data != null && data.getExtras().getInt(ARG_ELEMENTO_CENTRALIZAR, 0) == 1) {
                ElementoDao elementoDao = FabricaElementoDao.Criar(mContext);
                Elemento mItem = elementoDao.get(data.getExtras().getInt(ARG_ELEMENTOID, 0));
                map.getController().setCenter(mItem.getPontoCentral());
            }
            map.invalidate();
        }
        if (requestCode == PEGAR_MENU_CAMADAS_REQUEST) {
            CamadaHolder.getInstance().exibirCamadasSelecionadasNoMapa(map);
            map.invalidate();
        }
        if (requestCode == PEGAR_NOME_ARQUIVO_EXPORTAR_REQUEST) {
            if (resultCode == RESULT_OK) {
                try {
                    //TODO: Alterar a descrição dos elementos overlay para conter o mesmo que no cadastro!
                    String nomeArquivo = (data.getExtras().getString(ARG_EXPORTAR_NOME_ARQUIVO).equals("") ? "exportacao" : data.getExtras().getString(ARG_EXPORTAR_NOME_ARQUIVO));
                    String tipoArquivo = data.getExtras().getString(ARG_EXPORTAR_TIPO_ARQUIVO);
                    nomeArquivo += "." + tipoArquivo;
                    if (exportarArquivoMapa(nomeArquivo, tipoArquivo))
                        Utils.toast(mContext, "Arquivo \"" + nomeArquivo + "\" gerado com sucesso!");
                    else
                        Utils.toast(mContext, "Ocorreu erro ao exportar o arquivo.");
                } catch (Exception e) {
                    Utils.toast(mContext, "Ocorreu erro ao exportar o arquivo:\r\n" + e.getMessage());
                }
            }
        }
        if (requestCode == PEGAR_NOME_ARQUIVO_IMPORTAR_REQUEST) {
            if (resultCode == RESULT_OK) {
                try {
                    //TODO: Alterar a descrição dos elementos overlay para conter o mesmo que no cadastro!
                    String nomeArquivo = (data.getExtras().getString(ARG_IMPORTAR_NOME_ARQUIVO).equals("") ? "importacao.kmz" : data.getExtras().getString(ARG_IMPORTAR_NOME_ARQUIVO));
                    String tipoArquivo = FilenameUtils.getExtension(nomeArquivo);

                    if (importarArquivoMapa(nomeArquivo, tipoArquivo))
                        Utils.toast(mContext, "Arquivo \"" + nomeArquivo + "\" importado com sucesso!");
                    else
                        Utils.toast(mContext, "Ocorreu erro ao importar o arquivo.");
                } catch (Exception e) {
                    Utils.toast(mContext, "Ocorreu erro ao importar o arquivo:\r\n" + e.getMessage());
                }
            }
        }
        if (requestCode == RECARREGAR_LICENCA) {
            if (resultCode == RESULT_OK) {
                recarregarAppComMapa(getIntent().getIntExtra(ARG_MAPA_MODO, OFFLINE));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean exportarArquivoMapa(String nomeArquivo, String tipoArquivo) {
        boolean resultado = false;
        File arquivo = new File(br.com.neogis.agritopo.singleton.Configuration.getInstance().DiretorioExportacaoArquivos
                + nomeArquivo);
        KmlDocument kmlDocument = new KmlDocument();
        for (Overlay overlay : map.getOverlays()) {
            if (overlay instanceof ItemizedOverlayWithFocus) {
                for (int i = 0; i < ((ItemizedOverlayWithFocus) overlay).size(); i++) {
                    Marker m = new Marker(map);
                    m.setTitle(((ItemizedOverlayWithFocus) overlay).getItem(i).getTitle());
                    m.setSnippet(((ItemizedOverlayWithFocus) overlay).getItem(i).getSnippet());
                    m.setPosition((GeoPoint) ((ItemizedOverlayWithFocus) overlay).getItem(i).getPoint());
                    kmlDocument.mKmlRoot.addOverlay(m, kmlDocument);
                }
            } else {
                kmlDocument.mKmlRoot.addOverlay(overlay, kmlDocument);
            }
        }
        if (tipoArquivo.equals("kml")) {
            resultado = kmlDocument.saveAsKML(arquivo);
        }
        if (tipoArquivo.equals("geojson")) {
            resultado = kmlDocument.saveAsGeoJSON(arquivo);
        }
        return resultado;
    }

    private boolean importarArquivoMapa(String nomeArquivo, String tipoArquivo) throws IOException {
        boolean resultado = false;
        File arquivo = new File(nomeArquivo);
        KmlDocument kmlDocument = new KmlDocument();
        switch (tipoArquivo) {
            case "kml":
                resultado = kmlDocument.parseKMLFile(arquivo);
                break;
            case "kmz":
                BufferedInputStream stream = new BufferedInputStream(new FileInputStream(arquivo));
                ZipFile zipFile = new ZipFile(arquivo);
                resultado = kmlDocument.parseKMLStream(stream, zipFile);
                break;
            case "geojson":
                resultado = kmlDocument.parseGeoJSON(arquivo);
                break;
        }

        //TODO validar possivel solução, e criar serviço para alocar as regras de negocio
        /*
        //Percore os elementos lidos no arquivo, de tras para frente, para permitir apagar os pontos
        for(int i = kmlDocument.mKmlRoot.mItems.size()-1; i >= 0;i--)
        {
            KmlFeature feature = kmlDocument.mKmlRoot.mItems.get(i);
            //Verifica se é um placemark
            if(!(feature instanceof KmlPlacemark))
                continue;
            //verifica se é um ponto, que é oque não queremos importar duplicado
            KmlPlacemark point = (KmlPlacemark)feature;
            if(!(point.mGeometry instanceof KmlPoint))
                continue;

            //quando encontra um ponto, busca pelos polygonos, e valida se o ponto esta dentro de algum deles
            for (KmlFeature feature1 : kmlDocument.mKmlRoot.mItems) {
                //valida se é um placemark
                if(!(feature1 instanceof KmlPlacemark))
                    continue;

                //valida se é um poligono
                KmlPlacemark polygon = (KmlPlacemark)feature1;
                if(!(polygon.mGeometry instanceof KmlPolygon))
                    continue;

                //e por fim, verifica se o ponto esta dentro do poligono encontrado
                if (polygon.mGeometry.getBoundingBox().contains(((KmlPoint)point.mGeometry).getPosition())) {
                    //se o ponto esta dentro do poligono, então remove o ponto, e muda a descrição do poligono, para ficar com a descrição do ponto
                    feature1.mName = feature.mName;
                    kmlDocument.mKmlRoot.mItems.remove(i);
                    break;
                }
            }
        }
        */

        FolderOverlay kmlOverlay = (FolderOverlay) kmlDocument.mKmlRoot.buildOverlay(map, null, null, kmlDocument);
        map.getOverlays().add(kmlOverlay);

        map.invalidate();
        return resultado;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_ACCESS_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case MY_PERMISSIONS_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onFirstLayout(View v, int left, int top, int right, int bottom) {
        if (mInitialBoundingBox != null)
            map.zoomToBoundingBox(mInitialBoundingBox, false);
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        Utils.toast(this, Utils.getFormattedLocationInDegree(p));
        InfoWindow.closeAllInfoWindowsOn(map);
        return true;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        if (modalAtivo == null) {
            onLongPressMenuSelected(p);
        }
        return true;
    }

    private ArrayList<Elemento> listarElementosProximos(GeoPoint p, double proximidade) {
        ArrayList<Elemento> listElemento = new ArrayList<>();
        for (Area a : areaList) {
            GeoPoint centro = a.getCentro();
            if (p.distanceTo(centro) < proximidade) {
                listElemento.add(a.getElemento());
            }
        }

        for (Distancia d : distanciaList) {
            GeoPoint centro = d.getCentro();
            if (p.distanceTo(centro) < proximidade) {
                listElemento.add(d.getElemento());
            }
        }

        for (Ponto ponto : pontoList) {
            GeoPoint centro = ponto.getPonto().getPosition();
            if (p.distanceTo(centro) < proximidade) {
                listElemento.add(ponto.getElemento());
            }
        }
        return listElemento;
    }

    private void onLongPressMenuSelected(GeoPoint point) {
        final MyGeoPoint ponto = new MyGeoPoint(point);

        final View menuItemView = findViewById(R.id.action_recentralizar);
        PopupMenu popupMenu = new PopupMenu(this, menuItemView);
        popupMenu.inflate(R.menu.principal_mapa);
        for (Elemento elemento : listarElementosProximos(point, br.com.neogis.agritopo.singleton.Configuration.getInstance().ProximidadeElementos)) {
            String texto = elemento.getClasse().getNome() + " / " + elemento.getTipoElemento().getNome() + " / " + elemento.getTitulo();
            popupMenu.getMenu().add(0, elemento.getElementoid(), 0, texto);
        }
        popupMenu.getMenu().add(1, 0, 0, getResources().getString(R.string.content_map_novo_poi));

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getGroupId() == 0) {
                    Intent intent = new Intent(mActivity.getBaseContext(), ElementoDetailActivity.class);
                    intent.putExtra(ARG_ELEMENTOID, item.getItemId());
                    mActivity.startActivityForResult(intent, ALTERAR_ELEMENTO_REQUEST);
                }
                if (item.getGroupId() == 1) {
                    if (item.getItemId() == 0) {
                        Intent intent = new Intent(mActivity.getBaseContext(), ElementoDetailActivity.class);
                        intent.putExtra(ARG_ELEMENTOID, 0);
                        intent.putExtra(ARG_TIPOELEMENTOID, 1);
                        intent.putExtra(ARG_CLASSEID, 1);
                        intent.putExtra(ARG_GEOMETRIA, ponto.toString());
                        mActivity.startActivityForResult(intent, PEGAR_ELEMENTO_PONTO_REQUEST);
                    }
                }
                item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
                item.setActionView(layoutTelaPrincipal);
                item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        return false;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        return false;
                    }
                });
                return false;
            }
        });
        popupMenu.show();
    }

    private void carregarPontos() {
        ElementoDao elementoDao = FabricaElementoDao.Criar(this.getBaseContext());
        List<Elemento> elementos = elementoDao.getAll();
        for (Ponto p : pontoList) {
            p.removerDe(map);
        }
        pontoList.clear();
        for (Elemento e : elementos) {
            if (e.getClasse().getClasseEnum() == ClasseEnum.PONTO) {
                Ponto p = new Ponto(e);
                pontoList.add(p);
                if (exibirPontos) {
                    p.desenharEm(map);
                }
            }
        }
    }

    private void carregarAreas() {
        ElementoDao elementoDao = FabricaElementoDao.Criar(this.getBaseContext());
        List<Elemento> elementos = elementoDao.getAll();
        for (Area a : areaList) {
            a.removerDe(map);
        }
        areaList.clear();
        for (Elemento e : elementos) {
            if (e.getClasse().getClasseEnum() == ClasseEnum.AREA) {
                Area a = new Area(e);
                areaList.add(a);
                if (exibirAreas) {
                    a.desenharEm(map);
                }
            }
        }
    }

    private void carregarDistancias() {
        ElementoDao elementoDao = FabricaElementoDao.Criar(this.getBaseContext());
        List<Elemento> elementos = elementoDao.getAll();
        for (Distancia d : distanciaList) {
            d.removerDe(map);
        }
        distanciaList.clear();
        for (Elemento e : elementos) {
            if (e.getClasse().getClasseEnum() == ClasseEnum.DISTANCIA) {
                Distancia d = new Distancia(e);
                distanciaList.add(d);
                if (exibirDistancias)
                    d.desenharEm(map);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (map != null) {
            outState.putString(ARG_LICENCA_TIPO, licencaTipo.toString());
            outState.putInt(ARG_MAPA_ZOOMINICIAL, map.getZoomLevel());
            outState.putInt(ARG_MAPA_ID, mapFileController.GetMapFileSelected().getIndex());
            outState.putDouble(ARG_MAPA_LATITUDEATUAL, coordenadasIniciais.getLatitude());
            outState.putDouble(ARG_MAPA_LONGITUDEATUAL, coordenadasIniciais.getLongitude());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        licencaTipo = Licenca.LicencaTipo.valueOf(savedInstanceState.getString(ARG_LICENCA_TIPO, "Gratuito"));
        zoomInicial = savedInstanceState.getInt(ARG_MAPA_ZOOMINICIAL, 0);
        mapFileController.SetSelectedMap(savedInstanceState.getInt(ARG_MAPA_ID, 0));
        double lat = savedInstanceState.getDouble(ARG_MAPA_LATITUDEATUAL, 0.0);
        double lon = savedInstanceState.getDouble(ARG_MAPA_LONGITUDEATUAL, 0.0);
        if (lat != 0.0 && lon != 0.0)
            coordenadasIniciais = new GeoPoint(lat, lon);
    }

    private void onRestoreActivity() {
        licencaTipo = Licenca.LicencaTipo.valueOf(getIntent().getStringExtra(ARG_LICENCA_TIPO));
        zoomInicial = getIntent().getIntExtra(ARG_MAPA_ZOOMINICIAL, 0);
        mapFileController.SetSelectedMap(getIntent().getIntExtra(ARG_MAPA_ID, 0));
        double lat = getIntent().getDoubleExtra(ARG_MAPA_LATITUDEATUAL, 0.0);
        double lon = getIntent().getDoubleExtra(ARG_MAPA_LONGITUDEATUAL, 0.0);
        if (lat != 0.0 && lon != 0.0)
            coordenadasIniciais = new GeoPoint(lat, lon);
    }

    private void carregarCamadas() {
        new CamadasLoader(map, licencaTipo.equals(Licenca.LicencaTipo.Gratuito)).carregar(new AsyncResponse() {
            @Override
            public void processFinish() {
                invalidateOptionsMenu();

                // Reabilitar KML no menu lateral
                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                Menu menuNav = navigationView.getMenu();
                MenuItem nav_item2 = menuNav.findItem(R.id.nav_camadas);
                nav_item2.setEnabled(true);

                for (ArvoreCamada camada : camadaHolder.camadas)
                    camadaHolder.exibirCamadasSelecionadasNoMapa(camada, map);
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // deixar botão KML do menu desabilitado enquanto o carregamento não terminar
        //
        boolean kmlHabilitado = !camadaHolder.carregando;
        MenuItem item = menu.findItem(R.id.action_menu_kml);
        if (!kmlHabilitado) {
            ProgressBar pb = new ProgressBar(getApplicationContext());
            pb.setScaleX(0.5f); // deixar com tamanho parecido dos outros botões
            pb.setScaleY(0.5f);
            Drawable drawable = pb.getIndeterminateDrawable().mutate();
            drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            item.setActionView(pb);
        }
        item.setEnabled(kmlHabilitado);

        return true;
    }

    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (map != null) map.invalidate();
    }

    @Override
    public void onDestroy() {
        RemoverTodosOverlays();
        super.onDestroy();
    }

    private void RemoverTodosOverlays() {
        if (map == null)
            return;

        for (int i = map.getOverlays().size() - 1; i >= 0; i--)
            map.getOverlays().remove(i);
    }

    //0. Using the Marker and Polyline overlays - advanced options
    class OnMarkerDragListenerDrawer implements Marker.OnMarkerDragListener {
        ArrayList<GeoPoint> mTrace;
        Polyline mPolyline;

        OnMarkerDragListenerDrawer() {
            mTrace = new ArrayList<>(100);
            mPolyline = new Polyline();
            mPolyline.setColor(0xAA0000FF);
            mPolyline.setWidth(2.0f);
            mPolyline.setGeodesic(true);
            map.getOverlays().add(mPolyline);
        }

        @Override
        public void onMarkerDrag(Marker marker) {
            //mTrace.add(marker.getPosition());
        }

        @Override
        public void onMarkerDragEnd(Marker marker) {
            mTrace.add(marker.getPosition());
            mPolyline.setPoints(mTrace);
            map.invalidate();
        }

        @Override
        public void onMarkerDragStart(Marker marker) {
            //mTrace.add(marker.getPosition());
        }
    }
}
