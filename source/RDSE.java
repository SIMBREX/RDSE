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
 * VERSION - 0.1 (experimental)
 * HOW TO USE - https://github.com/SIMBREX/RDSE/wiki
 * FEEDBACK - rdse@simbrex.com */

class RDSE {

    private NumberGenerator numberGenerator;

    RDSE() {
        this.numberGenerator = new NumberGenerator();
    }

    public byte[] encrypt(byte[] data, byte[] key){
        return _encrypt(data, key, 0);
    }

    public byte[] encrypt(byte[] data, byte[] key, int depth){
        return _encrypt(data, key, depth);
    }

    public byte[] decrypt(byte[] encryptedData, byte[] key){
        return _decrypt(encryptedData, key, 0);
    }

    public byte[] decrypt(byte[] encryptedData, byte[] key, int depth){
        return _decrypt(encryptedData, key, depth);
    }

    public byte[] hash(byte[] encryptedData, byte[] key){
        return _hash(encryptedData, key, 0);
    }

    public byte[] hash(byte[] encryptedData, byte[] key, int depth){
        return _hash(encryptedData, key, depth);
    }

    private byte[] _encrypt(byte[] data, byte[] key, int depth){
        byte[] result = new byte[data.length];
        long keySign1 = generateKeySign(key);
        key = twinSort(numberGenerator.generateNumbersArray(key.length, keySign1, key.length), key);
        long keySign2 = generateKeySign(key);
        long resultKeySign = keySign2;
        data = twinSort(numberGenerator.generateNumbersArray(keySign1, keySign2, data.length), data);
        byte[][] bSign = new byte[2 + depth][];
        byte[] bSignCounter = new byte[2 + depth];
        long signBuffer;
        long bSignGen1 = keySign2;
        long bSignGen2 = keySign1;
        for (int i = 0; i < bSign.length; i++) {
            signBuffer = bSignGen2;
            bSignGen2 = Long.parseLong(numberGenerator.generateNumber(bSignGen1, bSignGen2, 18));
            bSign[i] = numberToBytes(bSignGen2);
            bSignCounter[i] = (byte) normalizeNumber(0, bSign[i].length - 1, Long.parseLong(numberGenerator.generateNumber(bSignGen2, bSignGen1, 18)));
            bSignGen1 = signBuffer;
        }
        keySign1 = bSignGen2;
        keySign2 = bSignGen1;
        for (int i = 0, k = 0; i < data.length; i++, k++) {
            if (k >= key.length){
                signBuffer = keySign2;
                keySign2 = Long.parseLong(numberGenerator.generateNumber(keySign1, keySign2, 18));
                key = twinSort(numberGenerator.generateNumbersArray(keySign2, keySign1, key.length), key);
                keySign1 = signBuffer;
                k = 0;
            }
            result[i] = (byte) (data[i] ^ key[k]);
            for (int j = 0; j < bSign.length; j++) {
                result[i] ^= bSign[j][bSignCounter[j]];
                bSignCounter[j]++;
                if (bSignCounter[j] >= bSign[j].length){
                    bSignCounter[j] = 0;
                }
            }
        }
        result = twinSort(numberGenerator.generateNumbersArray(key.length, resultKeySign, result.length), result);
        return result;
    }

    private byte[] _decrypt(byte[] data, byte[] key, int depth){
        byte[] result = new byte[data.length];
        long keySign1 = generateKeySign(key);
        key = twinSort(numberGenerator.generateNumbersArray(key.length, keySign1, key.length), key);
        long keySign2 = generateKeySign(key);
        long resultKeySign1 = keySign1;
        long resultKeySign2 = keySign2;
        data = twinRestoreSort(numberGenerator.generateNumbersArray(key.length, keySign2, data.length), data);
        byte[][] bSign = new byte[2 + depth][];
        byte[] bSignCounter = new byte[2 + depth];
        long signBuffer;
        long bSignGen1 = keySign2;
        long bSignGen2 = keySign1;
        for (int i = 0; i < bSign.length; i++) {
            signBuffer = bSignGen2;
            bSignGen2 = Long.parseLong(numberGenerator.generateNumber(bSignGen1, bSignGen2, 18));
            bSign[i] = numberToBytes(bSignGen2);
            bSignCounter[i] = (byte) normalizeNumber(0, bSign[i].length - 1, Long.parseLong(numberGenerator.generateNumber(bSignGen2, bSignGen1, 18)));
            bSignGen1 = signBuffer;
        }
        keySign1 = bSignGen2;
        keySign2 = bSignGen1;
        for (int i = 0, k = 0; i < data.length; i++, k++) {
            if (k >= key.length){
                signBuffer = keySign2;
                keySign2 = Long.parseLong(numberGenerator.generateNumber(keySign1, keySign2, 18));
                key = twinSort(numberGenerator.generateNumbersArray(keySign2, keySign1, key.length), key);
                keySign1 = signBuffer;
                k = 0;
            }
            result[i] = (byte) (data[i] ^ key[k]);
            for (int j = 0; j < bSign.length; j++) {
                result[i] ^= bSign[j][bSignCounter[j]];
                bSignCounter[j]++;
                if (bSignCounter[j] >= bSign[j].length){
                    bSignCounter[j] = 0;
                }
            }
        }
        result = twinRestoreSort(numberGenerator.generateNumbersArray(resultKeySign1, resultKeySign2, result.length), result);
        return result;
    }

