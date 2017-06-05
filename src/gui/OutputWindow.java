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

import utils.MyFileReader;

public class OutputWindow extends JFrame
{
	private JTextArea tarea;
	private JComboBox cbox;
	private JTextField lineField;
	private String[] colourNames = {"RED", "ORANGE", "CYAN"};
	private Color[] colours = {Color.RED, Color.ORANGE, Color.CYAN, Color.GREEN};

	private Highlighter.HighlightPainter redPainter;
	private Highlighter.HighlightPainter orangePainter;
	private Highlighter.HighlightPainter cyanPainter;   
	private Highlighter.HighlightPainter greenPainter;

	private int firstUpdateIndex;
	private int counter;

	private Map<Integer, Highlighter.Highlight> highlights = new HashMap<Integer, Highlighter.Highlight>();
	private Map<String, ArrayList<utils.Position>> positionByRuleName = new HashMap<String, ArrayList<utils.Position>>();
	private Map<String, Boolean> highlightState = new HashMap<String, Boolean>();

	private ArrayList<Highlighter.HighlightPainter> painters;
	private JPanel subPanel;
	private static int painterCounter = 0;

	public OutputWindow()
	{
		super("Pattern matches");
		redPainter = new DefaultHighlighter.DefaultHighlightPainter(colours[0]);
		orangePainter = new DefaultHighlighter.DefaultHighlightPainter(colours[1]);
		cyanPainter = new DefaultHighlighter.DefaultHighlightPainter(colours[2]);
		greenPainter = new DefaultHighlighter.DefaultHighlightPainter(colours[3]);

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
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setPreferredSize(new Dimension(700, 1000));

		Container pane = getContentPane();
		pane.setLayout(new BorderLayout());

		tarea = new JTextArea();
		tarea.setText(MyFileReader.read(file));

		// customize JTextArea
		tarea.setEditable(false);
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		tarea.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 20, 10, 20)));
		tarea.setTabSize(2);
		//tarea.setPreferredSize(new Dimension(600, 900));
		tarea.setLineWrap(true);
		tarea.setWrapStyleWord(true);

		JScrollPane scrollPane = new JScrollPane(tarea);
		pane.add(scrollPane, BorderLayout.CENTER);


		subPanel = new JPanel();
		pane.add(subPanel, BorderLayout.SOUTH);


		pack();
		setLocationByPlatform(true);
		setVisible(true);
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

	public void highlightPatterns(String ruleName, ArrayList<utils.Position> positions){
		Highlighter.HighlightPainter painter = this.painters.get(painterCounter);
		Highlighter highlighter = this.tarea.getHighlighter();

		for(int i = 0; i < positions.size(); i++){
			try {
				Highlighter.Highlight tmp = (Highlighter.Highlight) highlighter.addHighlight(positions.get(i).getBegin(), positions.get(i).getEnd(), painter);
				highlights.put(positions.get(i).getBegin(), tmp);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		//positionByRuleName.put(ruleName, positions);
		highlightState.put(ruleName, true);
		JButton tmp = new JButton(ruleName);
		tmp.setBackground(colours[painterCounter]);
		tmp.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				Object source = ae.getSource();
				if (source instanceof JButton) {
					JButton btn = (JButton)source;
					boolean isHighlighted = highlightState.get(btn.getText());
					highlightState.put(ruleName, !isHighlighted);
					//ArrayList<utils.Position> positions = positionByRuleName.get(btn.getText());
					if(isHighlighted){
						for(int i = 0; i < positions.size(); i++){
								highlighter.removeHighlight(highlights.get(positions.get(i).getBegin()));
								//highlights.remove(positions.get(i).getBegin());
						}
					}else{
						for(int i = 0; i < positions.size(); i++){
							try {
								Highlighter.Highlight tmp = (Highlighter.Highlight) highlighter.addHighlight(positions.get(i).getBegin(), positions.get(i).getEnd(), painter);
								highlights.put(positions.get(i).getBegin(), tmp);
							} catch (BadLocationException e) {
								e.printStackTrace();
							}
						}
					}
				}

			}
		});
		subPanel.add(tmp);

		painterCounter++;

		if(painterCounter ==painters.size())
			painterCounter = 0;

	}
}