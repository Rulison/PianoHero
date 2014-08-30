import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import java.awt.Graphics;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sun.org.apache.bcel.internal.generic.NEW;
/**
 * Main component organizer, implemented in PianoWindow class. Contains a panel to select difficulty,
 * one to select title font size, and one to select songs.
 *
 */
public class MenuScreen extends JPanel{
	Font titleFont;
	boolean playNow;
	int titleFontSize;
	DifficultyPanel diffPanel;
	String currentSong;
	SongPlayer player;
	JMenuBar bar;
	ArrayList scores=new ArrayList();
	
	/**
	 * Constructs and arranges components of Menu screen.
	 */
	public MenuScreen()
	{
		currentSong="SomeoneLikeYou";
		playNow=false;
		diffPanel=new DifficultyPanel();
		setBackground(Color.WHITE);
		setLayout(new BorderLayout());
		add(diffPanel, BorderLayout.SOUTH);

		add(new SizePanel(),BorderLayout.EAST);
		bar=new JMenuBar();
		JMenu toolsMenu = new JMenu("Play");
		JMenuItem beginCommand = new JMenuItem("Begin");   // Create a menu item.
		beginCommand.addActionListener(diffPanel);         // Add listener to menu item.
		toolsMenu.add(beginCommand); 
		bar.add(toolsMenu);
		JPanel n=new JPanel();
		//n.add(bar);
		n.add(new SelectionPanel());
		add(n,BorderLayout.NORTH);
		titleFontSize=72;
	}
	/**
	 * Called when a new song is beginning.  Stops the current songs and begins the new one.
	 */
	public void clearMenu()
	{
		if(currentSong.equals("SomeoneLikeYou"))
		{
			try
			{
				player.stopSong();
				remove(player);
			}
			catch(NullPointerException e)
			{};
		}
		try	{
			remove(player);
		}catch(NullPointerException e)
		{
			
		};
		int diff=diffPanel.getDifficulty();
		player=new SongPlayer(diff, scores, currentSong);
		add(player,BorderLayout.CENTER);


		playNow=true;
		revalidate();
		repaint();
	}

	/**
	 * Draws piano background
	 */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(!playNow)
		{
			titleFont=new Font("SansSerif", Font.BOLD, titleFontSize);
			g.setFont(titleFont);
			g.drawString("PIANO HERO", getWidth()/2-220, getHeight()/2+50);
			//white piano keys:
			for(int i=1;i<9;i++)
				g.drawLine(getWidth()/9*i, 0, getWidth()/9*i, getHeight());
			//black piano keys:
			g.fillRect(getWidth()/9-20,0,40,getHeight()/2-35);
			g.fillRect(getWidth()*2/9-20,0,40,getHeight()/2-35);
			g.fillRect(getWidth()*4/9-20,0,40,getHeight()/2-35);
			g.fillRect(getWidth()*5/9-20,0,40,getHeight()/2-35);
			g.fillRect(getWidth()*6/9-20,0,40,getHeight()/2-35);
			g.fillRect(getWidth()*8/9-20,0,40,getHeight()/2-35);
		}
	}

	public void repaintMenu()
	{
		repaint();
	}
	/**
	 * Slider that changed title font size.  Just for fun!
	 */
	public class SizePanel extends JPanel implements ChangeListener
	{
		JSlider titleSizer;
		public SizePanel()
		{
			titleSizer=new JSlider(12,100,72);
			add(titleSizer);
			titleSizer.addChangeListener(this);
			titleSizer.setOrientation(JSlider.VERTICAL);

		}

		public void stateChanged(ChangeEvent evt) {
			titleFontSize=titleSizer.getValue();
			repaintMenu();
		}

	}
	/**
	 * For song selection.
	 * @author Jared
	 *
	 */
	public class SelectionPanel extends JPanel implements ActionListener
	{
		JComboBox songs;
		JLabel disclaimer;
		public SelectionPanel() 
		{
			songs=new JComboBox();
			songs.setFont(new Font("TimesNewRoman", Font.PLAIN,12));
			songs.addItem("Someone Like You - Adele");
			songs.addItem("Don't Leave Me - Regina Spektor");
			disclaimer=new JLabel("More songs to be added later.");
			add(songs);
			add(disclaimer);
			songs.addActionListener(this);
		}

		public void actionPerformed(ActionEvent evt) {
			int index=songs.getSelectedIndex();
			if(index==0)
				currentSong="SomeoneLikeYou";
			else
				currentSong="DontLeaveMe";

		}

	}
	/**
	 * Contains three check boxes to let user select difficulty, as well as a Begin and Quit button.
	 *
	 */
	class DifficultyPanel extends JPanel implements ActionListener, FocusListener, MouseListener
	{
		JButton begin;
		JButton quit;
		JCheckBox easy;
		JCheckBox medium;
		JCheckBox difficult;

		DifficultyPanel()
		{
			begin=new JButton("Begin");
			quit=new JButton("Quit");
			
			easy=new JCheckBox("Easy");
			easy.setSelected(false);
			medium=new JCheckBox("Medium");
			medium.setSelected(false);
			difficult=new JCheckBox("Difficult");
			difficult.setSelected(true);
			
			//listen to buttons
			easy.addActionListener(this);
			quit.addActionListener(this);
			medium.addActionListener(this);
			difficult.addActionListener(this);
			begin.addActionListener(this);
			
			//add buttons
			add(easy);
			add(medium);
			add(difficult);
			add(begin);
			add(quit);
		}
		/**
		 * Returns which difficulty is selected.
		 * @return
		 */
		public int getDifficulty()
		{
			if(easy.isSelected())
				return SongPlayer.EASY;
			if(medium.isSelected())
				return SongPlayer.MEDIUM;
			else
				return SongPlayer.HARD;
		}
		
		public void actionPerformed(ActionEvent evt) {
			Object source=evt.getSource();
			String command = evt.getActionCommand();
			//makes sure only one box is selected at a time:
			if(source==easy)
			{
				easy.setSelected(true);
				if(medium.isSelected())
					medium.setSelected(false);
				if(difficult.isSelected())
					difficult.setSelected(false);
			}
			if(source==medium)
			{
				medium.setSelected(true);
				if(easy.isSelected())
					easy.setSelected(false);
				if(difficult.isSelected())
					difficult.setSelected(false);
			}
			if(source==difficult)
			{
				difficult.setSelected(true);
				if(medium.isSelected())
					medium.setSelected(false);
				if(easy.isSelected())
					easy.setSelected(false);
			}
			if(command.equals("Begin"))
			{
				clearMenu();
			}
			if(command.equals("Quit"))
			{
				try
				{
					if(player == null || !player.isPlaying()) {
						System.exit(0);
					}
					String name=JOptionPane.showInputDialog(this, "Enter name:");//gets name
					scores.add(new Score(name,player.score, currentSong));
					player.endScreen(scores);
				}catch (NullPointerException e)
				{}
			}
		}
		public void mouseClicked(MouseEvent arg0) {
			requestFocus();
		}
		//Unused methods-------------------------
		public void focusGained(FocusEvent arg0) {}
		public void focusLost(FocusEvent arg0) {}
		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mousePressed(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}
	}
}