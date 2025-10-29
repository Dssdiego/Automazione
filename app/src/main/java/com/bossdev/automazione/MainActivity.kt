package com.bossdev.automazione

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.checkSelfPermission
import com.bossdev.automazione.ui.theme.AutomazioneTheme

private lateinit var audioManager : AudioManager
private lateinit var bluetoothManager : BluetoothManager

enum class ERingerMode
{
    NORMAL,
    VIBRATE
}

enum class EMode
{
    WORK,
    SLEEP,
    CAR
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager

        setContent {
            AutomazioneTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainCompose()
                }
            }
        }

        // Example of requesting permissions
        // TODO: Make this better (and automatic!)
        if (checkSelfPermission(
                applicationContext,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            requestPermissions(arrayOf(Manifest.permission.BLUETOOTH_CONNECT), 1001)

            Toast.makeText(applicationContext, "NO PERMISSION for bluetooth", Toast.LENGTH_SHORT).show()

            return
        }
        val bluetoothAdapter = bluetoothManager.adapter

        if (bluetoothAdapter?.isEnabled == false) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)

            val enableBluetoothLauncher = registerForActivityResult(
                StartActivityForResult(),
                ActivityResultCallback { result: ActivityResult? ->
                    if (result?.resultCode == RESULT_OK) {
                        // Bluetooth was enabled successfully
                        // Proceed with your Bluetooth operations
                        Toast.makeText(applicationContext, "Bluetooth enabled", Toast.LENGTH_SHORT).show()
                    } else {
                        // User denied enabling Bluetooth or an error occurred
                        // Handle accordingly
                        Toast.makeText(applicationContext, "Bluetooth denied", Toast.LENGTH_SHORT).show()
                    }
                }
            )

            enableBluetoothLauncher.launch(enableBtIntent)
        }

        Toast.makeText(applicationContext, "Set bluetooth", Toast.LENGTH_SHORT).show()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewMain() {
    AutomazioneTheme {
        MainCompose()
    }
}

@Composable
fun MainCompose()
{
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp),
        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(16.dp, 64.dp)
    ) {
        item { MyCard( description = "Work mode",  { setMode(EMode.WORK) } ) }
        item { MyCard( description = "Car Mode",   { setMode(EMode.CAR) } ) }
        item { MyCard( description = "Sleep Mode", { setMode(EMode.SLEEP) } ) }
    }
}

@Composable
fun MyCard(description: String, onClickFn: () -> Unit) {
    Card(
        onClick = onClickFn,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
        ),
        modifier = Modifier.size(width = 240.dp, height = 100.dp))
    {
        Text(
            text = description,
            modifier = Modifier
                .padding(16.dp),
            textAlign = TextAlign.Center,
        )
    }
}

fun setMode(mode: EMode)
{
    if (mode == EMode.WORK)
    {
//                            val wifiManager = getSystemService(WIFI_SERVICE) as WifiManager
//
//                            wifiManager.isWifiEnabled = false
//
//                            Toast.makeText(baseContext, "Hello!", Toast.LENGTH_SHORT).show()

        // TODO: Turn on wifi
        // TODO: Turn off bluetooth
        // TODO: Turn off mobile data

        setMusicVolume(100)
        setRingerMode(ERingerMode.VIBRATE)
    }

    if (mode == EMode.SLEEP)
    {
        // TODO: Turn off wifi
        // TODO: Turn off bluetooth
        // TODO: Turn off mobile data
        // TODO: Turn off localization

        setMusicVolume(100)
        setRingerMode(ERingerMode.VIBRATE)
    }

    if (mode == EMode.CAR)
    {
//                            val blManager = applicationContext.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
//                            val blAdapter = blManager.adapter
//
//                            blManager.getConnectedDevices(0)

        // TODO: Turn off wifi
        // TODO: Start bluetooth
        // TODO: Connect bluetooth to 'X8'
        // TODO: Start mobile data

        setMusicVolume(100)
        setRingerMode(ERingerMode.NORMAL)
//        setBluetooth()
    }
}

fun setMusicVolume(volume: Int)
{
    val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    val mappedVolume = Utils.mapRange(volume, IntRange(0, 100), IntRange(0, maxVolume))

    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mappedVolume, AudioManager.FLAG_PLAY_SOUND)
}

fun setRingerMode(mode: ERingerMode)
{
    if (mode == ERingerMode.NORMAL)
    {
        audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
    }

    if (mode == ERingerMode.VIBRATE)
    {
        audioManager.ringerMode = AudioManager.RINGER_MODE_VIBRATE
    }
}

fun setBluetooth()
{

}

fun changeWifi() {
//    val wifiManager = baseContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
//
//// To enable Wi-Fi
//    wifiManager.isWifiEnabled = true
//
//// To disable Wi-Fi
//    wifiManager.isWifiEnabled = false
}
