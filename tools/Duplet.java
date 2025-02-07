package tools;
/* utility class the implements a pair of <long>

*/
import java.io.IOException;
import java.io.Serializable;

public final class Duplet implements Serializable {
	private static final long serialVersionUID = 1L;
	private long a, b;

	public Duplet(long a, long b) {// Default constructor 
		this.a = a;
		this.b = b;
	}

	public int bitCount() {
		return java.lang.Long.bitCount(a) + java.lang.Long.bitCount(b);
	}

	public long get_a() {
		return a;
	}
	public long get_b() {
		return b;
	}

	@Override
	public boolean equals(Object anObject) {
		if (this == anObject)
			return true;
		if (anObject instanceof Duplet) {
			Duplet anotherDuplet = (Duplet)anObject;
			return (a == anotherDuplet.a) && (b == anotherDuplet.b);
		}
		return false;
	}
	@Override
	public int hashCode() {
		return (int)(a ^ (a >>> 32) ^ b ^ (b >>> 32));
	}

	 private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		 out.writeLong(a);
		 out.writeLong(b);		 
	 }
	 private void readObject(java.io.ObjectInputStream in) throws IOException {
		a = in.readLong();
		b = in.readLong();
	 }
}
