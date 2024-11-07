package com.ipn.mx.service.interfaces;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FilesService {

	public void init();
	public Resource loadImage(String filename) throws MalformedURLException;
	public Resource loadPdf(String filename) throws IOException;
	public String saveImage(MultipartFile file) throws IOException;
	public String savePdf(MultipartFile file) throws IOException;
	public boolean delete(String filename);
	
}
