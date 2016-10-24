package pl.gda.pg.eti;

public class SixBitEnDec {
    final static public int SIX_BIT = 6;
    final static public int FIVE_BIT = 5;
    public SixBitEnDec() {
    }
	byte[] encode(String txt, int bit){
		int length = txt.length();
		float tmpRet1=0,tmpRet2=0;
		if(bit==6){
			tmpRet1=3.0f;
			tmpRet2=4.0f;
		}else if(bit==5){
			tmpRet1=5.0f;
			tmpRet2=8.0f;
		}
		byte encoded[]=new byte[(int)(tmpRet1*Math.ceil(length/tmpRet2))];
		char str[]=new char[length];
		txt.getChars(0,length,str,0);
		int chaVal = 0;
		String temp;
		String strBinary = new String("");
		for (int i = 0;i<length; i++){
			temp = Integer.toBinaryString(toValue(str[i]));
			while(temp.length()%bit != 0){
				temp="0"+temp;
			}
			strBinary=strBinary+temp;
		}
		while(strBinary.length()%8 != 0){
			strBinary=strBinary+"0";
		}
		Integer tempInt =new Integer(0);
		for(int i=0 ; i<strBinary.length();i=i+8){
			tempInt = tempInt.valueOf(strBinary.substring(i,i+8),2);
			encoded[i/8]=tempInt.byteValue();
		}
		return encoded;
	}
    
    String decode(byte[] encoded, int bit){
        String strTemp = new String("");
        String strBinary = new String("");
        String strText = new String("");
        Integer tempInt =new Integer(0);
        int intTemp=0;
        for(int i = 0;i<encoded.length;i++){         
            if(encoded[i]<0){
                intTemp = (int)encoded[i]+256;
            }else
                intTemp = (int)encoded[i];
            strTemp = Integer.toBinaryString(intTemp);
            while(strTemp.length()%8 != 0){
                strTemp="0"+strTemp;
            }
            strBinary = strBinary+strTemp;
        }
        for(int i=0 ; i<strBinary.length();i=i+bit){
            tempInt = tempInt.valueOf(strBinary.substring(i,i+bit),2);
            strText = strText + toChar(tempInt.intValue()); 
        }
        return strText;
    }

	private int toValue(char ch) {
		if (ch >= 'a' && ch <= 'z')
			return (int)(ch - 'a');
		else if (ch >= 'A' && ch <= 'Z')
			return (int)(ch - 'A' + 26);
		else if (ch >= '0' && ch <= '9')
			return (int)(ch - '0' + 52);
		else {
			return 62;
		}
	}

	private char toChar(int val) {
		char ch = 0;
		if (val >= 0 && val < 26)
			return (char)(val + 'a');
		else if (val >= 26 && val < 52)
			return (char)(val - 26 + 'A');
		else if (val >= 52 && val < 62)
			return (char)(val - 52 + '0');
		else if (val == 62)
			return ' ';
		return ch;
	}
}
