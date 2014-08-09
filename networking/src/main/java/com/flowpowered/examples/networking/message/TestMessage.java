package com.flowpowered.examples.networking.message;

import com.flowpowered.networking.Message;

public class TestMessage implements Message {
	private final int number;

	public TestMessage(int number) {
		this.number = number;
	}

	public boolean isAsync() {
		return true;
	}

	public int getNumber() {
		return number;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 17 * hash + this.number;
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TestMessage other = (TestMessage) obj;
		if (this.number != other.number)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TestMessage{" + "number=" + number + '}';
	}
}
