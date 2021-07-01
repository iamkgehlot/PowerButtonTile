package com.iamkgehlot.oos.qstile.tiles;

import android.content.SharedPreferences;
import android.graphics.drawable.Icon;
import android.service.quicksettings.Tile;
import android.text.TextUtils;

import androidx.preference.PreferenceManager;

import com.iamkgehlot.oos.qstile.R;

import java.io.DataOutputStream;
import java.io.IOException;

public class f90Tile extends BaseTileService {

    @Override
    public void onClick() {
        handleActionScreenshot();
        super.onClick();
    }

    @Override
    protected void updateTileResources() {
        if (f90Tile.this.getQsTile() != null) {
            Tile tile = f90Tile.this.getQsTile();
            tile.setIcon(Icon.createWithResource(getApplicationContext(), R.drawable.ic_screenshot_white_24dp));
            tile.setLabel("Force 90Hz");
        }
    }

    private void handleActionScreenshot() {
        if (isCollapseAndTakeScreenshot()) {
            sendBroadcastToCloseSystemDialogs();
            performAccessibilityServiceScreenshot();

        } else {
            performAccessibilityServiceScreenshot();
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

    private void performAccessibilityServiceScreenshot() {
        Tile tile = getQsTile();
        if (tile.getState() == 1) {
            try {
                tile.setState(2);
                tile.updateTile();
                sudo("settings put global oneplus_screen_refresh_rate 0");

            } catch (Exception e) {
                catchException(e);
                showSomethingWentWrong();
            }
        } else if (tile.getState() == 2) {
            try {
                tile.setState(1);
                tile.updateTile();
                sudo("settings put global oneplus_screen_refresh_rate 1");

            } catch (Exception e2) {
                catchException(e2);
                showSomethingWentWrong();
            }
        }
    }
    private boolean isCollapseAndTakeScreenshot() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(f90Tile.this);
        String action = sharedPreferences.getString(getString(R.string.key_screenshot_tile_action), getString(R.string.key_screenshot_tile_action_collapse_take_screenshot));
        return TextUtils.equals(action, getString(R.string.key_screenshot_tile_action_collapse_take_screenshot));
    }

}
