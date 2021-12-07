package com.harry.videowatermark.utils.id;

/**
 * 描述:
 *
 * @author mengweibo@kanzhun.com
 * @create 2021/9/2
 */

public class RC4 {
    private static final int STATE_LENGTH = 256;
    private static final int STATE_LENGTH_MASK = 255;
    private static final int MAX_INT_KEY_LENGTH = 1024;
    private static final ThreadLocal<int[]> INT_KEY_ARR_TL = new ThreadLocal<int[]>() {
        protected int[] initialValue() {
            return new int[1024];
        }
    };
    private static final ThreadLocal<int[]> STATE_BOX_TL = new ThreadLocal<int[]>() {
        protected int[] initialValue() {
            return new int[256];
        }
    };
    private static final ThreadLocal<int[]> KEY_BOX_TL = new ThreadLocal<int[]>() {
        protected int[] initialValue() {
            return new int[256];
        }
    };

    public RC4() {
    }

    /** @deprecated */
    @Deprecated
    public static byte[] RC4encrypt(byte[] key, byte[] plainData) {
        int[] intKey = fromByteArray(key);
        int[] stateBox = new int[256];
        int[] keyBox = new int[256];

        int i;
        for(i = 0; i < 256; ++i) {
            stateBox[i] = i;
            keyBox[i] = intKey[i % key.length];
        }

        i = 0;

        int j;
        int temp;
        for(j = 0; i < 256; ++i) {
            j = (j + stateBox[i] + keyBox[i]) % 256;
            temp = stateBox[i];
            stateBox[i] = stateBox[j];
            stateBox[j] = temp;
        }

        i = 0;
        j = 0;
        byte[] cipher = new byte[plainData.length];

        for(int k = 0; k < plainData.length; ++k) {
            i = (i + 1) % 256;
            j = (j + stateBox[i]) % 256;
            temp = stateBox[i];
            stateBox[i] = stateBox[j];
            stateBox[j] = temp;
            int tempKey = stateBox[(stateBox[i] + stateBox[j]) % 256];
            cipher[k] = (byte)(tempKey ^ plainData[k]);
        }

        return cipher;
    }

    private static int[] fromByteArray(byte[] array) {
        int[] intArray = new int[array.length];

        for(int i = 0; i < array.length; ++i) {
            intArray[i] = array[i] >= 0 ? array[i] : array[i] + 256;
        }

        return intArray;
    }

    public static byte[] RC4encrypt(byte[] key, byte[] plainData, int dataLength) {
        int[] stateBox = (int[])STATE_BOX_TL.get();
        int[] keyBox = (int[])KEY_BOX_TL.get();
        init(key, stateBox, keyBox);
        int i = 0;
        int j = 0;

        for(int k = 0; k < dataLength; ++k) {
            i = i + 1 & 255;
            int sbi = stateBox[i];
            j = j + sbi & 255;
            int sbj = stateBox[j];
            stateBox[i] = sbj;
            stateBox[j] = sbi;
            int tempKey = stateBox[sbi + sbj & 255];
            plainData[k] = (byte)(tempKey ^ plainData[k]);
        }

        return plainData;
    }

    private static void init(byte[] key, int[] stateBox, int[] keyBox) {
        int[] intKey = fromByteArrayV2(key);

        int sbi;
        for(sbi = 0; sbi < 256; ++sbi) {
            stateBox[sbi] = sbi;
            keyBox[sbi] = intKey[sbi % key.length];
        }

        int i = 0;

        for(int j = 0; i < 256; ++i) {
            sbi = stateBox[i];
            j = j + sbi + keyBox[i] & 255;
            stateBox[i] = stateBox[j];
            stateBox[j] = sbi;
        }

    }

    private static int[] fromByteArrayV2(byte[] array) {
        int[] intArray = getIntKeyArr(array);

        for(int i = 0; i < array.length; ++i) {
            byte b = array[i];
            intArray[i] = b >= 0 ? b : b + 256;
        }

        return intArray;
    }

    private static int[] getIntKeyArr(byte[] keyArr) {
        return keyArr.length <= 1024 ? (int[])INT_KEY_ARR_TL.get() : new int[keyArr.length];
    }
}
