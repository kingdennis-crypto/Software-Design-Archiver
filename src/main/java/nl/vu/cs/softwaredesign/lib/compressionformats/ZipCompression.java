package nl.vu.cs.softwaredesign.lib.compressionformats;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import nl.vu.cs.softwaredesign.lib.annotations.CompressionType;
import nl.vu.cs.softwaredesign.lib.interfaces.ICompressionFormat;
import nl.vu.cs.softwaredesign.lib.models.FileArchive;

import java.io.*;

/**
 * Implementation of the {@link ICompressionFormat} interface for ZIP compression format.
 * This class provides methods to compress and decompress files using ZIP format.
 */
@CompressionType(label = "ZIP", description = "Zip description", extensions = { ".zip" })
public class ZipCompression implements ICompressionFormat {
    /**
     * Compresses the files in the given source directory into a ZIP archive with the specified destination path.
     *
     * @param files           The FileArchive containing files to compress.
     * @param destinationPath The path of the resulting ZIP archive.
     * @return                The FileArchive representing the compressed ZIP archive.
     * @throws IOException    If an I/O error occurs during compression.
     */
    @Override
    public FileArchive compress(FileArchive files, String destinationPath) throws IOException {
        ZipParameters zipParameters = new ZipParameters();
        try (ZipFile zipFile = new ZipFile(destinationPath)) {
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
     * @return                The FileArchive representing the decompressed files.
     * @throws IOException    If an I/O error occurs during decompression.
     */
    @Override
    public FileArchive decompress(FileArchive compressedFiles, String destinationPath) throws IOException {

        try (ZipFile zipFile = new ZipFile(compressedFiles.getROOT().getAbsoluteFile())) {
            zipFile.extractAll(destinationPath);
        }

        return new FileArchive(new File(destinationPath));
    }

}
