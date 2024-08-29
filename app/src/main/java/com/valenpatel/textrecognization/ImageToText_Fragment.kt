package com.valenpatel.textrecognization

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.valenpatel.textrecognization.databinding.FragmentImageToTextBinding

class ImageToText_Fragment : Fragment() {
    lateinit var binding: FragmentImageToTextBinding
    private lateinit var cameraActivityResultLauncher: ActivityResultLauncher<Uri>
    private lateinit var galleryActivityResultLauncher: ActivityResultLauncher<String>
    private var imageUri: Uri? = null
    var text2 = ""

    private val cameraPermissions = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentImageToTextBinding.inflate(layoutInflater)

        // Register ActivityResultLaunchers
        cameraActivityResultLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccessful ->
                if (isSuccessful && imageUri != null) {
                    binding.imageView.setImageURI(imageUri)
                    imageUri?.let { uri ->
                        imageToText(uri)
                        binding.editText.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg)
                    }
                }
            }

        galleryActivityResultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                if (uri != null) {
                    binding.imageView.setImageURI(uri)
                    imageToText(uri)
                    binding.editText.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg)
                }
            }

        // Set up button click listeners
        binding.imageFromCameraView.setOnClickListener {
            if (hasCameraPermissions()) {
                captureImageFromCamera()
            } else {
                requestCameraPermissions()
            }
        }

        binding.imageFromGalleryView.setOnClickListener {
            galleryActivityResultLauncher.launch("image/*")
        }

        //copy text to clipboard
        binding.copyButton.setOnClickListener{  //copy button on click listener
            if(text2.isNotEmpty()){
                copy(text2)
            }else{
                Toast.makeText(requireContext(), "No text to copy", Toast.LENGTH_SHORT).show()
            }
        }
        //share text
        binding.shareButton.setOnClickListener{ //share button on click listener
            if(text2.isNotEmpty()){
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "text/plain"
                    intent.putExtra(Intent.EXTRA_TEXT, text2)
                    startActivity(Intent.createChooser(intent, "Share via"))
            }else{
                Toast.makeText(requireContext(), "No text to share", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun captureImageFromCamera() {
        // Define the values for the new image
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.TITLE, "Sample Title")
            put(MediaStore.Images.Media.DESCRIPTION, "Sample Description")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg") // Set MIME type
        }
        // Insert the values into the MediaStore and get a URI for the new image
        imageUri = requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues
        )

        imageUri?.let { uri ->
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, uri)
            }
            cameraActivityResultLauncher.launch(uri)
        }
    }

    private fun hasCameraPermissions(): Boolean {
        return cameraPermissions.all { permission -> ContextCompat.checkSelfPermission(requireContext(), permission) == android.content.pm.PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestCameraPermissions() {
        ActivityCompat.requestPermissions(requireActivity(), cameraPermissions, CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray ){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (hasCameraPermissions()) {
                captureImageFromCamera()
            } else {
                // Handle the case when permissions are not granted
                // Show a message to the user explaining the need for permissions
            }
        }
    }

    private fun imageToText(uri: Uri) {
        val image = InputImage.fromFilePath(requireContext(), uri)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                if (visionText.text.isNotEmpty()) {
                    //detect View On click Listener
                    binding.detectView.setOnClickListener {
                        binding.editText.setText(visionText.text)
                        binding.editText.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg)
                        binding.editText.elevation = 10f
                        text2=visionText.text
                    }
                } else {
                    Toast.makeText(requireContext(), "Invalid input OR No text found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                // Handle the error
            }
    }

    fun copy(text: String){
        val clipboard = ContextCompat.getSystemService(requireContext(), android.content.ClipboardManager::class.java)
        val clip = android.content.ClipData.newPlainText("Copied Text", text)
        clipboard?.setPrimaryClip(clip)
        Toast.makeText(requireContext(), "Text copied to clipboard", Toast.LENGTH_SHORT).show()
    }


    companion object {
        private const val CAMERA_REQUEST_CODE = 100
    }
}