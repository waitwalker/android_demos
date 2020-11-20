package com.etiantian.datastructure;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int f1 = f(1);
        int f2 = f(2);
        int f3 = f(3);
        int f10 = f(10);
        Log.d("1:", "f1:" + f1);
        Log.d("1:", "f2:" + f2);
        Log.d("1:", "f3:" + f3);
        Log.d("1:", "f10:" + f10);
    }

    ///
    /// @description 递归思想演练
    /// @param 
    /// @return 
    /// @author waitwalker
    /// @time 11/20/20 8:36 AM
    ///
    private int f(int x) {
        if (x == 0) {
            return 0;
        }
        return 2 * f(x - 1) + x * x;
    }
}