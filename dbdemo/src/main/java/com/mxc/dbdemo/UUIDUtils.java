package com.mxc.dbdemo;

import java.nio.ByteBuffer;
import java.util.UUID;

public class UUIDUtils {
    public static final String TAG = "UUIDUtils";

    /**
     * uuid转化 byte[]
     * @param uuid
     * @return
     */
    public static byte[] toByte(UUID uuid) {
        ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());
        return buffer.array();
    }

    /**
     * byte[] 转换 uuid
     * @param bytes
     * @return
     */
    public static UUID toUUID(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        long fistLong = buffer.getLong();
        long secondLong = buffer.getLong();
        return new UUID(fistLong, secondLong);
    }

    /**
     * byte[] 转换为 16进制字符串啊
     * @param bytes
     * @return
     */
    public static String bytesToHexStr(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (byte b : bytes) {
            int v = b & 0xff;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                sb.append("0");
            }
            sb.append(hv);
        }
        return sb.toString();
    }

}
