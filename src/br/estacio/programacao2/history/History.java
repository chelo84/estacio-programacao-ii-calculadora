package br.estacio.programacao2.history;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import static java.util.Objects.isNull;

public class History {
    private static String FILE_NAME = "Operation History";
    private static FileWriter fileWriter;
    private static StringBuilder log = new StringBuilder();

    private static void initializeFileWriter() {
        if(isNull(fileWriter)) {
            try {
                fileWriter = new FileWriter(FILE_NAME, true);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void logOperation(String operation) {
        log.append(String.format("> %s\n", operation));
    }
    
    public static void log() {
        initializeFileWriter();
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print("--------------------------OPERATION--------------\n");
        printWriter.print(log);
        printWriter.close();
    }
}
