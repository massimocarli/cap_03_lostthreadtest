package uk.co.massimocarli.lostthreadtest

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  /**
   * The Tag for the Log
   */
  private val LOG_TAG = "LOST THREAD!"

  /*
   * Key for the extra about counter state
   */
  private val COUNTER_EXTRA = "uk.co.massimocarli.lostthreadtest.extra.COUNTER_EXTRA"

  /*
   * The Counter
   */
  private var mCounter: Int = 0

  /*
   * The CounterThread
   */
  private lateinit var counterThread: CounterThread

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    // Comment this when testing the loose of the state
    if (savedInstanceState != null) {
      mCounter = savedInstanceState.getInt(COUNTER_EXTRA, 0)
    }
    counterThread = CounterThread()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putInt(COUNTER_EXTRA, mCounter)
  }

  override fun onStart() {
    super.onStart()
    counterThread.start()
  }

  override fun onStop() {
    super.onStop()
    counterThread.stopCounter()
  }

  inner class CounterThread : Thread() {

    private var mRunner = true

    override fun run() {
      super.run()
      Log.d(LOG_TAG, "Counter STARTED from $mCounter")
      while (mRunner) {
        try {
          Thread.sleep(500L)
        } catch (ie: InterruptedException) {
        }

        mCounter++
        runOnUiThread {
          output.text = "Counter: $mCounter"
        }
        Log.d(LOG_TAG, "Counter in Fragment is: $mCounter")
      }
      Log.d(LOG_TAG, "Counter ENDED at $mCounter")
    }

    fun stopCounter() {
      mRunner = false
    }
  }
}
