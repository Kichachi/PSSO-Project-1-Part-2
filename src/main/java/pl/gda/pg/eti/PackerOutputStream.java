package pl.gda.pg.eti;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class PackerOutputStream extends FilterOutputStream {

	private SixBitEnDec sixBitEnDec = new SixBitEnDec();
	private OutputStream outputStream;

	public PackerOutputStream(OutputStream out) {
		super(out);
		outputStream = out;
	}

	@Override
	public void write(byte b[], int off, int len) throws IOException {
		if (b == null) {
			throw new NullPointerException();
		} else if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) > b.length) || ((off + len) < 0)) {
			throw new IndexOutOfBoundsException();
		} else if (len == 0) {
			return;
		}
		b = sixBitEnDec.encode(new String(b, StandardCharsets.UTF_8), SixBitEnDec.SIX_BIT);
		len = b.length;
		for (int i = 0; i < len; i++) {
			outputStream.write((byte) b[off + i]);
		}
	}
}

