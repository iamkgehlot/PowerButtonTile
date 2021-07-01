package com.iamkgehlot.oos.qstile;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.iamkgehlot.oos.qstile.ui.MessageFragment;
import com.iamkgehlot.oos.qstile.ui.SettingsFragment;
import com.iamkgehlot.oos.qstile.utils.PowerButtonTileConstants;
import com.iamkgehlot.oos.qstile.R;

import java.io.DataOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String MESSAGE_FRAGMENT_TAG = "MESSAGE_FRAGMENT_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // always inflate SettingsFragment
        SettingsFragment settingsFragment = new SettingsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_main_frame_layout, settingsFragment)
                .commit();
        // check if any message fragment needs to be inflated
        if (getIntent().getStringExtra(PowerButtonTileConstants.MESSAGE_CONSTANT) != null
                && !TextUtils.isEmpty(getIntent().getStringExtra(PowerButtonTileConstants.MESSAGE_CONSTANT))) {
            inflateTileSettingsFragment(getIntent().getStringExtra(PowerButtonTileConstants.MESSAGE_CONSTANT));
        }
    }

    private void inflateTileSettingsFragment(String stringExtra) {
        MessageFragment messageFragment = MessageFragment.newInstance(stringExtra);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_main_frame_layout, messageFragment, MESSAGE_FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();
    }
}