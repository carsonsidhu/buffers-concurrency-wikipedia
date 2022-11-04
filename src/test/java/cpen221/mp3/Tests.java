package cpen221.mp3;

import cpen221.mp3.fsftbuffer.BufferableString;
import cpen221.mp3.fsftbuffer.FSFTBuffer;
import cpen221.mp3.fsftbuffer.IdNotFoundException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class Tests {

    private static final boolean REPEAT_TESTS = false;

    @Test
    public void FSFTTest1() throws Exception {
        int capacity = 10;
        int timeout = 5;

        FSFTBuffer<BufferableString> buffer = new FSFTBuffer<>(capacity, timeout);
        BufferableString s = new BufferableString("Testing123");

        buffer.put(s);
        assertEquals(s, buffer.get(s.id()));
    }

    @Test
    public void FSFTTest2() {
        int capacity = 10;
        int timeout = 2;

        FSFTBuffer<BufferableString> buffer = new FSFTBuffer<>(capacity, timeout);
        BufferableString s = new BufferableString("Testing123");
        buffer.put(s);
        assertTrue(buffer.touch(s.id()));
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertFalse(buffer.touch(s.id()));
    }

    @Test
    public void FSFTTest3() {
        int capacity = 5;
        int timeout = 10;

        FSFTBuffer<BufferableString> buffer = new FSFTBuffer<>(capacity, timeout);

        for (int i = 0; i <= capacity; i++) {
            BufferableString s = new BufferableString(Integer.toString(i));
            buffer.put(s);
            assertTrue(buffer.touch(s.id()));
        }
        assertFalse(buffer.touch("0"));
    }

    @Test
    public void FSFTTest4() throws Exception {
        int capacity = 10;
        int timeout = 1;
        int numRepeats = REPEAT_TESTS ? 50 : 1;

        for (int i = 0; i < numRepeats; i++) {
            FSFTBuffer<BufferableString> buffer = new FSFTBuffer<>(capacity, timeout);
            BufferableString s = new BufferableString("anotherStringToCheck");
            buffer.put(s);
            Thread testThread = new Thread(() -> {
                while (true) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    buffer.touch(s.id());
                }
            });

            testThread.start();
            TimeUnit.SECONDS.sleep(2);
            assertEquals(s, buffer.get(s.id()));
        }
    }

    @Test
    public void FSFTTest5() throws Exception {
        int capacity = 5;
        int timeout = 3;
        int numRepeats = REPEAT_TESTS ? 1000 : 1;

        for (int i = 0; i < numRepeats; i++) {
            FSFTBuffer<BufferableString> buffer = new FSFTBuffer<>(capacity, timeout);
            BufferableString s1 = new BufferableString("Test 1");
            BufferableString s2 = new BufferableString("Test 2");
            BufferableString s3 = new BufferableString("Test 3");
            BufferableString s4 = new BufferableString("Test 4");
            BufferableString s5 = new BufferableString("Test 5");

            Thread testThread1 = new Thread(() -> {
                buffer.put(s1);
                buffer.put(s2);
                buffer.put(s3);
            });
            Thread testThread2 = new Thread(() -> {
                buffer.put(s4);
                buffer.put(s5);
            });
            testThread1.start();
            testThread2.start();
            testThread1.join();
            testThread2.join();

            Thread testThread3 = new Thread(() -> {
                assertTrue(buffer.touch("Test 1"));
                assertTrue(buffer.touch("Test 3"));

                try {
                    assertEquals(s4, buffer.get("Test 4"));
                    assertEquals(s5, buffer.get("Test 5"));
                    assertNotEquals(s3, buffer.get("Test 1"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            Thread testThread4 = new Thread(() -> {
                assertTrue(buffer.touch("Test 5"));
                assertTrue(buffer.touch("Test 2"));
                assertTrue(buffer.touch("Test 4"));
                assertFalse(buffer.touch("Test 7"));

                try {
                    assertEquals(s1, buffer.get("Test 1"));
                    assertEquals(s2, buffer.get("Test 2"));
                    assertEquals(s3, buffer.get("Test 3"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            testThread3.start();
            testThread4.start();
        }
    }


    @Test
    public void FSFTTest6() throws Exception {
        int capacity = 10;
        int timeout = 1;

        FSFTBuffer<BufferableString> buffer = new FSFTBuffer<>(capacity, timeout);
        BufferableString s = new BufferableString("Testing123", "TestID");
        buffer.put(s);
        assertTrue(buffer.touch(s.id()));
        BufferableString s2 = new BufferableString("Testing456", "TestID");


        Thread testThread = new Thread(() -> assertTrue(buffer.update(s2)));

        testThread.start();
        testThread.join();
        assertEquals(s2, buffer.get("TestID"));
        TimeUnit.SECONDS.sleep(2);
        assertFalse(buffer.update(s2));
    }

    @Test
    public void FSFTTest7() throws Exception {
        int capacity = 5;
        int timeout = 1;
        int numRepeats = REPEAT_TESTS ? 50 : 1;


        for (int i = 0; i < numRepeats; i++) {
            FSFTBuffer<BufferableString> buffer = new FSFTBuffer<>(capacity, timeout);
            BufferableString s1 = new BufferableString("Test 1");
            BufferableString s2 = new BufferableString("Test 2");
            BufferableString s3 = new BufferableString("Test 3");
            BufferableString s4 = new BufferableString("Test 4");
            BufferableString s5 = new BufferableString("Test 5");

            buffer.put(s1);
            buffer.put(s2);
            buffer.put(s3);
            buffer.put(s4);
            buffer.put(s5);

            Thread testThread2 = new Thread(() -> {
                while (true) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    buffer.touch(s2.id());
                    buffer.touch(s3.id());
                    buffer.touch(s4.id());
                }
            });

            testThread2.start();
            TimeUnit.SECONDS.sleep(2);
            boolean s1Exception = false;
            boolean s5Exception = false;
            try {
                assertEquals(s1, buffer.get(s1.id()));

            } catch (Exception e) {
                s1Exception = true;
            }
            try {
                assertEquals(s5, buffer.get(s5.id()));

            } catch (Exception e) {
                s5Exception = true;
            }

            assertTrue(s1Exception && s5Exception);

            assertEquals(s2, buffer.get(s2.id()));
            assertEquals(s3, buffer.get(s3.id()));
            assertEquals(s4, buffer.get(s4.id()));
        }
    }

    @Test(expected = Exception.class)
    public void FSFTTest8_exception() throws Exception {
        int capacity = 10;
        int timeout = 5;

        FSFTBuffer<BufferableString> buffer = new FSFTBuffer<>(capacity, timeout);
        BufferableString s = new BufferableString("This is a test");

        assertEquals(s, buffer.get("aNonExistentID"));
    }

    @Test
    public void FSFTTest9() throws Exception {
        int capacity = 10;
        int timeout = 1;
        int numRepeats = REPEAT_TESTS ? 50 : 1;

        for (int i = 0; i < numRepeats; i++) {
            FSFTBuffer<BufferableString> buffer = new FSFTBuffer<>(capacity, timeout);
            BufferableString s1 = new BufferableString("Test 1");
            BufferableString s2 = new BufferableString("Test 2");
            BufferableString s3 = new BufferableString("Test 3");
            BufferableString s4 = new BufferableString("Test 4");
            BufferableString s5 = new BufferableString("Test 5");
            BufferableString s6 = new BufferableString("Test 6");
            BufferableString s7 = new BufferableString("Test 7");
            BufferableString s8 = new BufferableString("Test 8");
            BufferableString s9 = new BufferableString("Test 9");
            BufferableString s10 = new BufferableString("Test 10");

            buffer.put(s1);
            buffer.put(s2);
            buffer.put(s3);
            buffer.put(s4);
            buffer.put(s5);
            buffer.put(s6);
            buffer.put(s7);
            buffer.put(s8);
            buffer.put(s9);
            buffer.put(s10);

            Thread testThread2 = new Thread(() -> {
                while (true) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    buffer.touch(s1.id());
                    buffer.touch(s2.id());
                    buffer.touch(s3.id());
                    buffer.touch(s4.id());
                    buffer.touch(s5.id());
                    buffer.touch(s7.id());
                    buffer.touch(s8.id());
                    buffer.touch(s10.id());
                }
            });

            testThread2.start();
            TimeUnit.SECONDS.sleep(2);
            boolean s6Exception = false;
            boolean s9Exception = false;
            try {
                assertEquals(s6, buffer.get(s6.id()));

            } catch (Exception e) {
                s6Exception = true;
            }
            try {
                assertEquals(s9, buffer.get(s9.id()));

            } catch (Exception e) {
                s9Exception = true;
            }

            assertTrue(s6Exception && s9Exception);
            assertEquals(s1, buffer.get(s1.id()));
            assertEquals(s2, buffer.get(s2.id()));
            assertEquals(s3, buffer.get(s3.id()));
            assertEquals(s4, buffer.get(s4.id()));
            assertEquals(s5, buffer.get(s5.id()));
            assertEquals(s7, buffer.get(s7.id()));
            assertEquals(s8, buffer.get(s8.id()));
            assertEquals(s10, buffer.get(s10.id()));
        }
    }

    @Test
    public void FSFTTest10() throws Exception {
        int capacity = 3;
        int timeout = 5;

        FSFTBuffer<BufferableString> buffer = new FSFTBuffer<>(capacity, timeout);
        BufferableString s1 = new BufferableString("Test 1");
        BufferableString s2 = new BufferableString("Test 2");
        BufferableString s3 = new BufferableString("Test 3");

        assertTrue(buffer.put(s1));
        assertTrue(buffer.put(s2));
        assertTrue(buffer.put(s3));
        assertFalse(buffer.put(s1));

        assertEquals(s1, buffer.get("Test 1"));
        assertEquals(s2, buffer.get("Test 2"));
        assertEquals(s3, buffer.get("Test 3"));
    }


    @Test
    public void FSFTTest11() throws InterruptedException {
        int capacity = 100;
        int timeout = 5;
        int numRepeats = REPEAT_TESTS ? 1000 : 1;
        int numThreads = 100;

        for (int i = 0; i < numRepeats; i++) {
            FSFTBuffer<BufferableString> buffer = new FSFTBuffer<>(capacity, timeout);

            List<Thread> threads = new ArrayList<>();
            for (int j = 0; j < numThreads; j++) {
                int finalJ = j;
                Thread t = new Thread(() -> {
                    BufferableString s = new BufferableString(Integer.toString(finalJ));
                    buffer.put(s);
                    buffer.touch(s.id());
                    try {
                        buffer.get(Integer.toString(finalJ));
                    } catch (IdNotFoundException e) {
                        e.printStackTrace();
                    }

                });
                t.start();
                threads.add(t);
            }

            for (Thread t : threads) {
                t.join();
            }
            assertTrue(capacity >= buffer.getNumItems());
        }
    }

    @Test
    public void test12() throws InterruptedException, IdNotFoundException {
        int capacity = 5;
        int timeout = 1;

        FSFTBuffer<BufferableString> buffer = new FSFTBuffer<>(capacity, timeout);

        BufferableString s1 = new BufferableString("1");
        BufferableString s2 = new BufferableString("2");
        BufferableString s3 = new BufferableString("3");
        BufferableString s4 = new BufferableString("4");
        BufferableString s5 = new BufferableString("5");
        BufferableString s6 = new BufferableString("6");

        buffer.put(s1);
        buffer.put(s2);
        buffer.put(s3);
        buffer.put(s4);
        buffer.put(s5);

        TimeUnit.SECONDS.sleep(2);

        buffer.put(s6);

        assertEquals(s6, buffer.get("6"));
    }
}
