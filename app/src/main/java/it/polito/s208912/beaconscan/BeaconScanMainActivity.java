package it.polito.s208912.beaconscan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;

import java.util.List;
import java.util.UUID;

public class BeaconScanMainActivity extends AppCompatActivity {

    private BeaconManager bm;
    private Region region;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_scan_main);

        this.bm = new BeaconManager(getApplicationContext());
        this.region = new Region("ranged region",
                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);

        this.bm.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                bm.startRanging(region);
            }
        });

        this.bm.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    Beacon nearestBeacon = list.get(0);
                    // TODO: update the UI here
                    Log.d("Airport", "Nearest beacon: " + nearestBeacon.toString());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        SystemRequirementsChecker.checkWithDefaultDialogs(this);
    }

    @Override
    protected void onDestroy() {
        this.bm.stopRanging(this.region);
        this.bm.disconnect();
        super.onDestroy();
        setContentView(R.layout.activity_beacon_scan_main);
    }
}
