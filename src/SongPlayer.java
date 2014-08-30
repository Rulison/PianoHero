import java.awt.BorderLayout;


import java.awt.Color;
import java.io.*;
import javax.sound.midi.*;
import javax.swing.*;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import java.awt.event.*;
import java.awt.Graphics;

import java.util.ArrayList;

/**
 * Plays the music, the song depends on the String passed into it.  Creates an Ending when Quit is pressed.
 * @author apple
 *
 */
public class SongPlayer extends JPanel implements KeyListener, ActionListener, FocusListener, MouseListener
{
	AudioPlayer p;
	AudioStream as;
	ArrayList scores;
	int score;
	int notesAtATime;//number of notes displayed
	int startValue;
	int difficulty;
	boolean songCanPlay;
	//difficulties:
	static int EASY=0;
	static int MEDIUM=1;
	static int HARD=2;
	Beat currentBeat;
	int beatCount;
	Song currentSong;
	Timer timer;
	//plays Midi file:
	Sequencer player;
	//tempo multiplier:
	float tempoFactor;
	//milliseconds per beat
	int msbp;
	String songName;
	public void stopSong()
	{
		player.stop();
	}
	public SongPlayer(int difficulty, ArrayList scores,String songName)
	{
		this.songName=songName;
		this.scores=scores;
		songCanPlay=true;
		SongLibrary lib=new SongLibrary(difficulty);
		setBackground(Color.WHITE);

		if(songName.equals("SomeoneLikeYou"))
		{
			currentSong=lib.getSLO();
			msbp=214*(3-difficulty);
			tempoFactor=0.872F/(3-difficulty);
		}
		else
		{
			currentSong=lib.getDLM();
			msbp=167;//*(3-difficulty);
			tempoFactor=1F/(3-difficulty);
		}
		timer=new Timer(msbp, this);
		notesAtATime=15;
		addKeyListener(this);
		addFocusListener(this);
		addMouseListener(this);
		this.difficulty=difficulty;
		beatCount=0;

		currentBeat=(Beat)currentSong.getBeats().get(0);
		if(songName.equals("SomeoneLikeYou"))
		{
			Sequence song;
			try {
				song=MidiSystem.getSequence(getClass().getResource("slo.mid"));
				player = MidiSystem.getSequencer();
				player.setSequence(song);
				player.open();
				player.setLoopCount(0);
				player.setTempoFactor(tempoFactor);
			} catch (InvalidMidiDataException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (MidiUnavailableException e) {
				e.printStackTrace();
			}
		}
		else
		{
			try{ 
				p = AudioPlayer.player;
				as = new AudioStream(getClass().getResourceAsStream("dlm.wav"));
			}
			catch(IOException IOE){IOE.printStackTrace();}

		}
	}



	public void paintComponent(Graphics g)
	{
		if(songCanPlay)
		{
			super.paintComponent(g);
			//Constant background things:
			for(int i=1;i<9;i++)
				g.drawLine(getWidth()/9*i, 0, getWidth()/9*i, getHeight());
			g.drawString("a", getWidth()*1/9, getHeight()-10);
			g.drawString("s", getWidth()*2/9, getHeight()-10);
			g.drawString("d", getWidth()*3/9, getHeight()-10);
			g.drawString("f", getWidth()*4/9, getHeight()-10);
			g.drawString("h", getWidth()*5/9, getHeight()-10);
			g.drawString("j", getWidth()*6/9, getHeight()-10);
			g.drawString("k", getWidth()*7/9, getHeight()-10);
			g.drawString("l", getWidth()*8/9, getHeight()-10);
			g.drawString(""+score,5,15);
			//black keys
			g.fillRect(getWidth()/9-20,0,40,getHeight()/2-35);
			g.fillRect(getWidth()*2/9-20,0,40,getHeight()/2-35);
			g.fillRect(getWidth()*4/9-20,0,40,getHeight()/2-35);
			g.fillRect(getWidth()*5/9-20,0,40,getHeight()/2-35);
			g.fillRect(getWidth()*6/9-20,0,40,getHeight()/2-35);
			g.fillRect(getWidth()*8/9-20,0,40,getHeight()/2-35);

			g.drawLine(0,getHeight()*8/9+10,getWidth(),getHeight()*8/9+10);
			//notes:
			int count=0;
			for(int i=startValue;i-startValue<notesAtATime;i++,count++)
			{
				if(i>=currentSong.getBeats().size())
				{
					timer.stop();
					break;
				}
				g.setColor(Color.BLUE);
				if (count%notesAtATime==1)
				{
					g.setColor(Color.YELLOW);
				}
				currentBeat=(Beat)currentSong.getBeats().get(i);
				if(count%notesAtATime==0)
				{
					g.setColor(currentBeat.color);
				}


				if(currentBeat.a)
				{
					g.fillRect(getWidth()/9-7, getHeight()*((notesAtATime-1)-(count%notesAtATime))/notesAtATime, 15, 15);
				}
				if(currentBeat.s)
				{
					g.fillRect(getWidth()/9*2-7, getHeight()*((notesAtATime-1)-(count%notesAtATime))/notesAtATime, 15, 15);
				}
				if(currentBeat.d)
				{
					g.fillRect(getWidth()/9*3-7, getHeight()*((notesAtATime-1)-(count%notesAtATime))/notesAtATime, 15, 15);
				}
				if(currentBeat.f)
				{
					g.fillRect(getWidth()/9*4-7, getHeight()*((notesAtATime-1)-(count%notesAtATime))/notesAtATime, 15, 15);
				}
				if(currentBeat.h)
				{
					g.fillRect(getWidth()/9*5-7, getHeight()*((notesAtATime-1)-(count%notesAtATime))/notesAtATime, 15, 15);
				}
				if(currentBeat.j)
				{
					g.fillRect(getWidth()/9*6-7, getHeight()*((notesAtATime-1)-(count%notesAtATime))/notesAtATime, 15, 15);
				}
				if(currentBeat.k)
				{
					g.fillRect(getWidth()/9*7-7, getHeight()*((notesAtATime-1)-(count%notesAtATime))/notesAtATime, 15, 15);
				}
				if(currentBeat.l)
				{
					g.fillRect(getWidth()/9*8-7, getHeight()*((notesAtATime-1)-(count%notesAtATime))/notesAtATime, 15, 15);
				}
				currentBeat=(Beat)currentSong.getBeats().get(startValue+1);
			}
		}
	}

	public void endScreen(ArrayList scores)
	{
		if(songName.equals("SomeoneLikeYou"))
			player.stop();
		else
			p.stop(as);

		songCanPlay=false;
		repaint();
		setLayout(new BorderLayout());
		Ending end=new Ending(scores);
		add(end,BorderLayout.CENTER);
		revalidate();
	}
	public boolean isPlaying() {
		return songCanPlay;
	}

	public void actionPerformed(ActionEvent evt) 
	{
		startValue++;
		repaint();
	}

	public void keyPressed(KeyEvent evt) 
	{
		int key=evt.getKeyCode();
		if (key==KeyEvent.VK_A)
		{
			if(currentBeat.a)
			{
				score+=20;
				currentBeat.color=Color.GREEN;
			}
			else
			{
				score-=20;
				currentBeat.color=Color.RED;
			}
		}
		if (key==KeyEvent.VK_S)
		{
			if(currentBeat.s)
			{
				score+=20;
				currentBeat.color=Color.GREEN;
			}
			else
			{
				score-=20;
				currentBeat.color=Color.RED;
			}
		}
		if (key==KeyEvent.VK_D)
		{
			if(currentBeat.d)
			{
				score+=20;
				currentBeat.color=Color.GREEN;
			}
			else
			{
				score-=20;
				currentBeat.color=Color.RED;
			}
		}
		if (key==KeyEvent.VK_F)
		{
			if(currentBeat.f)
			{
				score+=20;
				currentBeat.color=Color.GREEN;
			}
			else
			{
				score-=20;
				currentBeat.color=Color.RED;
			}
		}
		if (key==KeyEvent.VK_H)
		{
			if(currentBeat.h)
			{
				score+=20;
				currentBeat.color=Color.GREEN;
			}
			else
			{
				score-=20;
				currentBeat.color=Color.RED;
			}
		}
		if (key==KeyEvent.VK_J)
		{
			if(currentBeat.j)
			{
				score+=20;
				currentBeat.color=Color.GREEN;
			}
			else
			{
				score-=20;
				currentBeat.color=Color.RED;
			}
		}
		if (key==KeyEvent.VK_K)
		{
			if(currentBeat.k)
			{
				score+=20;
				currentBeat.color=Color.GREEN;
			}
			else
			{
				score-=20;
				currentBeat.color=Color.RED;
			}
		}
		if (key==KeyEvent.VK_L)
		{
			if(currentBeat.l)
			{
				score+=20;
				currentBeat.color=Color.GREEN;
			}
			else
			{
				score-=20;
				currentBeat.color=Color.RED;
			}
		}

	}

	public void mouseClicked(MouseEvent arg0) {
		requestFocus();
		if(songCanPlay)
		{
			if(songName.equals("SomeoneLikeYou"))
				player.start();
			else
			{
				p.start(as);
			}
			timer.start();
		}
	}
	public void keyReleased(KeyEvent arg0) {}

	public void keyTyped(KeyEvent arg0) {}

	public void focusGained(FocusEvent arg0) {}

	public void focusLost(FocusEvent arg0) {}

	public void mouseEntered(MouseEvent arg0) {}

	public void mouseExited(MouseEvent arg0) {}

	public void mousePressed(MouseEvent arg0) {}

	public void mouseReleased(MouseEvent arg0) {}
	/**
	 * Defines a song, of which there are two so far.
	 *
	 */
	public class Song
	{
		ArrayList<Beat> beats;
		float tempo;
		public Song(int difficulty, float tempo)
		{
			beats=new ArrayList<Beat>();
			tempo=this.tempo/(3-difficulty);
		}
		public void add(Beat beat)
		{
			beats.add(beat);
		}
		public ArrayList getBeats()
		{
			return beats;
		}
		public void empty()
		{
			add(new Beat(false, false, false, false, false, false, false, false));
			add(new Beat(false, false, false, false, false, false, false, false));
			add(new Beat(false, false, false, false, false, false, false, false));
			add(new Beat(false, false, false, false, false, false, false, false));

		}
		public void AMajorTriad()
		{
			add(new Beat(false, false, false, true, false, true, false, false));//unison A
			add(new Beat(false, false, false, false, false, false, true, false));//with C#
			add(new Beat(false, false, false, false, false, false, false, true));//with E
			add(new Beat(false, false, false, false, false, false, true, false));//with C#

			add(new Beat(false, false, false, false, false, true, false, false));//unison A
			add(new Beat(false, false, false, false, false, false, true, false));//with C#
			add(new Beat(false, false, false, false, false, false, false, true));//with E
			add(new Beat(false, false, false, false, false, false, true, false));//with C#

			add(new Beat(false, false, false, false, false, true, false, false));//unison A
			add(new Beat(false, false, false, false, false, false, true, false));//with C#
			add(new Beat(false, false, false, false, false, false, false, true));//with E
			add(new Beat(false, false, false, false, false, false, true, false));//with C#

			add(new Beat(false, false, false, false, false, true, false, false));//unison A
			add(new Beat(false, false, false, false, false, false, true, false));//with C#
			add(new Beat(false, false, false, false, false, false, false, true));//with E
			add(new Beat(false, false, false, false, false, false, true, false));//with C#
		}
		public void AOverAb()
		{
			add(new Beat(false, false, true, false, false, true, false, false));//unison A
			add(new Beat(false, false, false, false, false, false, true, false));//with C#
			add(new Beat(false, false, false, false, false, false, false, true));//with E
			add(new Beat(false, false, false, false, false, false, true, false));//with C#

			add(new Beat(false, false, false, false, false, true, false, false));//unison A
			add(new Beat(false, false, false, false, false, false, true, false));//with C#
			add(new Beat(false, false, false, false, false, false, false, true));//with E
			add(new Beat(false, false, false, false, false, false, true, false));//with C#

			add(new Beat(false, false, false, false, false, true, false, false));//unison A
			add(new Beat(false, false, false, false, false, false, true, false));//with C#
			add(new Beat(false, false, false, false, false, false, false, true));//with E
			add(new Beat(false, false, false, false, false, false, true, false));//with C#

			add(new Beat(false, false, false, false, false, true, false, false));//unison A
			add(new Beat(false, false, false, false, false, false, true, false));//with C#
			add(new Beat(false, false, false, false, false, false, false, true));//with E
			add(new Beat(false, false, false, false, false, false, true, false));//with C#
		}
		public void FSharpMinor()
		{
			add(new Beat(false, true, false, false, true, false, false, false));//unison A
			add(new Beat(false, false, false, false, false, false, true, false));//with C#
			add(new Beat(false, false, false, false, false, false, false, true));//with E
			add(new Beat(false, false, false, false, false, false, true, false));//with C#

			add(new Beat(false, false, false, false, true, false, false, false));//unison A
			add(new Beat(false, false, false, false, false, false, true, false));//with C#
			add(new Beat(false, false, false, false, false, false, false, true));//with E
			add(new Beat(false, false, false, false, false, false, true, false));//with C#

			add(new Beat(false, false, false, false, true, false, false, false));//unison A
			add(new Beat(false, false, false, false, false, false, true, false));//with C#
			add(new Beat(false, false, false, false, false, false, false, true));//with E
			add(new Beat(false, false, false, false, false, false, true, false));//with C#

			add(new Beat(false, false, false, false, true, false, false, false));//unison A
			add(new Beat(false, false, false, false, false, false, true, false));//with C#
			add(new Beat(false, false, false, false, false, false, false, true));//with E
			add(new Beat(false, false, false, false, false, false, true, false));//with C#
		}
		public void DMajor()
		{
			add(new Beat(true, false, false, false, true, false, false, false));//unison A
			add(new Beat(false, false, false, false, false, false, true, false));//with C#
			add(new Beat(false, false, false, false, false, false, false, true));//with E
			add(new Beat(false, false, false, false, false, false, true, false));//with C#

			add(new Beat(false, false, false, false, true, false, false, false));//unison A
			add(new Beat(false, false, false, false, false, false, true, false));//with C#
			add(new Beat(false, false, false, false, false, false, false, true));//with E
			add(new Beat(false, false, false, false, false, false, true, false));//with C#

			add(new Beat(false, false, false, false, true, false, false, false));//unison A
			add(new Beat(false, false, false, false, false, false, true, false));//with C#
			add(new Beat(false, false, false, false, false, false, false, true));//with E
			add(new Beat(false, false, false, false, false, false, true, false));//with C#

			add(new Beat(false, false, false, false, true, false, false, false));//unison A
			add(new Beat(false, false, false, false, false, false, true, false));//with C#
			add(new Beat(false, false, false, false, false, false, false, true));//with E
			add(new Beat(false, false, false, false, false, false, true, false));//with C#
		}

		public void EMajor()
		{
			add(new Beat(false, true, false, false, true, false, false, false));//unison A
			add(new Beat(false, false, false, false, false, true, false, false));//with C#
			add(new Beat(false, false, false, false, false, false, false, true));//with E
			add(new Beat(false, false, false, false, false, true, false, false));//with C#

			add(new Beat(false, false, false, false, true, false, false, false));//unison A
			add(new Beat(false, false, false, false, false, true, false, false));//with C#
			add(new Beat(false, false, false, false, false, false, false, true));//with E
			add(new Beat(false, false, false, false, false, true, false, false));//with C#

		}
		public void HalfFSharp()
		{
			add(new Beat(false, false, true, false, true, false, false, false));//unison A
			add(new Beat(false, false, false, false, false, false, true, false));//with C#
			add(new Beat(false, false, false, false, false, false, false, true));//with E
			add(new Beat(false, false, false, false, false, false, true, false));//with C#

			add(new Beat(false, false, false, false, true, false, false, false));//unison A
			add(new Beat(false, false, false, false, false, false, true, false));//with C#
			add(new Beat(false, false, false, false, false, false, false, true));//with E
			add(new Beat(false, false, false, false, false, false, true, false));//with
		}

		public void DMajor7()
		{
			add(new Beat(true, false, false, false, true, false, false, false));//unison A
			add(new Beat(false, false, false, false, false, false, true, false));//with C#
			add(new Beat(false, false, false, false, false, false, false, true));//with E
			add(new Beat(false, false, false, false, false, false, true, false));//with C#

			add(new Beat(false, false, false, false, true, false, false, false));//unison A
			add(new Beat(false, false, false, false, false, false, true, false));//with C#
			add(new Beat(false, false, false, false, false, false, false, true));//with E
			add(new Beat(false, false, false, false, false, false, true, false));//with C#

			add(new Beat(false, false, false, false, true, false, false, false));//unison A
			add(new Beat(false, false, false, false, false, true, false, false));//with C#
			add(new Beat(false, false, false, false, false, false, false, true));//with E
			add(new Beat(false, false, false, false, false, true, false, false));//with C#

			add(new Beat(false, false, false, false, true, false, false, false));//unison A
			add(new Beat(false, false, false, false, false, true, false, false));//with C#
			add(new Beat(false, false, false, false, false, false, false, true));//with E
			add(new Beat(false, false, false, false, false, true, false, false));//with C#
		}

		public void halfDM7()
		{
			add(new Beat(false, false, false, false, true, false, false, false));//unison A
			add(new Beat(false, false, false, false, false, true, false, false));//with C#
			add(new Beat(false, false, false, false, false, false, false, true));//with E
			add(new Beat(false, false, false, false, false, true, false, false));//with C#

			add(new Beat(false, false, false, false, true, false, false, false));//unison A
			add(new Beat(false, false, false, false, false, true, false, false));//with C#
			add(new Beat(false, false, false, false, false, false, false, true));//with E
			add(new Beat(false, false, false, false, false, true, false, false));//with C#
		}

		public void halfA()
		{
			add(new Beat(false, false, false, true, false, true, false, false));//unison A
			add(new Beat(false, false, false, false, false, false, true, false));//with C#
			add(new Beat(false, false, false, false, false, false, false, true));//with E
			add(new Beat(false, false, false, false, false, false, true, false));//with C#

			add(new Beat(false, false, false, false, false, true, false, false));//unison A
			add(new Beat(false, false, false, false, false, false, true, false));//with C#
			add(new Beat(false, false, false, false, false, false, false, true));//with E
			add(new Beat(false, false, false, false, false, false, true, false));//with C#
		}

		public void HalfD()
		{
			add(new Beat(true, false, false, false, true, false, false, false));//unison A
			add(new Beat(false, false, false, false, false, false, true, false));//with C#
			add(new Beat(false, false, false, false, false, false, false, true));//with E
			add(new Beat(false, false, false, false, false, false, true, false));//with C#

			add(new Beat(false, false, false, false, true, false, false, false));//unison A
			add(new Beat(false, false, false, false, false, false, true, false));//with C#
			add(new Beat(false, false, false, false, false, false, false, true));//with E
			add(new Beat(false, false, false, false, false, false, true, false));//with C#
		}
		public void fullDLMriff()
		{


			add(new Beat(false, false, false, true, false, false, false, false));
			add(new Beat(false, false, false, false, false, false, false, false));
			add(new Beat(false, false, false, false, false, false, false, true));
			add(new Beat(false, false, false, false, false, false, false, false));

			add(new Beat(false, false, false, false, false, false, false, false));
			add(new Beat(false, false, false, true, false, false, false, false));
			add(new Beat(false, false, false, false, false, false, false, true));
			add(new Beat(false, false, false, true, false, false, false, false));

			add(new Beat(false, false, false, false, true, false, false, false));
			add(new Beat(false, false, false, false, false, false, false, false));
			add(new Beat(false, false, false, false, false, false, true, false));
			add(new Beat(false, false, false, false, false, false, false, false));

			add(new Beat(false, false, false, false, false, false, false, false));
			add(new Beat(false, false, false, false, true, false, false, false));
			add(new Beat(false, false, false, false, false, false, true, false));
			add(new Beat(false, false, false, false, true, false, false, false));

			add(new Beat(false, false, false, true, false, false, false, false));
			add(new Beat(false, false, false, false, false, false, false, false));
			add(new Beat(false, false, false, false, false, false, false, true));
			add(new Beat(false, false, false, false, false, false, false, false));

			add(new Beat(false, false, false, false, false, false, false, false));
			add(new Beat(false, false, false, true, false, false, false, false));
			add(new Beat(false, false, false, false, false, false, false, true));
			add(new Beat(false, false, false, true, false, false, false, false));

			add(new Beat(false, true, false, false, false, false, false, false));
			add(new Beat(false, false, false, false, false, false, false, false));
			add(new Beat(false, false, false, false, false, true, false, false));
			add(new Beat(false, false, false, false, false, false, false, false));

			add(new Beat(false, false, false, false, false, false, false, false));
			add(new Beat(false, true, false, false, false, false, false, false));
			add(new Beat(false, false, false, false, false, true, false, false));
			add(new Beat(false, true, false, false, false, false, false, false));

		}
		public void MostDLMriff()
		{
			add(new Beat(false, false, false, false, false, false, false, false));

			add(new Beat(false, false, false, true, false, false, false, false));
			add(new Beat(false, false, false, false, false, false, false, false));
			add(new Beat(false, false, false, false, false, false, false, true));
			add(new Beat(false, false, false, false, false, false, false, false));

			add(new Beat(false, false, false, false, false, false, false, false));
			add(new Beat(false, false, false, true, false, false, false, false));
			add(new Beat(false, false, false, false, false, false, false, true));
			add(new Beat(false, false, false, true, false, false, false, false));

			add(new Beat(false, false, false, false, true, false, false, false));
			add(new Beat(false, false, false, false, false, false, false, false));
			add(new Beat(false, false, false, false, false, false, true, false));
			add(new Beat(false, false, false, false, false, false, false, false));

			add(new Beat(false, false, false, false, false, false, false, false));
			add(new Beat(false, false, false, false, true, false, false, false));
			add(new Beat(false, false, false, false, false, false, true, false));
			add(new Beat(false, false, false, false, true, false, false, false));

			add(new Beat(false, false, false, true, false, false, false, false));
			add(new Beat(false, false, false, false, false, false, false, false));
			add(new Beat(false, false, false, false, false, false, false, true));
			add(new Beat(false, false, false, false, false, false, false, false));

			add(new Beat(false, false, false, false, false, false, false, false));
			add(new Beat(false, false, false, true, false, false, false, false));
			add(new Beat(false, false, false, false, false, false, false, true));
			add(new Beat(false, false, false, true, false, false, false, false));

			add(new Beat(false, true, false, false, false, false, false, false));
			add(new Beat(false, false, false, false, false, false, false, false));
			add(new Beat(false, false, false, false, false, false, false, false));
			add(new Beat(false, false, false, false, false, false, false, false));

			add(new Beat(false, false, false, false, false, false, false, false));
			add(new Beat(false, false, false, false, false, false, false, false));
			add(new Beat(false, false, false, false, false, false, false, false));
			add(new Beat(false, false, false, false, false, false, false, false));

		}
		public void chorusRiff()
		{
			add(new Beat(false, false, false, true, false, false, false, false));
			add(new Beat(false, false, false, false, false, false, false, false));
			add(new Beat(false, false, false, false, false, false, false, true));
			add(new Beat(false, false, false, false, false, false, false, false));

			add(new Beat(false, false, false, false, false, false, false, false));
			add(new Beat(false, false, false, true, false, false, false, false));
			add(new Beat(false, false, false, false, false, false, false, true));
			add(new Beat(false, false, false, true, false, false, false, false));

			add(new Beat(false, false, false, false, true, false, false, false));
			add(new Beat(false, false, false, false, false, false, false, false));
			add(new Beat(false, false, false, false, false, false, true, false));
			add(new Beat(false, false, false, false, false, false, false, false));

			add(new Beat(false, false, false, false, false, false, false, false));
			add(new Beat(false, false, false, false, true, false, false, false));
			add(new Beat(false, false, false, false, false, false, true, false));
			add(new Beat(false, false, false, false, true, false, false, false));

			add(new Beat(false, false, false, false, false, true, false, false));
			add(new Beat(false, false, false, false, true, false, false, false));
			add(new Beat(false, false, false, false, false, false, true, false));
			add(new Beat(false, false, false, false, false, false, false, false));

			add(new Beat(false, false, false, false, false, false, false, false));
			add(new Beat(false, false, false, false, false, true, false, false));
			add(new Beat(false, false, false, false, false, false, true, false));
			add(new Beat(false, false, false, false, false, true, false, false));

			add(new Beat(false, false, false, true, false, false, false, false));
			add(new Beat(false, false, false, false, false, false, false, false));
			add(new Beat(false, false, false, false, false, false, false, true));
			add(new Beat(false, false, false, false, false, false, false, false));

			add(new Beat(false, false, false, false, false, false, false, false));
			add(new Beat(false, false, false, true, false, false, false, false));
			add(new Beat(false, false, false, false, false, false, false, true));
			add(new Beat(false, false, false, true, false, false, false, false));
		}
		public void transition()
		{
			add(new Beat(false, false, false, true, false, false, false, false));
			add(new Beat(false, false, false, false, false, false, false, false));
			add(new Beat(false, false, false, false, false, false, false, true));
			add(new Beat(false, false, false, false, false, false, false, false));

			add(new Beat(false, false, false, false, false, false, false, false));
			add(new Beat(false, false, false, true, false, false, false, false));
			add(new Beat(false, false, false, false, false, false, false, true));
			add(new Beat(false, false, false, true, false, false, false, false));


		}
	}
	/**
	 * Adds note data to each song, referencing the Song class's methods.
	 * Contains some of its own data required for each song.
	 */
	public class SongLibrary
	{
		private float SLOtempof=0.872f;
		private float DLMtempof=1f;
		Song DontLeaveMe;
		Song SomeoneLikeYou;

		public SongLibrary(int difficulty)
		{
			SomeoneLikeYou=new Song(difficulty,SLOtempof);
			SomeoneLikeYou.add(new Beat(false, false, false, false, false, false, false, false));

			SomeoneLikeYou.AMajorTriad();
			SomeoneLikeYou.AOverAb();
			SomeoneLikeYou.FSharpMinor();
			SomeoneLikeYou.DMajor();

			SomeoneLikeYou.AMajorTriad();
			SomeoneLikeYou.AOverAb();
			SomeoneLikeYou.FSharpMinor();
			SomeoneLikeYou.DMajor();

			SomeoneLikeYou.AMajorTriad();
			SomeoneLikeYou.AOverAb();
			SomeoneLikeYou.FSharpMinor();
			SomeoneLikeYou.DMajor();

			SomeoneLikeYou.AMajorTriad();
			SomeoneLikeYou.AOverAb();
			SomeoneLikeYou.FSharpMinor();
			SomeoneLikeYou.DMajor();

			SomeoneLikeYou.EMajor();
			SomeoneLikeYou.HalfFSharp();
			SomeoneLikeYou.DMajor();

			SomeoneLikeYou.EMajor();
			SomeoneLikeYou.HalfFSharp();
			SomeoneLikeYou.DMajor7();
			SomeoneLikeYou.halfDM7();

			SomeoneLikeYou.halfA();
			SomeoneLikeYou.EMajor();
			SomeoneLikeYou.HalfFSharp();
			SomeoneLikeYou.HalfD();

			SomeoneLikeYou.halfA();
			SomeoneLikeYou.EMajor();
			SomeoneLikeYou.HalfFSharp();
			SomeoneLikeYou.HalfD();

			SomeoneLikeYou.halfA();
			SomeoneLikeYou.EMajor();
			SomeoneLikeYou.HalfFSharp();
			SomeoneLikeYou.HalfD();

			SomeoneLikeYou.halfA();
			SomeoneLikeYou.EMajor();
			SomeoneLikeYou.HalfFSharp();
			SomeoneLikeYou.HalfD();

			DontLeaveMe = new Song(difficulty, DLMtempof);
			DontLeaveMe.add(new Beat(false, false, false, false, false, false, false, false));
			DontLeaveMe.fullDLMriff();//intro
			DontLeaveMe.MostDLMriff();

			DontLeaveMe.fullDLMriff();//verse 1
			DontLeaveMe.fullDLMriff();
			DontLeaveMe.fullDLMriff();
			DontLeaveMe.fullDLMriff();
			DontLeaveMe.chorusRiff();//chorus
			DontLeaveMe.chorusRiff();
			DontLeaveMe.transition();
			DontLeaveMe.fullDLMriff();//verse 2
			DontLeaveMe.fullDLMriff();
			DontLeaveMe.fullDLMriff();
			DontLeaveMe.fullDLMriff();
			DontLeaveMe.empty();
			DontLeaveMe.empty();
			DontLeaveMe.empty();
			DontLeaveMe.empty();
		}
		public Song getDLM()
		{
			return DontLeaveMe;
		}
		public Song getSLO()
		{
			return SomeoneLikeYou;
		}

	}

}