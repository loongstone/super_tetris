package com.loongstone.supertetris;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.loongstone.supertetris.view.Tetris;

/**
 * @author loongstone
 * @date 2017/11/25 21:59
 */
public class MainActivity extends Activity {
    private static final String TAG = "Main";
    private TetrisView tetrisView;
    private Tetris tetris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tetrisView = findViewById(R.id.tv);
        initSeekBar();
        tetris = new Tetris(tetrisView);
    }

    private void initSeekBar() {
        SeekBar seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.d(TAG, "onProgressChanged: " + i);
                tetrisView.setCellCountOnWidth(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void startGame(View view) {
        tetris.startGame();
    }

    public void pauseGame(View view) {
        tetris.pauseGame();
    }

    public void turnRight(View view) {
        tetris.turnRight();
    }

    public void turnLeft(View view) {
        tetris.turnLeft();
    }

    public void shiftSharp(View view) {
        tetris.shift();
    }

    @Override
    protected void onStop() {
        tetris.pauseGame();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        tetris.stopGame();
        super.onDestroy();
    }

}
