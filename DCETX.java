/* Copyright 2017 Simbrex Studio
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * VERSION - 1.1.0
 * HOW TO USE - https://github.com/SIMBREX/DCETX
 * FEEDBACK - support@simbrex.com */

public class DCETX {

    private String text;
    private String key;
    private String code;
    private char[] chars;
    private int[] charsi;
    private char[] charsKey;
    private int[] charsiKey;
    private String finish;
    private String[] codeParts;
    private int[] codePartsi;
    private char[] codes;
    private int offset;

    public DCETX() {
        codes = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '/'};
        offset = 0;
    }

    public String generate(String text, String key) {
        try {
            this.text = text;
            this.key = key;
            gen();
        } catch (Exception e) {
            return "[E] " + e.toString();
        }

        return finish;
    }

    public String decrypt(String code, String key) {
        try {
            this.code = code;
            this.key = key;
            dec();
        } catch (Exception e) {
            return "[E] " + e.toString();
        }

        return finish;
    }

    public String hash(String text, String key) {
        try {
            this.text = text;
            this.key = key;
            hsh();
        } catch (Exception e) {
            return "[E] " + e.toString();
        }

        return finish;
    }

    public boolean setCodes(char[] charsArray) {
        try {
            if (charsArray.length < 3) {
                return false;
            }
            if (charsArray[charsArray.length - 1] == '$') {
                charsArray[charsArray.length - 1] = '#';
            }
            for (int i = 0; i < charsArray.length; i++) {
                if ((int) charsArray[i] < 32) {
                    return false;
                }
            }
            for (int l1 = 0; l1 < charsArray.length; l1++) {
                for (int l2 = 0; l2 < charsArray.length; l2++) {
                    if (charsArray[l1] == charsArray[l2] && l1 != l2) {
                        return false;
                    }
                }
            }
            codes = charsArray;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean setOffset(int offset) {
        if (offset >= 0) {
            this.offset = offset;
            return true;
        } else {
            return false;
        }
    }

    public int getOffset() {
        return offset;
    }

    private void gen() {
        finish = "";
        parseText();
        parseKey();
        int g = 1;
        for (int i = 1; i < charsi.length; i++) {
            int sum = getSumKey(String.valueOf(charsiKey[g])) + offset + charsiKey[g] + charsiKey.length - 1 + (i * g * (charsiKey[g] % 10 +
                    Math.round(charsiKey[g] / (10 ^ String.valueOf(Math.abs(charsiKey[g])).length())))) % 100 + charsi[i];
            String r = "";
            int s = codes.length - 1;
            while (sum >= s) {
                r = codes[sum % s] + r;
                sum /= s;
            }
            r = codes[sum] + r;
            finish += r;
            if (i < charsi.length - 1) {
                finish += codes[codes.length - 1];
            }
            g++;
            if (g >= charsiKey.length) {
                g = 1;
            }
        }
    }

    private void dec() {
        finish = "";
        parseCode();
        parseKey();
        int g = 1;
        for (int i = 1; i < codePartsi.length; i++) {
            char parseSym = (char) (codePartsi[i] - getSumKey(String.valueOf(charsiKey[g])) - offset - charsiKey[g] - charsiKey.length + 1 -
                    (i * g * (charsiKey[g] % 10 + Math.round(charsiKey[g] / (10 ^ String.valueOf(Math.abs(charsiKey[g])).length())))) % 100);
            finish += parseSym;
            g++;
            if (g >= charsiKey.length) {
                g = 1;
            }
        }
    }

    private void hsh() {
        finish = "";
        parseText();
        parseKey();
        int g = 1;
        for (int i = 1; i < charsi.length; i++) {
            int sum = getSumKey(String.valueOf(charsiKey[g])) + charsi[i]/getSumKey(String.valueOf(charsi[i])) + charsi[i] +
                    charsiKey[g] + offset + charsiKey.length*charsi.length + charsi[i]*charsiKey.length;
            String r = "";
            int s = codes.length - 1;
            while (sum >= s) {
                r = codes[sum % s] + r;
                sum /= s;
            }
            r = codes[sum] + r;
            finish += r;
            g++;
            if (g >= charsiKey.length) {
                g = 1;
            }
        }

    }

    private void parseText() {
        chars = text.toCharArray();
        charsi = new int[chars.length + 1];
        for (int i = 1; i <= chars.length; i++) {
            charsi[i] = chars[i - 1];
        }
    }

    private void parseKey() {
        charsKey = key.toCharArray();
        charsiKey = new int[charsKey.length + 1];
        for (int i = 1; i <= charsKey.length; i++) {
            charsiKey[i] += charsKey[i - 1];
        }
    }

    private void parseCode() {
        codeParts = code.split(String.valueOf(codes[codes.length - 1]));
        codePartsi = new int[codeParts.length + 1];
        for (int i = 1; i < codePartsi.length; i++) {
            codePartsi[i] = 0;
            char[] codeStr = codeParts[i - 1].toCharArray();
            int r = 0;
            int p = codeStr.length;
            for (int t = 1; t <= codeStr.length; t++) {
                for (int g = 0; g < codes.length - 1; g++) {
                    if (codes[g] == codeStr[t - 1]) {
                        p--;
                        r += Math.round(g*Math.pow(codes.length - 1, p));
                    }
                }
            }
            codePartsi[i] = r;
        }
    }


    private int getSumKey(String len) {
        int sumKey = 0;
        char[] charKey = len.toCharArray();
        for (int t = 1; t <= charKey.length; t++) {
            sumKey += Character.getNumericValue(charKey[t - 1]) * (charKey.length + 1);
        }
        return sumKey;
    }
}
