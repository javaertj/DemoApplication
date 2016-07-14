package com.drivingassisstantHouse.library.tools;

import java.io.UnsupportedEncodingException;


import android.text.TextUtils;


/**
 *
 * 处理字节和数字之间的转换
 * (只有多字节的数字才有大小端的概念, 意思是几个字节表示一个数字的时候的排列格式)
 * @author sunji <br/>
 *
 */
public class ToolBytes {


	/**
	 * 浮点转换为字节(小端)
	 *
	 * @param f
	 * @return
	 */
	public static byte[] float2byte(float f) {
		// 把float转换为byte[]
		int fbit = Float.floatToIntBits(f);

		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (fbit >> (24 - i * 8));
		}
		// 翻转数组
		int len = b.length;
		// 建立一个与源数组元素类型相同的数组,为了防止修改源数组，将源数组拷贝一份副本
		byte[] dest = new byte[len];
		System.arraycopy(b, 0, dest, 0, len);
		byte temp;
		// 将顺位第i个与倒数第i个交换
		for (int i = 0; i < len / 2; ++i) {
			temp = dest[i];
			dest[i] = dest[len - i - 1];
			dest[len - i - 1] = temp;
		}
		return dest;

	}

	/**
	 * 字节转换为浮点（小端）
	 *
	 * @param b     字节（至少4个字节）
	 * @param index 开始位置
	 * @return
	 */
	public static float byte2float(byte[] b, int index) {
		int l;
		l = b[index + 0];
		l &= 0xff;
		l |= ((long) b[index + 1] << 8);
		l &= 0xffff;
		l |= ((long) b[index + 2] << 16);
		l &= 0xffffff;
		l |= ((long) b[index + 3] << 24);
		return Float.intBitsToFloat(l);
	}


	/**
	 * int to byte[] 支持 1或者 4 个字节 小端：数据低位放在低地址，数据高位放在高地址 如: 0x12345678存放之后为:0x78,0x56,0x34.0x12
	 *
	 * @param i   要转换的数值
	 * @param len 所要占用的字节长度，支持，1或4
	 * @return
	 */
	public static byte[] intToByteLittle(int i, int len) {
		byte[] abyte = null;
		if (len == 1) {
			abyte = new byte[len];
			abyte[0] = (byte) (0xff & i);
		} else {
			abyte = new byte[len];
			abyte[0] = (byte) (0xff & i);
			abyte[1] = (byte) ((0xff00 & i) >> 8);
			abyte[2] = (byte) ((0xff0000 & i) >> 16);
			abyte[3] = (byte) ((0xff000000 & i) >> 24);
		}
		return abyte;
	}

	/**
	 * 将小端字节数组恢复到整型数值
	 *
	 * @param bytes
	 * @return
	 */
	public static int bytesToIntLittle(byte[] bytes) {
		int addr = 0;
		if (bytes.length == 1) {
			addr = bytes[0] & 0xFF;
		} else {
			addr = bytes[0] & 0xFF;// 0下标如果对应的是最低那字节，就是小端
			addr |= ((bytes[1] << 8) & 0xFF00);// 1下标的字节左移
			addr |= ((bytes[2] << 16) & 0xFF0000);// 2下标字节左移
			addr |= ((bytes[3] << 24) & 0xFF000000);
		}
		return addr;
	}

	/**
	 * 字节转换成整数
	 *
	 * @param b
	 * @return
	 */
	public static int byteToInt(byte b) {
		int addr = 0;
		addr = b & 0xFF;
		return addr;
	}

	/**
	 * int to byte[] 支持 1或者 4 个字节 大端:高位存在低地址，低位存在高地址 0x12345678存放之后为:0x12,0x34,0x56,0x78
	 *
	 * @param i   要转换的数值
	 * @param len 转换后需要占用的字节数，支持1或者4字节
	 * @return
	 */
	public static byte[] intToByteBig(int i, int len) {
		byte[] abyte = null;
		if (len == 1) {
			abyte = new byte[len];
			abyte[0] = (byte) (0xff & i);
		} else {
			abyte = new byte[len];
			abyte[0] = (byte) ((i >>> 24) & 0xff);
			abyte[1] = (byte) ((i >>> 16) & 0xff);
			abyte[2] = (byte) ((i >>> 8) & 0xff);
			abyte[3] = (byte) (i & 0xff);
		}
		return abyte;
	}

	/**
	 * 将大端字节序恢复到整型数值
	 *
	 * @param bytes
	 * @return
	 */
	public static int bytesToIntBig(byte[] bytes) {
		int addr = 0;
		if (bytes.length == 1) {
			addr = bytes[0] & 0xFF;
		} else {
			addr = bytes[0] & 0xFF;// 0下标如果对应的是最低那字节，就是小端
			addr = (addr << 8) | (bytes[1] & 0xff);// 这里将0下标对应的字节左移8位，说明0下标的对应的不是最低字节，故这里是大端
			addr = (addr << 8) | (bytes[2] & 0xff);
			addr = (addr << 8) | (bytes[3] & 0xff);
		}
		return addr;
	}

	/**
	 * byte数组转换成16进制字符串
	 *
	 * @param src
	 * @return
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * byte数组转换成16进制字符数组
	 *
	 * @param src
	 * @return
	 */
	public static String[] bytesToHexStrings(byte[] src) {
		if (src == null || src.length <= 0) {
			return null;
		}
		String[] str = new String[src.length];

		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				str[i] = "0";
			}
			str[i] = hv;
		}
		return str;
	}

	/**
	 * 将字符串转换成UTF-8字节数组,
	 *
	 * @param s 要转换的字符串
	 * @return 转换后的字节数组，注意如果s为空或者空串，将返回null
	 */
	public static byte[] stringToByte(String s) {
		byte[] result = null;
		if (TextUtils.isEmpty(s)) {
			return null;
		}
		try {
			result = s.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Convert hex string to byte[]
	 *
	 * @param hexString the hex string
	 * @return byte[]
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (TextUtils.isEmpty(hexString)) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	/**
	 * Convert char to byte
	 *
	 * @param c char
	 * @return byte
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	/**
	 * 将字节数组转换为字符串，
	 *
	 * @param param 要转换的字节数组
	 * @return 返回字符串，注意如果字节数组为空或者长度为0，将返回null
	 */
	public static String byteToStr(byte[] param) {
		String result = null;
		if (param != null && param.length > 0) {
			try {
				result = new String(param, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 将一个单字节转换成32位的int
	 *
	 * @param b
	 * @return
	 */
	public static int unsignedByteToInt(byte b) {
		return (int) b & 0xFF;
	}

	/**
	 * 将一个单字节转换成16进制字符串
	 *
	 * @param b
	 * @return
	 */
	public static String byteToHex(byte b) {
		int i = b & 0xFF;
		return Integer.toHexString(i);
	}

	/**
	 * 将16位的short转换成byte数组 大端
	 *
	 * @param s short
	 * @return byte[] 长度为2
	 */
	public static byte[] shortToByteArrayBig(short s) {
		byte[] targets = new byte[2];
		for (int i = 0; i < 2; i++) {
			int offset = (targets.length - 1 - i) * 8; // offset 1×8 0×8
			targets[i] = (byte) ((s >>> offset) & 0xff);
		}
		return targets;
	}

	/**
	 * 将16位的short转换成byte数组 小端
	 *
	 * @param s short
	 * @return byte[] 长度为2
	 */
	public static byte[] shortToByteLittle(short s) {
		byte[] targets = new byte[2];
		for (int i = 0; i < 2; i++) {
			int offset = i * 8;
			targets[i] = (byte) ((s >>> offset) & 0xff);
		}
		return targets;
	}

	/**
	 * 将长度为2的小端byte数组转换为16位int
	 *
	 * @param res byte[]
	 * @return int
	 */
	public static int byte2intLittle(byte[] res) {
		// res = InversionByte(res);
		// 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000
		int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00); // | 表示安位或
		return targets;
	}

	/**
	 * 将长度为2的大端数组转换为16位int
	 *
	 * @param res
	 * @return
	 */
	public static int byte2IntBig(byte[] res) {
		int addr = 0;
		addr = res[0] & 0xFF;// 0下标如果对应的是最低那字节，就是小端
		addr = (addr << 8) | (res[1] & 0xff);// 这里将0下标对应的字节左移8位，说明0下标的对应的不是最低字节，故这里是大端
		return addr;
	}


	/**
	 * 裁剪数组
	 *
	 * @param source 源数组
	 * @param offset 起始下标
	 * @param len    裁剪长度
	 * @return
	 */
	public static byte[] clipBytes(byte[] source, int offset, int len) {
		if (source == null)
			throw new NullPointerException();
		else if (offset > (source.length - 1) || offset < 0 || len < 0 || len > source.length
				|| (len + offset) > source.length)
			throw new ArrayIndexOutOfBoundsException();

		byte result[] = new byte[len];
		System.arraycopy(source, offset, result, 0, len);

		return result;
	}
}



