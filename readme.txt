Reflect about your solution!


Summary:
- alles implementiert
- !exit am server beendet auch alle clients
- beenden und schließen von ressourcen wird nicht synchronisiert (zu komplex)
- client und server erzeugen ~1000 threads
- zum teil wilde hacks um halbwegs korrektes interrupt handling zu ermöglichen
- sehr kurze timeouts (fast busy waiting) um schnell genung für automatische tests zu sein

die  InputStream (TestInputStream) klasse muss die available() methode implementieren!
z.B.:
    @Override
    public int available() throws IOException {
        if (this.in != null){
            return 1;
        }
        return this.lines.size();
    }
sonst ist kein ordentliches interrupt handling möglich, weil read blockiert und interrupts ignoriert
