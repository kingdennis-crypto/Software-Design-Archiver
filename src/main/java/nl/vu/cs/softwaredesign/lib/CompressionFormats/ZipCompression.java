package nl.vu.cs.softwaredesign.lib.CompressionFormats;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import nl.vu.cs.softwaredesign.lib.Annotations.CompressionType;
import nl.vu.cs.softwaredesign.lib.Interfaces.ICompressionFormat;
import nl.vu.cs.softwaredesign.lib.Models.FileArchive;

import java.io.*;

@CompressionType(label="ZIP", description="Zip description")
public class ZipCompression implements ICompressionFormat {
    /**
     * Compresses the files in the given source directory into a ZIP archive with the specified destination path.
     *
     * @param destinationPath The path of the resulting ZIP archive.
     * @throws IOException If an I/O error occurs during compression.
     */
    @Override
    public FileArchive compress(FileArchive files, String destinationPath, String password) throws IOException {
        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setEncryptFiles(true);
        zipParameters.setEncryptionMethod(EncryptionMethod.AES);

        try (ZipFile zipFile = new ZipFile(destinationPath, password.toCharArray())) {

            if (files.getROOT().isDirectory()) {
                zipFile.addFolder(files.getROOT(), zipParameters);
            } else {
                zipFile.addFile(files.getROOT(), zipParameters);
            }
        }

        return new FileArchive(new File(destinationPath));
    }

    /**
     * Decompresses a file (not implemented yet).
     * This method does not perform any action as decompression is pending.
     */
    @Override
    public FileArchive decompress(FileArchive zipFiles, String destinationPath, String password) throws IOException {

        try (ZipFile zipFile = new ZipFile(destinationPath)) {
            zipFile.extractAll(zipFiles.getROOT().getAbsolutePath());
        }

        return new FileArchive(new File(destinationPath));
    }

}
