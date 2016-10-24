package pl.gda.pg.eti;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CipherInputStream extends FilterInputStream {

	private InputStream inputStream;
	private byte[] key;
	private int[] S = new int[256];
	private int[] K = new int[256];
	private int pseudoNumberI;
	private int pseudoNumberJ;

	public CipherInputStream(InputStream in) {
		super(in);
		this.inputStream = in;
		this.key = Utils.getDefaultKey();
		initAll();
	}

	public CipherInputStream(InputStream out, byte[] key) {
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

	public int read(byte b[]) throws IOException {
		int readBytes= inputStream.read(b);
		for (int i = 0; i < b.length; i++) {
			b[i] = (byte)(b[i] ^ getPseudoNumber());
		}
		return readBytes;
	}


}
