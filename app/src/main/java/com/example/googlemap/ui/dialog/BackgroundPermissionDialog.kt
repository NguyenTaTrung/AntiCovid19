package com.example.googlemap.ui.dialog

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.googlemap.BuildConfig
import com.example.googlemap.R
import com.example.googlemap.databinding.FragmentPermissionRequestBinding
import com.example.googlemap.ui.main.BottomNavigationListener
import com.example.googlemap.utils.hasPermission
import com.example.googlemap.utils.requestPermissionWithRationale
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_permission_request.*

class BackgroundPermissionDialog : DialogFragment() {

    private var parentListener: BackgroundDialogCallbacks? = null
    private var permissionRequestType: PermissionRequestType? = null
    private var bottomNavigationListener: BottomNavigationListener? = null
    private lateinit var binding: FragmentPermissionRequestBinding

    private val fineLocationRationalSnackBar by lazy {
        Snackbar.make(
            binding.frameLayout,
            R.string.fine_location_permission_rationale,
            Snackbar.LENGTH_LONG
        )
            .setAction(R.string.ok) {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_FINE_LOCATION_PERMISSIONS_REQUEST_CODE
                )
            }
    }

    private val backgroundRationalSnackBar by lazy {
        Snackbar.make(
            binding.frameLayout,
            R.string.background_location_permission_rationale,
            Snackbar.LENGTH_LONG
        )
            .setAction(R.string.ok) {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                    REQUEST_BACKGROUND_LOCATION_PERMISSIONS_REQUEST_CODE
                )
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment is BackgroundDialogCallbacks) {
            parentListener = parentFragment as BackgroundDialogCallbacks
        } else {
            throw RuntimeException("$context must implement BackgroundDiaLogCallbacks")
        }
        if (context is BottomNavigationListener) bottomNavigationListener = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionRequestType =
            arguments?.getSerializable(ARG_PERMISSION_REQUEST_TYPE) as PermissionRequestType
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setGravity(Gravity.CENTER)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPermissionRequestBinding.inflate(inflater, container, false)
        isCancelable = false

        when (permissionRequestType) {
            PermissionRequestType.FINE_LOCATION -> {
                binding.apply {
                    titleTextView.text = getString(R.string.fine_location_access_rationale_title_text)
                    detailsTextView.text = getString(R.string.fine_location_access_rationale_details_text)
                    permissionRequestButton.text = getString(R.string.text_agree)
                }
            }

            PermissionRequestType.BACKGROUND_LOCATION -> {
                binding.apply {
                    titleTextView.text = getString(R.string.background_location_access_rationale_title_text)
                    detailsTextView.text = getString(R.string.background_location_access_rationale_details_text)
                    permissionRequestButton.text = getString(R.string.text_agree)
                }
            }
        }

        binding.permissionRequestButton.setOnClickListener {
            when (permissionRequestType) {
                PermissionRequestType.FINE_LOCATION ->
                    requestFineLocationPermission()

                PermissionRequestType.BACKGROUND_LOCATION ->
                    requestBackgroundLocationPermission()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        canPermissionRequestButton.setOnClickListener {
            this.dismiss()
            parentListener?.removeFragment()
            bottomNavigationListener?.showBottomNav()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_FINE_LOCATION_PERMISSIONS_REQUEST_CODE,
            REQUEST_BACKGROUND_LOCATION_PERMISSIONS_REQUEST_CODE -> when {
                grantResults.isEmpty() -> Log.d("TAG", "User interaction was cancelled.")

                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    this.dismiss()
                    //startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    parentListener?.refreshFragment()
                }

                else -> {
                    val permissionDeniedExplanation =
                        if (requestCode == REQUEST_FINE_LOCATION_PERMISSIONS_REQUEST_CODE) {
                            R.string.fine_permission_denied_explanation
                        } else {
                            R.string.background_permission_denied_explanation
                        }

                    Snackbar.make(
                        binding.frameLayout,
                        permissionDeniedExplanation,
                        Snackbar.LENGTH_LONG
                    )
                        .setAction(R.string.settings) {
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts(
                                "package",
                                BuildConfig.APPLICATION_ID,
                                null
                            )
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                        .show()
                }
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        parentListener = null
        bottomNavigationListener = null
    }

    private fun requestFineLocationPermission() {
        val permissionApproved =
            context?.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) ?: return

        if (!permissionApproved) {
            requestPermissionWithRationale(
                Manifest.permission.ACCESS_FINE_LOCATION,
                REQUEST_FINE_LOCATION_PERMISSIONS_REQUEST_CODE,
                fineLocationRationalSnackBar
            )
        }
    }

    private fun requestBackgroundLocationPermission() {
        val permissionApproved =
            context?.hasPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION) ?: return

        if (!permissionApproved) {
            requestPermissionWithRationale(
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                REQUEST_BACKGROUND_LOCATION_PERMISSIONS_REQUEST_CODE,
                backgroundRationalSnackBar
            )
        }
    }

    interface BackgroundDialogCallbacks {
        fun removeFragment()
        fun refreshFragment()
    }

    companion object {
        private const val ARG_PERMISSION_REQUEST_TYPE =
            "com.google.android.gms.location.sample.locationupdatesbackgroundkotlin.PERMISSION_REQUEST_TYPE"

        private const val REQUEST_FINE_LOCATION_PERMISSIONS_REQUEST_CODE = 34
        private const val REQUEST_BACKGROUND_LOCATION_PERMISSIONS_REQUEST_CODE = 56

        fun newInstance(permissionRequestType: PermissionRequestType) =
            BackgroundPermissionDialog().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PERMISSION_REQUEST_TYPE, permissionRequestType)
                }
            }
    }
}

enum class PermissionRequestType {
    FINE_LOCATION, BACKGROUND_LOCATION
}
