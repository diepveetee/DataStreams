import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class DataStreamsGUI extends JFrame {

    private JTextArea originalTextArea;
    private JTextArea filteredTextArea;
    private JTextField searchField;
    private JButton loadButton, searchButton, quitButton;
    private String filePath; // stores the loaded file path

    public DataStreamsGUI() {
        super("Java Data Streams Lab 09");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLayout(new BorderLayout());

        // Top panel for search field and buttons
        JPanel topPanel = new JPanel();
        searchField = new JTextField(20);
        loadButton = new JButton("Load File");
        searchButton = new JButton("Search");
        quitButton = new JButton("Quit");
        topPanel.add(loadButton);
        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(quitButton);
        add(topPanel, BorderLayout.NORTH);

        // Center panel for text areas
        originalTextArea = new JTextArea();
        filteredTextArea = new JTextArea();
        originalTextArea.setEditable(false);
        filteredTextArea.setEditable(false);
        JScrollPane scrollOriginal = new JScrollPane(originalTextArea);
        JScrollPane scrollFiltered = new JScrollPane(filteredTextArea);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollOriginal, scrollFiltered);
        splitPane.setDividerLocation(400);
        add(splitPane, BorderLayout.CENTER);

        // Button actions
        loadButton.addActionListener(this::loadFile);
        searchButton.addActionListener(this::searchFile);
        quitButton.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    private void loadFile(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            filePath = chooser.getSelectedFile().getAbsolutePath();
            originalTextArea.setText("");
            filteredTextArea.setText("");
            try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
                lines.forEach(line -> originalTextArea.append(line + "\n"));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error reading file: " + ex.getMessage());
            }
        }
    }

    private void searchFile(ActionEvent e) {
        if (filePath == null || searchField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Load a file and enter a search term first!");
            return;
        }

        filteredTextArea.setText(""); // clear previous results
        String searchText = searchField.getText();
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            lines.filter(line -> line.contains(searchText))
                    .forEach(line -> filteredTextArea.append(line + "\n"));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DataStreamsGUI::new);
    }
}