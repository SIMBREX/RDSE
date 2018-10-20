/* Copyright 2018 Simbrex Studio
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
 * VERSION - 1.0
 * HOW TO USE - https://github.com/SIMBREX/RDSE/wiki/Text-Encryption-Assistant
 * FEEDBACK - rdse@simbrex.com */

import java.nio.charset.StandardCharsets;

public class RDSEText extends RDSE {

    private int depth;
    private char[] charSet;

    RDSEText() {
        this.depth = 0;
        charSet = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '/'};
    }

    public boolean setDepth(int depth){
        if (depth < 0){
            return false;
        }
        this.depth = depth;
        return true;
    }

    public boolean setCharSet(char[] charSet){
        try {
            if (charSet.length < 3) {
                return false;
            }
            for (int l1 = 0; l1 < charSet.length; l1++) {
                for (int l2 = 0; l2 < charSet.length; l2++) {
                    if (charSet[l1] == charSet[l2] && l1 != l2) {
                        return false;
                    }
                }
            }
            this.charSet = charSet;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public int getDepth(){
        return this.depth;
    }

    public char[] getCharSet(){
        return this.charSet;
    }

    public String encrypt(String text, String key){
        String result = "";
        byte[] bytes = super.encrypt(text.getBytes(StandardCharsets.UTF_8), key.getBytes(StandardCharsets.UTF_8), depth);
        for (int i = 0; i < bytes.length; i += 2) {
            short nextByte = 256;
            if (bytes.length > i + 1) {
                nextByte = (short) (bytes[i + 1] + 128);
            }
            int num = (bytes[i] + 128)*1000 + nextByte;
            int ns = charSet.length - 1;
            String collector = "";
            while (num >= ns) {
                collector = charSet[num % ns] + collector;
                num /= ns;
            }
            collector = charSet[num] + collector;
            result += collector;
            if (i + 2 < bytes.length) {
                result += charSet[charSet.length - 1];
            }
        }
        return result;
    }

    public String decrypt(String code, String key){
        String[] codeParts = code.split(String.valueOf(charSet[charSet.length - 1]));
        byte[] bytes = new byte[codeParts.length * 2];
        for (int i = 0, b = 0, tempNumber; i < codeParts.length; i++, b += 2) {
            char[] codeStr = codeParts[i].toCharArray();
            tempNumber = 0;
            int p = codeStr.length;
            for (int t = 0; t < codeStr.length; t++) {
                for (int g = 0; g < charSet.length - 1; g++) {
                    if (charSet[g] == codeStr[t]) {
                        p--;
                        tempNumber += Math.round(g * Math.pow(charSet.length - 1, p));
                    }
                }
            }
            bytes[b] = (byte) (tempNumber / 1000 - 128);
            bytes[b + 1] = (byte) (tempNumber % 1000 - 128);
        }
        return new String(super.decrypt(bytes, key.getBytes(StandardCharsets.UTF_8), depth), StandardCharsets.UTF_8);
    }

    public String hash(String text, String key){
        String result = "";
        byte[] bytes = super.hash(text.getBytes(StandardCharsets.UTF_8), key.getBytes(StandardCharsets.UTF_8), depth);
        for (int i = 0; i < bytes.length; i += 2) {
            short nextByte = 256;
            if (bytes.length > i + 1) {
                nextByte = (short) (bytes[i + 1] + 128);
            }
            int num = (bytes[i] + 128)*1000 + nextByte;
            int ns = charSet.length - 1;
            String collector = "";
            while (num >= ns) {
                collector = charSet[num % ns] + collector;
                num /= ns;
            }
            collector = charSet[num] + collector;
            result += collector;
        }
        return result;
    }

}