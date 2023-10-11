package com.afolayanseyi.springboot.tutorial.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
internal class FileUploadRestControllerTest @Autowired constructor(
    val mockMvc: MockMvc
) {

    private val file = MockMultipartFile(
        "file",
        "hello.txt",
        MediaType.TEXT_PLAIN_VALUE,
        "Hello, World!".toByteArray()
    )

    @Test
    fun `should save a file`() {
        // when / then
        mockMvc.perform(multipart("/uploadFile").file(file))
            .andExpect(status().isOk)
            .andExpect {
                jsonPath("$.fileDownloadUrl").value("http://localhost/fake/hello.txt")
                jsonPath("$.fileName").value("hello.txt")
            }
    }

    @Test
    fun `should return all files`() {
        // given
        mockMvc.perform(multipart("/uploadFile").file(file))
            .andExpect(status().isOk)

        // when / then
        mockMvc.get("/getFiles")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$[0].fileDownloadUrl") {
                    isNotEmpty()
                }
                jsonPath("$[0].fileName") {
                    value("hello.txt")
                }
            }
    }
}