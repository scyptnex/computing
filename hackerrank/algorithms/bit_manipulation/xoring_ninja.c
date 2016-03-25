#include <stdio.h>
#include <string.h>
#include <math.h>
#include <stdlib.h>

#define bool unsigned char
#define false 0
#define true !false
#define WS (8*sizeof(unsigned long))
#define MOD 1000000007ul

/// From Wikipedia
unsigned long mod_pow(unsigned long base, unsigned long exp, unsigned long mod){
    unsigned long e;
    unsigned long c = 1;
    for(e=1; e<=exp; e++){
        c = (c*base)%mod;
    }
    return c;
}

int main() {
    unsigned long t, n, a, ax, ti, ni, i;
    scanf("%ld", &t);
    for(ti=0; ti<t; ti++){
        scanf("%ld", &n);
        ax = 0;
        for(ni=0; ni<n; ni++){
            scanf("%ld", &a);
            ax = ax | a;
        }
        if(ax == 0) printf("0\n");
        else printf("%ld\n", (ax%MOD * mod_pow(2, n-1, MOD))%MOD);
    }
    return 0;
}
