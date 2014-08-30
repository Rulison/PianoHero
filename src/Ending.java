import java.util.ArrayList;
import java.awt.*;

import javax.swing.*;

/**
 * Ending screen
 *
 */

public class Ending extends JPanel{

	//scores recorded
	ArrayList scores;
	public Ending(ArrayList scores)
	{
		this.scores=scores;
		setLayout(new GridLayout(4,4));
		add(new JLabel("Send Comments to the Creator!"));//I won't read your comments.
		add(new JTextField());
		String stringScores="";
		for(int i=0;i<scores.size();i++)
		{
			stringScores+=scores.get(i)+"\n";
		}
		add(new JTextArea(stringScores));

	}
	public ArrayList getScores()
	{
		return scores;
	}
	public void addScore(Score score)
	{
		scores.add(score);
	}
}



