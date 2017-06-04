package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.*;

public class OutputWindow
{
    private JTextArea tarea;
    private JComboBox cbox;
    private JTextField lineField;
    private String[] colourNames = {"RED", "ORANGE", "CYAN"};

    private Highlighter.HighlightPainter redPainter;
    private Highlighter.HighlightPainter orangePainter;
    private Highlighter.HighlightPainter cyanPainter;   
    private Highlighter.HighlightPainter greenPainter;

    private int firstUpdateIndex;
    private int counter;

    private Map<Integer, Highlighter.Highlight> highlights = new HashMap<Integer, Highlighter.Highlight>();
    
    private ArrayList<Highlighter.HighlightPainter> painters;
    private static int painterCounter = 0;

    public OutputWindow()
    {
        redPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.RED);
        orangePainter = new DefaultHighlighter.DefaultHighlightPainter(Color.ORANGE);
        cyanPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.CYAN);
        greenPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.GREEN);
        
        painters = new ArrayList<>();
        painters.add(redPainter);
        painters.add(orangePainter);
        painters.add(cyanPainter);
        painters.add(greenPainter);

        firstUpdateIndex = -1;
        counter = 0;
    }

    public void createAndDisplayGUI(File file)
    {
        final JFrame frame = new JFrame("Pattern matches");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(700, 1000));

        JPanel contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5), "Patterns"));

        tarea = new JTextArea();
        
        try {
        	FileReader reader = new FileReader(file.getAbsoluteFile());
            BufferedReader bufferedReader = new BufferedReader(reader);
            tarea.read(bufferedReader, null);
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
			
			System.out.println("Could not load file to output results.");
			
			return;
		}
        
        // customize JTextArea
        tarea.setEditable(false);
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        tarea.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        tarea.setTabSize(2);
        tarea.setPreferredSize(new Dimension(600, 900));
       
        JScrollPane scrollPane = new JScrollPane(tarea);
        contentPane.add(scrollPane);
        
        JButton remHighButton = new JButton("REMOVE HIGHLIGHT");
        remHighButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                String input = JOptionPane.showInputDialog(frame, "Please Enter Start Index : "
                                                        , "Highlighting Options : "
                                                        , JOptionPane.PLAIN_MESSAGE);

                if (input != null && (highlights.size() > 0))
                {               
                    int startIndex = Integer.parseInt(input.trim());
                    Highlighter highlighter = tarea.getHighlighter();
                    highlighter.removeHighlight(highlights.get(startIndex));
                    tarea.setCaretPosition(startIndex);
                    tarea.requestFocusInWindow();
                    highlights.remove(startIndex);
                }
            }
        });

        JButton button = new JButton("HIGHLIGHT TEXT");
        button.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                String text = null;
                text = tarea.getSelectedText();
                if (text != null && text.length() > 0)
                {
                    int startIndex = tarea.getText().indexOf(text);
                    int endIndex = startIndex + text.length();
                    Highlighter highlighter = tarea.getHighlighter();

                    int selection = JOptionPane.showConfirmDialog(
                                            frame, getOptionPanel(), "Highlight Colour : "
                                                , JOptionPane.OK_CANCEL_OPTION
                                                , JOptionPane.PLAIN_MESSAGE);

                    System.out.println("TEXT : " + text);
                    System.out.println("START INDEX : " + startIndex);
                    System.out.println("END INDEX : " + endIndex);

                    if (selection == JOptionPane.OK_OPTION)
                    {
                        String colour = (String) cbox.getSelectedItem();
                        try
                        {
                            if (colour == colourNames[0])
                            {
                                System.out.println("Colour Selected : " + colour);
                                highlighter.addHighlight(startIndex, endIndex, redPainter);
                            }
                            else if (colour == colourNames[1])
                            {
                                System.out.println("Colour Selected : " + colour);
                                highlighter.addHighlight(startIndex, endIndex, orangePainter);
                            }
                            else if (colour == colourNames[2])
                            {
                                System.out.println("Colour Selected : " + colour);
                                highlighter.addHighlight(startIndex, endIndex, cyanPainter);
                            }
                            Highlighter.Highlight[] highlightIndex = highlighter.getHighlights();
                            System.out.println("Lengh of Highlights used : " + highlightIndex.length);
                            highlights.put(startIndex, highlightIndex[highlightIndex.length - 1]);
                        }
                        catch(BadLocationException ble)
                        {
                            ble.printStackTrace();
                        }
                    }
                    else if (selection == JOptionPane.CANCEL_OPTION)
                    {
                        System.out.println("CANCEL BUTTON PRESSED.");
                    }
                    else if (selection == JOptionPane.CLOSED_OPTION)
                    {
                        System.out.println("JOPTIONPANE CLOSED DELIBERATELY.");
                    }                   
                }
            }
        });

        //frame.add(remHighButton, BorderLayout.PAGE_START);
        frame.add(contentPane, BorderLayout.CENTER);
        frame.add(button, BorderLayout.PAGE_END);


        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    private JPanel getOptionPanel()
    {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.DARK_GRAY, 2), "COLOUR SELECTION"));
        panel.setLayout(new GridLayout(0, 2, 5, 5));

        JLabel colourLabel = new JLabel("Select One Colour : ");
        cbox = new JComboBox(colourNames);

        panel.add(colourLabel);
        panel.add(cbox);

        return panel;
    }
    
    public void highlightPatterns(ArrayList<utils.Position> positions){
    	
    	for(int i = 0; i < positions.size(); i++){
    		
    		Highlighter highlighter = this.tarea.getHighlighter();
    		
    		try {
				highlighter.addHighlight(positions.get(i).getBegin(), positions.get(i).getEnd(), this.painters.get(painterCounter));
				
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
    	}
    	
    	painterCounter++;
		
		if(painterCounter ==painters.size())
			painterCounter = 0;
    	
    }
}