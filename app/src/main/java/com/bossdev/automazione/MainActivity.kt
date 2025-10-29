package com.bossdev.automazione

import android.media.AudioManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import com.bossdev.automazione.ui.theme.AutomazioneTheme

private lateinit var audioManager : AudioManager

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

        setContent {
            AutomazioneTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainCompose()
                }
            }
        }
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

//                            Toast.makeText(baseContext, "Hello!", Toast.LENGTH_SHORT).show()
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

fun changeWifi() {
//    val wifiManager = baseContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
//
//// To enable Wi-Fi
//    wifiManager.isWifiEnabled = true
//
//// To disable Wi-Fi
//    wifiManager.isWifiEnabled = false
}
