import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.awt.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class PianoWindow 
{
	public static void main(String[] args) 
	{
		JFrame window=new JFrame("Sing us a song, you're the Piano Hero");
		window.setContentPane(new MenuScreen());
		window.setLocation(100,75);
		window.setSize(22*40,22*20);
		window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		window.setVisible(true);
	}
}
