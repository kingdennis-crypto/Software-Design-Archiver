package nl.vu.cs.softwaredesign.lib.CompressionFormats;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import nl.vu.cs.softwaredesign.lib.Annotations.CompressionType;
import nl.vu.cs.softwaredesign.lib.Interfaces.ICompressionFormat;
import nl.vu.cs.softwaredesign.lib.Models.FileArchive;

import java.io.*;

/**
 * Implementation of the {@link ICompressionFormat} interface for ZIP compression format.
 * This class provides methods to compress and decompress files using ZIP format.
 */
@CompressionType(label = "ZIP", description = "Zip description", extensions = {})
public class ZipCompression implements ICompressionFormat {
    /**
     * Compresses the files in the given source directory into a ZIP archive with the specified destination path.
     *
     * @param files           The FileArchive containing files to compress.
     * @param destinationPath The path of the resulting ZIP archive.
     * @param password        The password for encryption (can be null if encryption is not desired).
     * @return                The FileArchive representing the compressed ZIP archive.
     * @throws IOException    If an I/O error occurs during compression.
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
     * Decompresses the given ZIP archive into the specified destination path.
     *
     * @param compressedFiles The FileArchive representing the compressed ZIP archive.
     * @param destinationPath The destination path for the decompressed files.
     * @param password        The password for decryption (can be null if encryption is not used).
     * @return                The FileArchive representing the decompressed files.
     * @throws IOException    If an I/O error occurs during decompression.
     */
    @Override
    public FileArchive decompress(FileArchive compressedFiles, String destinationPath, String password) throws IOException {

        try (ZipFile zipFile = new ZipFile(destinationPath)) {
            zipFile.extractAll(compressedFiles.getROOT().getAbsolutePath());
        }

        return new FileArchive(new File(destinationPath));
    }

}
