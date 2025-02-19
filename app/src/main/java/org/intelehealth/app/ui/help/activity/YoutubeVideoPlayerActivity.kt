package org.intelehealth.app.ui.help.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import org.intelehealth.app.databinding.ActivityYoutubeVideoPlayerBinding


/**
 * Created by Vaghela Mithun R. on 19-02-2025 - 11:23.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class YoutubeVideoPlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityYoutubeVideoPlayerBinding
    private val args: YoutubeVideoPlayerActivityArgs by navArgs<YoutubeVideoPlayerActivityArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        super.onCreate(savedInstanceState)
        binding = ActivityYoutubeVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        playVideo()
    }

    @SuppressLint("SetJavaScriptEnabled", "ObsoleteSdkInt")
    private fun playVideo() {
        binding.wvYoutubeVideoPlayer.apply {
            settings.javaScriptEnabled = true
            webChromeClient = WebChromeClient()
            triggerAutoPlayVideo()
            // Clip the WebView to the rounded corners
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                clipToOutline = true;
            }
            buildHTMLWithoutPending(getIframeUrl(args.videoId))
        }
    }

    private fun triggerAutoPlayVideo() {
        // Use JavaScript to start playback
        binding.wvYoutubeVideoPlayer.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                view.loadUrl("javascript:document.querySelector('video').play()")
            }
        }
    }

    private fun getIframeUrl(videoId: String): String {
        return "<iframe id='player' type='text/html' width=\"100%\" height=\"100%\" " +
                "src=\"https://www.youtube.com/embed/$videoId?enablejsapi=1&autoplay=1\" " +
                "frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" " +
                "allowfullscreen></iframe>"
    }

    private fun buildHTMLWithoutPending(iframe: String) {
        val html = """
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <style>
                    body, html {
                        margin: 0;
                        padding: 0;
                    }
                    iframe {
                        width: 100%;
                        height: 100vh;
                        border: none;
                    }
                </style>
            </head>
            <body>
            $iframe
            <script src='https://www.youtube.com/iframe_api'></script>
            <script>
                function onYouTubeIframeAPIReady() {
                    var player = new YT.Player('player', {
                        events: {'onReady': onPlayerReady}
                    });
                }
                function onPlayerReady(event) {
                    event.target.playVideo();
                }
            </script>
            </body>
            </html>""".trimIndent()
        binding.wvYoutubeVideoPlayer.loadData(html, "text/html", "utf-8")
    }
}
