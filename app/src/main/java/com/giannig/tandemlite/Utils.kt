package com.giannig.tandemlite

import kotlinx.coroutines.Job

fun Job?.cancelIfActive() {
    if(this != null){
        if (this.isActive){
            this.cancel()
        }
    }
}
