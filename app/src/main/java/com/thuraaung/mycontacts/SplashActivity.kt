package com.thuraaung.mycontacts

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


const val MAX_REQUEST_COUNT : Int = 2
const val REQUEST_CODE_PERMISSION = 101

class SplashActivity : AppCompatActivity() {

    private val permissions = listOf(
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.CALL_PHONE)

    private var permissionRequestCount : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        requestPermissionIfNecessary()
    }


    private fun requestPermissionIfNecessary() {
        if (!checkPermissions()) {
            if (permissionRequestCount < MAX_REQUEST_COUNT) {
                permissionRequestCount += 1

                ActivityCompat.requestPermissions(this,
                    permissions.toTypedArray(),
                    REQUEST_CODE_PERMISSION)

            } else {
                Toast.makeText(this,"Unable to get permission", Toast.LENGTH_SHORT).show()

            }
        } else {

            Handler().postDelayed({
                startMainActivity()
            },1000)

        }
    }

    private fun startMainActivity() {
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }

    private fun checkPermissions() : Boolean {
        for(permission in permissions) {
            if(ContextCompat.checkSelfPermission(this,permission) == PackageManager.PERMISSION_DENIED)
                return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION) {
            requestPermissionIfNecessary()
        }
    }
}