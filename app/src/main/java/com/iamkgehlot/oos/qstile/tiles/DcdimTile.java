package com.iamkgehlot.oos.qstile.tiles;

import android.content.SharedPreferences;
import android.graphics.drawable.Icon;
import android.service.quicksettings.Tile;
import android.text.TextUtils;

import androidx.preference.PreferenceManager;

import com.iamkgehlot.oos.qstile.R;

import java.io.DataOutputStream;
import java.io.IOException;


public class DcdimTile extends BaseTileService {

    @Override
    public void onClick() {
        handleActionSystemPowerDialog();
        super.onClick();
    }

    @Override
    protected void updateTileResources() {
        if (DcdimTile.this.getQsTile() != null) {
            Tile tile = DcdimTile.this.getQsTile();
            tile.setIcon(Icon.createWithResource(getApplicationContext(), R.drawable.ic_system_power_dialog_white_24dp));
            tile.setLabel("GAME-MODE");
            tile.updateTile();
        }
    }

    private void  handleActionSystemPowerDialog(){
        if (isCollapseAndOpenSystemPowerDialog()) {
            sendBroadcastToCloseSystemDialogs();
            performAccessibilityServiceDCDIM();
        } else {
            performAccessibilityServiceDCDIM();
        }
    }
    public static void sudo(String... strings) {
        try {
            Process su = Runtime.getRuntime().exec("su");
            DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());

            for (String s : strings) {
                outputStream.writeBytes(s + "\n");
                outputStream.flush();
            }

            outputStream.writeBytes("exit\n");
            outputStream.flush();
            try {
                su.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void performAccessibilityServiceDCDIM() {
        Tile tile = getQsTile();
        if (tile.getState() == 1) {
            try {
                tile.setState(2);
                tile.updateTile();
                basic.writeLine("/proc/touchpanel/game_switch_enable","1");
                basic.writeLine("/proc/touchpanel/oppo_tp_direction","1");
                basic.writeLine("/proc/touchpanel/oppo_tp_limit_enable","0");
                sudo("settings put global oneplus_screen_refresh_rate 0");

            } catch (Exception e) {
                catchException(e);
                showSomethingWentWrong();
            }
        } else if (tile.getState() == 2) {
            try {
                tile.setState(1);
                tile.updateTile();
                basic.writeLine("/proc/touchpanel/game_switch_enable","0");
                basic.writeLine("/proc/touchpanel/game_switch_enable","0");
                basic.writeLine("/proc/touchpanel/oppo_tp_limit_enable","1");

            } catch (Exception e2) {
                catchException(e2);
                showSomethingWentWrong();
            }
        }
    }
    private boolean isCollapseAndOpenSystemPowerDialog() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(DcdimTile.this);
        String action = sharedPreferences.getString(getString(R.string.key_system_power_dialog_tile_action), getString(R.string.key_system_power_dialog_tile_action_collapse_show_dialog));
        return TextUtils.equals(action, getString(R.string.key_system_power_dialog_tile_action_collapse_show_dialog));
    }

}