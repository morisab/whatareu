package com.whatareu.app;

import com.whatareu.gameinterface.Application;
import com.whatareu.gameinterface.ApplicationContext;
import com.whatareu.gameinterface.Event;
import com.whatareu.mechanics.Trajectory;
import com.whatareu.mechanics.handler.LevelHandler;
import com.whatareu.tools.Music;
import com.whatareu.tools.Status;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Objects;

final class GameMaster {
    private final List<LevelHandler> levels;
    private Runnable onGameLose;
    private Runnable onGameWin;

    GameMaster(List<LevelHandler> levels, Runnable onGameLose, Runnable onGameWin) {
        Objects.requireNonNull(levels);
        this.levels = List.copyOf(levels);
        this.onGameLose = onGameLose;
        this.onGameWin = onGameWin;
    }

    void run() {
        Music.playBackgroundMusic("resources/music/bgm.wav", 2000);
        levels.forEach(level -> Application.run(Color.BLACK, context -> {
            var screenInfo = context.getScreenInfo();
            var width = (int) screenInfo.getWidth();
            var height = (int) screenInfo.getHeight();
            level.update();
            do {
                var event = context.pollOrWaitEvent(300);
                if (event != null && event.getKey() != null && event.getAction() == Event.Action.KEY_PRESSED) {
                    switch (event.getKey()) {
                        case UP -> level.moveYou(Trajectory.UP);
                        case DOWN -> level.moveYou(Trajectory.DOWN);
                        case LEFT -> level.moveYou(Trajectory.LEFT);
                        case RIGHT -> level.moveYou(Trajectory.RIGHT);
                        case P -> {
                            Runtime.getRuntime().exit(0);
                        }
                    }
                    level.update();
                }
                render(context, level, width, height);
            } while (level.checkGameStatus() == Status.ONGOING);
            if (level.checkGameStatus() == Status.WIN) {
                onGameWin.run();
            } else {
                onGameLose.run();
            }
        }));
    }

    private void render(ApplicationContext context, LevelHandler level, int width, int height) {
        context.renderFrame(graphics -> {
            assert level != null;
            graphics.setColor(Color.BLACK);
            graphics.fill(new Rectangle2D.Float(0, 0, width, height));
            level.displayGame(graphics, 0, 0, width, height);
        });
    }
}
