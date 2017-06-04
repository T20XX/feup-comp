package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.swing.*;

import org.apache.commons.io.FilenameUtils;

import patternFinder.PAT;

public class MainWindow extends JFrame implements ActionListener
{
private JTextArea ta;
private int count;
private JMenuBar menuBar;
private JMenu fileM,editM;
private JScrollPane scpane;
private JMenuItem exitI,cutI,copyI,pasteI,selectI,saveI,loadI;
private String pad;
private JToolBar toolBar;
private TextLineNumber textLineNumber;
private Panel southPanel;
private JButton selectFile;
private JLabel fileName;
private JPanel outputPanel;
private JMenu helpM;
private JMenuItem syntaxM;
private JLabel textPane;
private JPanel fileSelection;
private JButton runButton;

private File javaFile = null;

public MainWindow()
{
    super("PAT");
    setSize(600, 600);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Container pane = getContentPane();
    pane.setLayout(new BorderLayout());

    count = 0;
    pad = " ";
    ta = new JTextArea(); //textarea
    menuBar = new JMenuBar(); //menubar
    fileM = new JMenu("File"); //file menu
    editM = new JMenu("Edit");
    scpane = new JScrollPane(ta); //scrollpane  and add textarea to scrollpane
    exitI = new JMenuItem("Exit");
    cutI = new JMenuItem("Cut");
    copyI = new JMenuItem("Copy");
    pasteI = new JMenuItem("Paste");
    selectI = new JMenuItem("Select All"); //menuitems
    saveI = new JMenuItem("Save"); //menuitems
    loadI = new JMenuItem("Load");
    toolBar = new JToolBar();
    
    // TODO: uncomment
    //textLineNumber = new TextLineNumber(ta);
    //scpane.setRowHeaderView(textLineNumber);

    ta.setLineWrap(true);
    ta.setWrapStyleWord(true);

    setJMenuBar(menuBar);
    menuBar.add(fileM);
    menuBar.add(editM);

    fileM.add(saveI);
    fileM.add(loadI);
    fileM.add(exitI);

    editM.add(cutI);
    editM.add(copyI);
    editM.add(pasteI);        
    editM.add(selectI);

    saveI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
    loadI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
    cutI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
    copyI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
    pasteI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
    selectI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
    
    helpM = new JMenu("Help");
    menuBar.add(helpM);
    
    syntaxM = new JMenuItem("Syntax");
    helpM.add(syntaxM);

    pane.add(scpane,BorderLayout.CENTER);
    pane.add(toolBar,BorderLayout.SOUTH);
    
    southPanel = new Panel();
    getContentPane().add(southPanel, BorderLayout.SOUTH);
    southPanel.setLayout(new GridLayout(2, 0, 0, 0));
    
    fileSelection = new JPanel();
    southPanel.add(fileSelection);
    
    selectFile = new JButton("Select file");
    fileSelection.add(selectFile);
    
    selectFile.addActionListener(new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			
			final JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(MainWindow.this);
			
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				
				File selectedFile = fc.getSelectedFile();
				
				// if file is not a java file
                if(!FilenameUtils.getExtension(selectedFile.getName()).equalsIgnoreCase("java"))
                {
                	final JFrame parent = new JFrame();
                	JOptionPane.showMessageDialog(parent, "Please select a .java file to examine.");
                    
                	System.out.println("Bad extension for file.");
                }
                else
                {
                	MainWindow.this.fileName.setText(selectedFile.getName());
                	MainWindow.this.javaFile = selectedFile;
                	
                	System.out.println("User decided to test file '" + MainWindow.this.javaFile.getName() + "'.");
                }
                
                
            } 
			
		}
    	
    });
    
    fileName = new JLabel("No file selected");
    fileSelection.add(fileName);
    
    runButton = new JButton("Run");
    fileSelection.add(runButton);
    runButton.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(MainWindow.this.javaFile == null)
			{
				final JFrame parent = new JFrame();
				JOptionPane.showMessageDialog(parent, "No file has been selected.");
				return;
			}
			
			String patterns = MainWindow.this.ta.getText();
			InputStream stream = new ByteArrayInputStream(patterns.getBytes(StandardCharsets.UTF_8));

			
			PAT.run(stream, MainWindow.this.javaFile);
			
		}
	});
    
    outputPanel = new JPanel();
    southPanel.add(outputPanel);
    outputPanel.setLayout(new BoxLayout(outputPanel, BoxLayout.X_AXIS));
    
    textPane = new JLabel();
    textPane.setText("Waiting for input.");
    textPane.setToolTipText("PAT's output");
    outputPanel.add(textPane);

    saveI.addActionListener(this);
    loadI.addActionListener(this);
    exitI.addActionListener(this);
    cutI.addActionListener(this);
    copyI.addActionListener(this);
    pasteI.addActionListener(this);
    selectI.addActionListener(this);

    setVisible(true);
}
public void actionPerformed(ActionEvent e) 
{
    JMenuItem choice = (JMenuItem) e.getSource();
    if (choice == exitI)
        System.exit(0);
    else if (choice == cutI)
    {
        pad = ta.getSelectedText();
        ta.replaceRange("", ta.getSelectionStart(), ta.getSelectionEnd());
    }
    else if (choice == copyI)
        pad = ta.getSelectedText();
    else if (choice == pasteI)
        ta.insert(pad, ta.getCaretPosition());
    else if (choice == selectI)
        ta.selectAll();

}

public static void main(String[] args) 
{
    new MainWindow();
}}