package pl.gda.pg.eti;

import java.io.*;

public class UnpackerInputStream extends FilterInputStream {

	private SixBitEnDec sixBitEnDec = new SixBitEnDec();
	private InputStream inputStream;

	public UnpackerInputStream(InputStream in) {
		super(in);
		inputStream = in;
	}

	public int read(byte b[]) throws IOException {
		byte bytes[] = new byte[(int) (b.length * 0.75)];
		int readBytes = inputStream.read(bytes);
		if (readBytes == -1)
			return -1;
		String decoded = sixBitEnDec.decode(bytes, SixBitEnDec.SIX_BIT);
		for (int i = 0; i < b.length; i++) {
			b[i] = decoded.getBytes()[i];
		}
		return b.length;
	}
}
