package com.nolgaemi.servermonitorapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nolgaemi.servermonitorapp.model.ServerStatus
import com.nolgaemi.servermonitorapp.network.RetrofitClient
import com.nolgaemi.servermonitorapp.ui.theme.ServerMonitorAppTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ServerMonitorAppTheme {
                MainScreen { serverId ->
                    val intent = Intent(this, DetailActivity::class.java)
                    intent.putExtra("serverId", serverId)
                    startActivity(intent)
                }
            }
        }
    }
}

@Composable
fun MainScreen(onServerClick: (Int) -> Unit) {
    var servers by remember { mutableStateOf(listOf<ServerStatus>()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        RetrofitClient.instance.getServers().enqueue(object : Callback<List<ServerStatus>> {
            override fun onResponse(call: Call<List<ServerStatus>>, response: Response<List<ServerStatus>>) {
                if (response.isSuccessful) {
                    servers = response.body().orEmpty()
                    isLoading = false
                    Log.d("MainScreen", "Fetched servers: $servers")
                } else {
                    Log.e("MainScreen", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<ServerStatus>>, t: Throwable) {
                Log.e("MainScreen", "Failure: ${t.message}")
                isLoading = false
            }
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Server Monitor App",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Server Name", fontSize = 18.sp, fontWeight = FontWeight.Medium)
            Text(text = "Server IP", fontSize = 18.sp, fontWeight = FontWeight.Medium)
        }
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(servers.size) { index ->
                    ServerItem(server = servers[index], onClick = onServerClick)
                }
            }
        }
    }
}

@Composable
fun ServerItem(server: ServerStatus, onClick: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(server.id) }
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = server.server_name, fontSize = 16.sp)
        Text(
            text = server.server_ip,
            color = Color.Blue,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
