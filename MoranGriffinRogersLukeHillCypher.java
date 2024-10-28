import java.util.HashMap;
import java.util.ArrayList;

public class MoranGriffinRogersLukeHillCypher{
	public static HashMap<Integer, Character> letterMapSetup(){
		HashMap<Integer, Character> map = new HashMap<>();
		for (int i = 0; i < 26; i++) {
            map.put(i, (char) ('A' + i));
        }
		return map;
	}

	public static HashMap<Character, Integer> numberMapSetup(){
		HashMap<Character, Integer> map = new HashMap<>();
		for (int i = 0; i < 26; i++) {
            map.put((char) ('A' + i), i);
        }
		return map;
	}

    //XGCD from project 1
    public static int xgcd (int inE, int inZ) {
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
    public static int[][] findDecryptionKey(int[][] encryptionKey){
		int determinate = encryptionKey[0][0] * encryptionKey[1][1] - encryptionKey[1][0] * encryptionKey[0][1];
		int update = xgcd(determinate, 26);

		int[][] inverse = new int[2][2];
		inverse[0][0] = (encryptionKey[1][1] * update) % 26;
		inverse[0][1] = (-1 * encryptionKey[0][1] * update) % 26;
		inverse[1][0] = (-1 * encryptionKey[1][0] * update) % 26;
		inverse[1][1] = (encryptionKey[0][0] * update) % 26;

        return inverse;
    }

    //Hill cypher encryption
    public static int[] encrypt(int[] plaintext, int[][] encryptionKey){
		int[] cur = new int[2];
		boolean needZ = false;

		//check for extra space
		if(plaintext.length % 2 != 0){
			needZ = true;
		}
		int length = plaintext.length;

		//fill space with z
		if(needZ){
			int[] cpy = new int[length + 1];
			for(int i = 0; i < length; i++){
				cpy[i] = plaintext[i];
			}
			cpy[length] = 25;
			length += 1;
			plaintext = cpy;
		}

		//encrypt plaintext
		int[] encrypted = new int[length];
		for(int i = 0; i < length; i += 2){
			cur[0] = plaintext[i];
			cur[1] = plaintext[i + 1];
			encrypted[i] = (encryptionKey[0][0] * cur[0] + encryptionKey[0][1] * cur[1]) % 26;
			encrypted[i + 1] = (encryptionKey[1][0] * cur[0] + encryptionKey[1][1] * cur[1]) % 26;
		}
        return encrypted;
    }

    //Hill cypher decryption
    
	
    public static int[] decrypt(int[] ciphertext, int[][] decryptionKey) {

		boolean needZ = false;

		//check for extra space
		if(ciphertext.length % 2 != 0){
			needZ = true;
		}
        int mod = 26;
		int[] plainText = new int[needZ ? ciphertext.length + 1 : ciphertext.length];
		ArrayList<int[]> pairs = new ArrayList<>();
		for (int i = 0; i < ciphertext.length - 1; i += 2) {
			int[] pair = {ciphertext[i], ciphertext[i + 1]};
			pairs.add(pair);
		}
		if (needZ) {
			plainText[-1] = 25;
		}
		int i = 0;
		for (int[] encPair : pairs) {
			int[] pair = matrixMult(decryptionKey, encPair);
			if (pair[0] < 0) {
				plainText[i++] = positiveModulo(pair[0], mod);
			}
			plainText[i++] = positiveModulo(pair[1], mod);

		}


		return plainText;
	}

	private static int[] matrixMult(int[][] matrix2x2, int[] matrix1x2) {
        // Result will be a 1x2 matrix
        int[] result = new int[2];
        
        // Perform the multiplication
        result[0] = matrix2x2[0][0] * matrix1x2[0] + matrix2x2[0][1] * matrix1x2[1];
        result[1] = matrix2x2[1][0] * matrix1x2[0] + matrix2x2[1][1] * matrix1x2[1];
		
        return result;
	}
	public static int positiveModulo(int a, int mod) {
        int result = a % mod;
        if (result < 0) {
            result += mod;
        }
        return result;
    }

	

	//small personal test
	public static void eTest0(){
		final HashMap<Integer, Character> map = letterMapSetup();
        int[][] a = {{8,5},{3,7}};
		int[] run = {3,20,10,4};
		int[] e = encrypt(run, a);
		for(int i = 0; i < e.length; i++){
			System.out.print(map.get(e[i]));
		}
		System.out.println();
		
	}
	
	//required test
	public static void eTest1(){
		final HashMap<Integer, Character> chrMap = letterMapSetup();
		final HashMap<Character, Integer> numMap = numberMapSetup();
        int[][] a = {{16,7},{9,14}};
		String str = "JMUCSISCOOL";

		int[] run = new int[str.length()];
		for(int i = 0; i < str.length(); i++){
			run[i] = numMap.get(str.charAt(i));
		}		
		int[] e = encrypt(run, a);
		int[][] key = findDecryptionKey(a);
		System.out.println("Decryption key: " +key[0][0] + " " + key[0][1] + " " + key[1][0] + " " + key[1][1]);

		for(int i = 0; i < e.length; i++){
			System.out.print(chrMap.get(e[i]));
		}
		System.out.println();
		int[] d = decrypt(e, findDecryptionKey(a));
		for(int i = 0; i < d.length; i++){
			System.out.print(chrMap.get(d[i]));
		}
		System.out.println();
		
	}

     public static void main(String[] args) {
		//eTest0();
		eTest1();
		
    }
}