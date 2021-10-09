package unittests;

import models.Service;
import org.junit.*;
import services.ServiceService;

import static org.junit.Assert.*;

public class ServiceTest {
    private Service service1;
    private Service service2;
    private Service service3;
    private static final ServiceService serviceService = new ServiceService();

    @Before
    public void setUp() {
        service1 = new Service("Service1", 100);
        service2 = new Service("Service2", 500);
        service3 = new Service("Service3", 1000);
        serviceService.save(service1);
        serviceService.save(service2);
        serviceService.save(service3);
    }

    @Test
    public void inDatabase() {
        boolean actual = serviceService.contains(service1.getName()) &&
                         serviceService.contains(service2.getName()) &&
                         serviceService.contains(service3.getName());
        assertTrue(actual);
    }

    @Test
    public void edit() {
        service1.setName("New Service1");
        service2.setName("New Service2");
        service3.setName("New Service3");
        serviceService.update(service1);
        serviceService.update(service2);
        serviceService.update(service3);
        assertEquals("New Service1", serviceService.findService(service1.getId()).getName());
        assertEquals("New Service2", serviceService.findService(service2.getId()).getName());
        assertEquals("New Service3", serviceService.findService(service3.getId()).getName());
    }

    @After
    public void clean() {
        serviceService.delete(service1);
        serviceService.delete(service2);
        serviceService.delete(service3);
        boolean actual = serviceService.contains(service1.getName()) ||
                         serviceService.contains(service2.getName()) ||
                         serviceService.contains(service3.getName());
        assertFalse(actual);
    }

}
