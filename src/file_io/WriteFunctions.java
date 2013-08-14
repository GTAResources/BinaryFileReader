package file_io;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kilian
 */
public class WriteFunctions {
    private RandomAccessFile data_out;

    /**
     * returns if a certain flag is on
     * @param flags
     * @param flag
     * @return if a flag has been set
     */
    public boolean hasFlag(int flags, int flag) {
        boolean hasFlag = false;
        boolean finished = false;
        int waarde = 1024;
        int newFlag = flags;
        while (!finished) {
            newFlag -= waarde;
            if (waarde < flag) {
                finished = true;
            } else {
                if (newFlag <= 0) {
                    if (waarde == 1) {
                        finished = true;
                    }
                    newFlag = flags;
                    waarde /= 2;
                } else {
                    flags = newFlag;
                    if (waarde == flag) {
                        hasFlag = true;
                        finished = true;
                    } else {
                        waarde /= 2;
                    }
                }
            }
        }
        return hasFlag;
    }

    /**
     * Opens a file
     * @param name
     * @return inputStream of the file
     */
    public boolean openFile(String name) {
        try {
            data_out = new RandomAccessFile(name, "rw");
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    /**
     * Closes the FileInputStream and DataInputStream
     * @param file_in
     * @param data_in
     */
    public boolean closeFile() {
        try {
            data_out.close();
        } catch (IOException ex) {
            Logger.getLogger(WriteFunctions.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    /**
     * Returns a byte from the DataInputStream
     * @param data_in
     * @return a byte from the DataInputStream
     */
    public void writeByte(int waarde) {
        try {
            data_out.writeByte(waarde);
        } catch (IOException ex) {
            waarde = -1;
        }
    }

    /**
     * Returns an int from the DataInputStream
     * @param data_in
     * @return an int from the DataInputStream
     */
    public void writeInt(int waarde) {
        ByteBuffer bbuf = ByteBuffer.allocate(4);
        bbuf.order(ByteOrder.BIG_ENDIAN);
        bbuf.putInt(waarde);
        bbuf.order(ByteOrder.LITTLE_ENDIAN);
        waarde = bbuf.getInt(0);
        try {
            data_out.writeInt(waarde);
        } catch (IOException ex) {
            waarde = -1;
        }
    }

    /**
     * Returns a short as an int from the DataInputStream
     * @param data_in
     * @return a short as an int from the DataInputStream
     */
    public void writeShort(int waarde) {
        ByteBuffer bbuf = ByteBuffer.allocate(4);
        bbuf.order(ByteOrder.BIG_ENDIAN);
        bbuf.putInt(waarde);
        bbuf.order(ByteOrder.LITTLE_ENDIAN);
        waarde = bbuf.getShort(2);
        try {
            data_out.writeShort(waarde);
        } catch (IOException ex) {
            waarde = -1;
        }
    }

    /**
     * Returns a float from the DataInputStream
     * @param data_in
     * @return a float from the DataInputStream
     */
    public void writeFloat(float waarde) {
        ByteBuffer bbuf = ByteBuffer.allocate(4);
        bbuf.order(ByteOrder.BIG_ENDIAN);
        bbuf.putFloat(waarde);
        bbuf.order(ByteOrder.LITTLE_ENDIAN);
        waarde = bbuf.getFloat(0);
        try {
            data_out.writeFloat(waarde);
        } catch (IOException ex) {
            Logger.getLogger(WriteFunctions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a Char from the DataInputStream
     * @param data_in
     * @return a char from the DatainputStream
     */
    public char writeChar(char waarde) {
        char letter = '\0';
        try {
            data_out.writeByte(waarde);
        } catch (IOException ex) {
            //Logger.getLogger(loadSAFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        return letter;
    }

    /**
     * writes a String to the dataoutputstream
     * @param data_in
     * @return a String
     */
    public void writeString(String waarde) {
        for(int i = 0; i < waarde.length(); i++){
            writeChar(waarde.charAt(i));
        }
    }

    /**
     * writes a String from the DataInputStream till a \0 char
     * @param data_in
     * @return a String
     */
    public void writeNullTerminatedString(String waarde) {
        for(int i = 0; i < waarde.length(); i++){
            writeChar(waarde.charAt(i));
        }
        writeByte(0);
    }

    public void writeVector3D(Vector3D vector){
        writeFloat(vector.x);
        writeFloat(vector.y);
        writeFloat(vector.z);
    }

    public void writeVector4D(Vector4D vector){
        writeFloat(vector.x);
        writeFloat(vector.y);
        writeFloat(vector.z);
        writeFloat(vector.w);
    }

    public void writeArray(byte[] array){
        try {
            data_out.write(array);
        } catch (IOException ex) {
            Logger.getLogger(WriteFunctions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void seek(int pos){
        try {
            data_out.seek(pos);
        } catch (IOException ex) {
            Logger.getLogger(WriteFunctions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void gotoEnd(){
        try {
            data_out.seek(data_out.length());
        } catch (IOException ex) {
            Logger.getLogger(WriteFunctions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getFileSize(){
        try {
            return (int) data_out.length();
        } catch (IOException ex) {
            return 0;
        }
    }

}
