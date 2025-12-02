package com.orcas.controller;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.orcas.constants.MappingConstants;
import com.orcas.service.PdfDataService;

@RestController
@RequestMapping(MappingConstants.URL_API_V1)
public class PdfUploadController {

	@Autowired
    private PdfDataService pdfDataService;
	
	@PostMapping(MappingConstants.URL_UPLOAD_SCORECARD)
	public ResponseEntity<Map<String, String>> uploadPdf(@RequestParam("file") MultipartFile file) {
		Map<String, String> response = new HashMap<>();
		try {
			pdfDataService.readPdfFile(file);
			response.put("message", "File uploaded successfully!");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (FileAlreadyExistsException e) {
			response.put("message", "Failed to upload PDF: Duplicate File");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (IOException e) {
			e.printStackTrace();
			response.put("message", "Failed to upload PDF: " + e);
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}