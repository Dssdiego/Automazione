package com.bossdev.automazione

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.checkSelfPermission
import com.bossdev.automazione.ui.theme.AutomazioneTheme

private lateinit var audioManager : AudioManager
private lateinit var bluetoothManager : BluetoothManager
private lateinit var wifiManager : WifiManager

private lateinit var mainActivity: Activity

private val PERMISSION_REQUEST_CODE = 1001

private val INTENT_WIFI_ENABLE = "android.net.wifi.action.REQUEST_ENABLE"
private val INTENT_WIFI_DISABLE = "android.net.wifi.action.REQUEST_DISABLE"
private val INTENT_BLUETOOTH_ENABLE = "android.bluetooth.adapter.action.REQUEST_ENABLE"
private val INTENT_BLUETOOTH_DISABLE = "android.bluetooth.adapter.action.REQUEST_DISABLE"

enum class ERingerMode
{
    NORMAL,
    VIBRATE
}

enum class EMode
{
    WORK,
    SLEEP,
    CAR,
    GRAB_FOOD
}

enum class EStateChange
{
    ENABLE,
    DISABLE
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        wifiManager = getSystemService(WIFI_SERVICE) as WifiManager

        mainActivity = this

        setContent {
            AutomazioneTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = {
                        AddButton(
                            modifier = Modifier.padding(),
                            onClick = {}
                        )
                    },
                    floatingActionButtonPosition = FabPosition.Center
                ) { innerPadding ->
                    MainCompose()
                }
            }
        }

        getPermissions(arrayOf(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.ACCESS_FINE_LOCATION))
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
        item { MyCard( description = "Grab Food Mode", { setMode(EMode.GRAB_FOOD) } ) }
    }
}

fun getPermissions(permissions: Array<String>)
{
    var allPermissionsEnabled = false

    permissions.forEach { permission ->
        allPermissionsEnabled = checkHasPermission(permission)
    }

    if (!allPermissionsEnabled)
    {
        mainActivity.requestPermissions(permissions, PERMISSION_REQUEST_CODE)
    }
}

fun checkHasPermission(permission: String) : Boolean
{
    return checkSelfPermission(mainActivity.applicationContext, permission) == PackageManager.PERMISSION_GRANTED
}

//    // Example of requesting permissions
//    // TODO: Make this better (and automatic!)
//    if (checkSelfPermission(
//            mainActivity.applicationContext,
//            permission
//        ) != PackageManager.PERMISSION_GRANTED
//    ) {
//        // TODO: Consider calling
//        //    ActivityCompat#requestPermissions
//        // here to request the missing permissions, and then overriding
//        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//        //                                          int[] grantResults)
//        // to handle the case where the user grants the permission. See the documentation
//        // for ActivityCompat#requestPermissions for more details.
//
//        mainActivity.requestPermissions(arrayOf(permission), PERMISSION_REQUEST_CODE)
//
////        Toast.makeText(activity.applicationContext, "NO PERMISSION for bluetooth", Toast.LENGTH_SHORT).show()
//    }
//}

fun setMode(mode: EMode)
{
    if (mode == EMode.WORK)
    {
//                            val wifiManager = getSystemService(WIFI_SERVICE) as WifiManager
//
//                            wifiManager.isWifiEnabled = false
//
//                            Toast.makeText(baseContext, "Hello!", Toast.LENGTH_SHORT).show()

        setWifi(EStateChange.ENABLE)
        setBluetooth(EStateChange.DISABLE)
        // TODO: Turn off mobile data
        setMusicVolume(100)
        setRingerMode(ERingerMode.VIBRATE)
    }

    if (mode == EMode.SLEEP)
    {
        setWifi(EStateChange.DISABLE)
        setBluetooth(EStateChange.DISABLE)

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

        setWifi(EStateChange.DISABLE)
        setBluetooth(EStateChange.ENABLE)

        // TODO: Connect bluetooth to 'X8'
        // TODO: Start mobile data

        setMusicVolume(100)
        setRingerMode(ERingerMode.NORMAL)
    }

    if (mode == EMode.GRAB_FOOD)
    {

        setWifi(EStateChange.ENABLE)
        setBluetooth(EStateChange.DISABLE)

        // TODO: Turn on localization
        // TODO: Turn on mobile data

        setMusicVolume(100)
        setRingerMode(ERingerMode.VIBRATE)
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

fun setBluetooth(state: EStateChange)
{
    if (state == EStateChange.ENABLE)
    {
        val intent = Intent(INTENT_BLUETOOTH_ENABLE)
        mainActivity.startActivity(intent)
    }

    if (state == EStateChange.DISABLE)
    {
        val intent = Intent(INTENT_BLUETOOTH_DISABLE)
        mainActivity.startActivity(intent)
    }
}

fun setWifi(state: EStateChange)
{
    if (state == EStateChange.ENABLE)
    {
        val intent = Intent(INTENT_WIFI_ENABLE)
        mainActivity.startActivity(intent)
    }

    if (state == EStateChange.DISABLE)
    {
        val intent = Intent(INTENT_WIFI_DISABLE)
        mainActivity.startActivity(intent)
    }
}
