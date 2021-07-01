package com.iamkgehlot.oos.qstile.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.iamkgehlot.oos.qstile.tiles.DcdimTile;
import com.iamkgehlot.oos.qstile.tiles.f90Tile;
import com.iamkgehlot.oos.qstile.tiles.flashTile;
import com.iamkgehlot.oos.qstile.utils.PowerButtonTileUtils;
import com.iamkgehlot.oos.qstile.R;

import java.io.DataOutputStream;
import java.io.IOException;

public class SettingsFragment extends PreferenceFragmentCompat {
    private SwitchPreference accessibilityServiceSwitchPreference;
    private SwitchPreference f90TileSwitchPreference;
    private SwitchPreference flashTileSwitchPreference;
    private SwitchPreference DcdimTileSwitchPreference;

    private ComponentName f90TileComponentName;
    private ComponentName flashTileComponentName;
    private ComponentName DcdimTileComponentName;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_fragment);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        f90TileComponentName = new ComponentName(getActivity(), f90Tile.class);
        flashTileComponentName = new ComponentName(getActivity(), flashTile.class);
        DcdimTileComponentName = new ComponentName(getActivity(), DcdimTile.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        accessibilityServiceSwitchPreference = getPreferenceManager().findPreference(getString(R.string.key_component_accessibility_service));
        if (accessibilityServiceSwitchPreference != null) {
            accessibilityServiceSwitchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                    return false;
                }
            });

        }

        // components category
        PreferenceCategory preferenceCategoryScreenshot = getPreferenceManager().findPreference(getString(R.string.key_settings_category_screenshot));
        PreferenceCategory preferenceCategorySleep = getPreferenceManager().findPreference(getString(R.string.key_settings_category_sleep));
        PreferenceCategory preferenceCategorySystemPowerDialog = getPreferenceManager().findPreference(getString(R.string.key_settings_category_system_power_dialog));

        // screenshot
        f90TileSwitchPreference = getPreferenceManager().findPreference(getString(R.string.key_component_screenshot_tile));
        if (f90TileSwitchPreference != null) {
            f90TileSwitchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean value = (Boolean) newValue;
                    PowerButtonTileUtils.setComponentEnabled(getActivity(), f90TileComponentName, value);
                    return true;
                }
            });
        }

        // sleep
        flashTileSwitchPreference = getPreferenceManager().findPreference(getString(R.string.key_component_sleep_tile));
        if (flashTileSwitchPreference != null) {
            flashTileSwitchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean value = (Boolean) newValue;
                    PowerButtonTileUtils.setComponentEnabled(getActivity(), flashTileComponentName, value);
                    return true;
                }
            });
        }

        // system power dialog
        DcdimTileSwitchPreference = getPreferenceManager().findPreference(getString(R.string.key_component_system_power_dialog_tile));
        if (DcdimTileSwitchPreference != null) {
            DcdimTileSwitchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean value = (Boolean) newValue;
                    PowerButtonTileUtils.setComponentEnabled(getActivity(), DcdimTileComponentName, value);
                    return true;
                }
            });
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateAccessibilityServiceSwitch();
        updateComponents();
    }

    private void updateComponents() {
        // update api visibilities
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            getPreferenceScreen().removePreference(flashTileSwitchPreference);
            getPreferenceScreen().removePreference(f90TileSwitchPreference);
        }
        f90TileSwitchPreference.setChecked(isComponentEnabled(f90TileComponentName));
        flashTileSwitchPreference.setChecked(isComponentEnabled(flashTileComponentName));
        DcdimTileSwitchPreference.setChecked(isComponentEnabled(DcdimTileComponentName));
    }

    private void updateAccessibilityServiceSwitch() {
        accessibilityServiceSwitchPreference.setChecked(PowerButtonTileUtils.isAccessibilityServiceEnabled(getActivity()));
    }

    private boolean isComponentEnabled(ComponentName componentName) {
        return PowerButtonTileUtils.isComponentEnabled(getActivity(), componentName);
    }
}
