package com.kilowhatt.kilowhatt;

import com.fazecast.jSerialComm.SerialPort;
import org.springframework.stereotype.Service;

@Service
public class SerialService {

    private SerialPort serialPort;

    public SerialService() {
        serialPort = SerialPort.getCommPort("COM3"); // або "COM3" для Windows
        serialPort.setBaudRate(9600);
        serialPort.openPort();
    }

    public String readFromSerial() {
        if (serialPort.isOpen()) {
            byte[] readBuffer = new byte[1024];
            int numRead = 0;
            for (int i = 0; i < 5; i++) {
                if (serialPort.bytesAvailable() > 0) {
                    numRead = serialPort.readBytes(readBuffer, readBuffer.length);
                    break;
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return new String(readBuffer, 0, numRead).trim();
        }
        return null;
    }

    public void writeToSerial(String data) {
        if (serialPort.isOpen()) {
            serialPort.writeBytes(data.getBytes(), data.length());
        }
    }

    // Метод для включення реле
    public void turnOnRelay(int relayNumber) {
        writeToSerial("TURN_ON_RELAY" + relayNumber + "\n");
    }

    // Метод для вимкнення реле
    public void turnOffRelay(int relayNumber) {
        writeToSerial("TURN_OFF_RELAY" + relayNumber + "\n");
    }

    public void resetEnergy() {
        writeToSerial("RESET_ENERGY\n");
    }
}
