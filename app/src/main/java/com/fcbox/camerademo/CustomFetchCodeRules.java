package com.fcbox.camerademo;

import android.text.TextUtils;
import android.util.Log;

/**
 * @创建者 000778
 * @创建时间 2018/9/15
 * @描述
 */

public class CustomFetchCodeRules {

    /**
     * 数据库保存尚未上架的取件码
     */
    static String customFetchCodeRegex = "[0-9]{1,8}";

	public static String getCustomFetchCode(String currentFetchCode) {
		if (currentFetchCode.matches(customFetchCodeRegex)) {
			if (currentFetchCode.startsWith("0")) {
				StringBuilder stringBuilder = new StringBuilder();
				char[] fetchCodeInt = currentFetchCode.toCharArray();
				for (int i = 0; i < fetchCodeInt.length; i++) {
					if (fetchCodeInt[i] == '0') {
						stringBuilder.append("0");
					} else {
						break;
					}
				}
				String num = currentFetchCode.substring(stringBuilder.length());
				Log.i("xl---test","stringBuild:"+stringBuilder.toString());
				Log.i("xl---test","stringBuild:"+num);

				StringBuilder stringBuilder2 = new StringBuilder();
				for (int i = 0; i < 8 - stringBuilder.length(); i++) {
					stringBuilder2.append("9");
				}
				if (Integer.parseInt(num) >= Integer.parseInt(stringBuilder2.toString())) {
					return "";
				} else {
					int result = Integer.parseInt(currentFetchCode);
					return stringBuilder.toString() + (++result);
				}

			} else {
				int length = currentFetchCode.length();
				StringBuilder stringBuilder = new StringBuilder();
				for (int i = 0; i < length; i++) {
					stringBuilder.append("9");
				}
				if (Integer.parseInt(currentFetchCode) >= Integer.parseInt(stringBuilder.toString())) {
					return "";
				} else {
					int result = Integer.parseInt(currentFetchCode);
					return (++result) + "";
				}
			}
		} else {
			int suffixIndex = 0;
			char[] fetchCodeArr = currentFetchCode.toCharArray();
			int length = currentFetchCode.length();
			for (int i = length - 1; i >= 0; i--) {
				if (!Character.isDigit(fetchCodeArr[i])) {
					suffixIndex = i+1 ;
					break;
				}
			}
			int suffixNum = Integer.parseInt(currentFetchCode.substring(suffixIndex));
			int zeroNum = 0;
			if((currentFetchCode.substring(suffixIndex)+"").startsWith("0")){
				char[] suffixNumStr = (currentFetchCode.substring(suffixIndex)+"").toCharArray();
				for (int i = 0; i <suffixNumStr.length; i++) {
					if(suffixNumStr[i] != '0'){
						break;
					}else{
						zeroNum++;
					}
				}
			}

			String prefixStr = currentFetchCode.substring(0, suffixIndex);
			int maxIndex = (8 - prefixStr.length());
			StringBuilder stringBuilder = new StringBuilder();
			for (int i = 0; i < maxIndex; i++) {
				stringBuilder.append("9");
			}
			int maxNum = Integer.parseInt(stringBuilder.toString());
			if (suffixNum > maxNum) {
				return "";
			} else {
				StringBuilder zeroBuilder = new StringBuilder();
				if(zeroNum >0){
					for (int i = 0; i < zeroNum; i++) {
						zeroBuilder.append("0");
					}
				}
				return prefixStr + (zeroBuilder.toString())
						+(++suffixNum);
			}

		}

	}


	public static boolean isCustomFetchCode(String fetchCode) {
        if (TextUtils.isEmpty(fetchCode)) {
            return false;
        }

        String regex = "[0-9a-zA-Z][0-9a-zA-Z-]{1,6}[0-9]||[0-9a-zA-Z]{1,7}[0-9]";

        String regex1 = "[0-9]{1,8}";
        return fetchCode.matches(regex) || fetchCode.matches(regex1);

    }
}
