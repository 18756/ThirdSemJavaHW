import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;

public class RecursiveWalk {
    private static final String ERROR_HASH = "00000000";
    public static void main(String[] args) throws WalkException {
        if (args.length < 2) {
            throw new WalkException("You should type input and output files");
        }
        Path inputFilePath = Paths.get(args[0]);
        Path outputFilePath = Paths.get(args[1]);
        try (
            BufferedReader reader = Files.newBufferedReader(inputFilePath, StandardCharsets.UTF_8);
            Writer writer = Files.newBufferedWriter(outputFilePath, StandardCharsets.UTF_8, StandardOpenOption.WRITE, StandardOpenOption.CREATE)
        ) {
            while (reader.ready()) {
                Path curFilePath = Paths.get(reader.readLine());
                recursiveWalk(curFilePath, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void recursiveWalk(Path fileOrDirPath, Writer writer) throws IOException {
        try (Stream<Path> paths = Files.walk(fileOrDirPath)) {
            paths.filter(Files::isRegularFile).forEach(path -> {
                try {
                    writer.write(getFNV(path) + " " + path.toString() + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            writer.write(ERROR_HASH + " " + fileOrDirPath.toString() + "\n");
        }
    }

    public static String getFNV(Path filePath) {
        final int x0 = 0x811c9dc5; // start value
        final int p = 0x01000193;    // prime number
        final int bufferSize = 100;
        byte[] buffer;
        int x = x0;
        try (InputStream inputStream = Files.newInputStream(filePath, StandardOpenOption.READ)) {
            while (inputStream.available() > 0) {
                buffer = inputStream.readNBytes(bufferSize);
                for (byte b : buffer) {
                    x = (x * p) ^ (b & 0xff);
                }
            }
        } catch (IOException e) {
            return ERROR_HASH;
        }
        return String.format("%08x", x);
    }
}
