package com.lib.dal.access;

import com.lib.dal.helpers.Utils;

import java.io.BufferedWriter;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

public class CsvHelper {
    private String lineSeparator;
    private String itemSeparator;

    public CsvHelper() {
        this(",");
    }

    public CsvHelper(String itemSeparator) {
        this(itemSeparator, "\n");
    }

    public CsvHelper(String itemSeparator, String lineSeparator) {
        this.lineSeparator = lineSeparator;
        this.itemSeparator = itemSeparator;
    }

    public <T> List<T> read(String filePath, int startingLine, Function<String[], T> converter) {
        startingLine = Math.max(1, startingLine);
        int line = 1;
        List<T> items = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(Paths.get(filePath));
            scanner.useDelimiter(lineSeparator);

            while (scanner.hasNext()) {
                String item = scanner.next();

                if (line++ < startingLine)
                    continue;

                T obj = converter.apply(item.split(itemSeparator));
                items.add(obj);
            }

            return items;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public <T> boolean write(String filePath, String[] headers, List<T> items, boolean writeHeaders) {
        try (BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(filePath), StandardOpenOption.WRITE, StandardOpenOption.CREATE
        )) {
            if (writeHeaders) {
                writer.write(String.join(itemSeparator,headers));
                writer.write(lineSeparator);
            }

            List<Method> getters = Utils.getGetters(items.get(0).getClass(), headers);

            for(T item : items){
                for(int i = 0; i < getters.size(); i++){
                    writer.write(getters.get(i).invoke(item).toString());

                    if(i != getters.size() - 1)
                        writer.write(itemSeparator);
                }

                writer.write(lineSeparator);
            }

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
