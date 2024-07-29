package com.kilowhatt.kilowhatt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class EnergyService {
    @Autowired
    private EnergyRepository energyRepository;

    @Autowired
    private SerialService serialService;

    public List<EnergyMeter> allEnergies() {
        return energyRepository.findAll();
    }

    public EnergyMeter createMeasurement(String value, String timeStamp) {
        return energyRepository.insert(new EnergyMeter(value, timeStamp));
    }

    public EnergyMeter getCurrentEnergyMeasurement() {
        serialService.writeToSerial("GET_ENERGY\n");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String energyData = serialService.readFromSerial();
        if (energyData == null || energyData.isEmpty()) {
            return null;
        }

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        return createMeasurement(energyData + " Wh", timeStamp);
    }

    // Включення реле
    public void turnOnRelay(int relayNumber) {
        serialService.turnOnRelay(relayNumber);
    }

    // Вимкнення реле
    public void turnOffRelay(int relayNumber) {
        serialService.turnOffRelay(relayNumber);
    }

    // Додайте новий метод для обнулення
    public EnergyMeter resetCurrentEnergy() {
        serialService.resetEnergy(); // Відправляємо команду на обнулення мікроконтролеру
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        EnergyMeter resetEnergy = new EnergyMeter("0", timeStamp);
        return energyRepository.insert(resetEnergy);
    }
}
