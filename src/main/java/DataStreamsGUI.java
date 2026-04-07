import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * DataStreamsGUI is a Swing-based GUI application for
 * loading text files and searching for specific strings
 * using Java Streams. It displays the original file on the left
 * and filtered search results on the right.
 * <p>
 * Features include:
 * <ul>
 *     <li>Loading a text file via JFileChooser</li>
 *     <li>Searching for a string using Java Streams and lambda expressions</li>
 *     <li>Displaying original and filtered content in separate text areas</li>
 *     <li>Simple Quit button to exit the application</li>
 * </ul>
 *
 * @Van Diep
 * @version Spring 2022
 */
public class DataStreamsGUI extends JFrame {

    /** Text area to display the contents of the loaded file. */
    private JTextArea originalTextArea;

    /** Text area to display the filtered search results. */
    private JTextArea filteredTextArea;

    /** Text field where the user enters the search string. */
    private JTextField searchField;

    /** Button to load a text file. */
    private JButton loadButton;

    /** Button to perform search on the loaded file. */
    private JButton searchButton;

    /** Button to quit the application. */
    private JButton quitButton;

    /** Stores the file path of the loaded file. */
    private String filePath;

    /**
     * Constructs the DataStreamsGUI frame with all components:
     * text areas, buttons, and layout.
     */

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

    /**
     * Handles loading a text file using JFileChooser.
     * Reads the file line by line using Java Streams and displays
     * it in the original text area.
     *
     * @param e the ActionEvent triggered by clicking the Load File button
     */

    private void loadFile(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            filePath = chooser.getSelectedFile().getAbsolutePath();
            originalTextArea.setText("");
            filteredTextArea.setText("");

            // DO NOT THROW THE IOEXCEPTION ON MAIN be sure to handle the errors with the usual
            // try catch mechanism..

            try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
                lines.forEach(line -> originalTextArea.append(line + "\n"));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error reading file: " + ex.getMessage());
            }
        }
    }

    /**
     * Handles searching the loaded file for the string entered
     * in the search field. Displays matching lines in the filtered
     * text area using Java Streams and a lambda expression.
     *
     * @param e the ActionEvent triggered by clicking the Search button
     */

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


            /*
            Thought of adding this, but there was no requirement for this in the assignment.
             */

            //lines.filter(line -> line.toLowerCase().matches(".*\\b" +
            //        searchText.toLowerCase() + "\\b.*"))


        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + ex.getMessage());
        }
    }

    /**
     * Launches the DataStreamsGUI application.
     *
     */

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DataStreamsGUI::new);
    }
}