    private byte[] _hash(byte[] data, byte[] key, int depth){
        byte[] result = new byte[data.length];
        long dataSign = generateKeySign(data);
        key = twinSort(numberGenerator.generateNumbersArray(key.length, dataSign, key.length), key);
        long keySign = generateKeySign(key);
        long resultKeySign = keySign;
        data = twinRestoreSort(numberGenerator.generateNumbersArray(dataSign, keySign, data.length), data);
        byte[][] bSign = new byte[2 + depth][];
        byte[] bSignCounter = new byte[2 + depth];
        long signBuffer;
        long bSignGen1 = keySign;
        long bSignGen2 = dataSign;
        for (int i = 0; i < bSign.length; i++) {
            signBuffer = bSignGen2;
            bSignGen2 = Long.parseLong(numberGenerator.generateNumber(bSignGen1, bSignGen2, 18));
            bSign[i] = numberToBytes(bSignGen2);
            bSignCounter[i] = (byte) normalizeNumber(0, bSign[i].length - 1, Long.parseLong(numberGenerator.generateNumber(bSignGen2, bSignGen1, 18)));
            bSignGen1 = signBuffer;
        }
        dataSign = bSignGen2;
        keySign = bSignGen1;
        for (int i = 0, k = 0; i < data.length; i++, k++) {
            if (k >= key.length){
                signBuffer = keySign;
                keySign = Long.parseLong(numberGenerator.generateNumber(dataSign, keySign, 18));
                key = twinSort(numberGenerator.generateNumbersArray(keySign, dataSign, key.length), key);
                dataSign = signBuffer;
                k = 0;
            }
            result[i] = (byte) (data[i] ^ key[k]);
            for (int j = 0; j < bSign.length; j++) {
                result[i] ^= bSign[j][bSignCounter[j]];
                bSignCounter[j]++;
                if (bSignCounter[j] >= bSign[j].length){
                    bSignCounter[j] = 0;
                }
            }
        }
        result = twinSort(numberGenerator.generateNumbersArray(key.length, resultKeySign, result.length), result);
        return result;
    }

    private int[] bytesToNumber(byte[] bytes){
        int[] ints = new int[(int)Math.ceil(bytes.length/2f)];
        for (int b = 0, i = 0; i < ints.length; b += 2, i++) {
            short nextByte = 256;
            if (bytes.length > b + 1){
                nextByte = (short) (bytes[b + 1] + 128);
            }
            ints[i] = (bytes[b] + 128)*1000 + nextByte;
        }
        return ints;
    }

    private byte[] numberToBytes(long num){
        byte[] bytes = new byte[9];
        byte i = 0;
        short buffer = 0;
        while (num != 0){
            if (buffer * 10 + (num % 10) > 255){
                bytes[i] = (byte)(buffer - 128);
                i++;
                buffer = 0;
            }
            buffer = (short) (buffer * 10 + (num % 10));
            num /= 10;
        }
        bytes[i] = (byte)(buffer - 128);
        if (i < 8) {
            byte[] result = new byte[i + 1];
            for (int j = 0; j <= i; j++) {
                result[j] = bytes[j];
            }
            return result;
        } else {
            return bytes;
        }
    }

    private long generateKeySign(byte[] key){
        long sign = (key[0] + 128) * 1000 + key[1] + 128;
        for (int i = 2; i < key.length; i += 2) {
            short nextByte = 256;
            if (key.length > i + 1){
                nextByte = (short) (key[i + 1] + 128);
            }
            sign = Long.parseLong(numberGenerator.generateNumber(sign, (key[i] + 128)*1000 + nextByte, normalizeNumber(1, 18, sign)));
        }
        return sign;
    }

    private int normalizeNumber(int min, int max, long num){
        float a = (float)(max - min)/2f;
        return Math.round((float)Math.sin(num)*a + a + min);
    }

    private byte[] twinSort(int[] sortArray, byte[] retArray){
        int i, m = sortArray[0], exp = 1, n = sortArray.length;
        int[][] b = new int[n][];

        int[][] bindArray = new int[sortArray.length][2];
        for (int j = 0; j < retArray.length; j++) {
            bindArray[j][0] = sortArray[j];
            bindArray[j][1] = retArray[j];
        }

        for (i = 1; i < n; i++) {
            if (sortArray[i] > m) {
                m = sortArray[i];
            }
        }

        while (m / exp > 0)
        {
            int[] bucket = new int[10];

            for (i = 0; i < n; i++) {
                bucket[(sortArray[i] / exp) % 10]++;
            }
            for (i = 1; i < 10; i++) {
                bucket[i] += bucket[i - 1];
            }
            for (i = n - 1; i >= 0; i--) {
                b[--bucket[(sortArray[i] / exp) % 10]] = bindArray[i];
            }
            for (i = 0; i < n; i++) {
                bindArray[i] = b[i];
            }
            exp *= 10;
        }
        byte[] returnArray = new byte[retArray.length];
        for (int g = 0; g < bindArray.length; g++) {
            returnArray[g] = (byte) bindArray[g][1];
        }
        return returnArray;
    }

