package gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import org.antlr.v4.runtime.*;
import antlr4.Hello;

public class LexerGUI extends JFrame {
	
	private static final long serialVersionUID = 1L;

    JTextArea inputArea = new JTextArea(15,50);
    JTextArea outputArea = new JTextArea(15,50);

    public LexerGUI() {
        setTitle("ANTLR Lexer IDE");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton openBtn = new JButton("Open File");
        JButton analyzeBtn = new JButton("Analyze");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(openBtn);
        buttonPanel.add(analyzeBtn);

        add(new JScrollPane(inputArea), BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(new JScrollPane(outputArea), BorderLayout.SOUTH);

        openBtn.addActionListener(e -> openFile());
        analyzeBtn.addActionListener(e -> analyze());

        pack();
    }

    void openFile() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                inputArea.read(new FileReader(chooser.getSelectedFile()), null);
            } catch(IOException ex) {
                JOptionPane.showMessageDialog(this, "Cannot open file: " + ex.getMessage());
            }
        }
    }

    void analyze() {
        try {
        	
            String code = inputArea.getText();
            CharStream input = CharStreams.fromString(code);

            // заміни PythonLexer на свій згенерований ANTLR lexer
            Hello lexer = new Hello(input);  
            lexer.removeErrorListeners(); // прибрати дефолтні повідомлення
            lexer.addErrorListener(new BaseErrorListener() {
                @Override
                public void syntaxError(Recognizer<?, ?> recognizer,
                                        Object offendingSymbol,
                                        int line, int charPositionInLine,
                                        String msg, RecognitionException e) {
                    outputArea.append(String.format("Error at line %d:%d -> %s\n", line, charPositionInLine, msg));
                }
            });

            CommonTokenStream tokens = new CommonTokenStream(lexer);
            tokens.fill();

            outputArea.setText(""); // очищення
            for(Token t : tokens.getTokens()) {

            	String name = lexer.getVocabulary().getDisplayName(t.getType());
                outputArea.append(String.format("%-15s -> %s\n", t.getText(), name));
            }

        } catch(Exception ex) {
            outputArea.setText("Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LexerGUI().setVisible(true));
    }
}
