package nl.shadowlink.file_io;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;

/**
 * @author Kilian
 */
public class ByteReader {
	private byte[] stream;
	private int currentOffset;
	private boolean system = true;

	private int sysSize = 0;

	public ByteReader(byte[] stream, int startOffset) {
		this.stream = stream;
		this.currentOffset = startOffset;
	}

	public int ReadUInt32() {
		int i = 0;
		int len = 4;
		int cnt = 0;
		byte[] tmp = new byte[len];
		for (i = currentOffset; i < (currentOffset + len); i++) {
			tmp[cnt] = stream[i];
			cnt++;
		}
		long accum = 0;
		i = 0;
		for (int shiftBy = 0; shiftBy < 32; shiftBy += 8) {
			accum |= ((long) (tmp[i] & 0xff)) << shiftBy;
			i++;
		}
		currentOffset += 4;
		return (int) accum;
	}

	public int ReadOffset() {
		int value;

		int offset = ReadUInt32();

		if (offset == 0) {
			value = -1;
		} else {
			if (offset >> 28 != 5) {
				value = -1;
			} else {
				value = offset & 0x0fffffff;
			}
		}
		return value;
	}

	public Vector4D readVector() {
		float x = readFloat();
		float y = readFloat();
		float z = readFloat();
		float w = readFloat();
		Vector4D vec = new Vector4D(x, y, z, w);
		return vec;
	}

	public float readFloat() {
		int i = 0;
		int len = 4;
		int cnt = 0;
		int accum = 0;
		i = 0;
		for (int shiftBy = 0; shiftBy < 32; shiftBy += 8) {
			accum |= ((long) (stream[currentOffset + i] & 0xff)) << shiftBy;
			i++;
		}
		currentOffset += 4;
		return Float.intBitsToFloat(accum);
	}

	public int ReadUInt16() {
		int low = stream[currentOffset] & 0xff;
		int high = stream[currentOffset + 1] & 0xff;
		currentOffset += 2;
		return (int) (high << 8 | low);
	}

	public short ReadInt16() {
		short ret = (short) ((((short) stream[currentOffset + 1]) << 8) | (((short) stream[currentOffset]) & 0xff));
		currentOffset += 2;
		return ret;
	}

	public int ReadDataOffset() {
		int value;
		int offset = ReadUInt32();

		if (offset == 0) {
			value = 0;
		} else {
			if (offset >> 28 != 6) {

			}
			value = offset & 0x0fffffff;
		}

		return value;
	}

	/**
	 * Returns a String from the DataInputStream with the given size till 0
	 *
	 * @param data_in
	 * @param size
	 * @return a String of the size size
	 */
	public String ReadNullTerminatedString(int size) {
		String woord = "";
		boolean gotNull = false;
		for (int i = 0; i < size; i++) {
			byte b = ReadByte();
			if (!gotNull) {
				if (b != 0)
					woord += (char) b;
				else
					gotNull = true;
			}
		}
		return woord;
	}

	public String ReadNullTerminatedString() {
		String sb = "";

		char c = (char) stream[currentOffset];
		while (c != 0) {
			// file_io.Message.displayMsgHigh("String: " + sb + " c: " + c);
			sb = sb + c;
			currentOffset++;
			c = (char) stream[currentOffset];
		}

		return sb;
	}

	public String ReadString(int length) {
		String sb = "";

		for (int i = 0; i < length; i++) {
			char c = (char) stream[currentOffset];
			sb += c;
			currentOffset++;
		}

		return sb;
	}

	public byte[] toArray(int bytes) {
		byte[] arr = new byte[bytes];
		for (int i = 0; i < bytes; i++) {
			arr[i] = stream[currentOffset];
			currentOffset++;
		}
		return arr;
	}

	public byte[] toArray() {
		return this.stream;
	}

	public byte[] toArray(int start, int end) {
		int retSize = end - start;
		Message.displayMsgLow("Size: " + retSize);
		byte[] retStream = new byte[retSize];
		this.setCurrentOffset(start);
		for (int i = 0; i < retSize; i++) {
			retStream[i] = stream[currentOffset];
			currentOffset++;
		}
		return retStream;
	}

	public byte ReadByte() {
		currentOffset++;
		return stream[currentOffset - 1];
	}

	public int getCurrentOffset() {
		return currentOffset;
	}

	public void setCurrentOffset(int offset) {
		this.currentOffset = offset;
		if (!system) {
			currentOffset += sysSize;
		}
	}

	public void setSysSize(int size) {
		this.sysSize = size;
	}

	public void setSystemMemory(boolean system) {
		this.system = system;
	}

	public ByteBuffer getByteBuffer(int size) {
		byte[] buffer = new byte[size];
		ByteBuffer bbuf = ByteBuffer.allocate(size);
		for (int i = 0; i < size; i++) {
			buffer[i] = ReadByte();
		}
		bbuf.put(buffer);
		bbuf.rewind();
		return bbuf;
	}

	public ByteArrayInputStream getInputStream() {
		return new ByteArrayInputStream(stream, currentOffset, stream.length - currentOffset);
	}

	public void skipBytes(int bytes) {
		currentOffset += bytes;
	}

	/**
	 * returns if a certain flag is on
	 *
	 * @param flags
	 * @param flag
	 * @return if a flag has been set
	 */
	public boolean hasFlag(int flags, int flag) {
		int test = flags & flag;
		if (test == flag)
			return true;
		else
			return false;
	}

	public int moreToRead() {
		return (stream.length - currentOffset);
	}

	public long unsignedInt() {
		int i = ReadUInt32();
		long l = i & 0xffffffffL;

		return l;
	}

}
