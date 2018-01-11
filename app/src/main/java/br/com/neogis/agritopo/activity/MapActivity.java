package br.com.neogis.agritopo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.apache.commons.io.FilenameUtils;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.location.POI;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.MapTileProviderArray;
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
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipFile;

import br.com.neogis.agritopo.R;
import br.com.neogis.agritopo.dao.Utils;
import br.com.neogis.agritopo.dao.tabelas.ClasseEnum;
import br.com.neogis.agritopo.dao.tabelas.Elemento;
import br.com.neogis.agritopo.dao.tabelas.ElementoDao;
import br.com.neogis.agritopo.dao.tabelas.ElementoDaoImpl;
import br.com.neogis.agritopo.fragment.CamadasFragment;
import br.com.neogis.agritopo.holder.AdicionarAreaHolder;
import br.com.neogis.agritopo.holder.AdicionarDistanciaHolder;
import br.com.neogis.agritopo.holder.AdicionarPontoHolder;
import br.com.neogis.agritopo.holder.CamadaHolder;
import br.com.neogis.agritopo.model.Area;
import br.com.neogis.agritopo.model.Distancia;
import br.com.neogis.agritopo.model.MapaTiles;
import br.com.neogis.agritopo.model.MyGpsMyLocationProvider;
import br.com.neogis.agritopo.model.MyItemizedIconOverlay;
import br.com.neogis.agritopo.model.MyItemizedOverlayWithFocus;
import br.com.neogis.agritopo.model.MyMarker;
import br.com.neogis.agritopo.model.MyOverlayItem;

import static android.view.View.VISIBLE;
import static br.com.neogis.agritopo.dao.Constantes.ALTERAR_ELEMENTO_REQUEST;
import static br.com.neogis.agritopo.dao.Constantes.ARG_CLASSEID;
import static br.com.neogis.agritopo.dao.Constantes.ARG_ELEMENTOID;
import static br.com.neogis.agritopo.dao.Constantes.ARG_ELEMENTO_CENTRALIZAR;
import static br.com.neogis.agritopo.dao.Constantes.ARG_EXPORTAR_NOME_ARQUIVO;
import static br.com.neogis.agritopo.dao.Constantes.ARG_EXPORTAR_TIPO_ARQUIVO;
import static br.com.neogis.agritopo.dao.Constantes.ARG_IMPORTAR_NOME_ARQUIVO;
import static br.com.neogis.agritopo.dao.Constantes.ARG_MAPA_MODO;
import static br.com.neogis.agritopo.dao.Constantes.MY_PERMISSIONS_ACCESS_COARSE_LOCATION;
import static br.com.neogis.agritopo.dao.Constantes.MY_PERMISSIONS_ACCESS_FINE_LOCATION;
import static br.com.neogis.agritopo.dao.Constantes.OFFLINE;
import static br.com.neogis.agritopo.dao.Constantes.ONLINE;
import static br.com.neogis.agritopo.dao.Constantes.PEGAR_ELEMENTO_AREA_REQUEST;
import static br.com.neogis.agritopo.dao.Constantes.PEGAR_ELEMENTO_DISTANCIA_REQUEST;
import static br.com.neogis.agritopo.dao.Constantes.PEGAR_ELEMENTO_PONTO_REQUEST;
import static br.com.neogis.agritopo.dao.Constantes.PEGAR_MENU_CADASTROS_REQUEST;
import static br.com.neogis.agritopo.dao.Constantes.PEGAR_MENU_CAMADAS_REQUEST;
import static br.com.neogis.agritopo.dao.Constantes.PEGAR_NOME_ARQUIVO_EXPORTAR_REQUEST;
import static br.com.neogis.agritopo.dao.Constantes.PEGAR_NOME_ARQUIVO_IMPORTAR_REQUEST;

public class MapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener, MapEventsReceiver, MapView.OnFirstLayoutListener {

