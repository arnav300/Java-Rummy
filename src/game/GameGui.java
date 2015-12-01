package game;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.Button;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Panel;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.ActionEvent;

/**
 * This represents the game's graphical user interface. As such, it extends JFrame.
 * 
 * @author Robert 
 * 
 */
public abstract class GameGui extends JFrame implements ActionListener, WindowListener {
	
	 /**
     * The game object.
     */
    protected Game game;
	
    
    // Used to generate unique event-action id's
    private static int localEventCount = 2000;
    
    // the GUI's default height and width, in pixels
    private static final int DEFAULT_HEIGHT = 400;
    private static final int DEFAULT_WIDTH = 400;

    /**
     * The GUI's initial height.
     *
     * @return the GUI's inital height, in pixels.
     */
    protected int initialHeight() { return DEFAULT_HEIGHT; }

    /**
     * The GUI's initial width.
     *
     * @return the GUI's inital width, in pixels.
     */
    protected int initialWidth() { return DEFAULT_WIDTH; }
    // Instantiation of helper class
    
    DummyButton dummyButton;
    

	public GameGui() {
		 
		 //  GUI (JFrame) construction ... 
		super();
		setMinimumSize(new Dimension(100, 100));
		setMaximumSize(new Dimension(700, 700));
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		   // set the GUI's initial size
        setSize(initialWidth(), initialHeight());

        // create a box and add it to the GUI's top pane
        Container topPane = getContentPane();
        Box semiTopPane = new Box(BoxLayout.X_AXIS);
        // create an application-specific GUI component; this is where
        // most of the action will occur
        Component applComponent = createApplComponent();
        
        // set our background color to match that of the application component
        topPane.setBackground(applComponent.getBackground());

        // create a Box to contain the application-specific portion of
        // the GUI and a "quit" button
        Box contentPane = new Box(BoxLayout.Y_AXIS);
        contentPane.add(Box.createGlue());
        contentPane.add(applComponent);
        contentPane.add(Box.createGlue());
        contentPane.add(Box.createVerticalGlue());

        // connect all the top-level GUI structure
        semiTopPane.add(Box.createHorizontalGlue());
        semiTopPane.add(contentPane);
        semiTopPane.add(Box.createHorizontalGlue());
        topPane.add(semiTopPane);

        // listen to the window so that we can interrupt a click on
        // the "quit" box
        this.addWindowListener(this);
        
		// ... and other setup stuff
		game = null;
	}
	
    /**
     * Creates the application-specific GUI component
     *
     * @return the application-specific component of the GUI
     */
    protected Component createApplComponent() {
        // for now, just return an empty panel.  It is expected that this
        // method will be subclassed
        return new Panel();
    }
	
    /**
     * Helper-method to add a button the GUI
     *
     * @param text the button's text
     * @param where the contiainer to which the button should be added
     * @param listener the listener that should listen to the button
     * @return the button created
     */
    protected JButton addButton(String text, Container where,
            ActionListener listener) {

        // create the button and add it
        JButton rtnVal = new JButton(text);
        where.add(rtnVal);

        // set up the listener
        rtnVal.addActionListener(listener);

        // set the button's action text to be the same as its displayed
        // text
        rtnVal.setActionCommand(text);

        // return the created button
        return rtnVal;
    }
    
	 /**
     * Lets the player know what the game object is.
     * 
     * @param game the game object
     * @param playerId the unique numeric player-id assigned to this
     *  player by the game
     */
    public void setGui(Game game) {

    	// Disallow setting game if one is already set
        if (this.game == null) {
            this.game = game; // set game
            setGuiMore(); // do additional initialization
            updateTitle(); // set title
            this.setVisible(true); // show the GUI to the user
        }
    }

    
    /**
     * Performs additional initialization, as required by the GUI.
     *
     */
    protected void setGuiMore() {
        // does nothing, but may be overriden by subclasses to do additional
        // initialization
    }

    /**
     * Updates the GUI's title.
     */
    protected void updateTitle() {
        this.setTitle(this.defaultTitle()); // set title
    }
    
    /**
     * Updates Gui according to state
     */
    public void stateChanged(GameState state){
    	// do nothing
    }
    
    /**
     * Abstract method returns the GUI's title-window
     *
     * @return the title to be put in the GUI's window
     */
    protected abstract String defaultTitle();

