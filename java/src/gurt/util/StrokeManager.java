package gurt.util;

import java.awt.BasicStroke;
import java.util.ArrayList;

/**
 * Strokes are not a serializable object, so any object that uses a stroke and
 * must be serializable cannot physically include a reference to a Stroke
 * object. So, in order to facilitate the creation of serializable objects with
 * references to stroke objects, this StrokeManager manages it's own references,
 * through integer ID numbers.
 * 
 * @author smcbeth
 * 
 */
public class StrokeManager {
	private static ArrayList<BasicStroke> strokes = new ArrayList<BasicStroke>();

	/**
	 * Creates a new BasicStroke in the manager and returns the index of that
	 * object in the collection of Strokes. If a similar Stroke already exists,
	 * it is reused.
	 * 
	 * @param lineWidth
	 *            the width of the stroke line
	 * @param cap
	 * @see BasicStroke.CAP_* values
	 * @param join
	 * @see BasicStroke.JOIN_* values
	 * @return the index of the created (or reused) BasicStroke object
	 */
	public static int createStroke(float lineWidth, int cap, int join) {
		int index = -1;
		for (int i = 0; i < strokes.size(); ++i) {
			BasicStroke s = strokes.get(i);
			if (s.getLineJoin() == join && s.getEndCap() == cap
					&& Math.abs(s.getLineWidth() - lineWidth) < 0.001) {
				index = i;
			}
		}
		if (index == -1) {

			index = strokes.size();
			BasicStroke stroke = new BasicStroke(lineWidth, cap, join);
			strokes.add(stroke);
		}
		return index;
	}

	/**
	 * retrieve a BasicStroke object from the collection
	 * 
	 * @param index
	 *            the object ID to retrieve
	 * @return a BasicStroke object
	 */
	public static BasicStroke getStroke(int index) {
		return strokes.get(index);
	}
}
