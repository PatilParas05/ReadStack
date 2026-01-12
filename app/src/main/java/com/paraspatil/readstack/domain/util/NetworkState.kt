package com.paraspatil.readstack.domain.util

sealed class NetworkResult<T>{
    data class Success<T>(val data:T):NetworkResult<T>()
    data class Error(val message:String,val exception:Exception?=null):NetworkResult<Nothing>()
    data class Loading<T>(val data:T?=null):NetworkResult<T>()
    data class Offline<T>(val data:T?=null):NetworkResult<T>()

}

data class UiState<T>(
    val data:T?=null,
    val isLoading:Boolean=false,
    val error:String?=null,
    val isOffline:Boolean=false
)