package com.ghost.android.test

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.metrics.performance.JankStats
import com.ghost.android.test.ui.GhostAndroidApp
import com.ghost.android.test.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    private var jankStats: JankStats? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { GhostAndroidApp(viewModel) }
        findViewById<View>(android.R.id.content).post {
            jankStats = JankStats.createAndTrack(window) { frameData ->
                if (frameData.isJank) viewModel.onJankDetected()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        jankStats?.isTrackingEnabled = true
    }

    override fun onPause() {
        super.onPause()
        jankStats?.isTrackingEnabled = false
    }
}
