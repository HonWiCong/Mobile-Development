package com.example.assignment4_task1.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.example.assignment4_task1.R

class WebViewFragment : Fragment() {
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_web_view, container, false)
        webView = view.findViewById(R.id.web_view)

        // Get the URL from arguments
        val url = arguments?.getString("url")

        // Load the URL in the WebView
        if (url != null) {
            webView.loadUrl(url)
        }

        return view
    }

    override fun onDestroy() {
        super.onDestroy()

        webView.destroy()
    }
}