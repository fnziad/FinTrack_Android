package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.shared.App
import com.example.shared.data.database.initDatabaseContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDatabaseContext(this)
        enableEdgeToEdge()

        setContent {
            App()
        }
    }
}
