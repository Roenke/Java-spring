import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        if(args.length != 2) {
            showUsage();
            System.exit(1);
        }

        FileConverter converter = new FileConverter(args[0], args[1]);
        try {
            converter.copy();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        System.exit(0);
    }

    private static void showUsage() {
        System.out.println("Usage: Main <source> <dest>");
    }
}
