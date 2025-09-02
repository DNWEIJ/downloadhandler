package com.dwe.springboot.tutorial.service


import com.dwe.springboot.tutorial.configuration.CarProperties
import com.dwe.springboot.tutorial.exception.StorageException
import com.dwe.springboot.tutorial.model.CarRecord
import org.springframework.stereotype.Service
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

@Service
class FileCarStorageServiceImpl(
    val properties: CarProperties
) : FileCarStorageService {

    override fun init() {
        try {
            val rootLocation = Paths.get(properties.location)
            Files.createDirectories(rootLocation)
            Files.createFile(Paths.get(properties.location + "/" + properties.file))
        } catch (exception: IOException) {
            if (exception is java.nio.file.FileAlreadyExistsException) {
                return
            } else {
                throw StorageException("Could not initialize storage")
            }

        }
    }

    override fun saveRecord(car: CarRecord) {

        Files.writeString(
            Paths.get(properties.location + "/" + properties.file),
            car.toString() + System.lineSeparator(), StandardOpenOption.APPEND
        )
    }

}