package unittests;

import models.*;
import org.junit.*;
import services.*;

import java.time.LocalDate;
import java.time.LocalTime;
import static org.junit.Assert.*;

public class WorkerTest {
    private Worker worker1;
    private Worker worker2;
    private Worker worker3;
    private static final WorkerService workerService = new WorkerService();

    @Before
    public void setUp() {
        worker1 = new Worker("Name1", "Surname1", "Spec1");
        worker2 = new Worker("Name2", "Surname2", "Spec2");
        worker3 = new Worker("Name3", "Surname3", "Spec3");
        workerService.save(worker1);
        workerService.save(worker2);
        workerService.save(worker3);
    }

    @Test
    public void edit() {
        worker1.setName("new Name1");
        worker2.setName("new Name2");
        worker3.setName("new Name3");
        workerService.update(worker1);
        workerService.update(worker2);
        workerService.update(worker3);
        assertEquals("new Name1", workerService.findWorker(worker1.getId()).getName());
        assertEquals("new Name2", workerService.findWorker(worker2.getId()).getName());
        assertEquals("new Name3", workerService.findWorker(worker3.getId()).getName());
    }


    @After
    public void clean() {
        workerService.delete(worker1);
        workerService.delete(worker2);
        workerService.delete(worker3);
        boolean actual = workerService.contains(worker1.getName(), worker1.getSurname()) ||
                         workerService.contains(worker2.getName(), worker2.getSurname()) ||
                         workerService.contains(worker3.getName(), worker3.getSurname());
        assertFalse(actual);
    }
}
