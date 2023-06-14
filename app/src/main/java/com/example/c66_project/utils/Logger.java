package com.example.c66_project.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;

public class Logger extends ContextWrapper {
    private final static String FILE_NAME = "logs.txt";
    private final static String DIR = "/files/";

    public Logger(Context base) {
        super(base);
    }

    public void log(String data) {
        File file = new File(getDataDir() + DIR + FILE_NAME);
        try {
            if (!file.exists()) {
                Files.createDirectories(Paths.get(getDataDir() + DIR));
                Files.createFile(Paths.get(file.getAbsolutePath()));
            }
            try (FileOutputStream stream = new FileOutputStream(file, true)) {
                stream.write(data.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
