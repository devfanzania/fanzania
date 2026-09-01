package com.yorker.fanzania.views.screens.webview;

import android.annotation.SuppressLint;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.databinding.ActivityWebviewBinding;
import com.yorker.fanzania.dependencyinjection.PresenterComponent;
import com.yorker.fanzania.views.shared.activity.BaseActivity;

public class WebviewActivity extends BaseActivity<WebviewPresenter> implements WebviewPresenter.IMainView {

    private WebviewPresenter presenter;
    private String URL = "";
    private ActivityWebviewBinding binding;

    @Override
    protected WebviewPresenter onCreatePresenter() {
        presenter = new WebviewPresenter(this, this);
        return presenter;
    }

    @Override
    protected void injectPresenter(PresenterComponent component, WebviewPresenter presenter) {
        WebviewComponent webviewComponent = DaggerWebviewComponent.builder()
                .presenterComponent(component)
                .webviewApplicationModule(new WebviewApplicationModule(WebviewActivity.this))
                .build();
        webviewComponent.inject(presenter);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_webview);

        binding.inToolbar.toolbar.setNavigationIcon(R.drawable.ic_back_white);
        binding.inToolbar.toolbar.setTitle("");

        WebSettings webSettings = binding.webview.getSettings();
        binding.webview.getSettings().setBuiltInZoomControls(true);
        binding.webview.getSettings().setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true);

        webSettings.setLoadWithOverviewMode(true);
        webSettings.setAllowFileAccess(true);

        if (getIntent() != null) {
            switch (getIntent().getStringExtra(Constants.TAG_INTENTKEY)) {
                case Constants.TAG_FAQ:

                    URL = presenter.getURL(Constants.TAG_FAQ);
                    binding.inToolbar.toolbarTitle.setText(getString(R.string.title_faqs));
                    binding.webview.loadUrl(URL);
                    break;
                case Constants.TAG_ABOUTUS:
                    URL = presenter.getURL(Constants.TAG_ABOUTUS);
                    binding.inToolbar.toolbarTitle.setText(getString(R.string.title_aboutus));
                    binding.webview.loadUrl(URL);
                    break;
                case Constants.TAG_HOWTOPLAY:
                    URL = presenter.getURL(Constants.TAG_HOWTOPLAY);
                    binding.inToolbar.toolbarTitle.setText(getString(R.string.title_gamerule));
                    break;
                case Constants.TAG_PRIVACYNOTICE:
                    URL = presenter.getURL(Constants.TAG_PRIVACYNOTICE);
                    break;
                case Constants.TAG_POINTRULES:
                    URL = presenter.getURL(Constants.TAG_POINTRULES);
                    binding.inToolbar.toolbarTitle.setText(getString(R.string.title_gamerule));
                    binding.webview.loadUrl(URL);
                    break;
                case Constants.TAG_TNC:
                    URL = presenter.getURL(Constants.TAG_TNC);
                    binding.inToolbar.toolbarTitle.setText(getString(R.string.title_termscondition));
                    binding.webview.loadUrl(URL);
                    break;
                case Constants.TAG_TEAMCOMPOSITIONRULE:
                    URL = presenter.getURL(Constants.TAG_TEAMCOMPOSITIONRULE);
                    binding.inToolbar.toolbarTitle.setText(getString(R.string.title_Teamrule));
                    break;
                case Constants.TAG_CONTACTUS:
                    URL = "https://www.fanzania.com/Home/Contact";
                    binding.inToolbar.toolbarTitle.setText(getString(R.string.text_contactus));
                    break;
            }

            binding.webview.loadUrl(URL);
        } else {
            onBackPressed();
        }

        setSupportActionBar(binding.inToolbar.toolbar);

        if (binding.inToolbar.toolbar != null) {
            binding.inToolbar.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }

        binding.webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                binding.webview.setVisibility(View.VISIBLE);
                binding.pBar.setVisibility(View.GONE);
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            }
        });
    }

    @Override
    public void RetryResponse(String type) {

    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
