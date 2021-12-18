package com.comino.mavutils.rtps;

import java.nio.ByteBuffer;

//class RTPpacket

import java.util.*;

public class RTPpacket{
	
  public static final int MAX_PAYLOAD = 65515;

  //size of the RTP header:
  static int HEADER_SIZE = 12;

  //Fields that compose the RTP header
  public int Version;
  public int Padding;
  public int Extension;
  public int CC;
  public int Marker;
  public int PayloadType;
  public int SequenceNumber;
  public int TimeStamp;
  public int Ssrc;
  
  //Bitstream of the RTP header
  public final byte[] header;

  //size of the RTP payload
  public int payload_size;
  //Bitstream of the RTP payload
  public byte[] payload;
  
  
  public RTPpacket(int PType, int Framenb, int Time, byte[] data, int data_length){
      //fill by default header fields:
      Version = 2;
      Padding = 0;
      Extension = 0;
      CC = 0;
      Marker = 0;
      Ssrc = 1337;    // Identifies the server

      //fill changing header fields:
      SequenceNumber = Framenb;
      TimeStamp = Time;
      PayloadType = PType;
      
      //build the header bistream:
      header = new byte[HEADER_SIZE];

      //fill the header array of byte with RTP header fields
      header[0] = (byte)(Version << 6 | Padding << 5 | Extension << 4 | CC);
      header[1] = (byte)(Marker << 7 | PayloadType & 0x000000FF);
      header[2] = (byte)(SequenceNumber >> 8);
      header[3] = (byte)(SequenceNumber & 0xFF); 
      header[4] = (byte)(TimeStamp >> 24);
      header[5] = (byte)(TimeStamp >> 16);
      header[6] = (byte)(TimeStamp >> 8);
      header[7] = (byte)(TimeStamp & 0xFF);
      header[8] = (byte)(Ssrc >> 24);
      header[9] = (byte)(Ssrc >> 16);
      header[10] = (byte)(Ssrc >> 8);
      header[11] = (byte)(Ssrc & 0xFF);

      //fill the payload bitstream:
      payload_size = data_length;
      payload =  data;
  }
    
  //--------------------------
  //Constructor of an RTPpacket object from the packet bistream 
  //--------------------------
  public RTPpacket(byte[] packet, int packet_size)
  {
      //fill default fields:
      Version = 2;
      Padding = 0;
      Extension = 0;
      CC = 0;
      Marker = 0;
      Ssrc = 0;
      header = new byte[HEADER_SIZE];

      //check if total packet size is lower than the header size
      if (packet_size >= HEADER_SIZE) 
      {
          //get the header bitsream:
  
          for (int i=0; i < HEADER_SIZE; i++)
              header[i] = packet[i];

          //get the payload bitstream:
          payload_size = packet_size - HEADER_SIZE;
          payload = new byte[payload_size];
          for (int i=HEADER_SIZE; i < packet_size; i++)
              payload[i-HEADER_SIZE] = packet[i];

          //interpret the changing fields of the header:
          Version = (header[0] & 0xFF) >>> 6;
          PayloadType = header[1] & 0x7F;
          SequenceNumber = (header[3] & 0xFF) + ((header[2] & 0xFF) << 8);
          TimeStamp = (header[7] & 0xFF) + ((header[6] & 0xFF) << 8) + ((header[5] & 0xFF) << 16) + ((header[4] & 0xFF) << 24);
      }
  }

  
  public int getpayload(byte[] data) {

	  System.arraycopy(payload, 0, data, 0, payload_size);
      
      return(payload_size);
  }
  

  public int getpayload_length() {
      return(payload_size);
  }

 
  public int getlength() {
      return(payload_size + HEADER_SIZE);
  }

  
  
  public int getpacket(byte[] packet)
  {

      System.arraycopy(header, 0, packet, 0, HEADER_SIZE);
      System.arraycopy(payload, 0, packet, HEADER_SIZE, payload_size);
   
      //return total size of the packet
      return(payload_size + HEADER_SIZE);
  }

  //--------------------------
  //gettimestamp
  //--------------------------

  public int gettimestamp() {
      return(TimeStamp);
  }

  //--------------------------
  //getsequencenumber
  //--------------------------
  public int getsequencenumber() {
      return(SequenceNumber);
  }

  //--------------------------
  //getpayloadtype
  //--------------------------
  public int getpayloadtype() {
      return(PayloadType);
  }

  //--------------------------
  //print headers without the SSRC
  //--------------------------
  public void printheader()
  {
      System.out.print("[RTP-Header] ");
      System.out.println("Version: " + Version 
                         + ", Padding: " + Padding
                         + ", Extension: " + Extension 
                         + ", CC: " + CC
                         + ", Marker: " + Marker 
                         + ", PayloadType: " + PayloadType
                         + ", SequenceNumber: " + SequenceNumber
                         + ", Payload: " + payload_size
                         + ", TimeStamp: " + TimeStamp);

  }
}