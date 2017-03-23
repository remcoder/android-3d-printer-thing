

package com.example.androidthings.rainbowhat_demo

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.google.android.things.pio.PeripheralManagerService
import com.google.firebase.database.*
import android.os.Debug.getMemoryInfo
import android.app.ActivityManager
import android.content.Context


class MainActivity : Activity() {

    var fbMode : DatabaseReference? = null

    private var mDatabase: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")

        bootSequence()

        initFirebase()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")

    }

    fun initFirebase() {
        mDatabase = FirebaseDatabase.getInstance()
        fbMode = mDatabase!!.getReference("mode")
        fbMode!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue(Long::class.java)
                Log.d(TAG, "Value is: " + value)

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }

    fun bootSequence() {
        Log.d(TAG, "Hello!!! from Android Things in Kotlin over REAL wifi!!!!")

        val mi = ActivityManager.MemoryInfo()
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.getMemoryInfo(mi)
        val percentAvail = mi.availMem.toDouble() / mi.totalMem.toDouble()
        Log.d(TAG, "*** free memory " + percentAvail *100 + "% ***")

        val manager = PeripheralManagerService()
        val portList = manager.getGpioList()
        if (portList.isEmpty()) {
            Log.i(TAG, "No GPIO port available on this device.")
        } else {
            Log.i(TAG, "List of available ports: " + portList)
        }

    }

    companion object {
        private val TAG = MainActivity::class.java!!.getSimpleName()
    }
}
