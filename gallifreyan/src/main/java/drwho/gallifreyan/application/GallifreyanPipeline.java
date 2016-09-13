package drwho.gallifreyan.application;

/**
 * Base class for a standard Gallifreyan pipeline (which coverts text to rendered Gallifreyan).  It treats all
 * sentences equally, if you want better sentence control (eg, a two sentences of adjacent chapters should not be drawn
 * next to each other) then you might need to extend your own pipeline.
 */
public class GallifreyanPipeline {

    protected final SentenceSplitter sentenceSplit;
    protected final WordSplitter wordSplit;
    protected final GlyphConverter glyphConv;
    protected final Arranger arranger;

    public GallifreyanPipeline(SentenceSplitter s, WordSplitter w, GlyphConverter g, Arranger a){
        this.sentenceSplit = s;
        this.wordSplit = w;
        this.glyphConv = g;
        this.arranger = a;
    }

    public void pipe(TextSource in, Renderer out){
        arranger.arrange(sentenceSplit
                .split(in.get())
                .map(snt -> wordSplit.split(snt).map(glyphConv::convert))
            ).forEach(out::render);
    }

}
