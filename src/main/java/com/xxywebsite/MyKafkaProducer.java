package com.xxywebsite;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xxywebsite.model.Student;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.ByteArraySerializer;

import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * author: xuxiaoyin
 * date:  3/22/2021 12:18 AM
 * description:
 */
public class MyKafkaProducer {
    private static final String TEST_TOPIC = "test_topic";
    private static final String BROKERS = "localhost:9092";
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Properties properties = createKafkaProperties();
        KafkaProducer<byte[], byte[]> kafkaProducer = new KafkaProducer<>(properties);
        Student student = new Student();
        student.setName("Cookie");
        student.setAge(22);
        byte[] value = JSONObject.toJSONBytes(student, SerializerFeature.NotWriteDefaultValue);
        ProducerRecord<byte[], byte[]> record = new ProducerRecord<>(TEST_TOPIC, value);
        Future<RecordMetadata> future = kafkaProducer.send(record);

        RecordMetadata recordMetadata = future.get();
        kafkaProducer.flush();
        System.out.println("waiting");
    }

    private static Properties createKafkaProperties() {
        Properties kafkaProps = new Properties();
        kafkaProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKERS);
        kafkaProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getCanonicalName());
        kafkaProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getCanonicalName());
        return kafkaProps;
    }
}
