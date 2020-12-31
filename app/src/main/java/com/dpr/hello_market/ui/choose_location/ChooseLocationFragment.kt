package com.dpr.hello_market.ui.choose_location

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dpr.hello_market.R
import com.dpr.hello_market.databinding.FragmentChooseLocationBinding
import com.dpr.hello_market.di.Injectable
import com.dpr.hello_market.helper.Helper
import com.dpr.hello_market.helper.dp
import com.dpr.hello_market.ui.edit_profile.EditProfileFragment
import com.dpr.hello_market.vo.Location
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import timber.log.Timber
import javax.inject.Inject

class ChooseLocationFragment : Fragment(), Injectable {

    enum class Mode {
        View, Choose
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentChooseLocationBinding
    private lateinit var viewModel: ChooseLocationViewModel


    private val args: ChooseLocationFragmentArgs by navArgs()

    private lateinit var googleMap: GoogleMap
    private lateinit var geoCoder: Geocoder

    private var homeMarker: Marker? = null
    private lateinit var myLocation: Location

    private val callback = OnMapReadyCallback { googleMap ->
        this.googleMap = googleMap
        val myPosition: LatLng
        if (myLocation.lat == null || myLocation.lng == null) {
            myPosition = LatLng(3.58333, 98.66667)
            moveWithAnimation(
                myPosition,
                15f
            )
        } else {
            myPosition = LatLng(myLocation.lat!!, myLocation.lng!!)
            addHomeMarker(myPosition)
            moveWithAnimation(
                myPosition
            )
        }

        googleMap.setOnCameraMoveStartedListener {
            if (binding.iconMarker.isVisible && binding.iconMarkerShadow.isVisible) {
                binding.iconMarker.animate().translationY(-50f).start()
                binding.iconMarkerShadow.animate().withStartAction {
                    binding.iconMarkerShadow.setPadding(10, 10, 10, 10)
                }.start()
            }
        }

        googleMap.setOnCameraIdleListener {
            if (binding.iconMarker.isVisible && binding.iconMarkerShadow.isVisible) {
                val newPosition = googleMap.cameraPosition.target

                binding.iconMarker.animate().translationY(0f).start()
                binding.iconMarkerShadow.animate().withStartAction {
                    binding.iconMarkerShadow.setPadding(0, 0, 0, 0)
                }.start()

                setTextAddress(newPosition)
            }
        }
    }

    private fun addHomeMarker(location: LatLng) {
        homeMarker = googleMap.addMarker(
            MarkerOptions()
                .position(location)
                .title("Your Home")
                .icon(
                    Helper.bitmapDescriptorFromVector(
                        requireContext(),
                        R.drawable.ic_home_marker
                    )
                )
        )
    }

    private fun removeHomeMarker() {
        homeMarker?.remove()
    }

    private fun moveWithAnimation(
        location: LatLng = googleMap.cameraPosition.target,
        zoom: Float = 10f
    ) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, zoom))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_choose_location, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChooseLocationViewModel::class.java)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        myLocation = args.location

        if (myLocation.lat == null || myLocation.lng == null) {
            initMode(Mode.Choose)
            binding.ivHomeLocation.isVisible = false
        } else {
            initMode(Mode.View)
        }



        mapFragment?.getMapAsync(callback)
        geoCoder = Geocoder(requireContext())

        initListener()
    }

    private fun initListener() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.ivMyLocation.setOnClickListener {
            requestPermission()
        }

        binding.ivHomeLocation.setOnClickListener {
            homeMarker?.position?.let { latlng ->
                moveWithAnimation(latlng, 15f)
            }
        }

        binding.ivStartChoose.setOnClickListener {
            removeHomeMarker()
            chooseMode()
        }

        binding.ivCancel.setOnClickListener {
            val myLat = myLocation.lat
            val myLng = myLocation.lng
            if (myLat != null && myLng != null) {
                addHomeMarker(LatLng(myLat, myLng))
            }
            viewMode()
        }

        binding.btnChooseLocation.setOnClickListener {
            val currLatLng = googleMap.cameraPosition.target
            val address = geoCoder.getFromLocation(
                currLatLng.latitude,
                currLatLng.longitude,
                1
            )[0].getAddressLine(0)
            addHomeMarker(currLatLng)
            myLocation = Location(address, currLatLng.latitude, currLatLng.longitude)
            viewMode()
            if (!binding.ivHomeLocation.isVisible)
                binding.ivHomeLocation.isVisible = true
        }

        binding.ivSave.setOnClickListener {
            setFragmentResult(EditProfileFragment.LOCATION, bundleOf(EditProfileFragment.LOCATION_DATA to myLocation))
            findNavController().popBackStack()
        }
    }

    private fun setTextAddress(latlng: LatLng) {
        val location = geoCoder.getFromLocation(latlng.latitude, latlng.longitude, 1)[0]
        binding.tvAddress.text = location.getAddressLine(0)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Timber.d("requestCode $requestCode\npermissions $permissions\ngrantResults $grantResults")
        var isAllGranted = true
        for (grantResult in grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                isAllGranted = false
                break
            }
        }
        if (isAllGranted) {
            requestPermission()
        }
    }

    private fun requestPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val permissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            requestPermissions(permissions, PERMISSION_CODE)
        } else {
            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = false
            getFusedLocationProviderClient(requireContext()).lastLocation.addOnSuccessListener { location ->
                moveWithAnimation(LatLng(location.latitude, location.longitude), 15f)
            }
        }
    }

    private fun chooseMode() {
        binding.ivStartChoose.animate().translationY(-64.dp.toFloat()).start()
        binding.ivSave.animate().translationX(-64.dp.toFloat()).start()
        binding.ivHomeLocation.animate().translationX(64.dp.toFloat()).start()

        binding.ivCancel.animate().translationX(0f).start()
        binding.btnChooseLocation.animate().translationY(0f).start()

        binding.cvAddress.isVisible = true
        binding.iconMarkerShadow.isVisible = true
        binding.iconMarker.isVisible = true

        moveWithAnimation(zoom = 15f)
    }

    private fun viewMode() {
        binding.ivStartChoose.animate().translationY(0f).start()
        binding.ivSave.animate().translationX(0f).start()
        binding.ivHomeLocation.animate().translationX(0f).start()

        binding.ivCancel.animate().translationX(64.dp.toFloat()).start()
        binding.btnChooseLocation.animate()
            .translationY(64.dp.toFloat()).start()

        binding.cvAddress.isVisible = false
        binding.iconMarkerShadow.isVisible = false
        binding.iconMarker.isVisible = false
    }

    private fun initMode(mode: Mode) {
        if (mode == Mode.Choose) { // chooseMode
            binding.ivStartChoose.translationY = -64.dp.toFloat()
            binding.ivSave.translationX = -64.dp.toFloat()
            binding.ivHomeLocation.translationX = 64.dp.toFloat()

            binding.iconMarkerShadow.isVisible = true
            binding.iconMarker.isVisible = true
        } else { // viewMode
            binding.ivCancel.translationX = 64.dp.toFloat()
            binding.btnChooseLocation.translationY = 64.dp.toFloat()

            binding.cvAddress.isVisible = false
            binding.iconMarkerShadow.isVisible = false
            binding.iconMarker.isVisible = false
        }
    }

    companion object {
        //Permission code
        private const val PERMISSION_CODE = 1001;
    }

}