package com.example.editorcamera

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.Size
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.File
import java.io.FileOutputStream
import java.util.*


class MainActivity : AppCompatActivity() {

    private val TAG = "Camera2Example"
    private val REQUEST_CAMERA_PERMISSION = 200
    private val REQUEST_EDIT_IMAGE = 201

    private lateinit var mSurfaceView: SurfaceView
    private lateinit var mCaptureButton: Button

    private lateinit var mCameraManager: CameraManager
    private lateinit var mCameraId: String
    private var mCameraDevice: CameraDevice? = null
    private lateinit var mCaptureSession: CameraCaptureSession
    private lateinit var mPreviewRequestBuilder: CaptureRequest.Builder
    private lateinit var mImageReader: ImageReader

    private var mBackgroundHandler: Handler? = null
    private var mBackgroundThread: HandlerThread? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSurfaceView = findViewById(R.id.surfaceView)
        mCaptureButton = findViewById(R.id.captureButton)

        mImageReader = ImageReader.newInstance(640, 480, ImageFormat.JPEG, 1)

        mSurfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                openCamera()
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                closeCamera()
            }
        })

        mCaptureButton.setOnClickListener { captureImage() }
    }

    private fun openCamera() {
        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            // Verifica permissões
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // Solicita permissão se não estiver concedida
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
                return
            }

            val cameraIdList = cameraManager.cameraIdList
            if (cameraIdList.isEmpty()) {
                // Não há câmeras disponíveis
                return
            }

            val cameraId = cameraIdList[0] // Assume que estamos usando a primeira câmera disponível

            // Configurações da câmera
            val characteristics = cameraManager.getCameraCharacteristics(cameraId)
            val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            val outputSizes = map?.getOutputSizes(SurfaceTexture::class.java) ?: arrayOf()
            val previewSize = chooseOptimalSize(outputSizes, mSurfaceView.width, mSurfaceView.height)

            // Configura a dimensão do SurfaceView
            mSurfaceView.holder.setFixedSize(previewSize.width, previewSize.height)

            // Configura a dimensão do ImageReader
            mImageReader = ImageReader.newInstance(previewSize.width, previewSize.height, ImageFormat.JPEG, 1)
            val mOnImageAvailableListener = ImageReader.OnImageAvailableListener { reader ->
                val image = reader.acquireNextImage()
                val buffer = image.planes[0].buffer
                val bytes = ByteArray(buffer.remaining())
                buffer.get(bytes)
                val outputDirectory = getExternalFilesDir(null)
                val file = File(outputDirectory, "photo.jpg")
                val outputStream = FileOutputStream(file)
                try {
                    outputStream.write(bytes)
                } finally {
                    image.close()
                    outputStream.close()
                }
            }
            mImageReader.setOnImageAvailableListener(mOnImageAvailableListener, null)

            // Abre a câmera
            cameraManager.openCamera(cameraId, object : CameraDevice.StateCallback() {
                override fun onOpened(camera: CameraDevice) {
                    mCameraDevice = camera
                    createCameraPreviewSession()
                }

                override fun onDisconnected(camera: CameraDevice) {
                    camera.close()
                    mCameraDevice = null
                }

                override fun onError(camera: CameraDevice, error: Int) {
                    camera.close()
                    mCameraDevice = null
                    // Trate o erro aqui, se necessário
                }
            }, null)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun chooseOptimalSize(outputSizes: Array<Size>, width: Int, height: Int): Size {
        // Sort the output sizes in descending order by area
        val sortedSizes = outputSizes.sortedWith(compareBy { it.height * it.width })

        // Return the smallest size that is at least as large as the desired size, or the largest size if none are big enough
        return sortedSizes.firstOrNull { it.width >= width && it.height >= height } ?: sortedSizes.last()
    }


    private fun createCameraPreviewSession() {
        try {
            val surface = mSurfaceView.holder.surface
            mPreviewRequestBuilder = mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            mPreviewRequestBuilder.addTarget(surface)

            mCameraDevice!!.createCaptureSession(Arrays.asList(surface, mImageReader.surface), object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(session: CameraCaptureSession) {
                    if (mCameraDevice == null) {
                        return
                    }
                    mCaptureSession = session
                    try {
                        mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
                        mCaptureSession.setRepeatingRequest(mPreviewRequestBuilder.build(), null, mBackgroundHandler)
                    } catch (e: CameraAccessException) {
                        e.printStackTrace()
                    }
                }

                override fun onConfigureFailed(session: CameraCaptureSession) {
                    Toast.makeText(this@MainActivity, "Failed to create camera session", Toast.LENGTH_SHORT).show()
                }
            }, null)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun captureImage() {
        mCameraDevice?.let { camera ->
            try {
                val captureBuilder = camera.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
                captureBuilder.addTarget(mImageReader.surface)

                mCaptureSession.stopRepeating()
                mCaptureSession.abortCaptures()

                val file = File(externalCacheDir, "temp_image.jpg")

                mImageReader.setOnImageAvailableListener({ reader ->
                    val image = reader.acquireLatestImage()
                    val buffer = image.planes[0].buffer
                    val bytes = ByteArray(buffer.capacity())
                    buffer.get(bytes)

                    file.outputStream().use {
                        it.write(bytes)
                    }

                    val intent = Intent(this@MainActivity, EditImageActivity::class.java)
                    intent.putExtra("imagePath", file.absolutePath)
                    startActivityForResult(intent, REQUEST_EDIT_IMAGE)
                }, mBackgroundHandler)

                mCaptureSession.capture(captureBuilder.build(), null, mBackgroundHandler)
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
        }
    }

    private fun closeCamera() {
        mCameraDevice?.close()
        mCameraDevice = null
        mCaptureSession.close()
        mCaptureSession = mCaptureSession ?: return
        mImageReader.close()
    }

    override fun onResume() {
        super.onResume()
        startBackgroundThread()
    }

    override fun onPause() {
        closeCamera()
        stopBackgroundThread()
        super.onPause()
    }

    private fun startBackgroundThread() {
        mBackgroundThread = HandlerThread("CameraBackground")
        mBackgroundThread!!.start()
        mBackgroundHandler = Handler(mBackgroundThread!!.looper)
    }

    private fun stopBackgroundThread() {
        mBackgroundThread?.quitSafely()
        try {
            mBackgroundThread?.join()
            mBackgroundThread = null
            mBackgroundHandler = null
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_EDIT_IMAGE && resultCode == RESULT_OK) {
            val editedImagePath = data?.getStringExtra("editedImagePath")
            if (editedImagePath != null) {
                // Faça algo com o caminho da imagem editada, como exibir ou processar
            }
        }
    }

}
