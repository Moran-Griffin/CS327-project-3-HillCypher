public class MoranGriffinRogersLukeHillCypher{

    //XGCD from project 1
    public int xgcd (int inE, int inZ) {
		int[] d = new int[3];
		d[0] = inZ;
		d[1] = inE;
		int[] s = new int[3];
		int q;
		s[0] = 1;
		s[1] = 0;
		int[] t = new int[3];
		t[0] = 0;
		t[1] = 1;
		
		while(d[1] != 0) {
			q = d[0] / d[1];
			d[2] = d[0] % d[1];
			d[0] = d[1];
			d[1] = d[2];
			
			t[2] = t[0] - (t[1] * q);
			t[0] = t[1];
			t[1] = t[2];

			s[2] = s[0] - (s[1] * q);
			s[0] = s[1];
			s[1] = s[2];
			

		}
		if (t[0] < 0) {
			return t[0] + inZ;
		}
		return t[0];
	}

    //Find the inverse of the matrix
    int[][] findDecryptionKey(int[][] encryptionKey){
        return null;
    }

    //Hill cypher encryption
    int[] encrypt(int[] plaintext, int[][] encryptionKey){
        return null;
    }

    //Hill cypher decryption
    int[] decrypt(int[] plaintext, int[][] encryptionKey){
        return null;
    }

     public static void main(String[] args) {
        
    }
}