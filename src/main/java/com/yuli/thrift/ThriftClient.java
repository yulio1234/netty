package com.yuli.thrift;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

public class ThriftClient {
    public static void main(String[] args) {
        TTransport tTransport = new TFramedTransport(new TSocket("localhost",8888),600);
        TProtocol protocol = new TCompactProtocol(tTransport);
        PersonService.Client client = new PersonService.Client(protocol);
        try {
            tTransport.open();
            Person yuli = client.getPersonByUsername("yuli");
            System.out.println(yuli.getAge());
            System.out.println(yuli.getUsername());
            System.out.println(yuli.isMarried());

            Person person = new Person();
            person.setUsername("gaga");
            person.setAge(14);
            person.setMarried(true);
            client.savePerson(person);
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (DataException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        } finally {
            tTransport.close();
        }

    }
}
