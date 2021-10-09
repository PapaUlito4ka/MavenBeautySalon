package unittests;

import models.*;
import org.junit.*;
import services.*;

import java.time.LocalDate;
import java.time.LocalTime;
import static org.junit.Assert.*;

public class ClientTest {
    private Client client1;
    private Client client2;
    private Client client3;
    private static final ClientService clientService = new ClientService();

    @Before
    public void setUp() {
        client1 = new Client("Name1", "Surname1");
        client2 = new Client("Name2", "Surname2");
        client3 = new Client("Name3", "Surname3");
        clientService.save(client1);
        clientService.save(client2);
        clientService.save(client3);
    }

    @Test
    public void edit() {
        client1.setName("new Name1");
        client2.setName("new Name2");
        client3.setName("new Name3");
        clientService.update(client1);
        clientService.update(client2);
        clientService.update(client3);
        assertEquals("new Name1", clientService.findClient(client1.getId()).getName());
        assertEquals("new Name2", clientService.findClient(client2.getId()).getName());
        assertEquals("new Name3", clientService.findClient(client3.getId()).getName());
    }

    @After
    public void clean() {
        clientService.delete(client1);
        clientService.delete(client2);
        clientService.delete(client3);
        boolean actual = clientService.contains(client1.getName(), client1.getSurname()) ||
                         clientService.contains(client2.getName(), client2.getSurname()) ||
                         clientService.contains(client3.getName(), client3.getSurname());
        assertFalse(actual);
    }
}
