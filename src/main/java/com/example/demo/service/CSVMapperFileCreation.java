package com.example.demo.service;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CSVMapperFileCreation {

    @SneakyThrows
    static void generate() {
        var file = ResourceUtils.getFile("classpath:input.txt");
        List<String> list = null;
        try (BufferedReader br = Files.newBufferedReader(Paths.get(file.getPath()))) {
            list = Arrays.stream(br.readLine().split(",")).map(String::trim).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.sort(list);
        convert(Objects.requireNonNull(list));
        Set<String> duplicates = findDuplicateBySetAdd(list);
        System.out.println("------------------------------------------------------");
        System.out.println("count:" + list.size());
        System.out.println("duplicates:" + duplicates);
        System.out.println("duplicates size:" + duplicates.size());
    }

    public static void main(String[] args) {
        generate();
    }

    public static String
    camelToSnake(String str) {
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";
        str = str.replaceAll(regex, replacement).toLowerCase();
        return str.replace("__c", "");
    }

    @SneakyThrows
    public static void convert(List<String> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : list) {
            String snake = camelToSnake(str);
            System.out.println(str + "," + snake);
            stringBuilder.append(str).append(",").append(snake).append("\n");

        }
        writeFile(stringBuilder.toString());
    }

    private static void writeFile(String value) throws IOException {
        Files.write(Paths.get("./ComJunction.csv"), value.getBytes());
    }

    private static <T> Set<T> findDuplicateBySetAdd(List<T> list) {

        Set<T> items = new HashSet<>();
        return list.stream()
                .filter(n -> !items.add(n)) // Set.add() returns false if the element was already in the set.
                .collect(Collectors.toSet());

    }
}
