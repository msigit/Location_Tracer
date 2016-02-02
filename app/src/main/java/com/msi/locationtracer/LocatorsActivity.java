package com.msi.locationtracer;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by sahid_000 on 2/2/2016.
 */
public class LocatorsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locators_list);
        setTitle("Peoples");
    }
}
