package com.ipn.mx.service.implementation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.NoSuchElementException;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ipn.mx.exeptions.InvalidRequestExeption;
import com.ipn.mx.service.interfaces.FilesService;

import jakarta.annotation.PostConstruct;

@Service
public class FilesServiceImpl implements FilesService {
	
	@Value("${media.location.img}")
	private String PATH_IMG;
	@Value("${media.location.pdf}")
	private String PATH_PDF;
	
	@Override
	@PostConstruct
	public void init() {
		Path pathImg = Paths.get(PATH_IMG);
		Path pathPdf = Paths.get(PATH_PDF);
        if (!Files.exists(pathImg) || !Files.exists(pathPdf)) {
            try {
                Files.createDirectories(pathImg);
                System.out.println("Directorio creado: " + PATH_IMG);
            } catch (IOException e) {
                System.err.println("Error al crear el directorio: " + e.getMessage());
                throw new RuntimeException("No se pudo crear el directorio", e);
            }
            try {
                Files.createDirectories(pathPdf);
                System.out.println("Directorio creado: " + PATH_PDF);
            } catch (IOException e) {
                System.err.println("Error al crear el directorio: " + e.getMessage());
                throw new RuntimeException("No se pudo crear el directorio", e);
            }
        }
	}

	@Override
	public String saveImage(MultipartFile file) throws IOException {
		if (file.isEmpty()) {
			throw new InvalidRequestExeption("No hay imagen adjunta"); 
		}
		String uniqueFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
		Path rootPath = getPathImg(uniqueFilename);
		Files.copy(file.getInputStream(), rootPath);
		return uniqueFilename;
	}

	@Override
	public Resource loadImage(String filename) throws MalformedURLException {
		Path path = getPathImg(filename);
		Resource resource = new UrlResource(path.toUri());
		if (!resource.exists() || !resource.isReadable()) {
			throw new NoSuchElementException("Error in path: " + path.toString());
		}
		return resource;
	}
	
	@Override
	public String savePdf(MultipartFile file) throws IOException {
		if (file.isEmpty()) {
            throw new InvalidRequestExeption("Archivo requerido");
        }
		String uuid = UUID.randomUUID().toString() + "_";
        String uniqueFilename = uuid + file.getOriginalFilename();
        Path encryptedFilePath = getPathPdf(uniqueFilename);

        Path tempFilePath = Files.createTempFile("temp_" + uuid, file.getOriginalFilename());
        Files.copy(file.getInputStream(), tempFilePath);

        File inputFile = tempFilePath.toFile();
        File outputFile = encryptedFilePath.toFile();
        try {
			encryptFile(inputFile, outputFile);
		} catch (Exception e) {
			throw new RuntimeException("Fallo al encriptar el archivo");
		} finally {
			Files.delete(tempFilePath);
		}
        return uniqueFilename;
    }

	@Override
	public Resource loadPdf(String filename) throws IOException{
	    Path encryptedPath = getPathPdf(filename);
	    Path tmpFilePath = Paths.get(System.getProperty("java.io.tmpdir")).resolve(filename);
	    if (Files.exists(tmpFilePath)) {
			return new UrlResource(tmpFilePath.toUri());
		}
	    if (!Files.exists(encryptedPath)) {
	        throw new NoSuchElementException("Error: No se encontró el archivo encriptado en la ruta: " + encryptedPath);
	    }
	    filename = filename.substring(0, filename.length() - 4);
	    Path decryptedPath = Files.createTempFile(filename, ".pdf");
	    File encryptedFile = encryptedPath.toFile();
	    File decryptedFile = decryptedPath.toFile();
	    
	    try {
			decryptFile(encryptedFile, decryptedFile);
		} catch (Exception e) {
			throw new RuntimeException("Fallo al desencriptar el archivo");
		}

	    Resource resource = new UrlResource(decryptedPath.toUri());

	    if (!resource.exists() || !resource.isReadable()) {
	        throw new IOException("Error: No se puede leer el archivo desencriptado en la ruta: " + decryptedPath);
	    }

	    return resource;
	}

	@Override
	public boolean delete(String filename) {
		boolean deleted;
		File tmpFile = Paths.get(System.getProperty("java.io.tmpdir")).resolve(filename).toFile();
		File file;
		if(filename.endsWith(".pdf")) {
			file = getPathPdf(filename).toFile();
		} else {
			file = getPathImg(filename).toFile();
		}
		
		deleted = file.delete();
		if (tmpFile.exists())
			tmpFile.delete();
		
		return deleted;
	}

	private Path getPathImg(String filename) {
		return Paths.get(PATH_IMG).resolve(filename).toAbsolutePath();
	}
	
	private Path getPathPdf(String filename) {
		return Paths.get(PATH_PDF).resolve(filename).toAbsolutePath();
	}
	
	
	private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String KEY = "1234567890123456";

    private void encryptFile(File inputFile, File outputFile) throws Exception {
        SecretKey secretKey = new SecretKeySpec(KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        
        byte[] iv = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

        try (FileInputStream inputStream = new FileInputStream(inputFile);
             FileOutputStream outputStream = new FileOutputStream(outputFile);
             CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, cipher)) {
            outputStream.write(iv);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                cipherOutputStream.write(buffer, 0, bytesRead);
            }
        }
    }

    private static void decryptFile(File inputFile, File outputFile) throws Exception {
        SecretKey secretKey = new SecretKeySpec(KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance(ALGORITHM);

        try (FileInputStream inputStream = new FileInputStream(inputFile)) {
            byte[] iv = new byte[16];
            inputStream.read(iv); // Lee el IV del archivo
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

            try (CipherInputStream cipherInputStream = new CipherInputStream(inputStream, cipher);
                 FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = cipherInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        }
    }
}
