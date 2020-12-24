package com.bang.ap.dp.socket;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class SocketClient {
    public static void main(String[] args) {
       int result= bytes2Int(new byte[0x55AAAA55]);
        System.out.println(result);
        System.out.println(int2bytes(0));
       /* String ip="121.248.106.130";
        int port=4008;

        try {
            Socket socket =new Socket(ip,port);
            OutputStream outputStream =socket.getOutputStream();

            //数据包
            String data ="ABCDEF";
            //4字节标志为
            byte[]head=new byte[]{};
            //4字节指令
            byte[]cmd=new byte[]{};
            //4字节包长度
            byte[]dataLength=int2bytes(data.length());

            outputStream.write(head);
            outputStream.write(cmd);
            outputStream.write(dataLength);
            outputStream.write(data.getBytes());
            outputStream.flush();
            outputStream.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
*/


    }

    public static byte[] int2bytes(int i){
        byte[] bytes=new byte[4];
        bytes[0]=(byte) (i>>>24&0xff);
        bytes[1]=(byte) (i>>>16&0xff);
        bytes[2]=(byte) (i>>>8&0xff);
        bytes[3]=(byte) (i&0xff);
        return bytes;
    }
    public static int bytes2Int(byte[] tar){
        int byte3=tar[3];
        int byte2=tar[2]<<8;
        int byte1=tar[1]<<16;
        int byte0=tar[0]<<24;
        return byte0|byte1|byte2|byte3;
    }

}
