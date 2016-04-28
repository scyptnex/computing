Scanner sca = new Scanner(System.in)
def cases = sca.nextInt()
for (tc=0; tc<cases; tc++){
    def n = sca.nextInt()
        def k = sca.nextInt()
        def e = 0
        for(s=0; s<n; s++) if (sca.nextInt() <= 0) e++
        if (e >= k) println "NO"
        else println "YES"
}
