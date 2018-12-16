package br.com.neogis.agritopo.activity;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.MenuItem;

import com.github.danielnilsson9.colorpickerview.dialog.ColorPickerDialogFragment;
import com.github.danielnilsson9.colorpickerview.preference.ColorPreference;

import java.util.List;

import br.com.neogis.agritopo.R;
import br.com.neogis.agritopo.dao.tabelas.ChaveSerial;
import br.com.neogis.agritopo.dao.tabelas.Fabricas.FabricaSincronizacaoDao;
import br.com.neogis.agritopo.dao.tabelas.Integracao.Sincronizacao;
import br.com.neogis.agritopo.dao.tabelas.Integracao.SincronizacaoDao;
import br.com.neogis.agritopo.service.SerialKeyService;
import br.com.neogis.agritopo.utils.DateUtils;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class ConfiguracaoActivity extends AppCompatPreferenceActivity implements ColorPickerDialogFragment.ColorPickerDialogListener {
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static final int PREFERENCE_DIALOG_COLOR_ID = 1;
    private static PreferenceFragment preferenceFragment = null;

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    public void onPause() {
        //Recarega as configurações ao sair das configurações
        br.com.neogis.agritopo.singleton.Configuration.getInstance().LoadConfiguration(this);
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    @Override
    public void onColorSelected(int dialogId, int color) {
        switch(dialogId) {
            case PREFERENCE_DIALOG_COLOR_ID:
                ((ColorPickerDialogFragment.ColorPickerDialogListener)preferenceFragment)
                        .onColorSelected(dialogId, color);
                break;
        }
    }

    @Override
    public void onDialogDismissed(int dialogId) {

        switch(dialogId) {
            case PREFERENCE_DIALOG_COLOR_ID:
                ((ColorPickerDialogFragment.ColorPickerDialogListener)preferenceFragment)
                        .onDialogDismissed(dialogId);

                break;
        }
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || MappingPreferenceFragment.class.getName().equals(fragmentName)
                || DataSyncPreferenceFragment.class.getName().equals(fragmentName)
                || NotificationPreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);
            preferenceFragment = this;

            //bindPreferenceSummaryToValue(findPreference("example_text"));
            //bindPreferenceSummaryToValue(findPreference("example_list"));
            bindPreferenceSummaryToValue(findPreference(getResources().getString(R.string.pref_key_measure_area)));
            bindPreferenceSummaryToValue(findPreference(getResources().getString(R.string.pref_key_diretorio_exportar_arquivos)));
            bindPreferenceSummaryToValue(findPreference(getResources().getString(R.string.pref_key_diretorio_leitura_arquivos)));

            Preference seletorDiretorio = findPreference(getResources().getString(R.string.pref_key_diretorio_exportar_arquivos));
            seletorDiretorio.setOnPreferenceChangeListener(this);

            seletorDiretorio = findPreference(getResources().getString(R.string.pref_key_diretorio_leitura_arquivos));
            seletorDiretorio.setOnPreferenceChangeListener(this);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object o)
        {
            if(preference.getKey().equals(getResources().getString(R.string.pref_key_diretorio_exportar_arquivos)) ||
                    preference.getKey().equals(getResources().getString(R.string.pref_key_diretorio_leitura_arquivos)))
            {
                String value=(String)o;
                String diretorios[]=value.split(":");
                preference.setSummary(diretorios[0]);

                SharedPreferences.Editor editor = preference.getEditor();
                editor.putString(preference.getKey(), diretorios[0]);
                editor.commit();
            }
            return false;
        }
    }

    //https://github.com/danielnilsson9/color-picker-view <- Origem do Color Picker
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class MappingPreferenceFragment extends PreferenceFragment implements ColorPickerDialogFragment.ColorPickerDialogListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_mapping);
            setHasOptionsMenu(true);
            preferenceFragment = this;

            ColorPreference pref = (ColorPreference) findPreference(getResources().getString(R.string.pref_key_color_cursor));
            pref.setOnShowDialogListener(new ColorPreference.OnShowDialogListener() {

                @Override
                public void onShowColorPickerDialog(String title, int currentColor) {
                    ColorPickerDialogFragment dialog = ColorPickerDialogFragment
                            .newInstance(PREFERENCE_DIALOG_COLOR_ID, "Selecione a cor", null, currentColor, false);

                    dialog.show(getFragmentManager(), "pre_dialog");
                }
            });
        }

        @Override
        public void onColorSelected(int dialogId, int color) {
            switch (dialogId) {
                case PREFERENCE_DIALOG_COLOR_ID:
                    //salva a cor selecionada
                    ColorPreference pref = (ColorPreference) findPreference(getResources().getString(R.string.pref_key_color_cursor));
                    pref.saveValue(color);
                    break;
            }
        }

        @Override
        public void onDialogDismissed(int dialogId) {
            // Nothing to do.
        }
    }

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_notification);
            setHasOptionsMenu(true);
            preferenceFragment = this;
            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("notifications_new_message_ringtone"));
        }
    }

    /**
     * This fragment shows data and sync preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class DataSyncPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_data_sync);
            setHasOptionsMenu(true);
            preferenceFragment = this;

            final Preference sincPref = findPreference( getResources().getString(R.string.pref_key_ultima_sincronizacao) );

            SerialKeyService serviceKey = new SerialKeyService(getActivity().getApplicationContext());

            if (serviceKey.containsValidSerialKey(ChaveSerial.LicencaTipo.Pago) || serviceKey.containsValidSerialKey(ChaveSerial.LicencaTipo.Gratuito)) {
                SincronizacaoDao sincDao = FabricaSincronizacaoDao.Criar(getActivity().getApplicationContext());
                Sincronizacao sinc = sincDao.get(1);
                if(sinc != null && sinc.getData().getTime() > 0)
                    sincPref.setTitle("Sincronizado em: " + DateUtils.formatDate(sinc.getData()));
                else
                    sincPref.setTitle("Sincronizado em: NUNCA");

                sincPref.setOnPreferenceClickListener( new Preference.OnPreferenceClickListener()
                {
                    public boolean onPreferenceClick( Preference pref )
                    {
                        Intent it = new Intent("br.com.neogis.agritopo.receiver.integracaoreceiver");
                        getActivity().getApplicationContext().sendBroadcast(it);
                        getActivity().finish();
                        return true;
                    }
                } );
            }else{
                sincPref.setTitle("Indisponível neste tipo de licença.");
            }
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), ConfiguracaoActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }
}
