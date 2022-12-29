package ru.vsu.cs.semenov_d_s.view;

import ru.vsu.cs.semenov_d_s.game.controller.Game;
import ru.vsu.cs.semenov_d_s.game.model.Cell;
import ru.vsu.cs.semenov_d_s.game.model.pieces.Move;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GUIView implements View {

    private static final Dimension SIZE = new Dimension(840, 770);

    private JFrame frame;
    private BoardPanel boardPanel;
    private DeadPanel deadPanel;
    private MoveLogPanel moveLogPanel;
    private DetailsPanel detailsPanel;

    private Game controller;
    private Cell startCell;
    private Cell selectedCell;
    private boolean highlightLegalMoves;
    private List<Move> moveLog;

    public GUIView(Game controller) {
        this.controller = controller;
        highlightLegalMoves = false;
        startCell = null;
        selectedCell = null;
        frame = new JFrame("Chess Game");
        frame.setPreferredSize(SIZE);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setJMenuBar(createMenuBar());
        frame.setResizable(false);
        boardPanel = new BoardPanel(this);
        deadPanel = new DeadPanel(this);
        moveLog = new ArrayList<>();
        moveLogPanel = new MoveLogPanel(moveLog);
        detailsPanel = new DetailsPanel(this);
        frame.add(boardPanel, BorderLayout.CENTER);
        frame.add(deadPanel, BorderLayout.WEST);
        frame.add(moveLogPanel, BorderLayout.EAST);
        frame.add(detailsPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        new Game().start();
    }

    public void start() {
        controller.reset();
        show();
    }

    public void redraw() {
        deadPanel.drawAllDeadPieces(this);
        moveLogPanel.showMoveLog(moveLog);
        boardPanel.drawBoard(this);
        detailsPanel.update(this);
        if (controller.gameOver()) {
            detailsPanel.showWinner(controller.getCurrentPlayer());
        }
    }

    public List<Move> getMoveLog() {
        return moveLog;
    }

    public void show() {
        frame.setVisible(true);
    }

    public void restart() {
        controller.reset();
        moveLog.clear();
        startCell = null;
        redraw();
    }

    public JMenu createGameMenu() {
        JMenu menu = new JMenu("Game");
        JMenuItem restartItem = new JMenuItem("Restart");
        restartItem.addActionListener(e -> restart());
        menu.add(restartItem);
        return menu;
    }

    public JMenuBar createMenuBar() {
        JMenuBar bar = new JMenuBar();
        bar.add(createFileMenu());
        bar.add(createGameMenu());
        bar.add(createPreferencesMenu());
        return bar;
    }

    public JMenu createFileMenu() {
        JMenu menu = new JMenu("File");
        JMenuItem loadItem = new JMenuItem("Load");
        loadItem.addActionListener(e -> System.out.println("Opened file"));
        menu.add(loadItem);
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        menu.add(exitItem);
        return menu;
    }

    public JMenu createPreferencesMenu() {
        JMenu menu = new JMenu("Preferences");
        JMenuItem flipItem = new JMenuItem("Flip Board");
        flipItem.addActionListener(e -> {
            boardPanel.flipBoard();
            boardPanel.drawBoard(this);
        });
        menu.add(flipItem);
        JCheckBoxMenuItem highLightCheck = new JCheckBoxMenuItem("Highlight Legal Moves", highlightLegalMoves);
        highLightCheck.addActionListener(e -> {
            highlightLegalMoves = highLightCheck.isSelected();
            boardPanel.drawBoard(this);
        });
        menu.add(highLightCheck);
        return menu;
    }

    public Game getController() {
        return controller;
    }

    public BoardPanel getBoardPanel() {
        return boardPanel;
    }

    public Cell getStartCell() {
        return startCell;
    }

    public Cell getSelectedCell() {
        return selectedCell;
    }

    public void setSelectedCell(Cell selectedCell) {
        this.selectedCell = selectedCell;
    }

    public void setStartCell(Cell startCell) {
        this.startCell = startCell;
    }

    public boolean shouldHighlightLegalMoves() {
        return highlightLegalMoves;
    }

}
