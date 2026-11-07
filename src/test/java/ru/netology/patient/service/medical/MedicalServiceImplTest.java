package ru.netology.patient.service.medical;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.alert.SendAlertServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class MedicalServiceImplTest {

    private static SendAlertService sendAlertService = Mockito.spy(SendAlertServiceImpl.class);
    private static ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

    @Mock
    private PatientInfoRepository patientInfoRepository;


    @BeforeEach
    public void setUp() {
        System.out.println("\n");
    }

    @BeforeAll
    public static void initSendAlertService() {
        System.out.println("Init SendAlertService");
        Mockito.verify(sendAlertService).send(captor.capture());
    }

    @Test
    public void checkBloodPressureHasMsgTest(){
        System.out.println("Check Blood Pressure test - has message");

        // arrange
        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        String patientId = "100";
        BloodPressure bloodPressure = new BloodPressure(120, 80);
        PatientInfo patientInfo = new PatientInfo(patientId, "John", "Do", LocalDate.now(), new HealthInfo(BigDecimal.valueOf(36.6), new BloodPressure(120, 90)));
        Mockito.when(patientInfoRepository.getById(patientId)).thenReturn(patientInfo);

        // act
        medicalService.checkBloodPressure(patientId,bloodPressure);

        // assert - has message
    }

    @Test
    public void checkBloodPressureNoMsgTest(){
        System.out.println("Check Blood Pressure test - no message");

        // arrange
        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        String patientId = "100";
        BloodPressure bloodPressure = new BloodPressure(120, 80);
        PatientInfo patientInfo = new PatientInfo(patientId, "John", "Do", LocalDate.now(), new HealthInfo(BigDecimal.valueOf(36.6), bloodPressure));
        Mockito.when(patientInfoRepository.getById(patientId)).thenReturn(patientInfo);

        // act
        medicalService.checkBloodPressure(patientId,bloodPressure);

        // assert - no message
    }

    @Test
    public void checkTemperatureHasMsgTest(){
        System.out.println("Check Temperature test - has message");

        // arrange
        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        String patientId = "200";
        BloodPressure bloodPressure = new BloodPressure(120, 80);
        PatientInfo patientInfo = new PatientInfo(patientId, "John", "Do", LocalDate.now(), new HealthInfo(BigDecimal.valueOf(36.6), bloodPressure));

        Mockito.when(patientInfoRepository.getById(patientId)).thenReturn(patientInfo);
        // act
        medicalService.checkTemperature(patientId, BigDecimal.valueOf(30));

        // assert -
    }

    @Test
    public void checkTemperatureNoMsgTest(){
        System.out.println("Check Temperature test - no message");

        // arrange
        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        String patientId = "200";
        BloodPressure bloodPressure = new BloodPressure(120, 80);
        PatientInfo patientInfo = new PatientInfo(patientId, "John", "Do", LocalDate.now(), new HealthInfo(BigDecimal.valueOf(36.6), bloodPressure));
        Mockito.verify(sendAlertService).send(captor.capture());
        Mockito.when(patientInfoRepository.getById(patientId)).thenReturn(patientInfo);
        // act
        medicalService.checkTemperature(patientId, BigDecimal.valueOf(36.6 - 1.5));

        // assert -
        Mockito.verify(patientInfoRepository, Mockito.times(1)).getById("200");
        System.out.println("Captured args: " + captor.getAllValues());
    }

}