    /**
     * Give the user an information dialog box
     *
     * @param message the text to display to the user
     */
    protected static void infoMessage(String message) {
        JOptionPane.showMessageDialog(null, message,
            "Please confirm", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Give the user an error dialog box
     *
     * @param message  the text to display to the user
     */
    protected static void errorMessage(String message) {
        JOptionPane.showMessageDialog(null, message,
            "Please confirm", JOptionPane.ERROR_MESSAGE);
    }


    /**
     * submit an action to the player.  This will cause me to respond to
     * an event in the GUI's main thread.  (Typically, this is called by the
     * game from the game's thread.)
     *
     * @param actionType  a string describing the action (e.g., "you won").
     */
    protected final void submitAction(String actionType) {
		// create the event
        ActionEvent event =
            new ActionEvent(dummyButton, localEventCount++, actionType);
        // submit the event
        dummyButton.submitActionEvent(event);
    }

    /**
     * Lets the player know that it should make a move.  Typically called
     * by the game object.
     */
    public void requestMove() {
        // since this a generic game, we do nothing
    }

    /**
     * Notifies the player know that it's not his move.  Typically
     * called by the game object when the player has made a move out-of-turn.
     */
    public void notYourMove() {
        // notify GUI in its main thread
        this.submitAction("cmdNotYours");
    }

    /**
     * Notifies a player that the move he made is illegal.  (For example,
     * moving a non-king checkers-piece backwards.) Typically called by
     * the game object when an illegal move has been detected.
     */
    public void illegalMove() {
        // notify GUI in its main thread
        this.submitAction("cmdBadMove");
    }

    /**
     * Notifies a player that it is time to quit.  Typically called by the
     * game object when a player has indicated a desire to quit.
     */
    public void timeToQuit() {
        // notify GUI in its main thread
        this.submitAction("cmdQuit");
    }

    /**
     * Notifies a player that a request he had made is invalid.  (For
     * example, if the player request a "quit", but he is not allowed to.)
     * Typically called by the game object.
     */
    public void invalidRequest() {
        // notify GUI in its main thread
        this.submitAction("cmdInvalid");
    }

    /**
     * Notifies a player that the game is over.  Typically called by the
     * game object when the game has been terminated.
     */
    public void gameIsOver() {
        // notify GUI in its main thread
        this.submitAction("cmdOver");
    }

    /**
     * Notifies a player that the state of the game has changed.  Typically
     * called by the game object when an opponent has made a move, and the
     * player may want to take the move into account (e.g., to display it
     * on the screen).
     */
    public void stateChanged() {
        // for now, do nothing
    }

    /**
     * Invoked when the mouse has been clicked on a component.
     *
     * @param e  the event object
     */
    public void mouseClicked(MouseEvent e) {
      // by default, do nothing (i.e., ignore it)
    }

    /**
     * Invoked when a mouse button has been pressed on a component.

     *
     * @param e  the event object
     */
    public void mousePressed(MouseEvent e) {
      // by default, do nothing (i.e., ignore it)
    }

    /**
     * Invoked when a mouse button has been released on a component.

     *
     * @param e  the event object
     */
    public void mouseReleased(MouseEvent e) {
      // by default, do nothing (i.e., ignore it)
    }

    /**
     * Invoked when the mouse enters a component.

     *
     * @param e  the event object
     */
    public void mouseEntered(MouseEvent e) {
      // by default, do nothing (i.e., ignore it)
    }

    /**
     * Invoked when the mouse exits a component.

     *
     * @param e  the event object
     */
    public void mouseExited(MouseEvent e) {
      // by default, do nothing (i.e., ignore it)
    }

    /**
     * Invoked when the user attempts to close the window from the
     * window's system menu.
     *
     * @param e  the event object
     */
    public void windowClosing(WindowEvent e) {
        // treat it as if the user pressed the "quit" button
        
    }

    /**
     * Invoked the first time a window is made visible.
     *
     * @param e  the event object
     */
    public void windowOpened(WindowEvent e) {
      // by default, do nothing (i.e., ignore it)
    }

    /**
     * Invoked when a window has been closed as the result of calling
     * dispose on the window.
     *
     * @param e  the event object
     */
    public void windowClosed(WindowEvent e) {
      // by default, do nothing (i.e., ignore it)
    }

    /**
     * Invoked when a window is changed from a normal to a minimized state.
     *
     * @param e  the event object
     */
    public void windowIconified(WindowEvent e) {
      // by default, do nothing (i.e., ignore it)
    }

    /**
     * Invoked when a window is changed from a minimized to a normal state.
     *
     * @param e  the event object
     */
    public void windowDeiconified(WindowEvent e) {
      // by default, do nothing (i.e., ignore it)
    }

    /**
     * Invoked when the window is set to be the user's active window,
     * which means the window (or one of its subcomponents) will receive
     * keyboard events.
     *
     * @param e  the event object
     */
    public void windowActivated(WindowEvent e) {
      // by default, do nothing (i.e., ignore it)
    }

    /**
     * Invoked when a window is no longer the user's active window, which
     * means that keyboard events will no longer be delivered to the window
     * or its subcomponents.
     *
     * @param e  the event object
     */
    public void windowDeactivated(WindowEvent e) {
      // by default, do nothing (i.e., ignore it)
    }


    /**
     * Notifies the player that all players have acknowledged that the game
     * will be "quit". Gives the player last chance to "clean up".  (E.g.,
     * closing windows, terminating network connections, closing files.)
     * Typically called by the Game object just before the entire program
     * terminates.
     */
    public void finishUp() {
        // hide and dispose of the GUI
        this.setVisible(false);
        this.dispose();
    }

}


/**
 * A helper-class that defines a button that is not shown on the GUI, but is
 * rather used via the game object to generate actions for the player objects.
 */
class DummyButton extends Button
{
    /**
     * Constructor for objects of class DummyButton
     *
     * @param name the text that is to appear on the button.
     */
    public DummyButton(String name)
    {
        // perform superclass initialization
        super(name);
    }

    /**
     * Submits an action event to the button.  This has the effect of causing
     * 'actionPerformed' to be called on the button's listener, which is the 
     * registered ActionListener.
     *
     * @param ae the ActionEvent object to be sent to the button's listener
     */
    public void submitActionEvent(ActionEvent ae)
    {
        // Cause the event to be processed
        super.processActionEvent(ae);
    }
}






