/**
 * Defines an individual score, including the name and score.
 *
 */

public class Score
{
	String name;
	int score;
	public Score(String name, int score)
	{
		this.name=name;
		this.score=score;
	}
	public String toString()
	{
		return name+": "+score;
	}
}