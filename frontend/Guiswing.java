import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Guiswing extends JFrame {

    private final JTextField expressionField;
    private final JTextArea outputArea;
    private final JButton compileButton;

    // Path to the existing C compiler executable.
    // Run this Java program from the java_frontend folder.
    private final Path compilerPath = Path.of(
        "..",
        "member4_ast_semantic_project",
        "mini_compiler.exe"
    ).toAbsolutePath().normalize();

    public Guiswing() {
        setTitle("Arithmetic Expression Compiler");
        setSize(760, 580);
        setMinimumSize(new Dimension(600, 450));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Color background = new Color(20, 29, 52);
        Color panelColor = new Color(31, 43, 72);
        Color accent = new Color(65, 190, 175);
        Color textColor = new Color(240, 244, 255);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(background);
        mainPanel.setBorder(
            BorderFactory.createEmptyBorder(22, 22, 22, 22)
        );

        JLabel title = new JLabel(
            "ARITHMETIC EXPRESSION COMPILER"
        );
        title.setForeground(textColor);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));

        JLabel subtitle = new JLabel(
            "Enter an expression to analyze it using your C compiler."
        );
        subtitle.setForeground(new Color(185, 199, 225));

        JPanel header = new JPanel(new GridLayout(2, 1, 4, 4));
        header.setOpaque(false);
        header.add(title);
        header.add(subtitle);

        expressionField = new JTextField("2+3*4");
        expressionField.setFont(new Font("Monospaced", Font.PLAIN, 17));
        expressionField.setToolTipText("Example: 2+3*4");

        compileButton = new JButton("Compile Expression");
        compileButton.setBackground(accent);
        compileButton.setForeground(Color.BLACK);
        compileButton.setFocusPainted(false);
        compileButton.setFont(
            new Font("SansSerif", Font.BOLD, 14)
        );

        JButton clearButton = new JButton("Clear");

        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputPanel.setBackground(panelColor);
        inputPanel.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    new Color(60, 78, 112)
                ),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            )
        );

        JLabel inputLabel = new JLabel("Arithmetic Expression");
        inputLabel.setForeground(textColor);
        inputLabel.setFont(
            new Font("SansSerif", Font.BOLD, 14)
        );

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.setOpaque(false);
        buttons.add(clearButton);
        buttons.add(compileButton);

        JPanel inputContent = new JPanel(new BorderLayout(8, 8));
        inputContent.setOpaque(false);
        inputContent.add(inputLabel, BorderLayout.NORTH);
        inputContent.add(expressionField, BorderLayout.CENTER);
        inputContent.add(buttons, BorderLayout.SOUTH);

        inputPanel.add(inputContent, BorderLayout.CENTER);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        outputArea.setBackground(new Color(12, 18, 33));
        outputArea.setForeground(new Color(200, 240, 220));
        outputArea.setText(
            "Compiler output will appear here.\n"
        );

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(
                    new Color(60, 78, 112)
                ),
                "Compilation Output"
            )
        );

        JLabel footer = new JLabel(
            "Lexical Analysis  |  Parsing  |  AST  |  Semantic Checks"
        );
        footer.setForeground(new Color(185, 199, 225));

        JPanel centerPanel = new JPanel(new BorderLayout(12, 12));
        centerPanel.setOpaque(false);
        centerPanel.add(inputPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(footer, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        compileButton.addActionListener(e -> compileExpression());

        expressionField.addActionListener(
            e -> compileExpression()
        );

        clearButton.addActionListener(e -> {
            expressionField.setText("");
            outputArea.setText("");
            expressionField.requestFocusInWindow();
        });
    }

    private void compileExpression() {
        String expression = expressionField.getText().trim();

        if (expression.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Please enter an arithmetic expression.",
                "Missing Input",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!Files.isRegularFile(compilerPath)) {
            outputArea.setText(
                "ERROR: C compiler executable not found.\n\n"
                + "Expected location:\n"
                + compilerPath
                + "\n\nCheck the compilerPath setting in "
                + "CompilerGUI.java."
            );
            return;
        }

        compileButton.setEnabled(false);
        outputArea.setText("Compiling expression...\n");

        SwingWorker<String, Void> worker =
            new SwingWorker<>() {

            @Override
            protected String doInBackground() throws Exception {
                ProcessBuilder builder = new ProcessBuilder(
                    compilerPath.toString()
                );

                builder.redirectErrorStream(true);

                Process process = builder.start();

                // Send the expression to the C program.
                try (BufferedWriter writer =
                    new BufferedWriter(
                        new OutputStreamWriter(
                            process.getOutputStream(),
                            StandardCharsets.UTF_8
                        )
                    )) {
                    writer.write(expression);
                    writer.newLine();
                }

                // Read the compiler's output.
                String result;
                try (InputStream stream =
                    process.getInputStream()) {
                    result = new String(
                        stream.readAllBytes(),
                        StandardCharsets.UTF_8
                    );
                }

                int exitCode = process.waitFor();

                if (exitCode != 0) {
                    result += "\n\nCompiler process exit code: "
                        + exitCode;
                }

                return result.isBlank()
                    ? "The compiler returned no output."
                    : result;
            }

            @Override
            protected void done() {
                try {
                    outputArea.setText(get());
                    outputArea.setCaretPosition(0);
                } catch (Exception ex) {
                    outputArea.setText(
                        "Could not run the C compiler.\n\n"
                        + ex.getMessage()
                        + "\n\nCheck that the executable exists "
                        + "and can accept input through stdin."
                    );
                } finally {
                    compileButton.setEnabled(true);
                }
            }
        };

        worker.execute();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception ignored) {
                // Use the default Swing appearance.
            }

            new Guiswing().setVisible(true);
        });
    }
}