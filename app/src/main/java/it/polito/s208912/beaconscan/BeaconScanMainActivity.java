package it.polito.s208912.beaconscan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class BeaconScanMainActivity extends AppCompatActivity {

    public static final String TAG = "BeaconScanMain";
    private BeaconManager bm;
    private Region region;

    private List<Beacon> lastSeenBeacons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_scan_main);

        this.bm = new BeaconManager(getApplicationContext());
        this.region = new Region("ranged region",
                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);
        this.lastSeenBeacons = new LinkedList<Beacon>();

        this.bm.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                Log.i(TAG, "Start Ranging");
                bm.startRanging(region);
            }
        });

        this.bm.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    Beacon nearestBeacon = list.get(0);

                    updateBeacons(list);

                    Log.d(TAG, "Nearest beacon: " + nearestBeacon.toString());
                }
                else {
                    Log.d(TAG, "No beacon visible");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check if the user gave Bluetooth permission
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
    }

    @Override
    protected void onDestroy() {
        this.bm.stopRanging(this.region);
        Log.i(TAG, "Stop Ranging");
        this.bm.disconnect();
        super.onDestroy();
        setContentView(R.layout.activity_beacon_scan_main);
    }

    /**
     * Update the list of beacons to show only the beacons
     * seen in the last Ranging
     * @param beacons
     */
    private void updateBeacons(List<Beacon> beacons){

    }
}
