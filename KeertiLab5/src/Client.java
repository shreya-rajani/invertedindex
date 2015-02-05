
import java.io.*;
import java.net.*;
 
public class Client {
    private DatagramSocket socket = null;
    private FileEvent fileEvent = null;
    
    public Client() {}
 
    public void createConnection() {
        
    	try{
    		socket = new DatagramSocket(6773);
        	byte[] incomingData = new byte[64512];
            while (true) 
            {
            	
                DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                socket.receive(incomingPacket);
                byte[] data = incomingPacket.getData();
                System.out.println("start");
                String fileName =  "/Users/keertisekharsahoo/Documents/file/images.jpeg";
        		File fCache = new File(fileName);
        		DataOutputStream fStream;
        		fStream = new DataOutputStream(new FileOutputStream(fCache));
        		fStream.write(data);
        		fStream.close();
            }
		}
        catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
       }


                //System.out.println("incoming data length is "+data.length);
                //System.out.println("data is "+data);
//                for(int i=0;i<64512;i++)
//                {
//                	System.out.println(data[i]);
//                	if(data[i]==115)
//                	{
//                		System.out.println("i is "+i);
//                	}
//                }
//                System.out.println("last value"+data[64511]);
//                byte[] receivedPacket = new byte[64512];
//                System.arraycopy(data, 0, receivedPacket, 0, 64512);
                //ByteArrayInputStream in = new ByteArrayInputStream(data);
//                ObjectInputStream is = new ObjectInputStream(in);
//                System.out.println("received len"+receivedPacket.length);
      
                //while( (String)is.readObject() != null)
//                System.out.println("incoming data length is "+data.length);
//                byte[] receivedPacket = new byte[64512];
//                int b;
//                int i=0;
//                while( (b = in.read()) != -1)
//                {
//                 receivedPacket[i] = (byte) b;
//                 //System.out.println(receivedPacket[i]);
//                 i++;
//                }
                //System.out.println("received packet size is "+receivedPacket.length);
//                System.exit(0);
               //ByteArrayInputStream ins = new ByteArrayInputStream(data);
                //ObjectInputStream ois = new ObjectInputStream(ins);
//                if((String) is.readObject() != null)
//                {
//                	System.out.println("inside if condition");
//                for(int j=0;j<64512;j++)
//                {
//                	//System.out.println(receivedPacket[j]);
//                }
                //System.exit(0);
                //while( (String) ois.readObject() != null)
//                {
                //fileEvent = (FileEvent) ois.readObject();
                //}
                
                //System.out.println("file event is "+fileEvent);
                
                
                
                
                
                
//                if (fileEvent.getStatus().equalsIgnoreCase("Error")) 
//                {
//                    System.out.println("Some issue happened while packing the data @ server side");
//                    System.exit(0);
//                }
                //createAndWriteFile(data);   // writing the file to hard disk
//                String filename= "C:/SO/SOBufferedOutputStreamAnswer";
//                BufferedOutputStream bos = null;
//                try {
//                //create an object of FileOutputStream
//                FileOutputStream fos = new FileOutputStream(new File(filename));
//
//                //create an object of BufferedOutputStream
//                bos = new BufferedOutputStream(fos);
//
//                KeyGenerator kgen = KeyGenerator.getInstance("AES"); 
//                kgen.init(128); 
//                SecretKey key = kgen.generateKey(); 
//                byte[] encoded = key.getEncoded();
//
//                bos.write(encoded);

                //System.out.println("data array length is "+data.length);
//                FileOutputStream fileOutputStream = null;
//                String fileName = "/Users/keertisekharsahoo/Documents/file/images.jpeg";
//                    fileOutputStream = new FileOutputStream(new File(fileName));
//                    fileOutputStream.write(data);
//                    //fileOutputStream.write(data);
//                    fileOutputStream.flush();
//                    fileOutputStream.close();
//                    System.out.println("Output file : " + fileOutputStream + " is successfully saved ");
                        			
                
//                InetAddress IPAddress = incomingPacket.getAddress();
//                int port = incomingPacket.getPort();
//                String reply = "Thank you for the message";
//                byte[] replyBytea = reply.getBytes();
//                DatagramPacket replyPacket = new DatagramPacket(replyBytea, replyBytea.length, IPAddress, port);
//                socket.send(replyPacket);
                //Thread.sleep(3000);
                	//System.exit(0);
            	//    	catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            } 
//    		catch (InterruptedException e) {
//                e.printStackTrace();
//            }
    	}
    
 
//            public void createAndWriteFile(byte[] receivedPacket) 
//            {
//            	System.out.println("inside createAndWriteFile");
//                String outputFile = fileEvent.getDestinationDirectory() + fileEvent.getFilename();
//                if (!new File(fileEvent.getDestinationDirectory()).exists()) 
//                {
//                    new File(fileEvent.getDestinationDirectory()).mkdirs();
//                }
//                File dstFile = new File(outputFile);
//                FileOutputStream fileOutputStream = null;
//                try {
//                    fileOutputStream = new FileOutputStream(dstFile);
//                    fileOutputStream.write(fileEvent.getFileData());
//                    //fileOutputStream.write(data);
//                    fileOutputStream.flush();
//                    fileOutputStream.close();
//                    System.out.println("Output file : " + outputFile + " is successfully saved ");
//                	} 
//                catch (FileNotFoundException e) 
//                {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
 
    public static void main(String[] args) {
        Client client = new Client();
        client.createConnection();
    }
}