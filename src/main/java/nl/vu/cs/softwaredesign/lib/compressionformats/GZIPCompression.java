package nl.vu.cs.softwaredesign.lib.compressionformats;

import nl.vu.cs.softwaredesign.lib.annotations.CompressionType;
import nl.vu.cs.softwaredesign.lib.interfaces.ICompressionFormat;
import nl.vu.cs.softwaredesign.lib.models.FileArchive;
import org.rauschig.jarchivelib.ArchiveFormat;
import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;
import java.io.File;
import java.io.IOException;
import java.util.stream.IntStream;

/**
 * A class implementing the GZIP compression format.
 * This class provides methods to compress and decompress files using GZIP compression algorithm.
 */
@CompressionType(label = "GZIP", description = "GZIP description", extension = ".tar.gz")
public class GZIPCompression extends Compression implements ICompressionFormat {
    /**
     * Compresses the given file archive using GZIP compression algorithm.
     *
     * @param fileArchive     The file archive to compress.
     * @param destinationPath The destination path for the compressed file.
     * @return                A FileArchive object representing the compressed file.
     * @throws IOException    If an I/O error occurs.
     */
    @Override
    public FileArchive compress(FileArchive fileArchive, String destinationPath) throws IOException {
        Archiver archiver = ArchiverFactory.createArchiver(ArchiveFormat.TAR, org.rauschig.jarchivelib.CompressionType.GZIP);
        File file = archiver.create(fileArchive.getROOT().getName(), new File(super.removeLastPathSection(destinationPath)), fileArchive.getROOT());

        return new FileArchive(file);
    }

    /**
     * Decompresses the given compressed file archive using GZIP decompression algorithm.
     *
     * @param compressedFiles The compressed file archive to decompress.
     * @param destinationPath The destination path for the decompressed files.
     * @return                A FileArchive object representing the decompressed files.
     * @throws IOException    If an I/O error occurs.
     */
    @Override
    public FileArchive decompress(FileArchive compressedFiles, String destinationPath) throws IOException {
        Archiver archiver = ArchiverFactory.createArchiver(ArchiveFormat.TAR, org.rauschig.jarchivelib.CompressionType.GZIP);
        archiver.extract(compressedFiles.getROOT(), new File(destinationPath));

        return new FileArchive(new File(destinationPath));
    }
}
