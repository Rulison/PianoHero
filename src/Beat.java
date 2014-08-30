import java.awt.Color;
import java.util.ArrayList;
/**
 * Represents a horizontal line across the screen in SongPlayer
 *
 */

public class Beat 
{
	//if true, note should be painted
	boolean a;
	boolean s;
	boolean d;
	boolean f;
	boolean h;
	boolean j;
	boolean k;
	boolean l;
	Color color;
	public Beat(boolean a, boolean s, boolean d, boolean f, boolean h, boolean j, boolean k, boolean l)
	{
		this.a=a;
		this.s=s;
		this.d=d;
		this.f=f;
		this.h=h;
		this.j=j;
		this.k=k;
		this.l=l;
		color=Color.red;
	}

}
