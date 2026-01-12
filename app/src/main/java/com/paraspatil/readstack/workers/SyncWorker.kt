package com.paraspatil.readstack.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.paraspatil.readstack.domain.repository.BookRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val bookRepository: BookRepository

): CoroutineWorker(appContext,workerParams){
    override suspend fun doWork(): Result {
        return try {
            val result=bookRepository.syncLibrary()
            if(result.isSuccess){
                Result.success()
            }else{
                Result.retry()
            }
        }catch (e:Exception){
            Result.failure()
        }
    }
    companion object{
        const val WORK_NAME="book_sync_worker"

    }
}