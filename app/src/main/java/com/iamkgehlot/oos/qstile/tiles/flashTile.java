package com.iamkgehlot.oos.qstile.tiles;

import android.graphics.drawable.Icon;
import android.service.quicksettings.Tile;

import com.iamkgehlot.oos.qstile.R;

import java.io.DataOutputStream;
import java.io.IOException;

public class flashTile extends BaseTileService {

    @Override
    public void onClick() {
        handleActionflash();
        super.onClick();
    }
    /* access modifiers changed from: protected */
    @Override // com.iamkgehlot.oos.qstile.tiles.BaseTileService
    public void updateTileResources() {
        if (getQsTile() != null) {
            Tile tile = getQsTile();
            tile.setIcon(Icon.createWithResource(getApplicationContext(), (int) R.drawable.ic_sleep_white_24dp));
            tile.setLabel("sRGB");
            tile.updateTile();
        }
    }

    private void handleActionflash() {

                    performAccessibilityflash();

    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void performAccessibilityflash() {
        Tile tile = getQsTile();
        if (tile.getState() == 1) {
            try {
                tile.setState(2);
                basic.writeLine("/sys/kernel/oppo_display/seed","1");
                tile.updateTile();
            } catch (Exception e) {
                catchException(e);
                showSomethingWentWrong();
            }
        } else if (tile.getState() == 2) {
            try {
                tile.setState(1);
                basic.writeLine("/sys/kernel/oppo_display/seed","0");
                tile.updateTile();
            } catch (Exception e2) {
                catchException(e2);
                showSomethingWentWrong();
            }
        }
    }


}
