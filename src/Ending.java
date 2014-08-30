import java.util.ArrayList;
import java.awt.*;

import javax.swing.*;

/**
 * Ending screen.  Lists high scores from this iteration of Piano Hero.
 *
 */

public class Ending extends JPanel{

	ArrayList scores; //scores recorded
	public Ending(ArrayList scores)
	{
		this.scores=scores;
		setLayout(new GridLayout(4,4));
		add(new JLabel("High scores:"));
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



