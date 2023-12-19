package com.whatareu.tools;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.whatareu.mechanics.gameboard.Board;
import com.whatareu.mechanics.gamelogic.Directive;
import com.whatareu.mechanics.handler.LevelHandler;

public final class LevelMaker {
    private final static String TEXT = "TEXT";

    public static LevelHandler buildLevelFromFile(String filename, List<String[]> additionalRules) throws IOException {
        Objects.requireNonNull(filename);
        var factory = new AssetFactory();
        var path = Path.of("resources/levels", filename);
        try (var buffer = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            final var lines = buffer.lines().collect(Collectors.toList());
            var size = lines.get(0).split(" ");
            var width = Integer.parseInt(size[0]);
            var height = Integer.parseInt(size[1]);
            var boardElements = parseBoardElements(lines, factory);
            var defaultRules = new ArrayList<Directive>();
            defaultRules.add(factory.provideRule(TEXT, "IS", "PUSH"));
            additionalRules.forEach(rule -> defaultRules.add(factory.provideRule(rule[0], rule[1], rule[2])));
            return new LevelHandler(width, height, boardElements, defaultRules);
        }
    }

    private static List<Board> parseBoardElements(List<String> lines, AssetFactory factory) {
        Objects.requireNonNull(lines);
        Objects.requireNonNull(factory);
        var boardElements = new ArrayList<Board>();
        for (var i = 0; i < lines.size(); i++) {
            var line = lines.get(i);
            if (!line.isEmpty()) {
                switch (line.charAt(0)) {
                    case 'n' -> boardElements.addAll(parseNounsEntities(lines, i, factory));
                    case 'o', 'p' -> boardElements.addAll(parseOpProps(lines, i, factory));
                }
            }
        }
        return boardElements;
    }

    private static List<Board> parseNounsEntities(List<String> lines, int index, AssetFactory factory) {
        Objects.requireNonNull(lines);
        Objects.requireNonNull(factory);
        var type = lines.get(index).split(" ")[1];
        factory.addType(type);
        return parseTypeElement(lines, index, factory, type);
    }

    private static List<Board> parseTypeElement(List<String> lines, int index, AssetFactory factory, String type) {
        var boardElements = new ArrayList<Board>();
        for (var i = index + 1; i < lines.size() && !lines.get(i).isEmpty(); i++) {
            var split = lines.get(i).split(" ");
            var x = Integer.parseInt(split[2]);
            var y = Integer.parseInt(split[3]);
            if (split[1].equals("e")) {
                boardElements.add(factory.provideElement(x, y, "NULL_RULE_PART", type));
            } else if (split[1].equals("t")) {
                boardElements.add(factory.provideElement(x, y, type, "TEXT"));
            }
        }
        return boardElements;
    }

    private static List<Board> parseOpProps(List<String> lines, int index, AssetFactory factory) {
        Objects.requireNonNull(lines);
        var boardElements = new ArrayList<Board>();
        var rulePart = lines.get(index).split(" ")[1];
        for (var i = index + 1; i < lines.size() && !lines.get(i).isEmpty(); i++) {
            var split = lines.get(i).split(" ");
            var x = Integer.parseInt(split[1]);
            var y = Integer.parseInt(split[2]);
            boardElements.add(factory.provideElement(x, y, rulePart, "TEXT"));
        }
        return boardElements;
    }
}
