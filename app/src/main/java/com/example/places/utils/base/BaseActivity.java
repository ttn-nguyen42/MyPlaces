package com.example.places.utils.base;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

abstract public class BaseActivity<B, V> extends AppCompatActivity {
    B mBinding;
    V mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind();
        init();
        listen();
    }

    protected abstract void bind();
    protected abstract void init();
    protected abstract void listen();
}
