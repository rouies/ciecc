package com.ciecc.common.io;


import com.ciecc.common.io.exception.SignedTypeFormatException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteOrder;

/*
 * ClassName : StreamProcessor
 * Creator   : louis zhang
 * Function  : Stream相关操作
 * Date      : 2015-8-15
 */
public class StreamProcessor {

	/*
	 * ClassName : Progress
	 * Creator   : louis zhang
	 * Function  : Stream进度回调接口
	 * Date      : 2015-8-15
	 */
	public static interface Progress{
		public void init(double current,double max);
		public void onProgress(double val);
	}

	/*
	 * MethodName : readLine
	 * MethodType : static
	 * Creator    : louis zhang
	 * Function   : 从输入流中读取一行数据
	 * Arguments  : InputStream input -> 要操作的输入流实例
	 * Return     : 读取的数据
	 */
	public static byte[] readLine(InputStream input) throws IOException{
		ByteArrayOutputStream out = new ByteArrayOutputStream(64);
		int bty = -1;
		while((bty = input.read())!= -1){
			if(bty != '\r' && bty !='\n'){
				out.write(bty);
			} else if(bty == '\n'){
				break;
			} else {
				bty = input.read();
				if(bty == '\n'){
					break;
				} else {
					out.write('\r');
					out.write(bty);
				}
			} 
		}
		return out.toByteArray();
	}

	/*
	 * MethodName : readLine
	 * MethodType : static
	 * Creator    : louis zhang
	 * Function   : 从输入流中读取一行数据
	 * Arguments  : InputStream input -> 要操作的输入流实例
	 *              String charset    -> 转换字符集
	 * Return     : 读取的数据
	 */
	public static String readLine(InputStream input,String charset) throws IOException{
		ByteArrayOutputStream out = new ByteArrayOutputStream(64);
		int bty = -1;
		while((bty = input.read())!= -1){
			if(bty != '\r' && bty !='\n'){
				out.write(bty);
			} else if(bty == '\r'){
				bty = input.read();
				if(bty == '\n'){
					break;
				} else {
					out.write('\r');
					out.write(bty);
				}
			} else {
				break;
			}
		}
		return new String(out.toByteArray(),charset);
	}

	/*
	 * MethodName : readByte
	 * MethodType : static
	 * Creator    : louis zhang
	 * Function   : 从输入流中读取所有数据
	 * Arguments  : InputStream input -> 要操作的输入流实例
	 * Return     : 读取的数据
	 */
	public static byte[] readByte(InputStream input) throws IOException{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		StreamProcessor.forword(input, out);
		byte[] result = out.toByteArray();
		out = null;
		return result;
	}

	/*
	 * MethodName : readByte
	 * MethodType : static
	 * Creator    : louis zhang
	 * Function   : 从输入流中读取指定长度数据
	 * Arguments  : InputStream input -> 要操作的输入流实例
	 *              long length       -> 要读取的数据长度
	 * Return     : 读取的数据
	 */
	public static byte[] readByte(InputStream input,long length) throws IOException{
		ByteArrayOutputStream out = new ByteArrayOutputStream(50);
		StreamProcessor.forword(input, out, length);
		byte[] result = out.toByteArray();
		out = null;
		return result;
	}

	/*
	 * MethodName : forword
	 * MethodType : static
	 * Creator    : louis zhang
	 * Function   : 将输入流中的数据转发到输出流中
	 * Arguments  : InputStream input -> 要操作的输入流实例
	 *              OutputStream out  -> 要操作的输出流实例
	 *              Progress progress -> 进度回调接口
	 * Return     : void
	 */
	public static void forword(InputStream input,OutputStream out,Progress progress) throws IOException{
		int index = -1;
		int bufferSize = 1024;
		double readBytes = 0;
		byte[] buffer = new byte[bufferSize];
		while((index = input.read(buffer, 0, bufferSize)) != -1){
			out.write(buffer, 0, index);
			if(progress != null){
				readBytes+=index;
				progress.onProgress(readBytes);
			}
		}
		buffer = null;
	}

