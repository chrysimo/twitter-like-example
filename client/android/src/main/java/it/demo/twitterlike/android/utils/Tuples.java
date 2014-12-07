package it.demo.twitterlike.android.utils;

import java.io.IOException;
import java.io.Serializable;

/**
 * Type safe, inmutable, tuple classes can be serialized with xmlencoder (>=1.6)
 * or objectoutputstream.
 * 
 * Whenever you need to create public api, it is best not to use these classes
 * since the given names (first, second, etc) are not descriptive.
 * 
 * @author i30817
 */
public final class Tuples {

	private Tuples() {
	}

	public static <X> T1<X> createSingleton(X target) {
		return new T1<X>(target);
	}

	public static <X, Y> T2<X, Y> createPair(X target1, Y target2) {
		return new T2<X, Y>(target1, target2);
	}

	public static <X, Y, Z> T3<X, Y, Z> createTriple(X target1, Y target2,
			Z target3) {
		return new T3<X, Y, Z>(target1, target2, target3);
	}

	public static <W, X, Y, Z> T4<W, X, Y, Z> createQuadruple(W target1,
			X target2, Y target3, Z target4) {
		return new T4<W, X, Y, Z>(target1, target2, target3, target4);
	}

	public static final class T1<X> implements Serializable {

		private static final long serialVersionUID = 362498860763181265L;
		private X first;

		// @java.beans.ConstructorProperties({"first"})
		private T1(X first) {
			this.first = first;
		}

		public X getFirst() {
			return first;
		}

		@Override
		public String toString() {
			return "(" + first + ")";
		}

		@SuppressWarnings(value = "unchecked")
		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final T1<X> other = (T1<X>) obj;
			if (this.first != other.first
					&& (this.first == null || !this.first.equals(other.first))) {
				return false;
			}
			return true;
		}

		@Override
		public int hashCode() {
			int hash = 5;
			hash = 97 * hash + (this.first != null ? this.first.hashCode() : 0);
			return hash;
		}

		private void writeObject(java.io.ObjectOutputStream out)
				throws IOException {
			out.writeObject(first);
		}

