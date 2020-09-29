package com.ciecc.common.network.tcp;


import java.io.IOException;

public class DefaultTcpProcessor implements TcpServer.TcpProcessor {

	@Override
	public void process(TcpClient client) {
		try {
//			try {
//				int readInt32 = client.getProcessor().readInt32();
//				System.out.println(readInt32);
//			} catch (SignedTypeFormatException e) {
//				e.printStackTrace();
//			}
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