    BoundingBox mInitialBoundingBox = null;
    float mGroundOverlayBearing = 0.0f;
    AdicionarAreaHolder adicionarAreaHolder;
    AdicionarPontoHolder adicionarPontoHolder;
    AdicionarDistanciaHolder adicionarDistanciaHolder;
    MyItemizedOverlayWithFocus<MyOverlayItem> geoPointList;
    List<Area> areaList;
    List<Distancia> distanciaList;
    CamadaHolder camadaHolder;
    private MyLocationNewOverlay mMyLocationNewOverlay;
    private CompassOverlay mCompassOverlay;
    private RotationGestureOverlay mRotationGestureOverlay;
    private MapView map;
    //Outros
    private String caminhoPastaMapas = Environment.getExternalStorageDirectory().getAbsolutePath() + "/agritopo/";
    private File[] listaArquivosMapas;
    //Interface
    private FloatingActionMenu famNovo;
    private Context mContext;
    private Activity mActivity;
    private FloatingActionButton fabNovoPonto, fabNovaArea, fabNovaDistancia, fabCamadas, fabGPS, fabRotacao, fabConcluido, fabCancelar, fabDesfazer;
    private PopupWindow popupLayers;
    private ConstraintLayout layoutTelaPrincipal;
    private boolean exibirPontos;
    private boolean exibirAreas;
    private boolean exibirDistancias;

    private GeoPoint coordenadasIniciais;
    private int zoomInicial = 0;

