package me.vesh.egg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private EggView mEggView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEggView = (EggView) findViewById(R.id.egg_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mEggView.resume();
    }

    @Override
    protected void onPause() {
        mEggView.pause();
        super.onPause();
    }

}
