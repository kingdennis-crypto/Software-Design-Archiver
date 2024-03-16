package nl.vu.cs.softwaredesign.lib.CompressionFormats;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import nl.vu.cs.softwaredesign.lib.Annotations.CompressionType;
import nl.vu.cs.softwaredesign.lib.Interfaces.ICompressionFormat;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@CompressionType(label="ZIP", description="Zip description")
public class ZipCompression implements ICompressionFormat {
    /**
     * Compresses the files in the given source directory into a ZIP archive with the specified destination path.
     *
     * @param destinationPath The path of the resulting ZIP archive.
     * @throws IOException If an I/O error occurs during compression.
     */
    @Override
    public ZipFile compress(List<File> files, String destinationPath, String password) throws IOException {
        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setEncryptFiles(true);
        zipParameters.setEncryptionMethod(EncryptionMethod.AES);

        ZipFile zipFile = new ZipFile(destinationPath, password.toCharArray());

        for (File file : files) {
            File fileToAdd = new File(file.getPath());
            addToZip(fileToAdd, zipFile, zipParameters);
        }

        zipFile.close();

        return zipFile;
    }

    public void addToZip(File file, ZipFile zipFile, ZipParameters zipParameters) throws ZipException {
        if (file.isDirectory()){
            zipFile.addFolder(file, zipParameters);
        }
        else {
            zipFile.addFile(file, zipParameters);
        }
    }

    /**
     * Decompresses a file (not implemented yet).
     * This method does not perform any action as decompression is pending.
     */
    @Override
    public List<File> decompress(List<ZipFile> zipFiles, String destinationPath, String password) throws IOException {
        List<File> files = new ArrayList<>();

        for (ZipFile zipFile : zipFiles) {
            String newDestionationPath = destinationPath + "/" + zipFile.getFile().getName();
            zipFile.extractAll(newDestionationPath);
            files.add(new File(newDestionationPath));
        }

        return files;
    }

}
