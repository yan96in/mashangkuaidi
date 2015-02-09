package com.striveen.express.util;

public class SubstituteEncrypt {
	/**
	 * 加密解密的key
	 */
	private static final String KEY = "http://www.hello36.com";
	public static char[] letterMap = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
			'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
			'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
			'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
			'V', 'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', '0', '=', '&'};
	public int n = letterMap.length;

	private static SubstituteEncrypt instance = null;
	private SubstituteEncrypt(){}
	public static SubstituteEncrypt getInstance(){
		if (instance == null) {
			instance = new SubstituteEncrypt();
		}
		return instance;
	}

	/**
	 * 加密
	 */
	public String encrypt(String text) {
		int key = getAsciiNum(KEY);
		int len = text.length();
		char[] tmpChar = new char[len];
		char tmpLetter;
		int i, pos;
		if (key > n) {
			key = key % n;
		}
		for (i = 0; i < len; i++) {
			tmpLetter = text.charAt(i);
			pos = getLetterPosition(tmpLetter);
			tmpChar[i] = getLetter((pos + key) % n);
		}
		return new String(tmpChar);
	}

	/**
	 * 解密
	 */
	public String decrypt(String text) {
		int key = getAsciiNum(KEY);
		int len = text.length();
		char[] tmpChar = new char[len];
		char tmpLetter;
		int i, pos;
		if (key > n) {
			key = key % n;
		}
		for (i = 0; i < len; i++) {
			tmpLetter = text.charAt(i);
			pos = getLetterPosition(tmpLetter);
			tmpChar[i] = getLetter((pos + n - key) % n);
		}
		return new String(tmpChar);
	}

	private char getLetter(int position) {
		return letterMap[position];
	}

	private int getLetterPosition(char letter) {
		int i;
		int len = letterMap.length;
		for (i = 0; i < len; i++) {
			if (letterMap[i] == letter) {
				break;
			}
		}
		return i;
	}

	private int getAsciiNum(String str) {
		int key = 0;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			key += (int)c*i;
		}
		return key;
	}
}