package nl.vu.cs.softwaredesign.lib.CompressionFormats;

import nl.vu.cs.softwaredesign.lib.Interfaces.ICompressionFormat;
import nl.vu.cs.softwaredesign.lib.Models.FileArchive;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZIPCompression implements ICompressionFormat {

    @Override
    public FileArchive compress(FileArchive fileArchive, String destinationPath, String password) throws IOException {
        byte[] buffer = new byte[1024];

        try (GZIPOutputStream outputStream = new GZIPOutputStream(new FileOutputStream(destinationPath))) {
            try (FileInputStream inputStream = new FileInputStream(fileArchive.getROOT())) {
                int totalSize;
                while ((totalSize = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, totalSize);
                }
            }

            outputStream.finish();
        }

        return new FileArchive(new File(destinationPath));
    }

    @Override
    public FileArchive decompress(FileArchive compressedFiles, String destinationPath, String password) throws IOException {
        byte[] buffer = new byte[1024];

        try (GZIPInputStream is = new GZIPInputStream(new FileInputStream(compressedFiles.getROOT()))) {
            try (FileOutputStream out = new FileOutputStream((destinationPath))) {
                int totalSize;
                while ((totalSize = is.read(buffer)) > 0) {
                    out.write(buffer, 0, totalSize);
                }
            }
        }

        return new FileArchive(new File(destinationPath));
    }

}