		@SuppressWarnings(value = "unchecked")
		private void readObject(java.io.ObjectInputStream in)
				throws IOException, ClassNotFoundException {
			first = (X) in.readObject();
		}
	}

	public static final class T2<X, Y> implements Serializable {

		private static final long serialVersionUID = 463498820763161265L;
		private X first;
		private Y second;

		// @java.beans.ConstructorProperties({"first","second"})
		private T2(X first, Y second) {
			this.first = first;
			this.second = second;
		}

		public X getFirst() {
			return first;
		}

		public Y getSecond() {
			return second;
		}

		@Override
		public String toString() {
			return "(" + first + ", " + second + ")";
		}

		@SuppressWarnings(value = "unchecked")
		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final T2<X, Y> other = (T2<X, Y>) obj;
			if (this.first != other.first
					&& (this.first == null || !this.first.equals(other.first))) {
				return false;
			}
			if (this.second != other.second
					&& (this.second == null || !this.second
							.equals(other.second))) {
				return false;
			}
			return true;
		}

		@Override
		public int hashCode() {
			int hash = 3;
			hash = 67 * hash + (this.first != null ? this.first.hashCode() : 0);
			hash = 67 * hash
					+ (this.second != null ? this.second.hashCode() : 0);
			return hash;
		}

		private void writeObject(java.io.ObjectOutputStream out)
				throws IOException {
			out.writeObject(first);
			out.writeObject(second);
		}

		@SuppressWarnings(value = "unchecked")
		private void readObject(java.io.ObjectInputStream in)
				throws IOException, ClassNotFoundException {
			first = (X) in.readObject();
			second = (Y) in.readObject();
		}
	}

	public static final class T3<X, Y, Z> implements Serializable {

		private static final long serialVersionUID = 162498830763481765L;
		private X first;
		private Y second;
		private Z third;

		// @java.beans.ConstructorProperties({"first","second","third"})
		private T3(X first, Y second, Z third) {
			this.first = first;
			this.second = second;
			this.third = third;
		}

		public X getFirst() {
			return first;
		}

		public Y getSecond() {
			return second;
		}

		public Z getThird() {
			return third;
		}

		@Override
		public String toString() {
			return "(" + first + ", " + second + ", " + third + ")";
		}

		@SuppressWarnings(value = "unchecked")
		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final T3<X, Y, Z> other = (T3<X, Y, Z>) obj;
			if (this.first != other.first
					&& (this.first == null || !this.first.equals(other.first))) {
				return false;
			}
			if (this.second != other.second
					&& (this.second == null || !this.second
							.equals(other.second))) {
				return false;
			}
			if (this.third != other.third
					&& (this.third == null || !this.third.equals(other.third))) {
				return false;
			}
			return true;
		}

		@Override
		public int hashCode() {
			int hash = 7;
			hash = 71 * hash + (this.first != null ? this.first.hashCode() : 0);
			hash = 71 * hash
					+ (this.second != null ? this.second.hashCode() : 0);
			hash = 71 * hash + (this.third != null ? this.third.hashCode() : 0);
			return hash;
		}

		private void writeObject(java.io.ObjectOutputStream out)
				throws IOException {
			out.writeObject(first);
			out.writeObject(second);
			out.writeObject(third);
		}

		@SuppressWarnings(value = "unchecked")
		private void readObject(java.io.ObjectInputStream in)
				throws IOException, ClassNotFoundException {
			first = (X) in.readObject();
			second = (Y) in.readObject();
			third = (Z) in.readObject();
		}
	}

	public static final class T4<W, X, Y, Z> implements Serializable {

		private static final long serialVersionUID = 162498830763581765L;
		private W first;
		private X second;
		private Y third;
		private Z fourth;

		// @java.beans.ConstructorProperties({"first","second","third","fourth"})
		private T4(W first, X second, Y third, Z fourth) {
			this.first = first;
			this.second = second;
			this.third = third;
			this.fourth = fourth;
		}

		public W getFirst() {
			return first;
		}

		public X getSecond() {
			return second;
		}

		public Y getThird() {
			return third;
		}

		public Z getFourth() {
			return fourth;
		}

		@Override
		public String toString() {
			return "(" + first + ", " + second + ", " + third + ", " + fourth
					+ ")";
		}

		@SuppressWarnings(value = "unchecked")
		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final T4<W, X, Y, Z> other = (T4<W, X, Y, Z>) obj;
			if (this.first != other.first
					&& (this.first == null || !this.first.equals(other.first))) {
				return false;
			}
			if (this.second != other.second
					&& (this.second == null || !this.second
							.equals(other.second))) {
				return false;
			}
			if (this.third != other.third
					&& (this.third == null || !this.third.equals(other.third))) {
				return false;
			}
			if (this.fourth != other.fourth
					&& (this.fourth == null || !this.fourth
							.equals(other.fourth))) {
				return false;
			}
			return true;
		}

		@Override
		public int hashCode() {
			int hash = 5;
			hash = 29 * hash + (this.first != null ? this.first.hashCode() : 0);
			hash = 29 * hash
					+ (this.second != null ? this.second.hashCode() : 0);
			hash = 29 * hash + (this.third != null ? this.third.hashCode() : 0);
			hash = 29 * hash
					+ (this.fourth != null ? this.fourth.hashCode() : 0);
			return hash;
		}

		private void writeObject(java.io.ObjectOutputStream out)
				throws IOException {
			out.writeObject(first);
			out.writeObject(second);
			out.writeObject(third);
			out.writeObject(fourth);
		}

		@SuppressWarnings(value = "unchecked")
		private void readObject(java.io.ObjectInputStream in)
				throws IOException, ClassNotFoundException {
			first = (W) in.readObject();
			second = (X) in.readObject();
			third = (Y) in.readObject();
			fourth = (Z) in.readObject();
		}
	}
}
