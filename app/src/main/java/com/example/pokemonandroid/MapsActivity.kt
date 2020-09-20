package com.example.pokemonandroid

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlin.Exception

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        checkPermission()
LoadPokemon()
    }
    var  accesslocation=123
    fun checkPermission(){
        if(Build.VERSION.SDK_INT>=23){
            if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission
                    .ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),accesslocation)
                return
            }
        }
        GetUserLoction()
    }

    fun GetUserLoction(){
        Toast.makeText(this,"user location access on", Toast.LENGTH_LONG).show()
        //TODO :WILL IMPLEMENT LATER
        var myLocation=MyLocationListener()
        var locationmanager=getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3,3f,myLocation)
        var mythread=myThread()
        mythread.start()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            accesslocation->{
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                    GetUserLoction()
                }else {
                    Toast.makeText(this,"cannot acess location", Toast.LENGTH_LONG).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera

    }
    var location:Location?=null
     inner class MyLocationListener:LocationListener{

         constructor(){
             location= Location("start")
             location!!.longitude=0.0
             location!!.latitude=0.0
         }
         override fun onLocationChanged(locations: Location?) {
             location=locations
         }

         override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
           //  TODO("Not yet implemented")
         }

         override fun onProviderEnabled(provider: String?) {
          //   TODO("Not yet implemented")
         }

         override fun onProviderDisabled(provider: String?) {
            // TODO("Not yet implemented")
         }

     }
var oldLocation:Location?=null
    inner class myThread:Thread{
        constructor():super(){
            oldLocation= Location("start")
            oldLocation!!.longitude=0.0
            oldLocation!!.latitude=0.0
        }

        override fun run() {
            while(true) {
                try {
                    if(oldLocation!!.distanceTo(location)==0f) {
                        continue
                    }
                    oldLocation=location
                    runOnUiThread {
                        mMap!!.clear()
                        val sydney = LatLng(location!!.latitude, location!!.longitude)
                        mMap.addMarker(
                            MarkerOptions()
                                .position(sydney).title("Me").snippet("here is my location")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mario)))

                        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 2f))
                        newLatLngZoom(sydney, 2f)

                        //show pokemons
                        for (i in 0..listPokemon.size-1){
                            var newPokemon=listPokemon[i]
                            if (newPokemon.IsCatched==false){
                                val pokemonLoc = LatLng(newPokemon.location!!.latitude, newPokemon.location!!.longitude)
                                mMap.addMarker(
                                    MarkerOptions()
                                        .position(pokemonLoc).title(newPokemon.name).snippet(newPokemon.des +", power:" + newPokemon!!.power)
                                        .icon(BitmapDescriptorFactory.fromResource(newPokemon.image!!)))


                                if(location!!.distanceTo(newPokemon.location)<2){
                                    newPokemon.IsCatched=true
                                    listPokemon[i]=newPokemon
                                    playerPower+=newPokemon.power!!
                      Toast.makeText(applicationContext," caught new pokemon, your new power is $playerPower",Toast.LENGTH_LONG).show()
                                }

                            }
                        }
                    }
                    Thread.sleep(1000)
                } catch (ex: Exception) {
                }

            }
            super.run()
        }
    }
   var playerPower=0.0
    var listPokemon=ArrayList<Pokemon>()
    fun LoadPokemon(){
        listPokemon.add(
            Pokemon(R.drawable.charmander,"charmander",
            "from japan",55.0,35.6762,138.2529)
        )
        listPokemon.add(Pokemon(R.drawable.bulbasaur,"bulbasaur",
            "from USA",90.0,37.7949,-122.4104))

        listPokemon.add(Pokemon(R.drawable.squirtle,"squirtile",
            "from iraq",33.5,32.3416,43.6793))
    }
}