	/*
	 * MethodName : forword
	 * MethodType : static
	 * Creator    : louis zhang
	 * Function   : 将输入流中的数据转发到输出流中
	 * Arguments  : InputStream input -> 要操作的输入流实例
	 *              OutputStream out  -> 要操作的输出流实例
	 * Return     : void
	 */
	public static void forword(InputStream input,OutputStream out) throws IOException{
		int index = -1;
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];
		while((index = input.read(buffer, 0, bufferSize)) != -1){
			out.write(buffer, 0, index);
		}
		buffer = null;
	}


	/*
	 * MethodName : forword
	 * MethodType : static
	 * Creator    : louis zhang
	 * Function   : 将输入流中指定长度的数据转发到输出流中
	 * Arguments  : InputStream input -> 要操作的输入流实例
	 *              OutputStream out  -> 要操作的输出流实例
	 *              long length       -> 要转发长度
	 *              Progress progress -> 进度回调接口
	 * Return     : void
	 */
	public static void forword(InputStream input,OutputStream out,long length,Progress progress) throws IOException{
		int index = -1;
		long readBytes = 0;
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];
		do{
			if(length - readBytes < bufferSize){
				bufferSize = (int) (length - readBytes);
			}
			index = input.read(buffer, 0, bufferSize);
			readBytes += index;
			out.write(buffer,0,index);
			if(progress != null){
				progress.onProgress(readBytes);
			}
		}
		while(index != -1 && readBytes < length);
		buffer = null;
	}

	/*
	 * MethodName : forword
	 * MethodType : static
	 * Creator    : louis zhang
	 * Function   : 将输入流中指定长度的数据转发到输出流中
	 * Arguments  : InputStream input -> 要操作的输入流实例
	 *              OutputStream out  -> 要操作的输出流实例
	 *              long length       -> 要转发长度
	 * Return     : void
	 */
	public static void forword(InputStream input,OutputStream out,long length) throws IOException{
		int index = -1;
		long readBytes = 0;
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];
		do{
			if(length - readBytes < bufferSize){
				bufferSize = (int) (length - readBytes);
			}
			index = input.read(buffer, 0, bufferSize);
			readBytes += index;
			out.write(buffer,0,index);
		}
		while(index != -1 && readBytes < length);
		buffer = null;
	}
	
	private InputStream inputStream;
	
	private OutputStream outputStream;
	
	private ByteOrder order;
	
	public InputStream getInputStream() {
		return inputStream;
	}

	public OutputStream getOutputStream() {
		return outputStream;
	}
	
	public ByteOrder getOrder() {
		return order;
	}

	public void setOrder(ByteOrder order) {
		this.order = order;
	}

	/*
	 * MethodName : 构造函数
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 通过输入流和输出流创建实例
	 * Arguments  : InputStream input -> 要操作的输入流实例
	 *              OutputStream out  -> 要操作的输出流实例
	 * Return     : void
	 */
	public StreamProcessor(InputStream inputStream,OutputStream outputStream){
		this.inputStream = inputStream;
		this.outputStream = outputStream;
		this.order = ByteOrder.BIG_ENDIAN;
	}

	/*
	 * MethodName : close
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 关闭输入输出流
	 * Arguments  :
	 * Return     : void
	 */
	public void close() throws IOException{
		if(this.inputStream!=null){
			this.inputStream.close();
		}
		if(this.outputStream!=null){
			this.outputStream.close();
		}
	}

	/*
	 * MethodName : readBytes
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 读取指定长度的数据
	 * Arguments  : InputStream input -> 要操作的输入流实例
	 *              OutputStream out  -> 要操作的输出流实例
	 *              long length       -> 要转发长度
	 *              Progress progress -> 进度回调接口
	 * Return     : void
	 */
	public byte[] readBytes(long length) throws IOException{
		return StreamProcessor.readByte(this.inputStream, length);
	}

	/*
	 * MethodName : readInt16
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 读取一个16位整数
	 * Arguments  :
	 * Return     : 16位整数
	 */
	public short readInt16() throws IOException, SignedTypeFormatException{
		byte[] data = StreamProcessor.readByte(this.inputStream,2);
		return SignedDataTypeConverter.toInt16(data, this.order);
	}

	/*
	 * MethodName : readInt32
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 读取一个32位整数
	 * Arguments  :
	 * Return     : 32位整数
	 */
	public int readInt32() throws IOException, SignedTypeFormatException{
		byte[] data = StreamProcessor.readByte(this.inputStream,4);
		return SignedDataTypeConverter.toInt32(data, this.order);
	}

	/*
	 * MethodName : readInt64
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 读取一个64位整数
	 * Arguments  :
	 * Return     : 64位整数
	 */
	public long readInt64() throws IOException, SignedTypeFormatException{
		byte[] data = StreamProcessor.readByte(this.inputStream,8);
		return SignedDataTypeConverter.toInt64(data, this.order);
	}

	/*
	 * MethodName : readFloat
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 读取一个浮点数
	 * Arguments  :
	 * Return     : 浮点数
	 */
	public float readFloat() throws IOException, SignedTypeFormatException {
		byte[] data = StreamProcessor.readByte(this.inputStream,4);
		return SignedDataTypeConverter.toFloat(data, this.order);
	}

	/*
	 * MethodName : readDouble
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 读取一个Double
	 * Arguments  :
	 * Return     : Double
	 */
	public double readDouble() throws IOException, SignedTypeFormatException{
		byte[] data = StreamProcessor.readByte(this.inputStream,8);
		return SignedDataTypeConverter.toDouble(data, this.order);
	}


	/*
	 * MethodName : readString
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 根据指定的字符集读取一个指定长度的字符串
	 * Arguments  : int length       -> 要读取字符串数据的长度
	 * 			    String charset   -> 字符串字符集
	 * Return     : 字符串
	 */
	public String readString(int length,String charset) throws IOException, SignedTypeFormatException{
		byte[] data = StreamProcessor.readByte(this.inputStream,length);
		return new String(data,charset);
	}

	/*
	 * MethodName : readInputStream
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 读取输入流中的说有数据到输出流
	 * Arguments  : OutputStream out -> 输出流
	 * Return     : void
	 */
	public void readInputStream(OutputStream out) throws IOException{
		StreamProcessor.forword(this.inputStream, out);
	}

	/*
	 * MethodName : readInputStream
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 读取输入流中的数据到输出流
	 * Arguments  : OutputStream out  -> 输出流
	 *              Progress progress -> 进度回调接口
	 * Return     : void
	 */
	public void readInputStream(OutputStream out,Progress progress) throws IOException{
		StreamProcessor.forword(this.inputStream, out,progress);
	}

	/*
	 * MethodName : readInputStream
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 读取输入流中的数据到输出流
	 * Arguments  : int length       -> 要读取字符串数据的长度
	 * 			    OutputStream out  -> 输出流
	 * Return     : void
	 */
	public void readInputStream(long length,OutputStream out) throws IOException{
		StreamProcessor.forword(this.inputStream, out,length);
	}

	/*
	 * MethodName : readInputStream
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 读取输入流中的数据到输出流
	 * Arguments  : int length       -> 要读取字符串数据的长度
	 * 			    OutputStream out  -> 输出流
	 *              Progress progress -> 进度回调接口
	 * Return     : void
	 */
	public void readInputStream(long length,OutputStream out,Progress progress) throws IOException{
		StreamProcessor.forword(this.inputStream, out,length,progress);
	}

	/*
	 * MethodName : writeBytes
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 像输出流中写入指定数据
	 * Arguments  : byte[] number  -> 要写入的数据
	 * Return     : void
	 */
	public void writeBytes(byte...number) throws IOException{
		this.outputStream.write(number);
	}

	/*
	 * MethodName : writeInt16
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 像输出流中写入16位整数
	 * Arguments  : short number  -> 要写入的数据
	 * Return     : void
	 */
	public void writeInt16(short number) throws IOException{
		this.outputStream.write(SignedDataTypeConverter.toByte(number, this.order));
	}

	/*
	 * MethodName : writeInt32
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 像输出流中写入32位整数
	 * Arguments  : int number  -> 要写入的数据
	 * Return     : void
	 */
	public void writeInt32(int number) throws IOException{
		this.outputStream.write(SignedDataTypeConverter.toByte(number, this.order));
	}

	/*
	 * MethodName : writeInt64
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 像输出流中写入64位整数
	 * Arguments  : long number  -> 要写入的数据
	 * Return     : void
	 */
	public void writeInt64(long number) throws IOException{
		this.outputStream.write(SignedDataTypeConverter.toByte(number, this.order));
	}

	/*
	 * MethodName : writeFloat
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 像输出流中写入浮点数
	 * Arguments  : float number  -> 要写入的数据
	 * Return     : void
	 */
	public void writeFloat(float number) throws IOException{
		this.outputStream.write(SignedDataTypeConverter.toByte(number, this.order));
	}

	/*
	 * MethodName : writeDouble
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 像输出流中写入Double
	 * Arguments  : float number  -> 要写入的数据
	 * Return     : void
	 */
	public void writeDouble(double number) throws IOException{
		this.outputStream.write(SignedDataTypeConverter.toByte(number, this.order));
	}

	/*
	 * MethodName : writeString
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 像输出流中写入字符串
	 * Arguments  : String str     -> 要写入的数据
	 *              String charset -> 字符集
	 * Return     : void
	 */
	public void writeString(String str,String charset) throws IOException{
		byte[] bytes = str.getBytes(charset);
		this.outputStream.write(bytes);
	}

	/*
	 * MethodName : writeFromInputStream
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 从指定的输入流中向输出流写数据
	 * Arguments  : InputStream in -> 输入流
	 * Return     : void
	 */
	public void writeFromInputStream(InputStream in) throws IOException{
		StreamProcessor.forword(in, this.outputStream);
	}


	/*
	 * MethodName : writeFromInputStream
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 从指定的输入流中向输出流写数据
	 * Arguments  : InputStream in    -> 输入流
	 *              Progress progress -> 进度回调接口
	 * Return     : void
	 */
	public void writeFromInputStream(InputStream in,Progress progress) throws IOException{
		StreamProcessor.forword(in, this.outputStream,progress);
	}


	/*
	 * MethodName : writeFromInputStream
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 从指定的输入流中向输出流写指定长度的数据
	 * Arguments  : InputStream in    -> 输入流
	 *              long length       -> 写入长度
	 * Return     : void
	 */
	public void writeFromInputStream(InputStream in,long length) throws IOException{
		StreamProcessor.forword(in, this.outputStream, length);
	}

	/*
	 * MethodName : writeFromInputStream
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 从指定的输入流中向输出流写指定长度的数据
	 * Arguments  : InputStream in    -> 输入流
	 *              long length       -> 写入长度
	 *              Progress progress -> 进度回调接口
	 * Return     : void
	 */
	public void writeFromInputStream(InputStream in,long length,Progress progress) throws IOException{
		StreamProcessor.forword(in, this.outputStream,length,progress);
	}

	/*
	 * MethodName : writeToOutputStream
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 从输入流中向指定输出流写数据
	 * Arguments  : OutputStream out    -> 输出流
	 * Return     : void
	 */
	public void writeToOutputStream(OutputStream out) throws IOException{
		StreamProcessor.forword(this.inputStream, out);
	}

	/*
	 * MethodName : writeToOutputStream
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 从输入流中向指定输出流写数据
	 * Arguments  : OutputStream out  -> 输出流
	 *              Progress progress -> 进度回调接口
	 * Return     : void
	 */
	public void writeToOutputStream(OutputStream out,Progress progress) throws IOException{
		StreamProcessor.forword(this.inputStream, out,progress);
	}

	/*
	 * MethodName : writeToOutputStream
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 从输入流中向指定输出流写数据
	 * Arguments  : OutputStream out  -> 输出流
	 *              long length       -> 写入长度
	 * Return     : void
	 */
	public void writeToOutputStream(OutputStream out,long length) throws IOException{
		StreamProcessor.forword(this.inputStream, out, length);
	}

	/*
	 * MethodName : writeToOutputStream
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 从输入流中向指定输出流写数据
	 * Arguments  : OutputStream out  -> 输出流
	 *              long length       -> 写入长度
	 *              Progress progress -> 进度回调接口
	 * Return     : void
	 */
	public void writeToOutputStream(OutputStream out,long length,Progress progress) throws IOException{
		StreamProcessor.forword(this.inputStream, out,length,progress);
	}
}
