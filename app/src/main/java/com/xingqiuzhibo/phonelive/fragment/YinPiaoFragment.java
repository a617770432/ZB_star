package com.xingqiuzhibo.phonelive.fragment;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.xingqiuzhibo.phonelive.HtmlConfig;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.utils.L;

/**
 * A simple {@link Fragment} subclass.
 */
public class YinPiaoFragment extends Fragment implements View.OnClickListener{

    private ProgressBar mProgressBar;
    private WebView mWebView;
    private String mLiveUid;

    public YinPiaoFragment() {
        // Required empty public constructor
    }

    public static YinPiaoFragment newInstance(String liveUid){
        YinPiaoFragment fragment = new YinPiaoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("liveUid" , liveUid);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mLiveUid = getArguments().getString("liveUid");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_yin_piao, container, false);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        mWebView = view.findViewById(R.id.web_view);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mWebView.setLayoutParams(params);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                L.e("H5-------->" + url);
                view.loadUrl(url);
                return true;
            }

        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress >= 70) {
                    if(mProgressBar.getVisibility()==View.VISIBLE){
                        mProgressBar.setVisibility(View.GONE);
                    }
                } else {
                    mProgressBar.setProgress(newProgress);
                }
            }
        });
        mWebView.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= 21) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebView.loadUrl(HtmlConfig.LIVE_LIST + mLiveUid);
        return view;
    }

    @Override
    public void onClick(View view) {

    }

}
