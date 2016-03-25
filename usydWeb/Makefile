
MDOWN=$(wildcard in/*.mdown)
HTML=$(subst in/,out/,$(MDOWN:.mdown=.html))
IMG=$(subst in/,out/,$(wildcard in/*.png))
IBID=$(subst in/,out/,$(patsubst %.ibid,%,$(wildcard in/*.ibid)))

all: out/ $(HTML) $(IMG) $(IBID)

out/%.html: in/%.mdown
	perl ./Markdown.pl $< > $@

out/%.png: in/%.png
	cp $< $@

out/%: in/%.ibid
	cp $< $@

out/:
	mkdir out

clean:
	rm -rf out

