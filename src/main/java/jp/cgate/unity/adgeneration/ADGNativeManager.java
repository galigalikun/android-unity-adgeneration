package jp.cgate.unity.adgeneration;

import com.unity3d.player.UnityPlayer;
import com.socdm.d.adgeneration.ADG;
import com.socdm.d.adgeneration.ADG.AdFrameSize;
import com.socdm.d.adgeneration.ADGListener;

import android.view.Gravity;
import android.app.Activity;
import android.widget.FrameLayout;
import android.view.ViewGroup.LayoutParams;
import android.view.View;
// import android.graphics.Color;
import android.util.Log;


public class ADGNativeManager {

    enum Horizontal {
        LEFT(1<<0),
        CENTER_HORIZONTAL(1<<1),
        RIGHT(1<<2);
        private int value;
        private Horizontal(int value) {
            this.value = value;
        }
        public boolean is(int value) {
            return ((this.value & value) == this.value);
        }
    }
    enum Vertical {
        TOP(1 << 0),
        CENTER_VERTICAL(1 << 1),
        BOTTOM(1 << 2);
        private int value;
        private Vertical(int value) {
            this.value = value;
        }
        public boolean is(int value) {
            return ((this.value & value) == this.value);
        }
    }
    enum Size {
        SP_320x50(0),
        LARGE_320x100(1),
        RECT_300x250(2),
        TABLET_728x90(3),
        FREE(4);
        private int value;
        private Size(int value) {
            this.value = value;
        }
        public boolean is(int value) {
            return this.value == value;
        }
    }

    private static final String TAG = ADGNativeManager.class.getSimpleName();
    private FrameLayout layout = null;
    private ADG adg;

    public ADGNativeManager() {
    }

    public ADGNativeManager instance() {
        return new ADGNativeManager();
    }

    public void initADG(final String adid, final int adtype, final int horizontal, final int vertical, final String gameObject, final int width, final int height) {
        final Activity a = UnityPlayer.currentActivity;
        a.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (layout == null) {
                    layout = new FrameLayout(a);
                    // layout.setBackgroundColor(Color.argb(50, 0, 0, 255));
                    a.addContentView(layout, new LayoutParams(
                                LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
                }

                Log.d(TAG, "ADG start");
                adg = new ADG(a);
                adg.setLocationId(adid);
                if(Size.SP_320x50.is(adtype)) {
                    adg.setAdFrameSize(AdFrameSize.SP);
                } else if(Size.LARGE_320x100.is(adtype)) {
                    adg.setAdFrameSize(AdFrameSize.LARGE);
                } else if(Size.RECT_300x250.is(adtype)) {
                    adg.setAdFrameSize(AdFrameSize.RECT);
                } else if(Size.TABLET_728x90.is(adtype)) {
                    adg.setAdFrameSize(AdFrameSize.TABLET);
                } else if(Size.FREE.is(adtype)) {
                    adg.setAdFrameSize(AdFrameSize.FREE.setSize(width, height));
                } else {
                    throw new IllegalArgumentException("adtype error");
                }
                Log.d(TAG, "ADG setAdListener");
                adg.setAdListener(new ADGListener() {
                    @Override
                    public void onReceiveAd() {
                        UnityPlayer.UnitySendMessage(gameObject, "ADGReceiveAd", "");
                    }
                    @Override
                    public void onFailedToReceiveAd() {
                        UnityPlayer.UnitySendMessage(gameObject, "ADGFailedToReceiveAd", "");
                    }
                    @Override
                    public void onInternalBrowserOpen() {
                        UnityPlayer.UnitySendMessage(gameObject, "ADGBrowserShow", "");
                    }
                    @Override
                    public void onInternalBrowserClose() {
                        UnityPlayer.UnitySendMessage(gameObject, "ADGBrowserClose", "");
                    }
                    @Override
                    public void onVideoPlayerStart() {
                        UnityPlayer.UnitySendMessage(gameObject, "ADGVideoShow", "");
                    }
                    @Override
                    public void onVideoPlayerEnd() {
                        UnityPlayer.UnitySendMessage(gameObject, "ADGVideoDisappear", "");
                    }
                });
                layout.addView(adg, new FrameLayout.LayoutParams(
                            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                            getGravity(horizontal, vertical)));
                Log.d(TAG, "ADG end");
            }
        });
    }

    private int getGravity(int horizontal, int vertical) {
        int gravity = Gravity.NO_GRAVITY;
        if(Horizontal.LEFT.is(horizontal)) {
            gravity |= Gravity.LEFT;
        }
        if(Horizontal.CENTER_HORIZONTAL.is(horizontal)) {
            gravity |= Gravity.CENTER_HORIZONTAL;
        }
        if(Horizontal.RIGHT.is(horizontal)) {
            gravity |= Gravity.RIGHT;
        }
        if(Vertical.TOP.is(vertical)) {
            gravity |= Gravity.TOP;
        }
        if(Vertical.CENTER_VERTICAL.is(vertical)) {
            gravity |= Gravity.CENTER_VERTICAL;
        }
        if(Vertical.BOTTOM.is(vertical)) {
            gravity |= Gravity.BOTTOM;
        }
        return gravity;
    }

    public void changeLocationADG(final int horizontal, final int vertical, final float left, final float top, final float right, final float bottom) {
        final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                            getGravity(horizontal, vertical));
        params.setMargins((int)left, (int)top, (int)right, (int)bottom);

        final Activity a = UnityPlayer.currentActivity;
        a.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(adg != null) {
                    adg.setLayoutParams(params);
                }
            }
        });
    }

    public void resumeADG() {
        final Activity a = UnityPlayer.currentActivity;
        a.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(adg != null) {
                    adg.start();
                }
            }
        });
    }
    public void pauseADG() {
        final Activity a = UnityPlayer.currentActivity;
        a.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(adg != null) {
                    adg.stop();
                }
            }
        });
    }
    public void showADG() {
        final Activity a = UnityPlayer.currentActivity;
        a.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(adg != null) {
                    adg.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    public void hideADG() {
        final Activity a = UnityPlayer.currentActivity;
        a.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(adg != null) {
                    adg.setVisibility(View.GONE);
                }
            }
        });
    }
    public void finishADG() {
        Activity a = UnityPlayer.currentActivity;
        a.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (adg != null) {
                    layout.removeView(adg);
                    adg.stop();
                    adg.setAdListener(null);
                    adg = null;
                }
            }
        });
    }
}
