import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Test {
    public static void main(String[] args) {
        //String a = Walk.getFNV(Paths.get("/home/sasha/java/IdeaProjects/Java_Sem3_HW1/modules/info.kgeorgiy.java.advanced.walk/info/kgeorgiy/java/advanced/walk/samples/1"));
        //System.out.println(a);
        Paths.get("/home/sasha/java/IdeaProjects/Java_Sem3_HW1/modules/info.kgeorgiy.java.advanced.walk1");
        try (Stream<Path> paths = Files.walk(Paths.get("/home/sasha/java/IdeaProjects/Java_Sem3_HW1/modules/info.kgeorgiy.java.advanced.walk1"))) {
            paths.filter(Files::isRegularFile).forEach(path -> {
                System.out.println(path.toString());
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
