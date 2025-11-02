package com.bossdev.automazione

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.media.AudioManager
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat.checkSelfPermission
import com.bossdev.automazione.ui.theme.AutomazioneTheme

private lateinit var audioManager : AudioManager
private lateinit var bluetoothManager : BluetoothManager
private lateinit var wifiManager : WifiManager
private lateinit var locationManager: LocationManager

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
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

        mainActivity = this

        setContent {
            val showDialog = remember { mutableStateOf(false) }

            AutomazioneTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = {
                        AddButton(
                            modifier = Modifier.padding(),
                            onClick = { showDialog.value = true }
                        )
                    },
                    floatingActionButtonPosition = FabPosition.Center
                ) { innerPadding ->
                    MainCompose(showDialog)
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
        MainCompose(mutableStateOf(false))
    }
}

@Composable
fun MainCompose(showDialog: MutableState<Boolean>)
{
    var wifiChecked by remember { mutableStateOf(false) }
    var bluetoothChecked by remember { mutableStateOf(false) }
    var localizationChecked by remember { mutableStateOf(false) }
    var mobileDataChecked by remember { mutableStateOf(false) }

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

    // Show 'Add Dialog'
    if (showDialog.value) {
        Dialog(onDismissRequest = { showDialog.value = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    // Title
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = "New Automation",
                            fontSize = 24.sp,
                            modifier = Modifier.padding(16.dp),
                        )
                    }
                    // Name Input
                    OutlinedTextField(
                        state = rememberTextFieldState(),
                        label = { Text("Name") },
                        modifier = Modifier.padding(16.dp)
                    )
                    // Wifi
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                    ) {
                        Text(
                            text = "Wifi",
                            modifier = Modifier.padding(14.dp),
                        )
                        Switch(
                            checked = wifiChecked,
                            onCheckedChange = {
                                wifiChecked = it
                            },
                        )
                    }
                    // Bluetooth
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                    ) {
                        Text(
                            text = "Bluetooth",
                            modifier = Modifier.padding(14.dp),
                        )
                        Switch(
                            checked = bluetoothChecked,
                            onCheckedChange = {
                                bluetoothChecked = it
                            }
                        )
                    }
                    // Localization
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                    ) {
                        Text(
                            text = "Localization",
                            modifier = Modifier.padding(14.dp),
                        )
                        Switch(
                            checked = localizationChecked,
                            onCheckedChange = {
                                localizationChecked = it
                            }
                        )
                    }
                    // Mobile Data
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                    ) {
                        Text(
                            text = "Mobile Data",
                            modifier = Modifier.padding(14.dp),
                        )
                        Switch(
                            checked = mobileDataChecked,
                            onCheckedChange = {
                                mobileDataChecked = it
                            },
                        )
                    }
                    // Cancel/Create buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        TextButton(
                            onClick = { showDialog.value = false },
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Text("Cancel")
                        }
                        TextButton(
                            onClick = {
                                showDialog.value = false
                            },
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Text("Create")
                        }
                    }
                }
            }
        }
    }
}

// region Permissions
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

// endregion

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

fun setLocation(state: EStateChange)
{
    if (state == EStateChange.ENABLE)
    {
//        val intent = Intent(INTENT_LOCATION_ENABLE)
//        mainActivity.startActivity(intent)
    }

    if (state == EStateChange.DISABLE)
    {
//        val intent = Intent(INTENT_LOCATION_DISABLE)
//        mainActivity.startActivity(intent)
    }
}

fun setMobileData(state: EStateChange)
{
    if (state == EStateChange.ENABLE)
    {
//        val intent = Intent(INTENT_MOBILE_DATA_ENABLE)
//        mainActivity.startActivity(intent)
    }

    if (state == EStateChange.DISABLE)
    {
//        val intent = Intent(INTENT_MOBILE_DATA_DISABLE)
//        mainActivity.startActivity(intent)
    }
}
