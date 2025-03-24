package org.intelehealth.data.provider.protocols.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.github.ajalt.timberkt.Timber
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.intelehealth.common.service.BaseResponse
import org.intelehealth.common.service.HttpStatusCode.HTTP_SUCCESS
import org.intelehealth.common.state.StateWorker
import org.intelehealth.data.provider.protocols.ProtocolsRepository
import org.intelehealth.data.provider.utils.CONFIG_JSON_FILE
import org.intelehealth.data.provider.utils.ENGINE_FOLDER
import org.intelehealth.data.provider.utils.FAMILY_HISTORY_JSON_FILE
import org.intelehealth.data.provider.utils.LOGO_FILE
import org.intelehealth.data.provider.utils.PATIENT_HISTORY_JSON_FILE
import org.intelehealth.data.provider.utils.PHYSICAL_EXAM_JSON_FILE
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import kotlin.Any
import kotlin.ByteArray
import kotlin.Exception
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.also
import kotlin.let
import kotlin.to
import org.intelehealth.common.state.Result as StateResult

/**
 * Created by Vaghela Mithun R. on 20-03-2025 - 12:40.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@HiltWorker
class DownloadProtocolsWorker @AssistedInject constructor(
    @Assisted private val ctx: Context,
    @Assisted private val params: WorkerParameters,
    private val protocolsRepository: ProtocolsRepository,
    private val okHttpClient: OkHttpClient
) : StateWorker(ctx, params) {
    private var progress = 0

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        params.inputData.getString(PROTOCOL_LICENSE_KEY)?.let {
            protocolsRepository.getDownloadProtocolsUrl(licenseKey = it).collect {
                withContext(Dispatchers.IO) { checkResultState(it) }
            }
        } ?: setFailResult()
        workerResult
    }

    private fun setFailResult() {
        workerResult = Result.failure()
    }

    private suspend fun checkResultState(result: StateResult<BaseResponse<Any?, String>>) {
        // Check if the download was successful
        handleState(result) { withContext(Dispatchers.IO) { handleSuccess(it) } }
    }

    private suspend fun handleSuccess(response: BaseResponse<Any?, String>) = withContext(Dispatchers.IO) {
        // Handle the success
        if (response.message == HTTP_SUCCESS) {
            response.data?.let { downloadAndExtractProtocolsZipFile(it) }
        } else {
            workerResult = Result.failure()
        }
    }

    private suspend fun downloadAndExtractProtocolsZipFile(protocolZipUrl: String) = withContext(Dispatchers.IO) {
        // Download the protocols
        setProgress(workDataOf(WORK_STATUS to PROTOCOL_LICENSE_KEY_VALIDATED))
        val request = Request.Builder().url(protocolZipUrl).build()
        val response = okHttpClient.newCall(request).execute()

        if (!response.isSuccessful) workerResult = Result.failure()

        val inputStream = response.body?.byteStream() ?: throw Exception("Failed to get input stream")
        deletePreviousProtocolsIfExists()
        extractZipFile(inputStream, ctx.filesDir.absolutePath, response.body?.contentLength() ?: 0)
    }

    private fun deletePreviousProtocolsIfExists() {
        File(ctx.filesDir.absolutePath, "/${ENGINE_FOLDER}").deleteRecursively()
        File(ctx.filesDir.absolutePath, "/${PHYSICAL_EXAM_JSON_FILE}").deleteRecursively()
        File(ctx.filesDir.absolutePath, "/${PATIENT_HISTORY_JSON_FILE}").deleteRecursively()
        File(ctx.filesDir.absolutePath, "/${FAMILY_HISTORY_JSON_FILE}").deleteRecursively()
        File(ctx.filesDir.absolutePath, "/${CONFIG_JSON_FILE}").deleteRecursively()
        File(ctx.filesDir.absolutePath, "/${LOGO_FILE}").deleteRecursively()
    }

    private suspend fun extractZipFile(
        inputStream: InputStream,
        outputDir: String,
        contentLength: Long
    ) = withContext(Dispatchers.IO) {
        setProgress(workDataOf(WORK_PROGRESS to progress))
        val buffer = ByteArray(1024)
        val zis = ZipInputStream(inputStream)
        var zipEntry: ZipEntry? = zis.nextEntry
        var totalRead = 0L
        while (zipEntry != null) {
            val newFile = File(outputDir, zipEntry.name)
            if (zipEntry.isDirectory) {
                newFile.mkdirs()
            } else {
                newFile.parentFile?.mkdirs()
                val fos = FileOutputStream(newFile)
                var len: Int
                while (zis.read(buffer).also { len = it } > 0) {
                    fos.write(buffer, 0, len)
                    totalRead += len
                }
                fos.close()
            }
            zipEntry = zis.nextEntry
            progress = ((totalRead * 100) / contentLength).toInt()
            Timber.d { "Progress: $progress" }
            Timber.d { "Total Read: $totalRead" }
            Timber.d { "Content Length: $contentLength" }
            setProgress(workDataOf(WORK_PROGRESS to progress))
        }
        zis.closeEntry()
        zis.close()
        workerResult = Result.success()
    }

    companion object {
        const val PROTOCOL_LICENSE_KEY = "protocol_license_key"
        const val PROTOCOL_LICENSE_KEY_VALIDATED = "protocol_license_key_validated"
    }
}
