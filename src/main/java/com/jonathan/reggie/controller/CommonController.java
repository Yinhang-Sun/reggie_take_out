package com.jonathan.reggie.controller;


import com.jonathan.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * File/image upload and download
 */

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;


    /**
     * File upload
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        //File is a temporary file and needs to be transferred to the specified location,
        // otherwise the temporary file will be deleted after this request is completed.
        log.info(file.toString());

        //Original file name
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        //Use UUID to regenerate file names to prevent file overwriting caused by duplicate file names.
        String fileName = UUID.randomUUID().toString() + suffix;

        // Create a directory object
        File dir = new File(basePath);
        // Determine whether the current directory exists
        if (!dir.exists()) {
            //The directory does not exist and needs to be created
            dir.mkdirs();
        }

        try {
            // Transfer temporary files to a specified location
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return R.success(fileName);
    }

    /**
     * File download
     *
     * @param name
     * @param response
     */

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        //Input stream, read the file content through the input stream
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));

            //Output stream, write the file back to the browser through the output stream,
            // and display the image in the browser
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }

            // Close resource
            outputStream.close();
            fileInputStream.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
