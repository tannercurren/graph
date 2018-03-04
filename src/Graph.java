import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Graph extends JFrame{
	// Set up GUI components
	JPanel screen;
	JTextField xMax, yMax, xMin, yMin;
	JButton func1, func2, func3;
	GraphPanel graph;
	JLabel selectedPoint;
	JFrame selfRef;
	JCheckBox party;
	
	public Graph() {
		// Set up the window
		setSize(960, 720);
		setTitle("Graph");
		selfRef = this;
		
		// Set up screen as a grid bag layout with bounds left,
		// right, above, and below with graph in the center
		
		//Create the components and set their preferences
		screen = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		xMax = new JTextField("10");
		xMax.setPreferredSize(new Dimension(80, 30));
		yMax = new JTextField("10");
		yMax.setPreferredSize(new Dimension(80, 30));
		xMin = new JTextField("-10");
		xMin.setPreferredSize(new Dimension(80, 30));
		yMin = new JTextField("-10");
		yMin.setPreferredSize(new Dimension(80, 30));
		graph = new GraphPanel();
		func1 = new JButton("f(x)=x\u00B2 + 5");
		func2 = new JButton("f(x)=(x-3)\u00B3");
		func3 = new JButton("f(x)=cos(x)");
		selectedPoint = new JLabel("Tracing:");
		party = new JCheckBox("Party Mode!");
		
		// Add the components and format the screen
		// Add in the screen and bounds
		add(screen);
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(10, 0, 15, 0);
		c.gridx = 1;
		c.gridy = 0;
		screen.add(yMax, c);
		c.insets = new Insets(0, 25, 0, 25);
		c.gridx = 0;
		c.gridy = 1;
		screen.add(xMin, c);
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 90;
		c.weighty = 60;
		c.insets = new Insets(0, 0, 0, 0);
		c.gridx = 1;
		c.gridy = 1;
		screen.add(graph, c);
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		c.weighty = 0;
		c.insets = new Insets(0, 25, 0, 25);
		c.gridx = 2;
		c.gridy = 1;
		screen.add(xMax, c);
		c.insets = new Insets(15, 0, 0, 8);
		c.gridx = 1;
		c.gridy = 2;
		screen.add(yMin, c);
		c.insets = new Insets(10,0,15,0);
		c.gridx = 1;
		c.gridy = 3;
		// Stick the buttons in a new flow layout for the sake of simplicity
		JPanel buttons = new JPanel();
		buttons.add(func1);
		buttons.add(func2);
		buttons.add(func3);
		screen.add(buttons, c);
		// Add party mode option!
		c.gridx = 2;
		c.gridy = 3;
		c.anchor = GridBagConstraints.LINE_END;
		screen.add(party, c);
		// Add in the display for the current highlighted point
		c.gridx = 2;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.HORIZONTAL;
		screen.add(selectedPoint, c);
		
		// Add in mouse listener to reset bounds
		graph.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				resetBounds();
			}
		});

		// Add in mouse motion listener to set the selected point label (rounded to 2 decimals)
		graph.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent arg0) {
				double xPoint = Math.round(graph.getSelectedPointX() * 100.) / 100.;
				double yPoint = Math.round(graph.getSelectedPointY() * 100.) / 100.;
				
				selectedPoint.setText("Tracing: (" + String.valueOf(xPoint) + ", " + String.valueOf(yPoint) + ")");
			}
		});
		
		// Add in mouse scroll listener to track when the mouse scrolls up or down
		// to zoom in or out, respectively, and alter the bounds as necessary
		addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				graph.zoom(e);
				resetBounds();
				if (graph.getCurGraph() == 1) {
					graph.drawFunc1((Graphics2D)graph.getGraphics());
				} else if (graph.getCurGraph() == 2) {
					graph.drawFunc2((Graphics2D)graph.getGraphics());
				} else if (graph.getCurGraph() == 3) {
					graph.drawFunc3((Graphics2D)graph.getGraphics());
				}
			}
		});
		
		// Add the action listeners to graphing buttons; each button should set
		// the bounds to the contents of the text fields and then draw the graph
		func1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (Double.parseDouble(xMax.getText()) > Double.parseDouble(xMin.getText()) &&
						Double.parseDouble(yMax.getText()) > Double.parseDouble(yMin.getText())) {
					graph.setBounds(xMax.getText(), yMax.getText(), 
									xMin.getText(), yMin.getText());
					
					// Reset point selection
					graph.setSelecting(false);
					selectedPoint.setText("Tracing:");
					
					graph.drawFunc1((Graphics2D)graph.getGraphics());
				} else {
					// If bounds don't work, show an error message
					JOptionPane.showMessageDialog(selfRef,
						    "Error: Inappropriate bounds",
						    "Boundary Error",
						    JOptionPane.ERROR_MESSAGE);
				}
			} 
		});
		
		func2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (Double.parseDouble(xMax.getText()) > Double.parseDouble(xMin.getText()) &&
						Double.parseDouble(yMax.getText()) > Double.parseDouble(yMin.getText())) {
					graph.setBounds(xMax.getText(), yMax.getText(), 
									xMin.getText(), yMin.getText());
					
					// Reset point selection
					graph.setSelecting(false);
					selectedPoint.setText("Tracing:");
				
					graph.drawFunc2((Graphics2D)graph.getGraphics());
				} else {
					// If bounds don't work, show an error message
					JOptionPane.showMessageDialog(selfRef,
						    "Error: Inappropriate bounds~",
						    "Boundary Error",
						    JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		
		func3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (Double.parseDouble(xMax.getText()) > Double.parseDouble(xMin.getText()) &&
						Double.parseDouble(yMax.getText()) > Double.parseDouble(yMin.getText())) {
					graph.setBounds(xMax.getText(), yMax.getText(), 
									xMin.getText(), yMin.getText());
					
					// Reset point selection
					graph.setSelecting(false);
					selectedPoint.setText("Tracing:");
				
					graph.drawFunc3((Graphics2D)graph.getGraphics());
				} else {
					// If bounds don't work, show an error message
					JOptionPane.showMessageDialog(selfRef,
						    "Error: Inappropriate bounds",
						    "Boundary Error",
						    JOptionPane.ERROR_MESSAGE);
				} 
			}
		});
		
		party.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				graph.partyOnOff();
			}
			
		});
	}
	
	/**
	 * Reset the bounds of the graph in the text fields based on dragging;
	 * rounds all decimals to 2 digits
	 */
	public void resetBounds() {
		xMax.setText(Double.toString((double) Math.round(graph.getXMax() * 100) / 100));
		yMax.setText(Double.toString((double) Math.round(graph.getYMax() * 100) / 100));
		xMin.setText(Double.toString((double) Math.round(graph.getXMin() * 100) / 100));
		yMin.setText(Double.toString((double) Math.round(graph.getYMin() * 100) / 100));
	}
	
	public static void main(String[] args) {
		Graph window = new Graph();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		window.setMinimumSize(new Dimension(600, 450));
	}

}
