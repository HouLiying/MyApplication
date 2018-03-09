package com.example.myapplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.net.URL;

public class MainActivity extends Activity {

    private static final String LOGTAG = "MainActivity";
    private WebView mWebView;
    private boolean isLoadUrl =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView=findViewById(R.id.mywebview);
        WebSettings webSettings =mWebView.getSettings();
        Toast.makeText(getApplicationContext(),getResources().getString(R.string.unclick),Toast.LENGTH_SHORT).show();
        webSettings.setJavaScriptEnabled(true);
        mWebView.loadUrl("file:///android_asset/js_java_interaction.html");
        //注入接口
        //js_java_interaction.html文件中的toastMessage(),sumToJava()方法中的window后面的字段
        mWebView.addJavascriptInterface(new JsInteration(),"control");
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                testMethod(view);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                System.out.println("====shouldOverrideUrlLoading===="+url);
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.click),Toast.LENGTH_SHORT).show();
                if(url.startsWith("http://m.baidu.com/")){
                    return false;
                }
                return true;
            }

            /*@Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    System.out.println("====shouldOverrideUrlLoading====" + request.toString());
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.click), Toast.LENGTH_SHORT).show();
                    System.out.println("被点击了");
                    if (request.toString().startsWith("http://m.baidu.com/")) {
                        return false;
                    }
                    return true;
                    //return super.shouldOverrideUrlLoading(view, request);
                }*/
        });
    }

    private void testMethod(WebView webView) {
     //只能一次
     // String call = "javascript:sayHello()";
        // 其中\"表示"本身作为一个字符，而不是作用符号
        //String call = "javascript:alertMessage(\""+"content"+"\")";

        //String call = "javascript:toastMessage(\"" + "content" + "\")";

        String call = "javascript:sumToJava(1,2)";
        webView.loadUrl(call);

    }

    //返回结果的处理()
    public class JsInteration {

        @JavascriptInterface
        public void toastMessage(String message) {
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
        }
        //      onSumResult字段和 window.control.onSumResult(number1 + number2)中的onSumResult一样
        @JavascriptInterface
        public void onSumResult(int result) {
            Log.i(LOGTAG, "onSumResult result=" + result);
        }
    }
}