    private byte[] twinRestoreSort(int[] sortArray, byte[] retArray){
        int[][] positionalSortArray = new int[sortArray.length][2];
        for (int i = 0; i < retArray.length; i++) {
            positionalSortArray[i][0] = sortArray[i];
            positionalSortArray[i][1] = i;
        }
        int i, m = sortArray[0], exp = 1, n = sortArray.length;
        int[][] b = new int[n][];

        for (i = 1; i < n; i++) {
            if (sortArray[i] > m) {
                m = sortArray[i];
            }
        }

        while (m / exp > 0)
        {
            int[] bucket = new int[10];

            for (i = 0; i < n; i++) {
                bucket[(sortArray[i] / exp) % 10]++;
            }
            for (i = 1; i < 10; i++) {
                bucket[i] += bucket[i - 1];
            }
            for (i = n - 1; i >= 0; i--) {
                b[--bucket[(sortArray[i] / exp) % 10]] = positionalSortArray[i];
            }
            for (i = 0; i < n; i++) {
                positionalSortArray[i] = b[i];
            }
            exp *= 10;
        }
        byte[] returnArray = new byte[retArray.length];
        for (int g = 0; g < positionalSortArray.length; g++) {
            returnArray[positionalSortArray[g][1]] = retArray[g];
        }
        return returnArray;
    }

    private class NumberGenerator {

        private String sResult;
        private int[] aResult;

        private String generateNumber(long dividend, long divider, int count){
            generate(false, dividend, divider, count);
            return sResult;
        }

        private int[] generateNumbersArray(long dividend, long divider, int count){
            generate(true, dividend, divider, count);
            return aResult;
        }

        private void generate(boolean isArray, long dividend, long divider, int count){
            if (isArray) {
                aResult = new int[count];
            } else {
                sResult = "";
            }
            long remDiv = -1;
            DynamicArray divList = new DynamicArray();
            int currentCell = 0;
            byte stackCount = (byte)(String.valueOf(count).length());
            byte stackCounter = 0;
            byte repeatCounter = 0;
            char lastChar = '\u0000';
            char newChar;
            long div = dividend;
            if (divider == 0){
                if (count != 0 && dividend/count != 0) {
                    divider = dividend/count;
                } else {
                    divider = dividend + 1;
                }
            }
            div = (div % divider) * 10;
            while (currentCell < count){
                newChar = (char)(div/divider);
                short numLength = (short)String.valueOf(div).length();
                short delPos = (short)(divider % numLength);
                long saveSecPart = div % (long)Math.pow(10, numLength - delPos - 1);
                div = (div / (long)Math.pow(10, numLength - delPos));
                if (div%divider <= 922337203685477580L){
                    div = div*10 + normalizeNumber(0,9, div);
                }
                div = (div*(long)Math.pow(10, numLength - delPos - 1) + saveSecPart)%divider*10;
                if (remDiv == div || divList.contains(div)){
                    remDiv = -1;
                    divList.clean();
                    div++;
                    divider++;
                    continue;
                }
                divList.add(div);
                if (remDiv == -1) {
                    remDiv = div;
                }
                if (newChar == lastChar){
                    repeatCounter++;
                } else {
                    repeatCounter = 0;
                }
                if (repeatCounter > 1){
                    divider++;
                    div = div%divider*10;
                    repeatCounter = 0;
                    remDiv = -1;
                    divList.clean();
                    continue;
                }
                lastChar = newChar;
                if (currentCell == 0 && stackCounter == 0 && newChar == 0){
                    continue;
                }
                if (isArray) {
                    aResult[currentCell] = aResult[currentCell] * 10 + newChar;
                    stackCounter++;
                    if (stackCounter >= stackCount) {
                        stackCounter = 0;
                        currentCell++;
                    }
                } else {
                    sResult += (int) newChar;
                    currentCell++;
                }
            }
        }
    }

    private class DynamicArray {

        private long[] array = new long[0];
        private int currentCell = 0;

        void add(long num){
            if (currentCell%10 == 0){
                updateLength(array.length + 10);
            }
            array[currentCell] = num;
            currentCell++;
        }

        boolean contains(long num){
            for (long anArray: array) {
                if (anArray == num){
                    return true;
                }
            }
            return false;
        }

        void clean(){
            array = new long[0];
            currentCell = 0;
        }

        private void updateLength(int newLength){
            long[] newArray = new long[newLength];
            for (int i = 0; i < array.length; i++) {
                newArray[i] = array[i];
            }
            array = newArray;
        }
    }
}