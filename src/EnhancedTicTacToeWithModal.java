import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EnhancedTicTacToeWithModal extends JFrame implements ActionListener {
    private JButton[][] buttons = new JButton[3][3];
    private boolean playerXTurn = true;
    private JLabel statusLabel;
    private JPanel boardPanel, controlPanel, mainMenuPanel;

    public EnhancedTicTacToeWithModal() {
        setTitle("Tic Tac Toe Game");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new CardLayout());

        // Main Menu Panel
        mainMenuPanel = new JPanel();
        mainMenuPanel.setLayout(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome to Tic Tac Toe", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        JButton playButton = new JButton("Play Tic Tac Toe");
        playButton.setFont(new Font("Arial", Font.PLAIN, 18));
        playButton.addActionListener(e -> showGameBoard());
        mainMenuPanel.add(welcomeLabel, BorderLayout.CENTER);
        mainMenuPanel.add(playButton, BorderLayout.SOUTH);

        // Game Board Panel
        boardPanel = new JPanel(new GridLayout(3, 3));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 36));
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].addActionListener(this);
                boardPanel.add(buttons[i][j]);
            }
        }

        // Control Panel (Restart and Quit Buttons)
        controlPanel = new JPanel(new GridLayout(2, 1));
        statusLabel = new JLabel("Player X's Turn", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        JButton restartButton = new JButton("Restart Game");
        restartButton.setFont(new Font("Arial", Font.PLAIN, 16));
        restartButton.addActionListener(e -> restartGame());
        JButton quitButton = new JButton("Quit Game");
        quitButton.setFont(new Font("Arial", Font.PLAIN, 16));
        quitButton.addActionListener(e -> System.exit(0));

        controlPanel.add(restartButton);
        controlPanel.add(quitButton);

        // Combine Game Board and Controls
        JPanel gamePanel = new JPanel(new BorderLayout());
        gamePanel.add(boardPanel, BorderLayout.CENTER);
        gamePanel.add(statusLabel, BorderLayout.NORTH);
        gamePanel.add(controlPanel, BorderLayout.SOUTH);

        // Add Panels to the Main Frame
        add(mainMenuPanel, "MainMenu");
        add(gamePanel, "GameBoard");

        setResizable(false);
    }

    private void showGameBoard() {
        CardLayout cardLayout = (CardLayout) getContentPane().getLayout();
        cardLayout.show(getContentPane(), "GameBoard");
    }

    private void restartGame() {
        playerXTurn = true;
        statusLabel.setText("Player X's Turn");
        for (JButton[] row : buttons) {
            for (JButton button : row) {
                button.setText("");
                button.setEnabled(true);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource();
        if (!clickedButton.getText().equals("")) {
            return; // Ignore clicks on already marked buttons
        }

        if (playerXTurn) {
            clickedButton.setText("X");
            clickedButton.setForeground(Color.BLUE);
            statusLabel.setText("Player O's Turn");
        } else {
            clickedButton.setText("O");
            clickedButton.setForeground(Color.RED);
            statusLabel.setText("Player X's Turn");
        }

        playerXTurn = !playerXTurn;

        checkWinner();
    }

    private void checkWinner() {
        // Check rows, columns, and diagonals
        for (int i = 0; i < 3; i++) {
            if (checkLine(buttons[i][0], buttons[i][1], buttons[i][2]) ||
                checkLine(buttons[0][i], buttons[1][i], buttons[2][i])) {
                showModal(buttons[i][i].getText() + " Wins!");
                return;
            }
        }
        if (checkLine(buttons[0][0], buttons[1][1], buttons[2][2]) ||
            checkLine(buttons[0][2], buttons[1][1], buttons[2][0])) {
            showModal(buttons[1][1].getText() + " Wins!");
            return;
        }

        // Check for a tie
        boolean full = true;
        for (JButton[] row : buttons) {
            for (JButton button : row) {
                if (button.getText().equals("")) {
                    full = false;
                    break;
                }
            }
        }
        if (full) {
            showModal("It's a Tie!");
        }
    }

    private boolean checkLine(JButton b1, JButton b2, JButton b3) {
        return !b1.getText().equals("") && 
               b1.getText().equals(b2.getText()) && 
               b2.getText().equals(b3.getText());
    }

    private void showModal(String message) {
        JDialog dialog = new JDialog(this, "Game Over", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(this);

        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 18));
        dialog.add(messageLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        JButton restartButton = new JButton("Restart");
        JButton quitButton = new JButton("Quit");

        restartButton.addActionListener(e -> {
            dialog.dispose();
            restartGame();
        });
        quitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(restartButton);
        buttonPanel.add(quitButton);

        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EnhancedTicTacToeWithModal game = new EnhancedTicTacToeWithModal();
            game.setVisible(true);
        });
    }
}
