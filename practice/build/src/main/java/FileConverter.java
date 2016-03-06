import java.io.*;

public class FileConverter {
    private String source;
    private String dest;

    private static final int BUFFER_SIZE = 1024;
    public FileConverter(String src, String dst){
        source = src;
        dest = dst;
    }

    public void copy() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(source));
        BufferedWriter writer = new BufferedWriter(new FileWriter(dest));

        int count;
        char[] buffer = new char[BUFFER_SIZE];
        while ((count = reader.read(buffer)) != -1) {
            writer.write(buffer, 0, count);
        }

        reader.close();
        writer.close();
    }
}