    private IMapController mapController;

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Restaurar posição no mapa, nível do zoom, e outros
        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);

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

        criarDiretorio("agritopo");
        criarDiretorio("OsmDroid");

        MyMarker.ENABLE_TEXT_LABELS_WHEN_NO_IMAGE = true;

        mContext = getApplicationContext();
        mActivity = MapActivity.this;

        exibirPontos = true;
        exibirAreas = true;
        areaList = new ArrayList<Area>();
        exibirDistancias = true;
        distanciaList = new ArrayList<Distancia>();
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

    public void inicializarBotoes() {
        famNovo = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        fabNovoPonto = (FloatingActionButton) findViewById(R.id.action_novo_ponto);
        fabNovaArea = (FloatingActionButton) findViewById(R.id.action_nova_area);
        fabNovaDistancia = (FloatingActionButton) findViewById(R.id.action_nova_distancia);
        fabGPS = (FloatingActionButton) findViewById(R.id.action_gps);
        fabCamadas = (FloatingActionButton) findViewById(R.id.action_layer);
        fabRotacao = (FloatingActionButton) findViewById(R.id.action_rotacao);
        fabConcluido = (FloatingActionButton) findViewById(R.id.action_concluido);
        fabCancelar = (FloatingActionButton) findViewById(R.id.action_cancelar);
        fabDesfazer = (FloatingActionButton) findViewById(R.id.action_desfazer);

        fabNovoPonto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (adicionarPontoHolder == null) {
                    adicionarPontoHolder = new AdicionarPontoHolder(mActivity);
                    map.getOverlays().add(adicionarPontoHolder);
                    famNovo.close(false);
                    famNovo.setVisibility(View.INVISIBLE);
                    fabConcluido.setVisibility(View.VISIBLE);
                }
                map.invalidate();
            }
        });
        fabNovaArea.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (adicionarAreaHolder == null) {
                    adicionarAreaHolder = new AdicionarAreaHolder(map, mActivity);
                    famNovo.close(false);
                    famNovo.setVisibility(View.INVISIBLE);
                    fabConcluido.setVisibility(View.VISIBLE);
                    fabCancelar.setVisibility(View.VISIBLE);
                    fabDesfazer.setVisibility(View.VISIBLE);
                }
            }
        });
        fabNovaDistancia.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (adicionarDistanciaHolder == null) {
                    adicionarDistanciaHolder = new AdicionarDistanciaHolder(map, mActivity);
                    famNovo.close(false);
                    famNovo.setVisibility(View.INVISIBLE);
                    fabConcluido.setVisibility(View.VISIBLE);
                    fabCancelar.setVisibility(View.VISIBLE);
                    fabDesfazer.setVisibility(View.VISIBLE);
                }
            }
        });
        fabGPS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mMyLocationNewOverlay.isFollowLocationEnabled()) {
                    mMyLocationNewOverlay.disableMyLocation();
                    mMyLocationNewOverlay.disableFollowLocation();
                    ((FloatingActionButton) v).setImageResource(R.drawable.ic_gps_not_fixed_white_24dp);
                } else {
                    mMyLocationNewOverlay.enableMyLocation();
                    mMyLocationNewOverlay.enableFollowLocation();
                    ((FloatingActionButton) v).setImageResource(R.drawable.ic_gps_fixed_white_24dp);
                    if (mMyLocationNewOverlay.getMyLocation() != null)
                        (map.getController()).setCenter(mMyLocationNewOverlay.getMyLocation());
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
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                if (Build.VERSION.SDK_INT >= 21) {
                    popupLayers.setElevation(5.0f);
                }
                // Get a reference for the custom view close button
                ImageButton btnPopupLayerFechar = (ImageButton) customView.findViewById(R.id.btnPopupLayerFechar);
                ImageButton btnPopupLayerFecharTop = (ImageButton) customView.findViewById(R.id.btnPopupLayerFecharTop);
                ImageButton btnPopupLayerFecharBottom = (ImageButton) customView.findViewById(R.id.btnPopupLayerFecharBottom);
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
                        if (isChecked) {
                            map.getOverlays().add(geoPointList);
                            Utils.toast(getBaseContext(), "Pontos de Interesse: Ativado");
                        } else {
                            map.getOverlays().remove(geoPointList);
                            Utils.toast(getBaseContext(), "Pontos de Interesse: Desativado");
                        }
                        map.invalidate();
                    }
                });
                cbxAreas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        exibirAreas = isChecked;
                        List<Overlay> overlays = map.getOverlays();
                        for (Area area : areaList) {
                            if (isChecked)
                                area.desenharEm(map);
                            else
                                area.removerDe(map);
                        }
                        map.invalidate();
                        if (isChecked) {
                            Utils.toast(getBaseContext(), "Áreas: Ativado");
                        } else {
                            Utils.toast(getBaseContext(), "Áreas: Desativado");
                        }
                    }
                });
                cbxDistancias.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        exibirDistancias = isChecked;
                        List<Overlay> overlays = map.getOverlays();
                        for (Distancia distancia : distanciaList) {
                            if (isChecked)
                                distancia.desenharEm(map);
                            else
                                distancia.removerDe(map);
                        }
                        map.invalidate();
                        if (isChecked) {
                            Utils.toast(getBaseContext(), "Distâncias: Ativado");
                        } else {
                            Utils.toast(getBaseContext(), "Distâncias: Desativado");
                        }
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
                btnPopupLayerFecharBottom.setOnClickListener(new View.OnClickListener() {
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
                        startActivityForResult(intent, PEGAR_MENU_CADASTROS_REQUEST);
                    }
                });
                btnElementoDistancia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupLayers.dismiss();
                        Intent intent = new Intent(getBaseContext(), CadastrosListarActivity.class);
                        intent.putExtra(ARG_CLASSEID, 3);
                        startActivityForResult(intent, PEGAR_MENU_CADASTROS_REQUEST);
                    }
                });
                btnElementoPonto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupLayers.dismiss();
                        Intent intent = new Intent(getBaseContext(), CadastrosListarActivity.class);
                        intent.putExtra(ARG_CLASSEID, 1);
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
                popupLayers.showAtLocation(layoutTelaPrincipal, Gravity.CENTER, 0, 0);
            }
        });
        fabRotacao.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mRotationGestureOverlay.isEnabled()) {
                    ((FloatingActionButton) v).setImageResource(R.drawable.ic_rotation_blocked_white_24dp);
                    mRotationGestureOverlay.setEnabled(false);
                } else {
                    ((FloatingActionButton) v).setImageResource(R.drawable.ic_rotation_white_24dp);
                    mRotationGestureOverlay.setEnabled(true);
                }
            }
        });
        fabConcluido.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (adicionarPontoHolder != null) {
                    List<Overlay> overlays = map.getOverlays();
                    overlays.remove(adicionarPontoHolder);
                    adicionarPontoHolder = null;
                }
                if (adicionarAreaHolder != null) {
                    adicionarAreaHolder.finalizar();
                    adicionarAreaHolder = null;
                }
                if (adicionarDistanciaHolder != null) {
                    adicionarDistanciaHolder.finalizar();
                    adicionarDistanciaHolder = null;
                }
                fabCancelar.setVisibility(View.INVISIBLE);
                fabConcluido.setVisibility(View.INVISIBLE);
                fabDesfazer.setVisibility(View.INVISIBLE);
                famNovo.setVisibility(View.VISIBLE);
                map.invalidate();
            }
        });
        fabCancelar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (adicionarAreaHolder != null) {
                    adicionarAreaHolder.cancelar();
                    adicionarAreaHolder = null;
                }
                if (adicionarDistanciaHolder != null) {
                    adicionarDistanciaHolder.cancelar();
                    adicionarDistanciaHolder = null;
                }
                fabCancelar.setVisibility(View.INVISIBLE);
                fabConcluido.setVisibility(View.INVISIBLE);
                fabDesfazer.setVisibility(View.INVISIBLE);
                famNovo.setVisibility(View.VISIBLE);
                map.invalidate();
            }
        });
        fabDesfazer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (adicionarAreaHolder != null) {
                    adicionarAreaHolder.desfazer();
                }
                if (adicionarDistanciaHolder != null) {
                    adicionarDistanciaHolder.desfazer();
                }
                map.invalidate();
            }
        });
    }

    public void inicializarMapas(boolean modoOffline) {
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        buscarMapasDoImovel();

        Configuration.getInstance().setMapViewHardwareAccelerated(true);
        map = (MapView) findViewById(R.id.map);

        map.setBuiltInZoomControls(true);
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
        Configuration.getInstance().setDebugMode(true);

        if ((listaArquivosMapas != null) && (listaArquivosMapas.length > 0) && (modoOffline)) {
            carregarMapaDeArquivo(map, listaArquivosMapas[0]);
        } else {
            map.setUseDataConnection(true);
            map.setTileSource(TileSourceFactory.MAPNIK);
            map.getController().setZoom(10);
            if (mMyLocationNewOverlay.getMyLocation() != null) {
                IMapController mapController = map.getController();
                mapController.setZoom(15);
                mapController.animateTo(mMyLocationNewOverlay.getMyLocation());
            }
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        carregarMapasNoMenu(menu);
        return true;
    }

    public boolean carregarMapasNoMenu(Menu menu) {
        menu.add(0, -1, 0, "Online (OpenStreetMaps)");
        if (listaArquivosMapas != null) {
            for (int i = 0; i < listaArquivosMapas.length; i++) {
                String nomeMenu = listaArquivosMapas[i].getName();

                // remover extensão
                int pos_ponto_extensao = nomeMenu.lastIndexOf(".");
                if (pos_ponto_extensao > 0)
                    nomeMenu = nomeMenu.substring(0, pos_ponto_extensao);

                menu.add(0, i, 0, nomeMenu);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == -1) {
            carregarMapaOnline();
        } else {
            if (listaArquivosMapas != null) {
                Log.d("Agritopo", "Mapa selecionado: " + listaArquivosMapas[id].toString());
                carregarMapaDeArquivo(map, listaArquivosMapas[id]);
            }
        }
        map.invalidate();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cadastros) {
            Intent intent = new Intent(this, CadastrosListarActivity.class);
            startActivityForResult(intent, PEGAR_MENU_CADASTROS_REQUEST);
        } else if (id == R.id.nav_camadas) {
            Class<?> clazz = CamadasFragment.class;
            Intent intent = new Intent(this, SingleFragmentActivity.class);
            intent.putExtra(SingleFragmentActivity.FRAGMENT_PARAM, clazz);
            startActivityForResult(intent, PEGAR_MENU_CAMADAS_REQUEST);
        } else if (id == R.id.nav_exportar) {
            Intent intent = new Intent(this, ExportarActivity.class);
            startActivityForResult(intent, PEGAR_NOME_ARQUIVO_EXPORTAR_REQUEST);
        } else if (id == R.id.nav_importar) {
            Intent intent = new Intent(this, ImportarActivity.class);
            startActivityForResult(intent, PEGAR_NOME_ARQUIVO_IMPORTAR_REQUEST);
        } else if (id == R.id.nav_configuracao) {
            Intent intent = new Intent(this, ConfiguracaoActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (mMyLocationNewOverlay.isFollowLocationEnabled()) {
            if (mMyLocationNewOverlay.getMyLocation() != null)
                mapController.setCenter(mMyLocationNewOverlay.getMyLocation());
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void buscarMapasDoImovel() {
        File pasta_mapas = new File(caminhoPastaMapas);
        FilenameFilter filtro = new FilenameFilter() {
            String[] extensoesValidas = {"mbtiles"};

            @Override
            public boolean accept(File dir, String name) {
                String extensao = name.substring(name.lastIndexOf(".") + 1);
                extensao = extensao.toLowerCase();
                Log.d("Agritopo", "extensao: " + extensao);
                return Arrays.asList(extensoesValidas).contains(extensao);
            }
        };
        listaArquivosMapas = pasta_mapas.listFiles(filtro);
        if (listaArquivosMapas != null)
            for (File arquivo : listaArquivosMapas)
                Log.d("Agritopo", "Arquivo mapa: " + arquivo.toString());
    }

    public void carregarMapaOnline() {
        Intent intent = new Intent();
        intent.putExtra(ARG_MAPA_MODO, ONLINE);
        setResult(RESULT_OK, intent);
        finish();
        onBackPressed();
    }

    public void carregarMapaDeArquivo(MapView map, File arquivo) {
        MapaTiles am = new MapaTiles(arquivo);

        SimpleRegisterReceiver simpleReceiver = new SimpleRegisterReceiver(this);
        XYTileSource mbtilesRender = new XYTileSource("mbtiles", am.zoomMin, am.zoomMax, 256, am.formatoImagem, new String[]{});
        IArchiveFile[] files = {MBTilesFileArchive.getDatabaseFileArchive(arquivo)};
        MapTileModuleProviderBase moduleProvider = new MapTileFileArchiveProvider(simpleReceiver, mbtilesRender, files);
        MapTileProviderArray mProvider = new MapTileProviderArray(mbtilesRender, null, new MapTileModuleProviderBase[]{moduleProvider});
        map.setTileProvider(mProvider);

        IMapController mapController = map.getController();
        if (zoomInicial == 0)
            zoomInicial = 15;
        mapController.setZoom(zoomInicial);

        // não usar animateTo(), senão demora meio ano para voltar onde estava quando gira o dispositivo
        if (coordenadasIniciais == null)
            coordenadasIniciais = am.pontoCentral;
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
                ElementoDao elementoDao = new ElementoDaoImpl(mContext);
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
                ElementoDao elementoDao = new ElementoDaoImpl(mContext);
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
                ElementoDao elementoDao = new ElementoDaoImpl(mContext);
                Elemento mItem = elementoDao.get(data.getExtras().getInt(ARG_ELEMENTOID));
                geoPointList.addItem(new MyOverlayItem(mItem));
                map.invalidate();
            }
        }
        if (requestCode == PEGAR_MENU_CADASTROS_REQUEST) {
            carregarPontos();
            carregarAreas();
            carregarDistancias();
            if( data != null && data.getExtras().getInt(ARG_ELEMENTO_CENTRALIZAR, 0) == 1 ) {
                ElementoDao elementoDao = new ElementoDaoImpl(mContext);
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
                    String nomeArquivo = (data.getExtras().getString(ARG_EXPORTAR_NOME_ARQUIVO) == "" ? "exportacao" : data.getExtras().getString(ARG_EXPORTAR_NOME_ARQUIVO));
                    String tipoArquivo = data.getExtras().getString(ARG_EXPORTAR_TIPO_ARQUIVO);
                    nomeArquivo += "." + tipoArquivo;
                    if (exportarArquivoMapa(nomeArquivo, tipoArquivo))
                        Utils.toast(mContext, "Arquivo \"" + nomeArquivo + "\" gerado com sucesso!");
                    else
                        Utils.toast(mContext, "Ocorreu erro ao exportar o arquivo.");
                } catch (Exception e) {
                    Utils.toast(mContext, "Ocorreu erro ao importar o arquivo:\r\n" + e.getMessage());
                }
            }
        }
        if (requestCode == PEGAR_NOME_ARQUIVO_IMPORTAR_REQUEST) {
            if (resultCode == RESULT_OK) {
                try {
                    //TODO: Alterar a descrição dos elementos overlay para conter o mesmo que no cadastro!
                    String nomeArquivo = (data.getExtras().getString(ARG_IMPORTAR_NOME_ARQUIVO) == "" ? "importacao.kmz" : data.getExtras().getString(ARG_IMPORTAR_NOME_ARQUIVO));
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
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean exportarArquivoMapa(String nomeArquivo, String tipoArquivo) {
        boolean resultado = false;
        File arquivo = new File(caminhoPastaMapas + nomeArquivo);
        KmlDocument kmlDocument = new KmlDocument();
        for (Overlay overlay : map.getOverlays()) {
            kmlDocument.mKmlRoot.addOverlay(overlay, kmlDocument);
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
        File arquivo = new File(caminhoPastaMapas + nomeArquivo);
        KmlDocument kmlDocument = new KmlDocument();

        if (tipoArquivo.equals("kml"))
            resultado = kmlDocument.parseKMLFile(arquivo);
        else if (tipoArquivo.equals("kmz")) {
            BufferedInputStream stream = new BufferedInputStream(new FileInputStream(arquivo));
            ZipFile zipFile = new ZipFile(arquivo);
            resultado = kmlDocument.parseKMLStream(stream, zipFile);
        } else if (tipoArquivo.equals("geojson"))
            resultado = kmlDocument.parseGeoJSON(arquivo);

        FolderOverlay kmlOverlay = (FolderOverlay) kmlDocument.mKmlRoot.buildOverlay(map, null, null, kmlDocument);
        map.getOverlays().add(kmlOverlay);

        map.invalidate();
        return resultado;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
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
                return;
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
//        if ((adicionarAreaHolder == null) && (adicionarDistanciaHolder == null) && (adicionarPontoHolder == null)) {
//            MyGeoPoint ponto = new MyGeoPoint(p);
//
//            Intent intent = new Intent(mActivity.getBaseContext(), ElementoDetailActivity.class);
//            intent.putExtra(ElementoDetailFragment.ARG_ELEMENTOID, 0);
//            intent.putExtra(ElementoDetailFragment.ARG_TIPOELEMENTOID, 1);
//            intent.putExtra(ElementoDetailFragment.ARG_CLASSEID, 1);
//            intent.putExtra(ElementoDetailFragment.ARG_GEOMETRIA, ponto.toString());
//            mActivity.startActivityForResult(intent, ElementoDetailFragment.PICK_PONTO_REQUEST);
//        }
        return true;
    }

    void carregarPontos() {
        // https://stackoverflow.com/questions/41090639/add-marker-to-osmdroid-5-5-map

        map.getOverlays().remove(geoPointList);
        ArrayList<MyOverlayItem> items = new ArrayList<MyOverlayItem>();
        //GeoPoint pontoAeroporto = new GeoPoint(-27.1341, -52.6606);
        //GeoPoint pontoDesbravador = new GeoPoint(-27.1048003, -52.6145871);
        //items.add(new OverlayItem("Aeroporto de Chapecó", "Descrição", pontoAeroporto));
        //items.add(new OverlayItem("Desbravador", "Descrição", pontoDesbravador));

        ElementoDao elementoDao = new ElementoDaoImpl(this.getBaseContext());
        List<Elemento> pontos = elementoDao.getAll();
        for (Elemento ponto : pontos) {
            if (ponto.getClasse().getClasseEnum() == ClasseEnum.PONTO) {
                items.add(new MyOverlayItem(ponto));
            }
        }

        //the overlay
        geoPointList = new MyItemizedOverlayWithFocus<MyOverlayItem>(
                this, items,
                new MyItemizedIconOverlay.OnItemGestureListener<MyOverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(int index, MyOverlayItem item) {
                        return false;
                    }

                    @Override
                    public boolean onItemLongPress(int index, MyOverlayItem item) {
                        if ((adicionarAreaHolder == null) && (adicionarDistanciaHolder == null) && (adicionarPontoHolder == null)) {
                            Intent intent = new Intent(mActivity.getBaseContext(), ElementoDetailActivity.class);
                            intent.putExtra(ARG_ELEMENTOID, item.getElemento().getElementoid());
                            mActivity.startActivityForResult(intent, ALTERAR_ELEMENTO_REQUEST);
                        }
                        return true;
                    }
                }
        );
        geoPointList.setFocusItemsOnTap(true);
        if (exibirPontos)
            map.getOverlays().add(geoPointList);
    }

    private void carregarAreas() {
        ElementoDao elementoDao = new ElementoDaoImpl(this.getBaseContext());
        List<Elemento> elementos = elementoDao.getAll();
        for (Area a : areaList) {
            a.removerDe(map);
        }
        areaList.clear();
        for (Elemento e : elementos) {
            if (e.getClasse().getClasseEnum() == ClasseEnum.AREA) {
                Area a = new Area(e);
                areaList.add(a);
                if (exibirAreas)
                    a.desenharEm(map);
            }
        }
    }

    private void carregarDistancias() {
        ElementoDao elementoDao = new ElementoDaoImpl(this.getBaseContext());
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

//         Se carregar o App sem mapas começamos posicionados próximo ao canto oposto de 0,0
//         Se tentar medir a distância através deste ponto, o label fica no canto errado do mundo
//        Area cantos = new Area();
//        cantos.adicionarPonto(new GeoPoint( 85.02,  179.6));
//        cantos.adicionarPonto(new GeoPoint( 85.02, -179.6));
//        cantos.adicionarPonto(new GeoPoint(-85.02, -179.6));
//        cantos.adicionarPonto(new GeoPoint(-85.02,  179.6));
//        cantos.desenharEm(map);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (map != null) {
            outState.putInt("zoomInicial", map.getZoomLevel());
            outState.putDouble("latitudeAtual", map.getMapCenter().getLatitude());
            outState.putDouble("longitudeAtual", map.getMapCenter().getLongitude());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        zoomInicial = savedInstanceState.getInt("zoomInicial", 0);

        double lat = savedInstanceState.getDouble("latitudeAtual", 0.0);
        double lon = savedInstanceState.getDouble("longitudeAtual", 0.0);
        if (lat != 0.0 && lon != 0.0)
            coordenadasIniciais = new GeoPoint(lat, lon);
    }

    private void carregarCamadas() {
        File pasta_camadas = new File(caminhoPastaMapas);
        FilenameFilter filtro = new FilenameFilter() {
            String[] extensoesValidas = {"kml"};

            @Override
            public boolean accept(File dir, String name) {
                String extensao = name.substring(name.lastIndexOf(".") + 1);
                extensao = extensao.toLowerCase();
                return Arrays.asList(extensoesValidas).contains(extensao);
            }
        };
        File[] listaArquivosCamadas = pasta_camadas.listFiles(filtro);
        camadaHolder.limparCamadas();
        if (listaArquivosCamadas != null) {
            for (File arquivo : listaArquivosCamadas) {
                camadaHolder.adicionarArquivo(arquivo, map);
            }
        }
    }

    //0. Using the Marker and Polyline overlays - advanced options
    class OnMarkerDragListenerDrawer implements Marker.OnMarkerDragListener {
        ArrayList<GeoPoint> mTrace;
        Polyline mPolyline;

        OnMarkerDragListenerDrawer() {
            mTrace = new ArrayList<GeoPoint>(100);
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

    //7. Customizing the bubble behaviour
    class CustomInfoWindow extends MarkerInfoWindow {
        POI mSelectedPoi;

        public CustomInfoWindow(MapView mapView) {
            super(org.osmdroid.bonuspack.R.layout.bonuspack_bubble, mapView);
            Button btn = (Button) (mView.findViewById(org.osmdroid.bonuspack.R.id.bubble_moreinfo));
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (mSelectedPoi.mUrl != null) {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mSelectedPoi.mUrl));
                        view.getContext().startActivity(myIntent);
                    } else {
                        Toast.makeText(view.getContext(), "Button clicked", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        @Override
        public void onOpen(Object item) {
            super.onOpen(item);
            mView.findViewById(org.osmdroid.bonuspack.R.id.bubble_moreinfo).setVisibility(VISIBLE);
            Marker marker = (Marker) item;
            mSelectedPoi = (POI) marker.getRelatedObject();

            //8. put thumbnail image in bubble, fetching the thumbnail in background:
            if (mSelectedPoi.mThumbnailPath != null) {
                ImageView imageView = (ImageView) mView.findViewById(org.osmdroid.bonuspack.R.id.bubble_image);
                mSelectedPoi.fetchThumbnailOnThread(imageView);
            }
        }
    }
}
