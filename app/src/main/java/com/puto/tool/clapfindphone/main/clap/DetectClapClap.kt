package com.puto.tool.clapfindphone.main.clap

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioRecord
import android.util.Log
import be.hogent.tarsos.dsp.AudioEvent
import be.hogent.tarsos.dsp.AudioFormat
import be.hogent.tarsos.dsp.onsets.OnsetHandler
import be.hogent.tarsos.dsp.onsets.PercussionOnsetDetector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


@SuppressLint("MissingPermission")
class DetectClapClap internal constructor(context: Context, mCallback: IDetect) : OnsetHandler {
    private val buffer: ByteArray
    private var clap: Int
    private val classesApp: ClassesApp
    private val mContext: Context
    private var mIsRecording: Boolean
    private val mPercussionOnsetDetector: PercussionOnsetDetector
    private var rateSupported = 0
    private var rateSend = false
    private val recorder: AudioRecord
    private val callback: IDetect
    private var lastClapTime: Long = 0
    private var startTime: Long = 0
    private val DETECTION_DELAY = 4000L  // Wait 4 seconds before starting detection
    private val MIN_CLAP_INTERVAL = 100L  // Minimum 100ms between claps
    private val MAX_CLAP_INTERVAL = 1000L  // Maximum 1000ms between claps (1 second)

    init {
        classesApp = ClassesApp(context)
        SAMPLE_RATE = validSampleRates
        mContext = context
        val minBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, 16, 2)
        buffer = ByteArray(minBufferSize)
        recorder = AudioRecord(1, SAMPLE_RATE, 16, 2, minBufferSize)
        mPercussionOnsetDetector = PercussionOnsetDetector(SAMPLE_RATE.toFloat(), minBufferSize / 2, this, 24.0, 5.0)
        clap = 0
        mIsRecording = true
        callback = mCallback
    }

    private val validSampleRates: Int
        get() {
            for (i in intArrayOf(44100, 22050, 16000, 11025, 8000)) {
                if (AudioRecord.getMinBufferSize(i, 1, 2) > 0 && !rateSend) {
                    rateSupported = i
                    rateSend = true
                }
            }
            return rateSupported
        }

    override fun handleOnset(d: Double, d2: Double) {
        val currentTime = System.currentTimeMillis()
        
        // Ignore all claps during the initial delay period
        if (currentTime - startTime < DETECTION_DELAY) {
            Log.d(TAG, "Ignoring - still in delay period (${currentTime - startTime}ms / ${DETECTION_DELAY}ms)")
            return
        }
        
        val timeSinceLastClap = currentTime - lastClapTime
        
        Log.d(TAG, "Onset detected! Time since last: ${timeSinceLastClap}ms, Current clap count: $clap")
        
        // Ignore if too soon after last clap (noise/echo)
        if (timeSinceLastClap < MIN_CLAP_INTERVAL && lastClapTime != 0L) {
            Log.d(TAG, "Ignoring - too soon after last clap")
            return
        }
        
        // Reset counter if too much time has passed
        if (timeSinceLastClap > MAX_CLAP_INTERVAL && lastClapTime != 0L) {
            Log.d(TAG, "Resetting counter - too much time passed")
            clap = 0
        }
        
        clap++
        lastClapTime = currentTime
        Log.d(TAG, "Clap counted! Total: $clap")
        
        val nbClaps = 2
        if (clap >= nbClaps) {
            Log.d(TAG, "Detection triggered! Playing sound...")
            classesApp.save("detectClap", "1")
            mIsRecording = false
            callback.onDetected()
            clap = 0  // Reset clap counter after detection
            lastClapTime = 0  // Reset last clap time
        }
    }

    open fun continueRecord() {
        clap = 0  // Reset clap counter when continuing recording
        lastClapTime = 0  // Reset last clap time
        startTime = System.currentTimeMillis()  // Reset start time
        mIsRecording = true
        listen()
    }
    open fun stopRecord() {
        mIsRecording = false
    }

    val TAG = "~~~"
    private var listenJob: Job? = null

    fun listen() {
        if (listenJob != null && listenJob!!.isActive) {
            listenJob!!.cancel()
            return // Return if the job is already running
        }

        clap = 0  // Reset clap counter when starting to listen
        lastClapTime = 0  // Reset last clap time
        startTime = System.currentTimeMillis()  // Set start time for delay
        Log.d(TAG, "Starting to listen for claps... (will start detecting after 4 seconds)")
        listenJob = CoroutineScope(Dispatchers.IO).launch {
            recorder.startRecording()
            val torsosFormat = AudioFormat(SAMPLE_RATE.toFloat(), 16, 1, true, false)

            while (mIsRecording) {
                val audioEvent = AudioEvent(
                    torsosFormat,
                    recorder.read(buffer, 0, buffer.size).toLong()
                )
                audioEvent.setFloatBufferWithByteBuffer(buffer)
                mPercussionOnsetDetector.process(audioEvent)
                Log.e(TAG, "listen: inside while loop")
            }

            recorder.stop()
            Log.e(TAG, "listen: stop recorder")
        }
    }

    companion object {
        var SAMPLE_RATE = 8000
    }
}