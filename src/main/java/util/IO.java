package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * @author Bernhard Halbartschlager
 */
public final class IO {

    public static String interruptableReadln(InputStream inputStream, Scanner scanner) throws InterruptedException, IOException {
        while (!Thread.currentThread().isInterrupted()) {
            if (inputStream.available() > 0) {
                // can read something
                try {
                    // read line
                    return scanner.nextLine();
                } catch (NoSuchElementException e) {
                    // not a full line jet
                    Thread.sleep(200);
                }
            } else {
                Thread.sleep(200);
            }
        }
        throw new InterruptedException();
    }

    public static Scanner toScanner(InputStream inputStream) {
        return new Scanner(new BufferedReader(new InputStreamReader(inputStream)));
    }

}
