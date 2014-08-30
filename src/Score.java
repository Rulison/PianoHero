/**
 * Defines an individual score, including the name and score.
 *
 */

public class Score
{
	String name;
	int score;
	String song;
	public Score(String name, int score, String song)
	{
		this.name=name;
		this.score=score;
		this.song = song;
	}
	public String toString()
	{
		return name+": "+score+" for "+song;
	}
}