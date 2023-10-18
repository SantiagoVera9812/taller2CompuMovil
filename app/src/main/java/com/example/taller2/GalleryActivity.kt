package com.example.taller2
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.taller2.databinding.ActivityGalleryBinding
import java.io.File
import java.io.IOException

class GalleryActivity : AppCompatActivity() {
    //Acà me encarguè de inicializar variables necesarias para el resto de la interfaz.
    private lateinit var binding: ActivityGalleryBinding
    private var cameraUri: Uri? = null
    val getContentGallery = registerForActivityResult(
        ActivityResultContracts.GetContent(),
        { uri ->
            if (uri != null) {
                loadImage(uri)
            }
        }
    )
    val getContentCamera = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            if (cameraUri != null) {
                loadImage(cameraUri!!)
            }
        }
    }

    // inicialización de la actividad como tal,
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Por si quiere seleccionar una imagen desde galeria.
        binding.galleryButton.setOnClickListener {
            getContentGallery.launch("image/*")
        }
        // Por si quiere es tomar una imagen desde su camara.
        binding.cameraButton.setOnClickListener {

            val file = createImageFile()
            cameraUri = FileProvider.getUriForFile(
                baseContext,
                baseContext.packageName + ".fileprovider",
                file
            )
            getContentCamera.launch(cameraUri)

        }
    }


    // Verifica si existe un permiso en la camara
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 123) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    val file = createImageFile()
                    cameraUri = FileProvider.getUriForFile(baseContext, baseContext.packageName + ".fileprovider", file)
                    getContentCamera.launch(cameraUri)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    // Se guarda un archivo en el almacenamiento externo
    private fun createImageFile(): File {
        val timeStamp = System.currentTimeMillis().toString()
        val storageDir = getExternalFilesDir(null)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }

    // Correcion de orientacion
    private fun fixImageOrientation(uri: Uri, bitmap: Bitmap): Bitmap {
        val orientationColumnIndex = ExifInterface.TAG_ORIENTATION
        // Acà se obtiene la informaciòn de rotaciòn de la imagen tomada.
        val inputStream = contentResolver.openInputStream(uri)
        val exif = inputStream?.let { ExifInterface(it) }
        val orientation = exif?.getAttributeInt(orientationColumnIndex, ExifInterface.ORIENTATION_NORMAL)
        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    // Crea un bitmap segun la uri de la camara
    private fun loadImage(uri: Uri) {
        val imageStream = contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(imageStream)
        // Se tiene un tamaño fijo para la camara
        val desiredWidthInPixels = 1000
        val desiredHeightInPixels = 1000
        val layoutParams = binding.image.layoutParams
        layoutParams.width = desiredWidthInPixels
        layoutParams.height = desiredHeightInPixels
        binding.image.layoutParams = layoutParams
        // Acà corrijo la orientación de la imagen
        val fixedBitmap = fixImageOrientation(uri, bitmap)
        binding.image.setImageBitmap(fixedBitmap)
    }
}

