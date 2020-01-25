package org.usfirst.frc3620.misc;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.*;

public class DPad {
	private GenericHID genericHID;
	private int pov;

	public DPad(GenericHID genericHID, int pov) {
		super();
		this.genericHID = genericHID;
		this.pov = pov;
	}

	class AngleLimits {
		private int lo, hi;
		private boolean loNarrowed = false, hiNarrowed = false;
		private boolean wrapped = false;

		public AngleLimits(int lo, int hi) {
			super();
			this.lo = lo;
			this.hi = hi;

			wrapped = (lo > hi);
		}

		boolean inLimit(int angle) {
			if (wrapped) {
				return angle >= lo || angle <= hi;
			} else {
				return angle >= lo && angle <= hi;
			}
		}

		void narrowHi() {
			if (!hiNarrowed)
				hi -= 1;
			hiNarrowed = true;
		}

		void narrowLo() {
			if (!loNarrowed)
				lo += 1;
			loNarrowed = true;
		}
	}

	AngleLimits alRight = new AngleLimits(45, 135);
	AngleLimits alDown = new AngleLimits(135, 225);
	AngleLimits alLeft = new AngleLimits(225, 315);
	AngleLimits alUp = new AngleLimits(315, 45);

	class AlTrigger extends Button {
		private AngleLimits angleLimits;

		public AlTrigger(AngleLimits angleLimits) {
			super();
			this.angleLimits = angleLimits;
		}

		@Override
		public boolean get() {
			int angle = genericHID.getPOV(pov);
			if (angle < 0)
				return false;
			return angleLimits.inLimit(angle);
		}
	}

	public Button up() {
        alLeft.narrowHi();
        alRight.narrowLo();
		return new AlTrigger(alUp);
	}

	public Button right() {
        alUp.narrowHi();
        alDown.narrowLo();
		return new AlTrigger(alRight);
	}

	public Button down() {
        alRight.narrowHi();
        alLeft.narrowLo();
		return new AlTrigger(alDown);
	}

	public Button left() {
        alDown.narrowHi();
        alUp.narrowLo();
		return new AlTrigger(alLeft);
	}
	
	public void horizontalCommand (Command command) {
		right().whileHeld(command);
		left().cancelWhenPressed(command);
	}

	public void verticalCommand (Command command) {
		up().whileHeld(command);
		down().cancelWhenPressed(command);
	}

}