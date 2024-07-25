package com.nolgaemi.servermonitorapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nolgaemi.servermonitorapp.model.ServerStatus
import com.nolgaemi.servermonitorapp.network.RetrofitClient
import com.nolgaemi.servermonitorapp.ui.theme.ServerMonitorAppTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val serverId = intent.getIntExtra("serverId", 1)  // assuming you pass server ID
        setContent {
            ServerMonitorAppTheme {
                ServerDetailScreen(serverId) {
                    finish()
                }
            }
        }
    }
}

@Composable
fun ServerDetailScreen(serverId: Int, onBackClick: () -> Unit) {
    var serverStatus by remember { mutableStateOf<ServerStatus?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(serverId) {
        RetrofitClient.instance.getServerStatus(serverId).enqueue(object : Callback<ServerStatus> {
            override fun onResponse(call: Call<ServerStatus>, response: Response<ServerStatus>) {
                if (response.isSuccessful) {
                    serverStatus = response.body()
                    isLoading = false
                } else {
                    Log.e("ServerDetailScreen", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ServerStatus>, t: Throwable) {
                Log.e("ServerDetailScreen", "Failure: ${t.message}")
                isLoading = false
            }
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "Server Status: ${serverStatus?.server_name ?: "Loading..."}", fontSize = 18.sp)
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            serverStatus?.let {
                Text(text = "Server IP: ${it.server_ip}", fontSize = 16.sp)
                Text(text = "Network: ${it.network_status}", fontSize = 16.sp)
                Text(text = "CPU usage: ${it.cpu_usage}%", fontSize = 16.sp)
                Text(text = "Memory usage: ${it.memory_usage}%", fontSize = 16.sp)
                Text(text = "Disk usage: ${it.disk_usage}%", fontSize = 16.sp)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBackClick) {
            Text(text = "Back")
        }
    }
}
