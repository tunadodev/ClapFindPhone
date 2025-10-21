package com.ibl.tool.clapfindphone.onboard_flow.splash

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.ibl.tool.clapfindphone.R

class SplashProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val TAG = "SplashProgressBar"
        private const val PROGRESS_MAX = 100
        private const val PROGRESS_INTERVAL = 30 // 30ms between updates
    }

    interface ProgressCallback {
        fun onProgressCompleted()
    }

    private var progressBar: ProgressBar? = null
    private var progressHandler: Handler? = null
    private var progress = 0.0 // Use double for progress to handle decimal increments precisely
    private var isFirstPhase = true
    private var isRunning = false
    private var pendingFirstDuration: Long = -1
    private var progressCallback: ProgressCallback? = null
    private var totalDuration: Long = 0

    init {
        init(context)
    }

    private fun init(context: Context) {
        try {
//            Log.d(TAG, "Initializing SplashProgressBar")

            // Check if layout exists
            val layoutId = context.resources.getIdentifier("view_splash_progress_bar", "layout", context.packageName)
            if (layoutId == 0) {
//                Log.e(TAG, "Layout view_splash_progress_bar not found!")
                // Create a fallback layout
                createFallbackViews(context)
                return
            }

            LayoutInflater.from(context).inflate(R.layout.view_splash_progress_bar, this, true)

            progressBar = findViewById(R.id.progressBar)
            if (progressBar == null) {
//                Log.e(TAG, "ProgressBar with ID progressBar not found!")
                createFallbackViews(context)
                return
            }
            progressHandler = Handler(Looper.getMainLooper())

            // Initialize views
            progressBar?.apply {
                max = PROGRESS_MAX
                progress = 0
            }

//            Log.d(TAG, "SplashProgressBar initialized successfully")
        } catch (e: Exception) {
//            Log.e(TAG, "Error initializing SplashProgressBar: ${e.message}")
            e.printStackTrace()
            createFallbackViews(context)
        }
    }

    private fun createFallbackViews(context: Context) {
        Log.d(TAG, "Creating fallback views")

        // Clear any existing views
        removeAllViews()

        // Create a simple layout programmatically
        orientation = VERTICAL
        gravity = Gravity.CENTER

        // Create ProgressBar
        progressBar = ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal).apply {
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                dpToPx(context, 8)
            )
            max = PROGRESS_MAX
            progress = 0
        }
        addView(progressBar)

        progressHandler = Handler(Looper.getMainLooper())
        Log.d(TAG, "Fallback views created")
    }

    private fun dpToPx(context: Context, dp: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
//        Log.d(TAG, "onAttachedToWindow called, pendingFirstDuration=$pendingFirstDuration")
        if (pendingFirstDuration != -1L) {
            start(pendingFirstDuration, progressCallback)
            pendingFirstDuration = -1
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
//        Log.d(TAG, "onDetachedFromWindow called, stopping animation")
        isRunning = false
        progressHandler?.removeCallbacksAndMessages(null) // Stop the animation when detached
    }

    fun start(firstDuration: Long, callback: ProgressCallback? = null) {
        Log.d(TAG, "start called with duration: $firstDuration")
        
        this.totalDuration = firstDuration
        this.progressCallback = callback

        // Stop any existing animation
        if (isRunning) {
            Log.d(TAG, "Stopping existing animation")
            progressHandler?.removeCallbacksAndMessages(null)
        }

        if (!isAttachedToWindow) {
            Log.d(TAG, "View not attached to window, setting pending duration")
            pendingFirstDuration = firstDuration
            return
        }

        // Check if views are properly initialized
        if (progressBar == null) {
//            Log.e(TAG, "Views not properly initialized. progressBar=$progressBar")

            // Create fallback views if they don't exist
            createFallbackViews(context)

            // Safety check after fallback creation
            if (progressBar == null) {
//                Log.e(TAG, "Still cannot initialize views after fallback creation")
                return
            }
        }

        // Reset state
        isRunning = true
        isFirstPhase = true
        progress = 0.0
        progressBar?.progress = 0

        // Calculate the increment to complete in totalDuration
        val totalSteps = (firstDuration / PROGRESS_INTERVAL).toInt()
        val progressPerStep = PROGRESS_MAX.toDouble() / totalSteps

//        Log.d(TAG, "Starting animation with progressPerStep=$progressPerStep")
        startAnimation(progressPerStep)
    }

    private fun startAnimation(progressPerStep: Double) {
        val runnable = object : Runnable {
            override fun run() {
                if (!isRunning) {
                    Log.d(TAG, "Animation stopped")
                    return
                }

                if (progress < PROGRESS_MAX) {
                    progress += progressPerStep
                    if (progress >= PROGRESS_MAX) {
                        progress = PROGRESS_MAX.toDouble()
                        updateProgress()
                        isRunning = false
                        Log.d(TAG, "Progress completed at 100%")
                        progressCallback?.onProgressCompleted()
                        return
                    }
                    
//                    Log.d(TAG, "Progress: $progress")
                    updateProgress()
                    progressHandler?.postDelayed(this, PROGRESS_INTERVAL.toLong())
                }
            }
        }
        progressHandler?.post(runnable)
    }
    
    /**
     * Force complete progress immediately and trigger callback
     */
    fun forceComplete() {
        Log.d(TAG, "Force completing progress")
        isRunning = false
        progressHandler?.removeCallbacksAndMessages(null)
        
        progress = PROGRESS_MAX.toDouble()
        updateProgress()
        progressCallback?.onProgressCompleted()
    }

    private fun updateProgress() {
        progressBar?.let { bar ->
            bar.progress = progress.toInt() // Cast to int for ProgressBar (visual steps will be integers)
//            Log.d(TAG, "Progress updated to: ${"%.2f%%".format(progress)}")

            // Force redraw of the progress bar
            bar.invalidate()
        }
    }

    fun end() {
        Log.d(TAG, "end called")
        isRunning = false
        progressHandler?.removeCallbacksAndMessages(null)

        // Jump to 100%
        progress = PROGRESS_MAX.toDouble()
        updateProgress()
        Log.d(TAG, "Progress set to 100%")
    }
}
