
package com.whatareu.app;

import javax.swing.*;

import com.whatareu.mechanics.handler.LevelHandler;
import com.whatareu.tools.LevelMaker;
import com.whatareu.tools.Music;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameMenu {
    private JFrame frame;
    private static int currentLevel = 1;
    private static final int totalLevels = 8;

    public GameMenu() {
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        frame = new JFrame("Game Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.getContentPane().setBackground(Color.BLACK);

        showMainMenu();
    }

    private void showMainMenu() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.BLACK);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalGlue());
        for (int i = 1; i <= totalLevels; i++) {
            int level = i;
            JButton levelButton = createMenuButton("Level " + level);
            levelButton.addActionListener(e -> runGame("world1/level" + level + ".txt", level));
            panel.add(levelButton);
            panel.add(Box.createRigidArea(new Dimension(0, 10))); // Jarak antar tombol
        }

        JButton exitButton = createMenuButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));
        panel.add(exitButton);
        panel.add(Box.createVerticalGlue());

        frame.setContentPane(panel);
        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 50));
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        return button;
    }

    private void runGame(String levelFile, int level) {
        SwingWorker<Void, Void> gameWorker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                List<String[]> dummyList = new ArrayList<>();
                LevelHandler levelHandler = LevelMaker.buildLevelFromFile(levelFile, dummyList);
                List<LevelHandler> levels = new ArrayList<>();
                levels.add(levelHandler);
                Runnable onGameWin = () -> SwingUtilities.invokeLater(() -> showLevelCompletionScreen(level));
                Runnable onGameLose = () -> SwingUtilities.invokeLater(() -> showGameOverScreen(level));
                GameMaster gameMaster = new GameMaster(levels, onGameLose, onGameWin);
                gameMaster.run();
                return null;
            }

            @Override
            protected void done() {
            }
        };
        gameWorker.execute();
    }

    private void showLevelCompletionScreen(int level) {
        Music.stopBackgroundMusic(2000);
        Music.playBackgroundMusic("resources/music/win.wav", 0);
        frame.getContentPane().removeAll();
        JPanel screenCompletionPanel = new JPanel();
        screenCompletionPanel.setLayout(new BoxLayout(screenCompletionPanel, BoxLayout.Y_AXIS));
        screenCompletionPanel.setBackground(Color.BLACK);
        screenCompletionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        screenCompletionPanel.add(Box.createVerticalGlue());

        JLabel completionLabel = new JLabel("Level " + level + " Completed! Curse has been lifted!");
        completionLabel.setForeground(Color.WHITE);
        completionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        screenCompletionPanel.add(completionLabel);

        JButton nextLevelButton = createMenuButton("Next Level");
        nextLevelButton.addActionListener(e -> {
            if (level < totalLevels) {
                runGame("world1/level" + (level + 1) + ".txt", level + 1);
            } else {
                JOptionPane.showMessageDialog(frame, "All Level Clear! You Did Great!\nThank You For Playing!");
                showMainMenu();
            }
        });

        JButton mainMenuButton = createMenuButton("Main Menu");
        mainMenuButton.addActionListener(e -> showMainMenu());

        JButton exitButton = createMenuButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));

        screenCompletionPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        screenCompletionPanel.add(nextLevelButton);
        screenCompletionPanel.add(mainMenuButton);
        screenCompletionPanel.add(exitButton);
        screenCompletionPanel.add(Box.createVerticalGlue());
        screenCompletionPanel.add(Box.createVerticalGlue());
        

        frame.setContentPane(screenCompletionPanel);
        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    private void showGameOverScreen(int level) {
        Music.stopBackgroundMusic(2000);
        Music.playBackgroundMusic("resources/music/lose.wav", 0);
        frame.getContentPane().removeAll();
        JPanel gameOverPanel = new JPanel();
        gameOverPanel.setLayout(new BoxLayout(gameOverPanel, BoxLayout.Y_AXIS));
        gameOverPanel.setBackground(Color.BLACK);
        gameOverPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameOverPanel.add(Box.createVerticalGlue());

        JLabel gameOverLabel = new JLabel("Game Over");
        gameOverLabel.setForeground(Color.WHITE);
        gameOverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameOverPanel.add(gameOverLabel);

        JButton retryButton = createMenuButton("Retry");
        retryButton.addActionListener(e -> runGame("world1/level" + level + ".txt", level));
        gameOverPanel.add(retryButton);

        JButton mainMenuButton = createMenuButton("Main Menu");
        mainMenuButton.addActionListener(e -> showMainMenu());
        gameOverPanel.add(mainMenuButton);

        gameOverPanel.add(Box.createVerticalGlue());

        frame.setContentPane(gameOverPanel);
        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

}