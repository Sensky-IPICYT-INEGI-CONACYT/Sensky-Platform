package com.youilab.senskywebapp.util;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;

public class FileHandler {

    private String fileExtension, fileName, fileType, filePath, fullFilePath;
    private String owner;
    private File file;
    private InputStream stream;
    private final static String STORE = "/var/www/html/sensky_store/";

    public FileHandler(MultipartFile file, String type, String owner) throws Exception{
        this.stream = file.getInputStream();
        this.fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());

        //this.file = file.getResource().getFile();
        this.owner = owner;
        if (type.equals("image")) processImageFile();
        else processSurveyFile();
        store();
    }

    public FileHandler() {
    }

    /**
     * Use this method to read the content of a file, specially for files containing JSON array structures.
     * @param filePath The path of the file to read.
     * @return The content of the file or an empty JSON Array.
     */
    public String readFile(String filePath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            StringBuilder tmpString = new StringBuilder();
            String tmpLine;
            while ((tmpLine = reader.readLine()) != null) tmpString.append(tmpLine);
            return tmpString.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("[SenSky] Parallel tasks file not found");
            return "[]";
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("[SenSky] Cannot read parallel tasks file");
            return "[]";
        }
    }

    /**
     * Encodes the filename
     */
    public void processImageFile() {
        this.fileType = "image";
        this.fileName = Encoders.toSHA256("" + System.currentTimeMillis()) + "." + this.fileExtension;

    }

    /**
     * Encodes the filename
     */
    public void processSurveyFile() {
        this.fileType = "survey";
        this.fileName = Encoders.toSHA256("" + System.currentTimeMillis()) + "." + this.fileExtension;
    }

    /**
     * Moves a file to a directory specifying open permissions on the directory
     * @throws Exception if an error occurs when moving the file to the destiny directory.
     */
    public void store() throws Exception{
        Path targetDir = Paths.get(STORE + Encoders.toBase64(this.owner));
        if (!Files.exists(targetDir)) targetDir = Files.createDirectory(
                targetDir, PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwxrwxrwx"))
            );
        String absolutePath = targetDir.toAbsolutePath() + File.separator + this.fileName;
        moveFile(this.stream, absolutePath);
        this.filePath = Encoders.toBase64(this.owner) + File.separator + this.fileName;
    }

    /**
     * Moves an input stream bytes to a destiny file.
     * @param fileStream The input stream that contains the bytes to move.
     * @param dest The destiny or target file.
     * @return true if every byte was moved correctly, false otherwise.
     */
    private Boolean moveFile(InputStream fileStream, String dest){
        try {
            int read;
            byte[] bytes = new byte[1024];
            OutputStream out = new FileOutputStream(new File(dest));
            while ((read = fileStream.read(bytes)) != -1) out.write(bytes, 0, read);
            out.flush();
            out.close();
            Runtime.getRuntime().exec("chmod 777 " + dest);
            return true;
        } catch (IOException e){
            return false;
        }
    }

    /**
     * Getter for this instance file path attribute.
     * @return this instance file path
     */
    public String getFilePath() {
        return this.filePath;
    }



}
