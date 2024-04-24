package com.example.editorcamera

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.util.Date
import java.util.Locale

class EditImageActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var imageView: ImageView
    private lateinit var buttonRotate: Button
    private lateinit var filterSpinner: Spinner
    private lateinit var buttonSave: Button
    private lateinit var contrastSeekBar: SeekBar
    private lateinit var brightnessSeekBar: SeekBar
    private lateinit var buttonEdgeDetection: Button

    private lateinit var imagePath: String

    private var contrastValue = 100
    private var brightnessValue = 100

    private lateinit var originalBitmap: Bitmap
    private lateinit var editedBitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_image)

        imageView = findViewById(R.id.imageView)
        buttonRotate = findViewById(R.id.buttonRotate)
        filterSpinner = findViewById(R.id.filterSpinner)
        buttonSave = findViewById(R.id.buttonSave)
        contrastSeekBar = findViewById(R.id.contrastSeekBar)
        brightnessSeekBar = findViewById(R.id.brightnessSeekBar)
        buttonEdgeDetection = findViewById(R.id.buttonEdgeDetection)

        buttonRotate.setOnClickListener(this)
        buttonSave.setOnClickListener(this)
        buttonEdgeDetection.setOnClickListener(this)

        val filterOptions = resources.getStringArray(R.array.filter_options)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, filterOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        filterSpinner.adapter = adapter

        filterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                applyFilter(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Não é necessário fazer nada aqui
            }
        }

        // Recebe o caminho da imagem capturada da Intent
        imagePath = intent.getStringExtra("imagePath") ?: ""
        if (imagePath.isNotEmpty()) {
            // Carrega a imagem capturada e ajusta a orientação, se necessário
            val bitmap = BitmapFactory.decodeFile(imagePath)
            val orientedBitmap = ImageUtils.rotateImageIfRequired(bitmap, imagePath)
            imageView.setImageBitmap(orientedBitmap)
            originalBitmap = orientedBitmap
        }

        // Configurar listeners para as SeekBars
        contrastSeekBar.setOnSeekBarChangeListener(seekBarChangeListener)
        brightnessSeekBar.setOnSeekBarChangeListener(seekBarChangeListener)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.buttonRotate -> {
                rotateImage(90f)
            }
            R.id.buttonSave -> {
                saveImageToGallery(editedBitmap)
            }
            R.id.buttonEdgeDetection -> {
                detectEdges()
            }
        }
    }

    private fun detectEdges() {
        val edgesBitmap = applySobel(originalBitmap)
        imageView.setImageBitmap(edgesBitmap)
        editedBitmap = edgesBitmap
    }

    private fun applySobel(bitmap: Bitmap): Bitmap {
        val sobelX = arrayOf(intArrayOf(-1, 0, 1), intArrayOf(-2, 0, 2), intArrayOf(-1, 0, 1))
        val sobelY = arrayOf(intArrayOf(-1, -2, -1), intArrayOf(0, 0, 0), intArrayOf(1, 2, 1))

        val grayscale = toGrayscale(bitmap)
        val edgesBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.RGB_565)

        for (y in 1 until bitmap.height - 1) {
            for (x in 1 until bitmap.width - 1) {
                var sumX = 0
                var sumY = 0

                for (i in -1..1) {
                    for (j in -1..1) {
                        val pixel = grayscale[y + i][x + j]
                        sumX += pixel * sobelX[i + 1][j + 1]
                        sumY += pixel * sobelY[i + 1][j + 1]
                    }
                }

                val magnitude = Math.sqrt((sumX * sumX + sumY * sumY).toDouble()).toInt()
                edgesBitmap.setPixel(x, y, Color.rgb(magnitude, magnitude, magnitude))
            }
        }

        return edgesBitmap
    }

    private fun toGrayscale(bitmap: Bitmap): Array<IntArray> {
        val grayscale = Array(bitmap.height) { IntArray(bitmap.width) }

        for (y in 0 until bitmap.height) {
            for (x in 0 until bitmap.width) {
                val pixel = bitmap.getPixel(x, y)
                grayscale[y][x] = (Color.red(pixel) * 0.299 + Color.green(pixel) * 0.587 + Color.blue(pixel) * 0.114).toInt()
            }
        }

        return grayscale
    }


    private fun applyFilter(position: Int) {
        val bitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true)

        when (position) {
            0 -> {} // Nenhum filtro
            1 -> applyGrayscaleFilter(bitmap)
            2 -> applySepiaFilter(bitmap)
            3 -> applyInvertFilter(bitmap)
            // Adicione mais casos conforme necessário para outros filtros
        }
    }

    private fun applyGrayscaleFilter(bitmap: Bitmap) {
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f)

        val filter = ColorMatrixColorFilter(colorMatrix)
        applyColorFilter(bitmap, filter)
    }

    private fun applySepiaFilter(bitmap: Bitmap) {
        val colorMatrix = ColorMatrix()
        colorMatrix.set(floatArrayOf(
            0.393f, 0.769f, 0.189f, 0f, 0f,
            0.349f, 0.686f, 0.168f, 0f, 0f,
            0.272f, 0.534f, 0.131f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        ))

        val filter = ColorMatrixColorFilter(colorMatrix)
        applyColorFilter(bitmap, filter)
    }

    private fun applyInvertFilter(bitmap: Bitmap) {
        val colorMatrix = ColorMatrix()
        colorMatrix.set(floatArrayOf(
            -1f, 0f, 0f, 0f, 255f,
            0f, -1f, 0f, 0f, 255f,
            0f, 0f, -1f, 0f, 255f,
            0f, 0f, 0f, 1f, 0f
        ))

        val filter = ColorMatrixColorFilter(colorMatrix)
        applyColorFilter(bitmap, filter)
    }


    private fun applyColorFilter(bitmap: Bitmap, filter: ColorFilter) {
        val paint = Paint()
        paint.colorFilter = filter

        val canvas = Canvas(bitmap)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        editedBitmap = bitmap
        imageView.setImageBitmap(editedBitmap)
    }

    private val seekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            seekBar?.let {
                when (it.id) {
                    R.id.contrastSeekBar -> {
                        contrastValue = progress - 100 // Normalizando para o intervalo -100 a 100
                        applyFilters()
                    }
                    R.id.brightnessSeekBar -> {
                        brightnessValue = progress - 100 // Normalizando para o intervalo -100 a 100
                        applyFilters()
                    }
                }
            }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {}

        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
    }

    private fun applyFilters() {
        editedBitmap = applyContrastAndBrightness(originalBitmap, contrastValue, brightnessValue)
        imageView.setImageBitmap(editedBitmap)
    }

    private fun applyContrastAndBrightness(bitmap: Bitmap, contrast: Int, brightness: Int): Bitmap {
        val cm = ColorMatrix().apply {
            set(floatArrayOf(
                contrast / 100f, 0f, 0f, 0f, 0f,
                0f, contrast / 100f, 0f, 0f, 0f,
                0f, 0f, contrast / 100f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            ))
        }

        val brightnessCM = ColorMatrix().apply {
            set(floatArrayOf(
                1f, 0f, 0f, 0f, brightness.toFloat(),
                0f, 1f, 0f, 0f, brightness.toFloat(),
                0f, 0f, 1f, 0f, brightness.toFloat(),
                0f, 0f, 0f, 1f, 0f
            ))
        }

        val paint = Paint().apply {
            colorFilter = ColorMatrixColorFilter(ColorMatrix().apply {
                postConcat(cm)
                postConcat(brightnessCM)
            })
        }

        val filteredBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        Canvas(filteredBitmap).drawBitmap(bitmap, 0f, 0f, paint)
        return filteredBitmap
    }

    private fun rotateImage(degrees: Float) {
        imageView.drawable?.let { drawable ->
            if (drawable is BitmapDrawable) {
                val bitmap = drawable.bitmap
                val rotatedBitmap = ImageUtils.rotateImage(bitmap, degrees)
                imageView.setImageBitmap(rotatedBitmap)
                editedBitmap = rotatedBitmap
            }
        }
    }

    private fun cropBitmap(bitmap: Bitmap, rect: Rect): Bitmap {
        return Bitmap.createBitmap(bitmap, rect.left, rect.top, rect.width(), rect.height())
    }

    private fun saveImageToGallery(bitmap: Bitmap) {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "IMG_${timeStamp}.jpg"

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "EditorCamera")
        }

        val resolver = contentResolver

        // Salva a imagem em um novo thread para evitar bloquear a thread principal
        Thread {
            val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            imageUri?.let { uri ->
                try {
                    resolver.openOutputStream(uri)?.use { outputStream ->
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    }

                    // Notifica a galeria sobre a nova imagem
                    sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))

                    // Retorna à Activity anterior com o caminho da imagem salva
                    val returnIntent = Intent()
                    returnIntent.putExtra("editedImagePath", uri.toString())
                    setResult(RESULT_OK, returnIntent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }.start()

        val returnIntent = Intent()

        val imageUri= MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Title", null)
        returnIntent.putExtra("editedImagePath", imageUri)
        setResult(RESULT_OK, returnIntent)

        // Finaliza a Activity
        finish()
    }
}


