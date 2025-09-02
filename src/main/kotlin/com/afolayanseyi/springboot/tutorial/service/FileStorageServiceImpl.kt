package com.afolayanseyi.springboot.tutorial.service

import com.afolayanseyi.springboot.tutorial.configuration.StorageProperties
import com.afolayanseyi.springboot.tutorial.exception.StorageException
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Service
class FileStorageServiceImpl(
    properties: StorageProperties
) : FileStorageService {
    private val rootLocation = Paths.get(properties.location)

    override fun init() {
        try {
            Files.createDirectories(rootLocation)
        } catch (exception: IOException) {
            throw StorageException("Could not initialize storage")
        }
    }

    override fun saveFile(file: MultipartFile): String {
        return try {
            val originalFilename = file.originalFilename ?: ""
            if (file.isEmpty || originalFilename.isBlank())
                throw StorageException("Failed to store empty file.")

            val destinationFile = rootLocation.resolve(
                Paths.get(originalFilename)
            ).normalize().toAbsolutePath()

            if (destinationFile.parent != rootLocation.toAbsolutePath()) {
                throw StorageException("Cannot store file outside current directory")
            }

            file.inputStream.use { inputStream ->
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING)
            }
            StringUtils.cleanPath(originalFilename)
        } catch (exception: IOException) {
            throw StorageException("Failed to read stored files", exception)
        }
    }


}