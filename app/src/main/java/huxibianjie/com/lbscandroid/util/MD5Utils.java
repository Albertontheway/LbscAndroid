package huxibianjie.com.lbscandroid.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密方法
 * 
 * @author zhangtb
 * @date 2016-8-2 13:45:09
 * @since 1.0.0
 */
public class Md5Utils {
	
	/**
	 * MD5加密，用户验签
	 * 
	 * @author zhangtb
	 * @date 2016-8-2 13:49:54
	 * @param plainText 要加密的字符串
	 * @return 加密后的字符串
	 */
	public static String md5s(String plainText) {
		String str = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			
			int i;
			
			StringBuilder buf = new StringBuilder("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0){
					i += 256;
				}
				if (i < 16){
					buf.append("0");
				}
				buf.append(Integer.toHexString(i));
			}
			str = buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return str;
	}

	public static void main(String args) {
		String str=Md5Utils.md5s("182010646491527651362uS1ibFQyRby2");
		System.out.println(str+","+str.length());
	}
	
}
