

package com.zhaoweihao.architechturesample.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;

import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.base.BaseActivity;

/**
*@description 个人-设置-关于-许可
*@author
*@time 2019/1/28 13:20
*/
public class PersonalSettingAboutLicenseActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_open_source_license);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp);

        WebView webView = findViewById(R.id.web_view);
        webView.loadUrl("file:///android_asset/licenses.html");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
}
