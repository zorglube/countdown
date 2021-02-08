/**
 * 
 */
package org.bcrm.countdown;

/**
 * @author zorglube
 *
 */
public class ClockCount {

	private int h;
	private String hh;
	private int m;
	private String mm;
	private int s;
	private String ss;
	private boolean end = false;

	public ClockCount(int hour, int minute, int second) {
		this.h = hour;
		this.m = minute;
		this.s = second;
		if (h == 0 && m == 0 && s == 0)
			end = true;
	}

	public synchronized String valueString() {
		hh = h < 10 ? String.format("0%d", h) : String.format("%d", h);
		mm = m < 10 ? String.format("0%d", m) : String.format("%d", m);
		ss = s < 10 ? String.format("0%d", s) : String.format("%d", s);
		return String.format("%s:%s:%s", hh, mm, ss);
	}

	public synchronized void decrement() {
		if (s == 0) {
			if (m == 0) {
				if (h == 0) {
					end = true;
				} else {
					h--;
					m = 59;
				}
			} else {
				m--;
				s = 59;
			}
		} else {
			s--;
		}
	}

	public boolean isFishied() {
		return end;
	}
}
