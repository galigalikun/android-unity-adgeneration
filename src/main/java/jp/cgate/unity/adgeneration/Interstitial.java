package jp.cgate.unity.adgeneration;

import com.unity3d.player.UnityPlayer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Gravity;
import android.util.Log;
import android.widget.FrameLayout;
import android.view.ViewGroup.LayoutParams;
import android.os.Bundle;

import com.socdm.d.adgeneration.ADG;
import com.socdm.d.adgeneration.ADG.AdFrameSize;
import com.socdm.d.adgeneration.ADGListener;

public class Interstitial {

    private static final String TAG = Interstitial.class.getSimpleName();

	public Interstitial() {
        Log.d(TAG, "Interstitial");
	}

    public void show(final String adid) {
        Log.d(TAG, "Show");
		UnityPlayer.currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Bundle bundle = new Bundle();
                bundle.putString("adid", adid);
                _show(bundle);
            }
        });
    }

    private void _show(final Bundle bundle) {
		final Activity a = UnityPlayer.currentActivity;
        Log.d(TAG, "_Show");
        FrameLayout frame = new FrameLayout(a);
        ADG adg = new ADG(a);
        adg.setLocationId(bundle.getString("adid"));
        adg.setAdFrameSize(AdFrameSize.RECT);
        adg.setAdListener(new ADGListener() {
            @Override
            public void onReceiveAd() {
            }
            @Override
            public void onFailedToReceiveAd() {
            }
            @Override
            public void onInternalBrowserOpen() {
            }
            @Override
            public void onInternalBrowserClose() {
            }
            @Override
            public void onVideoPlayerStart() {
            }
            @Override
            public void onVideoPlayerEnd() {
            }
        });
        frame.addView(adg, new FrameLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
            Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL));

		AlertDialog.Builder dialog = new AlertDialog.Builder(a)
		.setView(frame);

        dialog.setNegativeButton("閉じる", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.create().show();
    }

}
