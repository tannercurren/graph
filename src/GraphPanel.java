import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class GraphPanel extends JPanel{
	private double xscale;
	private double yscale;
	private double xMax;
	private double yMax;
	private double xMin;
	private double yMin;
	private int clickedX;
	private int clickedY;
	private int curGraph;
	private double selectedPointX;
	private double selectedPointY;
	private boolean selecting;
	private boolean partyMode;
	
	public GraphPanel() {
		// Set up borders and background
		setBorder(BorderFactory.createLineBorder(Color.black));
		setOpaque(true);
		setBackground(Color.WHITE);
		// Set curGraph; 0 if none, 1 if graph 1, 2 if graph 2, 3 if graph 3
		curGraph = 0;
		// Selecting defaults to false; true when selecting a point with mouse
		selecting = false;
		// Default partyMode to false
		partyMode = false;
		
		// Add in mouse events to allow for zooming by drawing a box with the mouse
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				clickedX = e.getX();
				clickedY = e.getY();
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// If the box moves from left to right
				if (e.getX() > clickedX) {
					// If the box moves from top to bottom
					if (e.getY() > clickedY) {
						// We must use temp. variables since the conversions rely on bounds
						double xMaxTmp = xToCartesian(e.getX());
						double yMaxTmp = yToCartesian(clickedY);
						double xMinTmp = xToCartesian(clickedX);
						double yMinTmp = yToCartesian(e.getY());
						xMax = xMaxTmp;
						yMax = yMaxTmp;
						xMin = xMinTmp;
						yMin = yMinTmp;
						xscale = Math.abs((xMax - xMin)/getWidth());
						yscale = Math.abs((yMax - yMin)/getHeight());
					} else { // If the box moves from bottom to top
						// We must use temp. variables since the conversions rely on bounds
						double xMaxTmp = xToCartesian(e.getX());
						double yMaxTmp = yToCartesian(e.getY());
						double xMinTmp = xToCartesian(clickedX);
						double yMinTmp = yToCartesian(clickedY);
						xMax = xMaxTmp;
						yMax = yMaxTmp;
						xMin = xMinTmp;
						yMin = yMinTmp;
						xscale = Math.abs((xMax - xMin)/getWidth());
						yscale = Math.abs((yMax - yMin)/getHeight());
					}
				} else { // If the box moves from right to left
					// If the box moves from top to bottom
					if (e.getY() > clickedY) {
						// We must use temp. variables since the conversions rely on bounds
						double xMaxTmp = xToCartesian(clickedX);
						double yMaxTmp = yToCartesian(clickedY);
						double xMinTmp = xToCartesian(e.getX());
						double yMinTmp = yToCartesian(e.getY());
						xMax = xMaxTmp;
						yMax = yMaxTmp;
						xMin = xMinTmp;
						yMin = yMinTmp;
						xscale = Math.abs((xMax - xMin)/getWidth());
						yscale = Math.abs((yMax - yMin)/getHeight());
					} else { // If the box moves from bottom to top
						// We must use temp. variables since the conversions rely on bounds
						double xMaxTmp = xToCartesian(clickedX);
						double yMaxTmp = yToCartesian(e.getY());
						double xMinTmp = xToCartesian(e.getX());
						double yMinTmp = yToCartesian(clickedY);
						xMax = xMaxTmp;
						yMax = yMaxTmp;
						xMin = xMinTmp;
						yMin = yMinTmp;
						xscale = Math.abs((xMax - xMin)/getWidth());
						yscale = Math.abs((yMax - yMin)/getHeight());
					}
				}
				if (curGraph == 1) {
					drawFunc1((Graphics2D)getGraphics());
				} else if (curGraph == 2) {
					drawFunc2((Graphics2D)getGraphics());
				} else if (curGraph == 3) {
					drawFunc3((Graphics2D)getGraphics());
				}
			}
		});
		
		// Add in mouse motion listener to track where mouse is on the graph and display
		// the current point
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				double loc = MouseInfo.getPointerInfo().getLocation().getX() - getLocationOnScreen().getX();
				selecting = true;
				
				// Calculate the selected point based on mouse's location and redraw the graph
				if (curGraph == 1) {
					selectedPointX = xToCartesian((int)loc);
					selectedPointY = Math.pow(xToCartesian((int)loc), 2) + 5;
					drawFunc1((Graphics2D)getGraphics());
				} else if (curGraph == 2) {
					selectedPointX = xToCartesian((int)loc);
					selectedPointY = Math.pow(xToCartesian((int)loc) - 3, 3);
					drawFunc2((Graphics2D)getGraphics());
				} else if (getCurGraph() == 3) {
					selectedPointX = xToCartesian((int)loc);
					selectedPointY = Math.cos(xToCartesian((int)loc));
					drawFunc3((Graphics2D)getGraphics());
				}
				
			}
		});	
	}
	
	/**
	 * Set the bounds of the axes to the text fields, and then reformat
	 * the axes based on these bounds
	 * 
	 * @param xMaxRecv  Maximum x bound
	 * @param yMaxRecv  Maximum y bound
	 * @param xMinRecv  Minimum x bound
	 * @param yMinRecv  Minimum y bound
	 */
	public void setBounds(String xMaxRecv, String yMaxRecv, 
						  String xMinRecv, String yMinRecv) {
		 xMax = Double.parseDouble(xMaxRecv);
		 yMax = Double.parseDouble(yMaxRecv);
		 xMin = Double.parseDouble(xMinRecv);
		 yMin = Double.parseDouble(yMinRecv);
		 
		 // Calculate the scales as the percentage of the size that the bounds are
		 xscale = Math.abs((xMax - xMin)/getWidth());
		 yscale = Math.abs((yMax - yMin)/getHeight());
	}
	
	/**
	 * Converts a pixel to a Cartesian x-coordinate for calculating the function
	 * @param   x  the pixel coordinate to be converted
	 * @return  x in Cartesian coordinates
	 */
	public double xToCartesian(int x) {
		return xMin + x * xscale;
	}
	
	/**
	 * Converts a pixel to a Cartesian x-coordinate for calculating the function
	 * @param   x  the pixel coordinate to be converted
	 * @return  x in Cartesian coordinates
	 */
	public double yToCartesian(int y) {
		return yMax - y * yscale;
	}
	
	/**
	 * Converts a Cartesian x-coordinate to a pixel coord. for drawing on the graph
	 * @param   x the Cartesian coordinate to be converted
	 * @return  x in pixel coordinates
	 */
	public int xToPx(double x) {
		return (int)((x - xMin)/xscale);
	}
	
	/**
	 * Converts a Cartesian y-coordinate to a pixel coord. for drawing on the graph
	 * @param y  the Cartesian coordinate to be converted
	 * @return   y in pixel coordinates
	 */
	public int yToPx(double y) {
		return (int)((yMax - y)/yscale);
	}
	
	/**
	 * Draw the axes onto the graph
	 * @param g  The graphics environment
	 */
	public void drawAxes(Graphics2D g) {
		// Set the color to gray
		g.setColor(Color.gray);
		
		// Draw the x-axis (where y = 0)
		g.drawLine(0, yToPx(0), getWidth(), yToPx(0));
		// Draw the y-axis (where x = 0)
		g.drawLine(xToPx(0), 0, xToPx(0), getHeight());
		
		// Set the color back to black
		g.setColor(Color.black);
	}
	
	/**
	 *  Zooms in if the mouse is scrolled upward and out if the mouse is
	 *  scrolled downward; zooms by a factor of 10%. To be used in graph's
	 *  zoom event to ensure that all bounds are set before drawing.
	 */
	public void zoom(MouseWheelEvent e) {
		int notches = e.getWheelRotation();
		// Zoom in if moved up, zoom out if moved down; zoom factor of 10% of the screen
		if (notches > 0) {
			xMax = xMax + getWidth() * xscale * .1;
			yMax = yMax + getHeight() * yscale * .1;
			xMin = xMin - getWidth() * xscale * .1;
			yMin = yMin - getHeight() * yscale * .1;
		} else {
			xMax = xMax - getWidth() * xscale * .1;
			yMax = yMax - getHeight() * yscale * .1;
			xMin = xMin + getWidth() * xscale * .1;
			yMin = yMin + getHeight() * yscale * .1;
		}
		
		// Reset scales and redraw the graph
		xscale = Math.abs((xMax - xMin)/getWidth());
		yscale = Math.abs((yMax - yMin)/getHeight());
	}

	/**
	 * Perform function 1 and draw it on the graph
	 * Function 1 is currently: f(x) = x^2 + 5
	 */
	public void drawFunc1(Graphics2D g) {
		
		// Blank out any previous drawings
		removeAll(); 
		updateUI();
		
		// Draw the axes
		drawAxes(g);
		
		// Instantiate the starting point at the left side of the graph
		int prevx = 0;
		int prevy = yToPx(xToCartesian(prevx)*xToCartesian(prevx)+5);
		
		//Every two pixels, draw a new line
		int r = 255, gr = 0, b = 0;
		for (int x = 2; x <= getSize().getWidth(); x += 2) {
			// Use a rainbow if party mode is on
			if (partyMode) {
				if ( r == 255 && gr < 255 && b == 0 ) gr+= 5;
			    if ( gr == 255 && r > 0 && b == 0 ) r-= 5;
			    if ( gr == 255 && b < 255 && r == 0 ) b+= 5;
			    if ( b == 255 && gr > 0 && r == 0 ) gr-= 5;
			    if ( b == 255 && r < 255 && gr == 0 ) r+= 5;
			    if ( r == 255 && b > 0 && gr == 0 ) b-= 5;
							
				Color customColor = new Color(r, gr, b);
				g.setColor(customColor);
			}
			
			// Convert to Cartesian, calculate f(x), then convert back to pixels
			double yCart = xToCartesian(x);
			yCart = yCart*yCart + 5;
			int y = yToPx(yCart);
			
			// Draw the line and overwrite the previous point with the current point
			g.drawLine(prevx, prevy, x, y);
			prevx = x;
			prevy = y;
		}
		
		// Set the current graph
		curGraph = 1;
		
		// Draw a dot at the selected point
		g.setColor(Color.black);
		if (selecting) {
			g.drawOval(xToPx(selectedPointX)-3, yToPx(selectedPointY)-3, 6, 6);
		}
	}
	
	/**
	 * Perform function 2 and draw it on the graph
	 * Function 2 is currently: f(x) = (x-3)^3
	 */
	public void drawFunc2(Graphics2D g) {
				
		// Blank out any previous drawings
		removeAll(); 
		updateUI();
		
		// Draw the axes
		drawAxes(g);
		
		// Instantiate the starting point at the left side of the graph
		int prevx = 0;
		int prevy = yToPx(Math.pow(xToCartesian(prevx)-3, 3));
		
		//Every two pixels, draw a new line
		int r = 255, gr = 0, b = 0;
		for (int x = 2; x <= getSize().getWidth(); x += 2) {
			// Use a rainbow if party mode is on
			if (partyMode) {
				if ( r == 255 && gr < 255 && b == 0 ) gr+= 5;
			    if ( gr == 255 && r > 0 && b == 0 ) r-= 5;
			    if ( gr == 255 && b < 255 && r == 0 ) b+= 5;
			    if ( b == 255 && gr > 0 && r == 0 ) gr-= 5;
			    if ( b == 255 && r < 255 && gr == 0 ) r+= 5;
			    if ( r == 255 && b > 0 && gr == 0 ) b-= 5;
							
				Color customColor = new Color(r, gr, b);
				g.setColor(customColor);
			}
						
			// Convert to Cartesian, calculate f(x), then convert back to pixels
			double yCart = xToCartesian(x);
			yCart = Math.pow(yCart - 3, 3);
			int y = yToPx(yCart);
			
			// Draw the line and overwrite the previous point with the current point
			g.drawLine(prevx, prevy, x, y);
			prevx = x;
			prevy = y;
		}
				
		// Set the current graph
		curGraph = 2;
		
		// Draw a dot at the selected point
		g.setColor(Color.black);
		if (selecting) {
			g.drawOval(xToPx(selectedPointX)-2, yToPx(selectedPointY)-3, 6, 6);
		}
	}
	
	/**
	 * Perform function 3 and draw it on the graph
	 * Function 3 is currently: f(x) = cos(x)
	 */
	public void drawFunc3(Graphics2D g) {
		
		// Blank out any previous drawings
		removeAll(); 
		updateUI();
		
		// Draw the axes
		drawAxes(g);
		
		// Instantiate the starting point at the left side of the graph
		int prevx = 0;
		int prevy = yToPx(Math.cos(xToCartesian(prevx)));
		
		// Every two pixels, draw a new line
		int r = 255, gr = 0, b = 0;
		for (int x = 2; x <= getSize().getWidth(); x += 2) {
			// Use a rainbow if party mode is on
			if (partyMode) {
				if ( r == 255 && gr < 255 && b == 0 ) gr+= 5;
			    if ( gr == 255 && r > 0 && b == 0 ) r-= 5;
			    if ( gr == 255 && b < 255 && r == 0 ) b+= 5;
			    if ( b == 255 && gr > 0 && r == 0 ) gr-= 5;
			    if ( b == 255 && r < 255 && gr == 0 ) r+= 5;
			    if ( r == 255 && b > 0 && gr == 0 ) b-= 5;
				
				Color customColor = new Color(r, gr, b);
				g.setColor(customColor);
			}
			
			// Convert to Cartesian, calculate f(x), then convert back to pixels
			double yCart = xToCartesian(x);
			yCart = Math.cos(yCart);
			int y = yToPx(yCart);
			
			// Draw the line and overwrite the previous point with the current point
			g.drawLine(prevx, prevy, x, y);
			prevx = x;
			prevy = y;
		}
		
		// Set the current graph
		curGraph = 3;
		
		// Draw a dot at the selected point
		g.setColor(Color.black);
		if (selecting) {
			g.drawOval(xToPx(selectedPointX)-2, yToPx(selectedPointY)-3, 6, 6);
		}
	}
	
	public void partyOnOff() {
		if (!partyMode) {
			partyMode = true;
		} else {
			partyMode = false;
		}
	}
	
	// Mutators
	public void setSelecting(boolean set) {
		selecting = set;
	}
	
	// Accessors
	public double getXMax() {
		return xMax;
	}
	public double getYMax() {
		return yMax;
	}
	public double getXMin() {
		return xMin;
	}
	public double getYMin() {
		return yMin;
	}
	public int getCurGraph() {
		return curGraph;
	}
	public double getSelectedPointX() {
		return selectedPointX;
	}
	public double getSelectedPointY() {
		return selectedPointY;
	}
	
	
	@Override
	public void paint(Graphics gc) {
		super.paint(gc);

		// Redraw the graph based on which one is on-screen
		Graphics2D g = (Graphics2D) gc;
		xscale = Math.abs((xMax - xMin)/getWidth());
		yscale = Math.abs((yMax - yMin)/getHeight());
		if (curGraph == 1) {
			drawFunc1(g);
		} else if (curGraph == 2) {
			drawFunc2(g);
		} else if (curGraph == 3) {
			drawFunc3(g);
		}
	}

	
	
}
