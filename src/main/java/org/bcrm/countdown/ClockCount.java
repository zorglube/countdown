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
	private int m;
	private int s;
	private boolean end = false;

	public ClockCount(int hour, int minute, int second) {
		this.h = hour;
		this.m = minute;
		this.s = second;
		if (h == 0 && m == 0 && s == 0)
			end = true;
	}

	synchronized String valueString() {
		String hh = String.format(h < 10 ? "0%d" : "%d", h);
		String mm = String.format(m < 10 ? "0%d" : "%d", m);
		String ss = String.format(s < 10 ? "0%d" : "%d", s);
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
