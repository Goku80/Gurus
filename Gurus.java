import java.util.Random;

public class Gurus
{
    // Klasse für die ChopSticks
    static class ChopSticks
    {
        private boolean[] sticks;

        // Konstruktor, der die Anzahl der Stäbchen initialisiert
        public ChopSticks(int n)
        {
            sticks = new boolean[n];
        }

        // Synchronisierte Methode zum Nehmen der Stäbchen
        public synchronized void get(int left, int right)
        {
            // Solange die Stäbchen nicht verfügbar sind, warten
            while (sticks[left] || sticks[right])
            {
                try
                {
                    wait(); // Warten, bis die Stäbchen verfügbar sind
                }
                catch (InterruptedException ex)
                {
                    Thread.currentThread().interrupt(); // Unterbrechungsstatus wiederherstellen
                }
            }
            // Stäbchen als belegt markieren
            sticks[left] = true;
            sticks[right] = true;
        }

        // Synchronisierte Methode zum Zurücklegen der Stäbchen
        public synchronized void put(int left, int right)
        {
            sticks[left] = false;
            sticks[right] = false;
            notifyAll(); // Benachrichtige alle wartenden Threads
        }

        // Methode zur Rückgabe der Anzahl der Stäbchen
        public int length()
        {
            return sticks.length;
        }
    }

    // Klasse für die Gurus, die Runnable implementiert
    static class Guru implements Runnable
    {
        static final int MAX_WAITTIME_MILLIS = 2000; // Maximale Wartezeit in Millisekunden
        private ChopSticks sticks; // Referenz auf das ChopSticks-Objekt
        final int index; // Index des Gurus
        private Random random = new Random(); // Zufallszahlengenerator

        // Konstruktor für den Guru
        public Guru(ChopSticks sticks, int index)
        {
            this.sticks = sticks;
            this.index = index;
        }

        // Methode, die den Lebenszyklus eines Gurus definiert
        @Override
        public void run()
        {
            while (true)
            {
                // Versuche, die Stäbchen zu nehmen
                sticks.get(index, (index + 1) % sticks.length());
                eat(); // Der Guru isst
                // Lege die Stäbchen zurück
                sticks.put(index, (index + 1) % sticks.length());
                sleep(); // Der Guru schläft
            }
        }

        // Methode, die das Essen eines Gurus simuliert
        private void eat()
        {
            System.out.println("Guru " + index + " isst mit Stäbchen " + index + "," + (index + 1) % sticks.length());
            try
            {
                Thread.sleep(random.nextInt(MAX_WAITTIME_MILLIS)); // Simuliere die Zeit, die zum Essen benötigt wird
            }
            catch (InterruptedException ex)
            {
                Thread.currentThread().interrupt(); // Unterbrechungsstatus wiederherstellen
            }
        }

        // Methode, die das Schlafen eines Gurus simuliert
        private void sleep()
        {
            System.out.println("Guru " + index + " schläft");
            try
            {
                Thread.sleep(random.nextInt(MAX_WAITTIME_MILLIS)); // Simuliere die Zeit, die zum Schlafen benötigt wird
            }
            catch (InterruptedException ex)
            {
                Thread.currentThread().interrupt(); // Unterbrechungsstatus wiederherstellen
            }
        }
    }

    // Hauptmethode zum Starten des Programms
    public static void main(String[] args)
    {
        final int numGurus = 6; // Anzahl der Gurus
        ChopSticks sticks = new ChopSticks(numGurus); // Erzeuge ein ChopSticks-Objekt

        // Starte Threads für jeden Guru
        for (int i = 0; i < numGurus; i++)
        {
            new Thread(new Guru(sticks, i)).start();
        }
    }
}
