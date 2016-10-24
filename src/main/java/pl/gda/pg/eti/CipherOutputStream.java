package pl.gda.pg.eti;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CipherOutputStream extends FilterOutputStream {

	private OutputStream outputStream;
	private byte[] key;
	private int[] S = new int[256];
	private int[] K = new int[256];
	private int pseudoNumberI;
	private int pseudoNumberJ;

	public CipherOutputStream(OutputStream out) {
		super(out);
		outputStream = out;
		this.key = Utils.getDefaultKey();
		initAll();
	}

	public CipherOutputStream(OutputStream out, byte[] key) {
		super(out);
		this.key = key;
		initAll();
	}

	private void initAll() {
		pseudoNumberI = 0;
		pseudoNumberJ = 0;
		initS();
		initK();
		scramble();
	}

	private void initS() {
		for (int i = 0; i < 256; i++) {
			S[i] = i;
		}
	}

	private void initK() {
		int j=0;
		for (int i = 0; i < 256; i++) {
			if(j == key.length) {
				j=0;
			}
			K[i] = key[j++] & 0xFF;
		}
	}

	private void scramble() {
		int j = 0;
		for (int i = 0; i < 256; i++) {
			j = (j + S[i] + K[i]) % 256;
			int t = S[i];
			S[i] = S[j];
			S[j] = t;
		}
	}

	private int getPseudoNumber() {
		pseudoNumberI = (pseudoNumberI + 1) % 256;
		pseudoNumberJ = (pseudoNumberJ + S[pseudoNumberI]) % 256;
		Utils.swap(S[pseudoNumberI],S[pseudoNumberJ]);
		return (S[(S[pseudoNumberI] + S[pseudoNumberJ]) % 256]) ;
	}

	@Override
	public void write(byte b[], int off, int len) throws IOException {
		if (b == null) {
			throw new NullPointerException();
		} else if ((off < 0) || (off > b.length) || (len < 0) ||
				((off + len) > b.length) || ((off + len) < 0)) {
			throw new IndexOutOfBoundsException();
		} else if (len == 0) {
			return;
		}
		for (int i = 0 ; i < len ; i++) {
			outputStream.write((byte)(b[off + i] ^ (byte)getPseudoNumber() ));
		}
	}
}
