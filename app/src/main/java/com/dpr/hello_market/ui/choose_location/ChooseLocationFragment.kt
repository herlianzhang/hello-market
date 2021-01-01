package com.dpr.hello_market.ui.choose_location

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
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

    private val callback = OnMapReadyCallback { googleMap ->
        viewModel.googleMap = googleMap
        val myPosition: LatLng
        if (viewModel.myLocation.lat == null || viewModel.myLocation.lng == null) {
            myPosition = LatLng(3.58333, 98.66667)
            moveWithAnimation(
                myPosition,
                15f
            )
        } else {
            myPosition = LatLng(viewModel.myLocation.lat!!, viewModel.myLocation.lng!!)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GPS_CODE) {
            requestPermission()
        }
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_choose_location, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChooseLocationViewModel::class.java)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        viewModel.myLocation = args.location

        if (viewModel.myLocation.lat == null || viewModel.myLocation.lng == null) {
            initMode(Mode.Choose)
            binding.ivHomeLocation.isVisible = false
        } else {
            initMode(Mode.View)
        }

        mapFragment?.getMapAsync(callback)
        viewModel.geoCoder = Geocoder(requireContext())

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
            viewModel.homeMarker?.position?.let { latlng ->
                moveWithAnimation(latlng, 15f)
            }
        }

        binding.ivStartChoose.setOnClickListener {
            removeHomeMarker()
            chooseMode()
        }

        binding.ivCancel.setOnClickListener {
            val myLat = viewModel.myLocation.lat
            val myLng = viewModel.myLocation.lng
            if (myLat != null && myLng != null) {
                addHomeMarker(LatLng(myLat, myLng))
            }
            viewMode()
        }

        binding.btnChooseLocation.setOnClickListener {
            val currLatLng = viewModel.googleMap.cameraPosition.target
            val address = viewModel.geoCoder.getFromLocation(
                currLatLng.latitude,
                currLatLng.longitude,
                1
            )[0].getAddressLine(0)
            addHomeMarker(currLatLng)
            viewModel.myLocation = Location(address, currLatLng.latitude, currLatLng.longitude)
            viewMode()
            if (!binding.ivHomeLocation.isVisible)
                binding.ivHomeLocation.isVisible = true
        }

        binding.ivSave.setOnClickListener {
            setFragmentResult(
                EditProfileFragment.LOCATION,
                bundleOf(EditProfileFragment.LOCATION_DATA to viewModel.myLocation)
            )
            findNavController().popBackStack()
        }
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

    private fun addHomeMarker(location: LatLng) {
        viewModel.homeMarker = viewModel.googleMap.addMarker(
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
        viewModel.homeMarker?.remove()
    }

    private fun moveWithAnimation(
        location: LatLng = viewModel.googleMap.cameraPosition.target,
        zoom: Float = 10f
    ) = viewModel.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, zoom))

    private fun setTextAddress(latlng: LatLng) {
        try {
            val location =
                viewModel.geoCoder.getFromLocation(latlng.latitude, latlng.longitude, 1).getOrNull(0)
            binding.tvAddress.text = location?.getAddressLine(0) ?: "Not Known"
        } catch (e: Exception) {
            Timber.e("setText address exception; ${e.message}")
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
            if (Helper.isGpsEnabled(requireContext())) {
                try {
                    viewModel.googleMap.isMyLocationEnabled = true
                    viewModel.googleMap.uiSettings.isMyLocationButtonEnabled = false
                    getFusedLocationProviderClient(requireContext()).lastLocation.addOnSuccessListener { location ->
                        if (location != null) {
                            moveWithAnimation(LatLng(location.latitude, location.longitude), 15f)
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                }
            } else {
                requestGps()
            }
        }
    }

    private fun requestGps() {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setMessage("Turn on device location, which uses Google's location service.")
            .setPositiveButton("Ok") { _, _ ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(intent, GPS_CODE)
            }
            .setNegativeButton("No Thanks") { _, _ ->
                Toast.makeText(requireContext(), "Fail to get current location", Toast.LENGTH_SHORT)
                    .show()
            }.show()
    }

    companion object {
        //Permission code
        private const val PERMISSION_CODE = 1001

        //gps
        private const val GPS_CODE = 1002
    }
}