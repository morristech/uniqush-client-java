package org.uniqush.diffiehellman;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Random;


public class DHGroup {
	protected BigInteger modulus;
	protected BigInteger generator;

	public DHGroup(BigInteger p, BigInteger g) {
		this.modulus = p;
		this.generator = g;
	}
	
	static public DHGroup getGroup(int groupid) throws NoSuchAlgorithmException {
		switch (groupid) {
		case 0:
		case 14:
			BigInteger p = new BigInteger("FFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80DC1CD129024E088A67CC74020BBEA63B139B22514A08798E3404DDEF9519B3CD3A431B302B0A6DF25F14374FE1356D6D51C245E485B576625E7EC6F44C42E9A637ED6B0BFF5CB6F406B7EDEE386BFB5A899FA5AE9F24117C4B1FE649286651ECE45B3DC2007CB8A163BF0598DA48361C55D39A69163FA8FD24CF5F83655D23DCA3AD961C62F356208552BB9ED529077096966D670C354E4ABC9804F1746C08CA18217C32905E462E36CE3BE39E772C180E86039B2783A2EC07A28FB5C55DF06F4C52C9DE2BCBF6955817183995497CEA956AE515D2261898FA051015728E5A8AACAA68FFFFFFFFFFFFFFFF", 16);
			BigInteger g = new BigInteger("2");
			DHGroup ret = new DHGroup(p, g);
			return ret;
		}
		throw new NoSuchAlgorithmException();
	}
	
	public DHPrivateKey generatePrivateKey(Random random) {
        int bits = modulus.bitLength();
        BigInteger max = modulus.subtract(BigInteger.ONE);
        DHPrivateKey privkey = null;
        while (true) {
            BigInteger pkey = new BigInteger(bits, random);
            if (pkey.compareTo(max) >= 0) { //too large
                continue;
            }
            else if (pkey.compareTo(BigInteger.ONE) <= 0) {//too small
                continue;
            }
            privkey = new DHPrivateKey(pkey, this);
            break;
        }
        return privkey;
	}
	
	public byte[] computeKey(DHPublicKey pub, DHPrivateKey priv) {
		BigInteger k = pub.y.modPow(priv.x, this.modulus);
		byte[] b = k.toByteArray();
		if (b[0] == 0) {
		    byte[] tmp = new byte[b.length - 1];
		    System.arraycopy(b, 1, tmp, 0, tmp.length);
		    b = tmp;
		}
		byte[] ret = new byte[(modulus.bitLength() + 7)/8];
		
		int nrZeros = ret.length - b.length;
		for (int i = 0; i < nrZeros; i++) {
			ret[i] = 0;
		}
		for (int i = 0; i < b.length; i++) {
			ret[i + nrZeros] = b[i];
		}
		return ret;
	}

}
