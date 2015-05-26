package nl.fontys.exercisecontrol.exercise.recorder;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Handler;
import android.os.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Vincent on 21.05.15.
 */
public class MeasurementCollectorAdaptor implements SensorEventListener {

    private final MeasurementCollector collector;
    private final List<MeasurementSensorData> sensorData;
    private final Handler messageHandler;
    private final Map<Sensor, MeasurementAdaptor> adaptorMap;
    private long startTime = 0;

    public MeasurementCollectorAdaptor(MeasurementCollector collector, List<MeasurementSensorData> sensorData, Handler messageHandler) {
        this.collector = collector;
        this.sensorData = sensorData;
        this.messageHandler = messageHandler;
        adaptorMap = new HashMap<Sensor, MeasurementAdaptor>();
    }

    public void onRecordingStart(String guid) throws MeasurementException {
        startTime = System.nanoTime();
        collector.startCollecting(guid);

        for (MeasurementSensorData data : sensorData)
            adaptorMap.put(data.getSensor(), new MeasurementAdaptor(data, collector, startTime));
    }

    public void onRecordingStop() throws MeasurementException {
        adaptorMap.clear();
        collector.stopCollecting((double)(System.nanoTime() - startTime) / 1000000000.0);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        MeasurementAdaptor adaptor;

        if ((adaptor = adaptorMap.get(event.sensor)) == null)
            return;

        try {
            adaptor.sensorEvent(System.nanoTime(), event);
        } catch (MeasurementException ex) {
            adaptorMap.clear();
            Message message = new Message();
            message.obj = MeasurementRecorderMessageObject.fail(ex);
            messageHandler.sendMessage(message);
        }
    }

    public void onRecordingFailed(MeasurementException ex) {
        adaptorMap.clear();
        collector.collectionFailed(ex);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }
}
