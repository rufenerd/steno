package steno;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static steno.Arpabet.*;

public class Schemes {
    private static final ImmutableList<Arpabet> VOWEL_SOUNDS = ImmutableList.of(AA, AE, AH, AO, AW, AY, EH, ER, EY, IH, IY, OW, OY, UH, UW);

    public static Map<Arpabet, Enum> IDENTITY = Arrays.stream(Arpabet.values()).collect(Collectors.toMap(a -> a, a -> a));
    public static Map<Arpabet, Enum> COLLAPSED_VOWELS = Arrays.stream(Arpabet.values()).collect(
            Collectors.toMap(a -> a, a -> VOWEL_SOUNDS.contains(a) ? ExtendedArpabet.VOWEL : a)
    );

    public static Map<Arpabet, Enum> CRUNCHY = new HashMap<>(COLLAPSED_VOWELS);
    static {
        CRUNCHY.putAll(new ImmutableMap.Builder<Arpabet, Enum>()
                .put(HH, B)
                .put(D, DH)
                .put(F, G)
                .put(K, L)
                .put(M, N)
                .put(P, R)
                .put(S, T)
                .put(SH, V)
                .put(W, Y)
                .put(Z, CH)
                .put(ZH, TH)
                .put(NG, JH)
                .build());
    }


    public static Map<Arpabet, Enum> DUMB_SUPER_CRUNCHY = new HashMap<>(COLLAPSED_VOWELS);
    static {
        DUMB_SUPER_CRUNCHY.putAll(new ImmutableMap.Builder<Arpabet, Enum>()
                .put(HH, DH)
                .put(B, DH)
                .put(D, DH)
                .put(G, L)
                .put(F, L)
                .put(K, L)
                .put(M, N)
                .put(R, N)
                .put(P, T)
                .put(S, T)
                .put(V, Y)
                .put(SH, Y)
                .put(W, Y)
                .put(CH, TH)
                .put(Z, TH)
                .put(ZH, TH)
                .put(NG, JH)
                .build());
    }

    enum ExtendedArpabet {
        VOWEL
    }

}
