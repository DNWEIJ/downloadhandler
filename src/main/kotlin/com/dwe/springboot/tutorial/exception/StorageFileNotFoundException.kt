package com.dwe.springboot.tutorial.exception

class StorageFileNotFoundException : StorageException {
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
}