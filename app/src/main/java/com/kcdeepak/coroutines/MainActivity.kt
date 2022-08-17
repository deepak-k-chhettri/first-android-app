package com.kcdeepak.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //coroutines()
        cancellable()
        //isactive()
        //timeout()
        val result:Int?= timeoutOrNull()
        Log.i("MainActivity", "onCreate: $result")
    }
}

fun coroutines() = runBlocking {
    val TAG = "MainActivity"
    Log.i(TAG, "Main started : ${Thread.currentThread().name}")

    launch {
        Log.i(TAG, "Fake Work Started : ${Thread.currentThread().name}")
        delay(1000)
        Log.i(TAG, "Fake Work Finished : ${Thread.currentThread().name}")
    }.join()

    Log.i(TAG,"Main ended : ${Thread.currentThread().name}")
}

fun cancellable() = runBlocking {
    val TAG = "Cancellable"
    Log.i(TAG, "cancellable: ${Thread.currentThread().name}")

    val job = launch {
        try {
            for (i in 1..1000) {
                Log.i(TAG, "$i.")
                delay(50)
            }
        }catch (e:CancellationException){
            Log.i(TAG, "cancellable: Exception caught safely")
        }finally {
            Log.i(TAG, "cancellable: $this")
            withContext(NonCancellable) {
                Log.i(TAG, "cancellable: $this")
                delay(1000)
                Log.i(TAG, "cancellable: Finally block")
            }
        }

    }
    delay(1000)
    job.cancelAndJoin()

    Log.i(TAG, "cancellable: ended ${Thread.currentThread().name}")
}

fun isactive() = runBlocking {
    val TAG = "Cancellable"
    Log.i(TAG, "cancellable: ${Thread.currentThread().name}")

    val job = launch(Dispatchers.Default) {
        for (i in 1..1000) {
            if(!isActive){
                return@launch
            }
            Log.i(TAG, "$i.")
        }
    }
    delay(10)
    job.cancelAndJoin()

    Log.i(TAG, "cancellable: ended ${Thread.currentThread().name}")
}

fun timeout() = runBlocking{
    try {
        withTimeout(2000) {
            for (i in 1..500) {
                Log.i("Timeout", "$i")
                delay(100)
            }
        }
    }catch (e:TimeoutCancellationException){
        Log.i("Timeout", "timeout: Coroutine terminated")
    }
    finally {
        Log.i("Timtout", "timeout: Finally Blocked")
    }
}

fun timeoutOrNull() = runBlocking {
    Log.i("TAG", "timeoutOrNull: $this")
    withTimeoutOrNull(2000) {
        Log.i("TimeoutOrNull", "timeoutOrNull: $this")
        for (i in 1..20) {
            Log.i("TIMEOUTORNULL", "$i")
            delay(50)
        }
        1000
    }
}