package nl.vu.cs.softwaredesign.lib.compressionformats;

import java.util.stream.IntStream;

public abstract class Compression {
    public Compression() {}

    protected String removeLastPathSection(String path) {
        int lastIndex = IntStream.range(0, path.length())
                .filter(i -> path.charAt(i) == '/')
                .reduce((a, b) -> b)
                .orElse(-1);

        if (lastIndex != -1)
            return path.substring(0, lastIndex + 1);

        return path;
    }
}
