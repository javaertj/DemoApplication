package com.drivingassisstantHouse.library.tools;


import java.util.regex.Pattern;

/**
 * java 常用正则表达式验证。
 *
 * @author sunji <br/>
 */
public final class ToolRegex {
    private static final String regexkEmaiL = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
    private static final String regexkIdCard = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}";
    //	private static  final String regexMobilePhone="^((13[0-9])|(15[0-9])|(18[0-9])|(145)|(147)|(170)|(177)|(176)|(178))\\d{8}$";
    private static final String regexMobilePhone = "(\\+\\d+)?1[3458]\\d{9}$";
    //由于验证号码不全，故暂时舍去
    //private static final String regexkMobileL = "(\\+\\d+)?1[3458]\\d{9}$";
    private static final String regexkPhone = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$";
    private static final String regexkDigit = "\\-?[1-9]\\d+";
    private static final String regexkDecimals = "\\-?[1-9]\\d+(\\.\\d+)?";
    private static final String regexkBlankSpace = "\\s+";
    private static final String regexkChinese = "^[\u4E00-\u9FA5]+$";
    private static final String regexkURL = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?";
    private static final String regexkPostcode = "[1-9]\\d{5}";
    private static final String regexkIp = "\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b";
    //汉字，字母，数字，空格
    private static final String regexName="^[\\u4e00-\\u9fa5 a-zA-Z0-9]+$";
    public enum StringType {
        /**
         * 邮箱格式 验证
         */
        EMAIL,

        /**
         * 身份证验证
         */
        IDCARD,

        /**
         * 移动电话 验证
         */
        MOBILEL,

        /**
         * 座机号码验证
         */
        PHONE,

        /**
         * 整数验证
         */
        DIGIT,

        /**
         * 验证整数和浮点数
         */
        DECIMALS,

        /**
         * 空白验证
         */
        BLANKSPACE,

        /**
         * 中文验证
         */
        CHINESE,

        /**
         * 互联网地址
         */
        URL,

        /**
         * 邮政编码验证
         */
        POSTCODE,

        /**
         * IP地址验证
         */
        IP,

        /**
         * 端口验证
         */
        PORT,
        /**
         * 姓名:汉字，字母，数字，空格
         */
        NAME

    }

    /**
     * 验证身份证号码
     *
     * @param idCard 居民身份证号码15位或18位，最后一位可能是数字或字母
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkIdCard(String idCard) {
        String regex = "(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[Xx])$)";
        if ((idCard == null || idCard.isEmpty())) {
            return false;
        }

        if (!idCard.matches(regex)) {
            return false;
        } else {
            if (idCard.length() == 18) {
                int[] idCardWi = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2}; //将前17位加权因子保存在数组里
                int[] idCardY = new int[]{1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2}; //这是除以11后，可能产生的11位余数、验证码，也保存成数组
                int idCardWiSum = 0; //用来保存前17位各自乖以加权因子后的总和
                for (int i = 0; i < 17; i++) {
                    idCardWiSum += Integer.valueOf(idCard.substring(i, i + 1)) * idCardWi[i];
                }
                int idCardMod = idCardWiSum % 11;//计算出校验码所在数组的位置
                String idCardLast = idCard.substring(17);//得到最后一位身份证号码
                //如果等于2，则说明校验码是10，身份证号码最后一位应该是X
                if (idCardMod == 2) {
                    if (idCardLast.equalsIgnoreCase("x")) {
                    } else {
                        return false;
                    }
                } else {
                    //用计算出的验证码与最后一位身份证号码匹配，如果一致，说明通过，否则是无效的身份证号码
                    if (Integer.valueOf(idCardLast) == idCardY[idCardMod]) {
                    } else {
                        return false;
                    }
                }
            }
            return true;
        }
    }


    /**
     * 正在表达式验 证
     *
     * @param string     要验证的字符串
     * @param stringType 枚举值
     * @return true验证通过，其他则返回false
     */
    public static boolean regexk(String string, StringType stringType) {

        if (null == string || null == stringType) {
            return false;
        }
        String regexk = null;
        if (stringType.equals(StringType.EMAIL)) {
            regexk = regexkEmaiL;
        }
        if (stringType.equals(StringType.IDCARD)) {
            regexk = regexkIdCard;
        }
        if (stringType.equals(StringType.MOBILEL)) {
            regexk = regexMobilePhone;
        }
        if (stringType.equals(StringType.PHONE)) {
            regexk = regexkPhone;
        }
        if (stringType.equals(StringType.DIGIT)) {
            regexk = regexkDigit;
        }
        if (stringType.equals(StringType.DECIMALS)) {
            regexk = regexkDecimals;
        }
        if (stringType.equals(StringType.BLANKSPACE)) {
            regexk = regexkBlankSpace;
        }
        if (stringType.equals(StringType.CHINESE)) {
            regexk = regexkChinese;
        }
        if (stringType.equals(StringType.CHINESE)) {
            regexk = regexkURL;
        }
        if (stringType.equals(StringType.POSTCODE)) {
            regexk = regexkPostcode;
        }
        if (stringType.equals(StringType.IP)) {
            regexk = regexkIp;
        }
        if(stringType.equals(StringType.NAME)){//名字之间可以有空格，但不能全是空格
            String temp=string.replaceAll(" ", "");
            if(temp.equals("")){
                return false;
            }
            regexk = regexName;
        }
        if (stringType.equals(StringType.PORT)) {
            int port = Integer.parseInt(string);
            if (port > 0 && port < 65535) {
                return true;
            } else {
                return false;
            }
        }
        return Pattern.matches(regexk, string);
    }


}
