package com.killarney.todolist.models;

/**
 * Created by Anthony on 7/11/2016.
 */
public class EventManagerTest {
/*
    EventManager eventManager;

    @Before
    public void setUp(){
        eventManager = EventManager.getInstance();
    }

    @After
    public void clearManager(){
        int size = eventManager.getEvents().size();

        do{
            for(int i=0; i<size; i++){
                eventManager.remove(0);
            }
        }while(eventManager.removeDepth());
    }

    @Test
    public void testInitialization(){
        assertEquals(0, eventManager.getEvents().size());
        assertEquals(0, eventManager.getEventsAtCurrentDepth().size());
    }

    @Test
    public void testAddEventSingle(){
        Event event = new Event(2017, 9, 30, 7, 30, "Hello", "World");
        try {
            eventManager.addEvent(2017, 9, 30, 7, 30, "Hello", "World", Event.class);
        } catch (Exception e) {
            fail();
        }

        assertEquals(1, eventManager.getEvents().size());
        assertEquals(1, eventManager.getEventsAtCurrentDepth().size());

        boolean x = event.equals(eventManager.getEventAtCurrentDepthAtPos(0));

        assertEquals(event, eventManager.getEventAtCurrentDepthAtPos(0));

        eventManager.remove(0);
        assertEquals(0, eventManager.getEvents().size());
        assertEquals(0, eventManager.getEventsAtCurrentDepth().size());
    }

    @Test (expected = InvalidClassException.class)
    public void testAddEventInvalidClass() throws InvalidClassException{
        try {
            eventManager.addEvent(2017, 9, 30, 7, 30, "Hello", "World", this.getClass());
        } catch (InvalidDateException e) {
            fail();
        } catch (InvalidTitleException e) {
            fail();
        }
    }

    @Test (expected = InvalidTitleException.class)
    public void testAddEventInvalidTitle() throws InvalidTitleException{
        try {
            eventManager.addEvent(2017, 9, 30, 7, 30, "", "World", Event.class);
        } catch (InvalidDateException e) {
            fail();
        } catch (InvalidClassException e) {
            fail();
        }
    }

    @Test (expected = InvalidDateException.class)
    public void testAddEventInvalidDate() throws InvalidDateException{
        try {
            eventManager.addEvent(2000, 9, 30, 7, 30, "Hello", "World", this.getClass());
        } catch (InvalidClassException e) {
            fail();
        } catch (InvalidTitleException e) {
            fail();
        }
    }

    @Test
    public void testAddEventMultiple(){
        Event event1 = new Event(2017, 9, 30, 7, 30, "Hello", "World");
        Event event2 = new Event(2020, 5, 20, 5, 20, "Hello1", "World1");
        TodoList tl = new TodoList(2021, 6, 21, 1, 2, "Hello2", "World2");
        try {
            eventManager.addEvent(2017, 9, 30, 7, 30, "Hello", "World", Event.class);
            eventManager.addEvent(2020, 5, 20, 5, 20, "Hello1", "World1", Event.class);
            eventManager.addEvent(2021, 6, 21, 1, 2, "Hello2", "World2", TodoList.class);
        } catch (Exception e) {
            fail();
        }

        assertEquals(3, eventManager.getEvents().size());
        assertEquals(3, eventManager.getEventsAtCurrentDepth().size());

        assertEquals(event1, eventManager.getEventAtCurrentDepthAtPos(0));
        assertEquals(event2, eventManager.getEventAtCurrentDepthAtPos(1));
        assertEquals(tl, eventManager.getEventAtCurrentDepthAtPos(2));

    }

    @Test
    public void testAddEventDepthTwo(){
        Event event1 = new Event(2017, 9, 30, 7, 30, "Hello", "World");
        Event event2 = new Event(2020, 5, 20, 5, 20, "Hello1", "World1");
        TodoList tl = new TodoList(2021, 6, 21, 1, 2, "Hello2", "World2");
        try {
            eventManager.addEvent(2021, 6, 21, 1, 2, "Hello2", "World2", TodoList.class);

            eventManager.addDepth(0);

            eventManager.addEvent(2017, 9, 30, 7, 30, "Hello", "World", Event.class);
            eventManager.addEvent(2020, 5, 20, 5, 20, "Hello1", "World1", Event.class);

        } catch (Exception e) {
            fail();
        }

        assertEquals(1, eventManager.getEvents().size());
        assertEquals(2, eventManager.getEventsAtCurrentDepth().size());

        assertEquals(event1, eventManager.getEventAtCurrentDepthAtPos(0));
        assertEquals(event2, eventManager.getEventAtCurrentDepthAtPos(1));
    }

    @Test
    public void testEditEvent() {
        Event event1 = new Event(2021, 6, 21, 1, 2, "Hello2", "Good");
        Event event2 = new Event(2018, 10, 20, 8, 10, "Day", "Good");
        try {
            eventManager.addEvent(2021, 6, 21, 1, 2, "Hello2", "World2", TodoList.class);
            eventManager.editDescAt("Good", 0);
            assertEquals("Good", eventManager.getDescriptionAt(0));
            assertEquals(event1, eventManager.getEventAtCurrentDepthAtPos(0));
            eventManager.editEvent(2018, 10, 20, 8, 10, "Day", "Good", 0);
            assertEquals(event2, eventManager.getEventAtCurrentDepthAtPos(0));
        } catch (Exception e) {
            fail();
        }

    }

    @Test (expected = InvalidTitleException.class)
    public void testEditEventInvalidTitle() throws InvalidTitleException {
        try {
            eventManager.addEvent(2021, 6, 21, 1, 2, "Hello2", "World2", TodoList.class);
            eventManager.editEvent(2018, 10, 20, 8, 10, "", "Good", 0);
        } catch (InvalidDateException e) {
            fail();
        } catch (InvalidClassException e) {
            fail();
        }
    }

    @Test (expected = InvalidDateException.class)
    public void testEditEventInvalidDate() throws InvalidDateException {
        try {
            eventManager.addEvent(2021, 6, 21, 1, 2, "Hello2", "World2", TodoList.class);
            eventManager.editEvent(2000, 10, 20, 8, 10, "Hello", "Good", 0);
        } catch (InvalidTitleException e) {
            fail();
        } catch (InvalidClassException e) {
            fail();
        }
    }


*/
}
